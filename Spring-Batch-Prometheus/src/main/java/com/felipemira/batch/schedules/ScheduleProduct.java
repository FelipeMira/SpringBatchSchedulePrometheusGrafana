package com.felipemira.batch.schedules;

import com.felipemira.batch.config.ProductJobConfiguration;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@EnableScheduling
@Configuration
public class ScheduleProduct {

    @Autowired
    private JobOperator operator;

    @Autowired
    private JobExplorer jobs;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ProductJobConfiguration productJobConfiguration;

    @Scheduled(fixedRate = 60000)
    public JobExecution run() throws Exception {
        JobExecution jobExecution;
        List<JobInstance> lastInstances = jobs.getJobInstances("Product_Job", 0, 1);
        if (lastInstances.isEmpty()) {
            jobExecution = jobLauncher.run(productJobConfiguration.productJob(), new JobParametersBuilder()
                    .addString(String.valueOf(java.util.UUID.randomUUID()), String.valueOf(System.currentTimeMillis()))
                    .toJobParameters());
            return jobExecution;
        } else {
            operator.startNextInstance("Product_Job");
        }

        return null;
    }
}
