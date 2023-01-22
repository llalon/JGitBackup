package de.llalon.gitbackup.task;

import de.llalon.gitbackup.GitBackupRunnable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
@Profile("!test")
public class RunScheduledTask {
    private final GitBackupRunnable task;

    @Scheduled(cron = "${schedule}")
    public void runScheduled() {
        log.debug("Running scheduled task");
        task.run();
    }
}
