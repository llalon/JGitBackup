package de.llalon.gitbackup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class GitBackupApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitBackupApplication.class, args);
    }
}
