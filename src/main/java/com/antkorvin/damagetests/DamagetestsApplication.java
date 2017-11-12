package com.antkorvin.damagetests;

import com.antkorvin.damagetests.utills.CustomRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DamagetestsApplication {

	public static void main(String[] args) {
		CustomRunner.run(DamagetestsApplication.class, args);
	}
}
