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
public class HelloJobConfiguration {

    /*
    Spring Batch 5 부터 사용변경


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob").start(helloStep()).build();
    }

    @Bean
    public Step helloStep() {
        return stepBuilderFactory.get("helloStep")
            .tasklet(((contribution, chunkContext) -> {
                System.out.println("Hello Spring Batch");
                return RepeatStatus.FINISHED;
            })).build();
    }
     */


    /*
        사용자가 문서를 읽어 인지하지 않는 한 Builder에서 JobRepository가 생성되고 설정된다는 사실을 숨기고 있다.
        그래서 아래와 같이 JobRepository과 PlatformTransactionManager를 명시적으로 제공하는 방식을 사용하길 권장한다.
     */

    @Bean
    @Qualifier("helloJob")
    public Job helloJob(JobRepository jobRepository, @Qualifier("helloStep") Step helloStep) {
        return new JobBuilder("helloJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(helloStep)
                .end()
                .build();
    }

    @Bean
    public Step helloStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("helloStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Hello Spring Batch");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

}
