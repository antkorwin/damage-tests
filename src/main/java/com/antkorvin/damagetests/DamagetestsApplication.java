package com.antkorvin.damagetests;

import com.antkorvin.damagetests.utills.CustomRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class DamagetestsApplication {

	public static void main(String[] args) {
		CustomRunner.run(DamagetestsApplication.class, args);
	}
}
