package com.tipre.dashboard;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tipre.dashboard.service.FilesStorageService;

import javax.annotation.Resource;

@SpringBootApplication
public class DashboardApplication implements CommandLineRunner {

	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(DashboardApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		storageService.init();
	}
}
