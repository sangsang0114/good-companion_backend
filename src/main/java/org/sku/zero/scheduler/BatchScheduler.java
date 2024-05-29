package org.sku.zero.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job job;

    @Scheduled(cron = "0 0 0 * * *")
    public void runJob() throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .addString("jobName", "exchangeJob" + System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(job, parameters);
    }
}
