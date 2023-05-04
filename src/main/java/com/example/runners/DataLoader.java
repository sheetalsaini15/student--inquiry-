package com.example.runners;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.entity.CourseEntity;
import com.example.entity.EnqStatusEntity;
import com.example.repositories.CourseRepo;
import com.example.repositories.EnqStatusRepo;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private CourseRepo courseRepo;
	
	@Autowired
	private EnqStatusRepo enqStatusRepo;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		courseRepo.deleteAll();
		
		CourseEntity c1=new CourseEntity();
		
		c1.setCourseName("java");
		
		CourseEntity c2=new CourseEntity();
		
		c2.setCourseName("python");
		
		CourseEntity c3=new CourseEntity();
		
		c3.setCourseName("Devops");
		
		courseRepo.saveAll(Arrays.asList(c1,c2,c3));
		
		enqStatusRepo.deleteAll();
		
		EnqStatusEntity e1=new EnqStatusEntity();
		
		e1.setStatusName("New");
		
		EnqStatusEntity e2=new EnqStatusEntity();
		
		e2.setStatusName("Enrolled");
		
		EnqStatusEntity e3=new EnqStatusEntity();
		
		e3.setStatusName("Lost");
		
		enqStatusRepo.saveAll(Arrays.asList(e1,e2,e3));
	}

}
