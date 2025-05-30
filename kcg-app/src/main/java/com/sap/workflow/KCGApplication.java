package com.sap.workflow;

import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.management.ManagementFactory;
import java.util.List;
//import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableTransactionManagement
@EnableAsync
public class KCGApplication {

	private static Logger logger = LoggerFactory.getLogger(Consumer.class);

	public static void main(String[] args) {
		List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
		logger.info("JVM arguments:");
		for (String arg : arguments) {
			logger.info(arg);
		}
		SpringApplication.run(KCGApplication.class, args);
	}

//	@Bean("threadPoolTaskExecutor")
//	public TaskExecutor getAsyncExecutor() {
//		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//		executor.setCorePoolSize(20);
//		executor.setMaxPoolSize(1000);
//		executor.setWaitForTasksToCompleteOnShutdown(true);
//		executor.setThreadNamePrefix("Async-");
//		return executor;
//	}


}
