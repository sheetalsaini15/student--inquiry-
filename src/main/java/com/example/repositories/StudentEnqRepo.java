package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.StudentEnqEntity;
import com.example.entity.UserDtlsEntity;

public interface StudentEnqRepo extends JpaRepository<StudentEnqEntity, Integer> {

	
	public  List<StudentEnqEntity> findByUser(UserDtlsEntity user);
	
}
