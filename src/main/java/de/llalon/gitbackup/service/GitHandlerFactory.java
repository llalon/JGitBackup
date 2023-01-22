package de.llalon.gitbackup.service;

import de.llalon.gitbackup.config.AppConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class GitHandlerFactory {
    private final AppConfig config;

    public GitHandler createHandler() {
        log.debug("Creating handler for {}", config.getProvider().getQualifier());

        switch (config.getProvider()) {
            case Github:
                return new GithubHandler(config);
            case Gitlab:
                return new GitLabHandler(config);
            default:
                throw new RuntimeException("Git provider is not supported!");
        }
    }
}
