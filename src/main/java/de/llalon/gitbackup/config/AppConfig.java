package de.llalon.gitbackup.config;

import de.llalon.gitbackup.model.GitProvider;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@Slf4j
@ConfigurationProperties
public class AppConfig {

    private String username;
    private String directory;
    private String token;
    private List<String> owners = new ArrayList<>();
    private List<String> repos = new ArrayList<>();
    private boolean whiteList = false;
    private String host;
    private GitProvider provider;
    private String schedule = "";

    public void setUsername(String username) {
        this.username = username.replaceAll("^\"|\"$", "");
    }

    public void setDirectory(String directory) {
        this.directory = directory.replaceAll("^\"|\"$", "");
    }

    public void setToken(String token) {
        this.token = token.replaceAll("^\"|\"$", "");
    }

    public void setHost(String host) {
        this.host = host.replaceAll("^\"|\"$", "");
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule.replaceAll("^\"|\"$", "");
    }

    @PostConstruct
    public void validate() throws IllegalArgumentException {
        log.debug("Validating user configuration: {}", this);

        if (this.getToken() == null
                || this.getToken().isBlank()
                || this.getToken().isEmpty()) {
            log.error("Invalid configuration. Token must be provided.");
            throw new IllegalArgumentException("Invalid configuration");
        }

        if (this.getProvider() == null
                || this.getProvider().getQualifier().isBlank()
                || this.getProvider().getQualifier().isEmpty()) {
            log.error("Invalid configuration. Provider not found.");
            throw new IllegalArgumentException("Invalid configuration");
        }

        if (this.getUsername() == null || this.getUsername().isBlank()) {
            log.error("Invalid configuration. Username must be provided.");
            throw new IllegalArgumentException("Invalid configuration");
        }

        log.debug("Configuration is valid");
    }
}
