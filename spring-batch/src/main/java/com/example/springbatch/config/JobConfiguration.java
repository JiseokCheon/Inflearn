package com.example.springbatch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JobConfiguration {

    @Bean
    @Qualifier("testJob")
    public Job testJob(JobRepository jobRepository, @Qualifier("step11") Step step11, @Qualifier("step22") Step step22) {
        return new JobBuilder("testJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step11)
                .next(step22)
                .end()
                .build();
    }

    @Bean
    public Step step11(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step11", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step11 was executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step22(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step22", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step22 was executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
