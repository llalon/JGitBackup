package de.llalon.gitbackup;

import de.llalon.gitbackup.config.AppConfig;
import de.llalon.gitbackup.model.GitRepo;
import de.llalon.gitbackup.service.GitHandlerFactory;
import de.llalon.gitbackup.service.GitRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Actual main runnable process invoked by scheduler or startup process.
 */
@Component
@Slf4j
public class GitBackupRunnable implements Runnable {

    private final AppConfig config;
    private final GitRunner gitRunner;
    private final GitHandlerFactory gitHandlerFactory;

    @Autowired
    public GitBackupRunnable(AppConfig config, GitRunner gitRunner, GitHandlerFactory gitHandlerFactory) {
        this.config = config;
        this.gitRunner = gitRunner;
        this.gitHandlerFactory = gitHandlerFactory;
    }

    @Override
    public void run() {
        log.info("Starting mirroring task for {}", config.getUsername());

        try {
            var repos = gitHandlerFactory.createHandler().getAllRepos();
            log.info("Found {} repos", repos.size());
            repos.forEach(this::processRepo);
            log.info("Completed mirroring task for {}", config.getUsername());
        } catch (Exception e) {
            log.error("Mirroring task completed exceptionally", e);
        }
    }

    private void processRepo(GitRepo repo) {
        log.debug("Checking repo {} - {}", repo.getName(), repo.getUrl());

        // (repo/owner NOT empty) AND (repo/owner match XOR whitelist)
        if (!config.getRepos().isEmpty()
                && (config.isWhiteList()
                        ^ (config.getRepos().contains(repo.getName())
                                || config.getRepos().contains(repo.getUrl())))) {
            log.debug(
                    "Skipping repo {} due to repo in {}",
                    repo.getName(),
                    config.isWhiteList() ? "whitelist" : "blacklist");
            return;
        }
        // Check owner whitelist
        if (!config.getOwners().isEmpty()
                && config.isWhiteList() ^ (config.getOwners().contains(repo.getOwner()))) {
            log.debug(
                    "Skipping repo {} for due to owner {} in {}.",
                    repo.getName(),
                    repo.getOwner(),
                    config.isWhiteList() ? "whitelist" : "blacklist");
            return;
        }

        // Do repo
        try {
            log.info("Updating {}", repo.getName());
            gitRunner.mirrorRepo(repo.getName(), repo.getUrl());
        } catch (Exception e) {
            log.error("Error mirroring repo {} @ {}", repo.getName(), repo.getUrl(), e);
        }
    }
}
