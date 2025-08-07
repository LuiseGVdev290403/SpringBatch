package com.batch.config;

import com.batch.steps.ItemDescompressStep;
import com.batch.steps.ItemProcessorStep;
import com.batch.steps.ItemReaderStep;
import com.batch.steps.ItemWriterstep;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Bean
    @JobScope // este objeto solo disponible en el job
    public ItemDescompressStep itemDescompressStep () {
        return new ItemDescompressStep();
    }

    @Bean
    @JobScope
    public ItemReaderStep itemReaderStep(){
        return new ItemReaderStep();
    }
    @Bean
    @JobScope
    public ItemProcessorStep itemProcessorStep(){
        return new ItemProcessorStep();
    }
    @Bean
    @JobScope
    public ItemWriterstep itemWriterstep () {
        return new ItemWriterstep();
    }

    @Bean
    public Step descompressFileStep(JobRepository jobRepo, Tasklet t, PlatformTransactionManager txtManager) {
        return new StepBuilder("descompressFileStep", jobRepo)
                .tasklet(itemDescompressStep(), txtManager)
                .build();
    }

    @Bean
    public Step readFileStep (JobRepository jobRepository, Tasklet t, PlatformTransactionManager txt) {
        return new StepBuilder("readFileStep", jobRepository)
                .tasklet(itemReaderStep(), txt)
                .build();
    }

    @Bean
    public Step processFileStep (JobRepository jobRepository, Tasklet t, PlatformTransactionManager txt) {
        return new StepBuilder("processFileStep", jobRepository)
                .tasklet(itemProcessorStep(), txt)
                .build();
    }

    @Bean
    public Step writerDataStep ( JobRepository jobRepository, Tasklet t, PlatformTransactionManager txt ) {
        return new StepBuilder("writerDataStep", jobRepository)
                .tasklet(itemWriterstep(), txt)
                .build();
    }

    @Bean
    public Job readCSVJob (JobRepository jobRepository,
                           Step descompressFileStep,
                           Step readPersonStep,
                           Step processPersonStep,
                           Step writePersonStep) {
        return new JobBuilder("readCSVJob", jobRepository)
                .start(descompressFileStep)
                .next(readPersonStep)
                .next(processPersonStep)
                .next(writePersonStep)
                .build();
    }
}
