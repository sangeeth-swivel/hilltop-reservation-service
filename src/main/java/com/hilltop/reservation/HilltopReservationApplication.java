package com.hilltop.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class HilltopReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(HilltopReservationApplication.class, args);
	}

}
