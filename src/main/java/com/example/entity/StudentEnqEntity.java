package com.example.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity 
@Table(name = "AIT_STUDENT_ENQURIES")
public class StudentEnqEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer enquiryId;
	private String studentName;
	private Integer studentPhone;
	private String classMode;
	private String courseName;
	private String enquiryStatus;
	
	@CreationTimestamp
	private LocalDate createDate;
	@UpdateTimestamp
	private LocalDate updatedDate;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserDtlsEntity user;
}
