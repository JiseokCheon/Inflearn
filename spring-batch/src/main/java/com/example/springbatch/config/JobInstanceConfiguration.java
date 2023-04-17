package com.example.springbatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobInstanceConfiguration {

    @Bean("runnerTestJob")
    public Job runnerTestJob(JobRepository jobRepository, @Qualifier("step111") Step step111, @Qualifier("step222") Step step222) {
        return new JobBuilder("testJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step111)
                .next(step222)
                .end()
                .build();
    }

    @Bean
    public Step step111(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step111", jobRepository)
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, transactionManager)
                .build();
    }

    @Bean
    public Step step222(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step222", jobRepository)
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, transactionManager)
                .build();
    }
}
