package com.example.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JobConfiguration {

	@Bean
	public Job testJob(JobRepository jobRepository, Step step11, Step step22) {
		return new JobBuilder("testJob", jobRepository)
			.start(step11)
			.next(step22)
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
