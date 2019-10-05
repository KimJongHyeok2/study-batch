package com.web.batch;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.web.batch.domain.User;
import com.web.batch.item.QueueItemReader;
import com.web.batch.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SimpleConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private UserRepository repository;
	
	@Bean
	public Job job() {
		JobParametersBuilder paramerterBuilder = new JobParametersBuilder();
		paramerterBuilder.addLong("currentTime", System.currentTimeMillis());
		JobParameters parameters = new JobParameters();
		parameters = paramerterBuilder.toJobParameters();
		JobParametersIncrementer incrementer = new RunIdIncrementer();
		incrementer.getNext(parameters);
		return jobBuilderFactory.get("job")
				.preventRestart()
				.incrementer(incrementer)
				.start(step())
				.build();
	}
	
	@Bean
	@JobScope
	public Step step() {
		return stepBuilderFactory.get("step")
				.<User, User> chunk(10)
				.reader(userReader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	@Bean
	@StepScope
	public QueueItemReader<User> userReader() {
		List<User> oldUser = repository.findAll();
		log.info("Reader...");
		return new QueueItemReader<>(oldUser);
	}
	
	public ItemProcessor<User, User> processor() {
		return new ItemProcessor<User, User>() {

			@Override
			public User process(User item) throws Exception {
				log.info("Processor...");
				return item;
			}
		};
	}
	
	public ItemWriter<User> writer() {
		return new ItemWriter<User>() {

			@Override
			public void write(List<? extends User> items) throws Exception {
				log.info("Writer...");
			}
		};
	}
	
}