package com.cts.o2o_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cts.o2o_platform", "com.o2o.clothing"})
@org.springframework.boot.autoconfigure.domain.EntityScan("com.o2o.clothing.entity")
@org.springframework.data.jpa.repository.config.EnableJpaRepositories("com.o2o.clothing.repository")
public class O2oPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(O2oPlatformApplication.class, args);
	}

}
