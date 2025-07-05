package com.example.ApiGatewayService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ImportAutoConfiguration(exclude = {org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration.class})
class ApiGatewayServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
