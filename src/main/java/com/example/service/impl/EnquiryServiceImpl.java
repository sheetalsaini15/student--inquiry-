package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.binding.DashboardResponse;
import com.example.binding.EnquiryForm;
import com.example.binding.EnquirySearchCriteria;
import com.example.entity.CourseEntity;
import com.example.entity.EnqStatusEntity;
import com.example.entity.StudentEnqEntity;
import com.example.entity.UserDtlsEntity;
import com.example.repositories.CourseRepo;
import com.example.repositories.EnqStatusRepo;
import com.example.repositories.StudentEnqRepo;
import com.example.repositories.UserDtlsRepo;
import com.example.service.EnquiryService;

@Service
public class EnquiryServiceImpl implements EnquiryService{
	
	@Autowired
	private StudentEnqRepo studentRepo;
	
	@Autowired
	private UserDtlsRepo userRepo;
	
	@Autowired
	private CourseRepo courseRepo;
	
	@Autowired
	private EnqStatusRepo enqStatusRepo;
	
	@Autowired
	private HttpSession session;

	@Override
	public List<String> getCourseNames() {
		List<CourseEntity> courses = courseRepo.findAll();
		
		List<String> courseName=new ArrayList<>();
		
		for (CourseEntity courseEntity : courses) {
			courseName.add(courseEntity.getCourseName());
		}
		return courseName;
	}

	@Override
	public List<String> getEnqStatus() {
		
		List<EnqStatusEntity> enqStatus = enqStatusRepo.findAll();
		List<String> enqStatusName=new ArrayList<>();
		
		for (EnqStatusEntity entity:enqStatus) {
			enqStatusName.add(entity.getStatusName());
		}
		
		return enqStatusName;
	}

	@Override
	public DashboardResponse getDashboardData(Integer userId) {
		
		DashboardResponse response=new DashboardResponse();
		
		Optional<UserDtlsEntity> userOptional = userRepo.findById(userId);
		
		if (userOptional.isPresent()) {
			UserDtlsEntity user = userOptional.get();
			
			List<StudentEnqEntity> enquiries = user.getEnquiries();
			
			Integer totalCnt = enquiries.size();
			
			Integer enroledCnt = enquiries.stream()
					.filter(e->e.getEnquiryStatus().equals("Enrolled"))
					.collect(Collectors.toList()).size();
			
			Integer LostCnt = enquiries.stream()
					.filter(e->e.getEnquiryStatus().equals("Lost"))
					.collect(Collectors.toList()).size();
			
			response.setTotalEnquries(totalCnt);
			response.setEnrolledCnt(enroledCnt);
			response.setLostCnt(LostCnt);	
			
		}
		
		return response;
	}

	@Override
	public Boolean saveEnquiry(EnquiryForm form) {
		
		StudentEnqEntity enqEntity=new StudentEnqEntity(); 
		 
		BeanUtils.copyProperties(form, enqEntity);
		
		Integer userId=(Integer)session.getAttribute("userId");
		Optional<UserDtlsEntity> findById = userRepo.findById(userId);
		
		UserDtlsEntity userDtlsEntity = findById.get();
		enqEntity.setUser(userDtlsEntity);
		
		studentRepo.save(enqEntity);
		
		return true;
	}

	@Override
	public List<EnquiryForm> getEnquies(Integer userId, EnquirySearchCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StudentEnqEntity> getEnquiry() {
		Integer userId=(Integer)session.getAttribute("userId");
		
		Optional<UserDtlsEntity> findById = userRepo.findById(userId);
			
		UserDtlsEntity userDtlsEntity = findById.get();
		
		List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
		
		return enquiries;
		
	}

	@Override
	public List<StudentEnqEntity> getFilteredEnqs(EnquirySearchCriteria criteria, Integer userId) {

		Optional<UserDtlsEntity> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
		
		UserDtlsEntity userDtlsEntity = findById.get();
		
		List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
		
		//filter Logic
		
		if (null !=criteria.getCourseName() & !"".equals(criteria.getCourseName())) {
			enquiries = enquiries.stream()
			.filter(e -> e.getCourseName().equals(criteria.getCourseName()))
			.collect(Collectors.toList());
		}
		
		if (null !=criteria.getEnquiryStatus() & !"".equals(criteria.getEnquiryStatus())) {
			enquiries = enquiries.stream()
			.filter(e -> e.getEnquiryStatus().equals(criteria.getEnquiryStatus()))
			.collect(Collectors.toList());
		}
		
		if (null !=criteria.getClassMode() & !"".equals(criteria.getClassMode() )) {
			enquiries = enquiries.stream()
			.filter(e -> e.getClassMode() .equals(criteria.getClassMode() ))
			.collect(Collectors.toList());
		}
	
		return enquiries;
		}
		return null;
	}

}
