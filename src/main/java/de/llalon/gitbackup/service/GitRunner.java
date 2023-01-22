package de.llalon.gitbackup.service;

import de.llalon.gitbackup.config.AppConfig;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class GitRunner {

    private final AppConfig config;

    /**
     * Mirrors a remote repo to a specified folder
     *
     * @param repoName Name of repo and directory it will be saved. Does not HAVE to match actual
     *     repo name.
     * @param baseUrl URL of the repository
     * @throws URISyntaxException provided repo or credentials are invalid
     * @throws IOException Error writing to specified folder
     */
    public void mirrorRepo(String repoName, String baseUrl)
            throws URISyntaxException, IOException, InterruptedException {
        log.debug("Mirroring {} to {}", baseUrl, repoName);

        URI uri = new URIBuilder(new URI(baseUrl))
                .setUserInfo(config.getUsername(), config.getToken())
                .build();

        Path path = Path.of(config.getDirectory(), repoName);

        init(uri.toString(), path.toAbsolutePath().toString()).waitFor(5, TimeUnit.SECONDS); // must block

        var process = mirror(uri.toString(), path.toAbsolutePath().toString());

        process.waitFor(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

    private Process init(String url, String dir) throws IOException {
        log.debug("Initializing repo {} to path {}", url, dir);

        final String[] command = {"/usr/bin/git", "init", "--bare", "--quiet"};

        Files.createDirectories(Path.of(dir));

        return new ProcessBuilder()
                .directory(new File(dir))
                .redirectErrorStream(true)
                .command(command)
                .start();
    }

    private Process mirror(String url, String dir) throws IOException {
        log.debug("Cloning repo {}", url);

        final String[] command = {
            "/usr/bin/git", "fetch", "--force", "--prune", "--tags", url, "refs/heads/*:refs/heads/*"
        };

        return new ProcessBuilder()
                .directory(new File(dir))
                .redirectErrorStream(true)
                .command(command)
                .start();
    }
}
