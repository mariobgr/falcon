package com.mariobgr.falcon;

import com.mariobgr.falcon.config.DataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableAutoConfiguration
@Import(value = { DataSourceConfig.class })
public class FalconApplication {

	public static void main(String[] args) {
		SpringApplication.run(FalconApplication.class, args);
	}

}
