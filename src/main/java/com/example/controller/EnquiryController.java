package com.example.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.binding.DashboardResponse;
import com.example.binding.EnquiryForm;
import com.example.binding.EnquirySearchCriteria;
import com.example.entity.StudentEnqEntity;
import com.example.repositories.StudentEnqRepo;
import com.example.service.EnquiryService;

@Controller
public class EnquiryController {
	
	@Autowired
	private EnquiryService studentEnquiryService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private StudentEnqRepo studentEnqRepo;
	
	@GetMapping("/logout")
	public String logout() {
		
		session.invalidate();
		
		return "index";
	}
	

	@GetMapping("/dashboard")
	public String dashboardPage(Model model) {
		
		Integer userId =(Integer) session.getAttribute("userId");
		
		DashboardResponse dashboardData = studentEnquiryService.getDashboardData(userId);
		
		model.addAttribute("dashboardData", dashboardData);
		
		return "dashboard";
	}
	
	
	@GetMapping("/enquiry")
	public String addEnquiryPage(Model model) {
		
		initForm(model);
		
		return "add-enquiry";
	}


	private void initForm(Model model) {
		//get courses for the drop down
		List<String> courseNames = studentEnquiryService.getCourseNames();
		//get enq status for drop down
		List<String> enqStatus = studentEnquiryService.getEnqStatus();
		//create binding class object
		EnquiryForm formObj=new EnquiryForm();
		//set data in model object
		model.addAttribute("courseName",courseNames );
		model.addAttribute("StatusNames",enqStatus );
		model.addAttribute("formObj",formObj );
	}
	@PostMapping("/addEnq")
	public String addEnquiry(@ModelAttribute("formObj") EnquiryForm formObj,Model model) {
		System.out.println(formObj);
		
      Boolean status = studentEnquiryService.saveEnquiry(formObj);
		
      if (status) {
		model.addAttribute("succMsg", "Enquiry Added");	
		}else {
			model.addAttribute("errMsg", "Problem Occured");
		}
      
      
		return "add-enquiry";
	}
	
	
	
	@GetMapping("/enquires")
	public String viewEnquiriesPage(Model model) {
		initForm(model);
		 List<StudentEnqEntity> enquires = studentEnquiryService.getEnquiry();
		
		model.addAttribute("enquiries", enquires);
		
		
		return "view-enquiries";
	}
	
	@GetMapping("/filter-enquiries")
	public String getFilteredEnqs(@RequestParam String cname,
			@RequestParam String status,
			@RequestParam String mode,
			Model model) {
		
	EnquirySearchCriteria criteria=	new EnquirySearchCriteria();
	criteria.setCourseName(cname);
	criteria.setEnquiryStatus(status);
	criteria.setClassMode(mode);
	
	System.out.println(criteria);
	
	Integer userId =(Integer) session.getAttribute("userId");
	
	List<StudentEnqEntity> filteredEnqs = studentEnquiryService.getFilteredEnqs(criteria, userId);
	
	model.addAttribute("enquiries", filteredEnqs);
	
	return "filter-enquiries";
	}
	
	@GetMapping("/edit")
	public String editEnq(@RequestParam("enquiryId") Integer enquiryId,Model model) {
		
		Optional<StudentEnqEntity> findById = studentEnqRepo.findById(enquiryId);
		
		if(findById.isPresent()) {
			
			StudentEnqEntity studentEnqEntity = findById.get();
			
			
			
			//get courses for the drop down
			List<String> courseNames = studentEnquiryService.getCourseNames();
			//get enq status for drop down
			List<String> enqStatus = studentEnquiryService.getEnqStatus();
			//create binding class object
			EnquiryForm formObj=new EnquiryForm();
			
			BeanUtils.copyProperties(studentEnqEntity, formObj);
			
			//set data in model object
			model.addAttribute("courseName",courseNames );
			model.addAttribute("StatusNames",enqStatus );
			model.addAttribute("formObj",formObj );
			
		}
		
		return"add-enquiry";
	}
}
