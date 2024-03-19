package com.pagbet4.pagbet4;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class Pagbet4mainApplication {

	public static void main(String[] args) {
		java.util.logging.Logger.getLogger("org.apache").setLevel(java.util.logging.Level.WARNING);
		new SpringApplicationBuilder(Pagbet4mainApplication.class).logStartupInfo(false).run(args);
	}

}
