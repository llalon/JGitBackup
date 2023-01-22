package de.llalon.gitbackup.task;

import de.llalon.gitbackup.GitBackupRunnable;
import de.llalon.gitbackup.config.AppConfig;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
@Profile("!test")
public class RunOnceTask {
    private final GitBackupRunnable task;
    private final AppConfig config;
    private final ConfigurableApplicationContext context;

    /**
     * On start up of the application, if a schedule is not set, run once and exit.
     */
    @PostConstruct
    public void runOnce() {
        if (config.getSchedule().isBlank()) {
            log.debug("Running single task");

            task.run();
            log.debug("Closing application after initial task");
            exit();
        }
    }

    private void exit() {
        context.close();
        System.exit(0);
    }
}
