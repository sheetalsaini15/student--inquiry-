package com.example.service;

import java.util.List;

import com.example.binding.DashboardResponse;
import com.example.binding.EnquiryForm;
import com.example.binding.EnquirySearchCriteria;
import com.example.entity.StudentEnqEntity;

public interface EnquiryService {
	
	public List<String> getCourseNames();
	
	public List<String> getEnqStatus();

	public DashboardResponse getDashboardData(Integer userId);
	
	public Boolean saveEnquiry(EnquiryForm form);
	
	public List<EnquiryForm> getEnquies(Integer userId , EnquirySearchCriteria criteria);
	
	public List<StudentEnqEntity> getEnquiry();
	
	public List<StudentEnqEntity> getFilteredEnqs(EnquirySearchCriteria criteria,Integer userId);
	
}
