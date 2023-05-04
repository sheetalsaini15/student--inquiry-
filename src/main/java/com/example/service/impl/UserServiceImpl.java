package com.example.service.impl;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.binding.LoginForm;
import com.example.binding.SignUpForm;
import com.example.binding.UnlockForm;
import com.example.entity.UserDtlsEntity;
import com.example.repositories.UserDtlsRepo;
import com.example.service.UserService;
import com.example.utils.EmailUtils;
import com.example.utils.PwdUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PwdUtils pwdUtils;

	@Autowired
	private UserDtlsRepo userRepo;

	@Autowired
	private EmailUtils emailUtils;
	
	@Autowired
	private HttpSession session;

	//first way
	
//	@Override
//	public String login(LoginForm form) {
//		
//		UserDtlsEntity entity = userRepo.findByEmail(form.getEmail());
//		
//		Optional<UserDtlsEntity> optional = Optional.ofNullable(entity);
//		
//		if(optional.isEmpty()) {
//			return "Account not Exist with Login Email please check your Email";
//			
//			
//		}else {
//			if (entity.getPwd().equals(form.getPwd())) {
//			
//			if (entity.getAccStatus().equals("UNLOCKED")) {
//				return "SUCCESS";
//			}else {
//				return "Account is locked first unlocked the Account then login for unlock your Account check your Email";
//			}
//		}else {
//			return "Password is not correct please check your Password";
//		}
//			
//		}
//
//		
//	}
	
	
	//second Way(sir way)
	
	@Override
	public String login(LoginForm form) {
		
		UserDtlsEntity entity = userRepo.findByEmailAndPwd(form.getEmail(), form.getPwd());
		
		if (entity==null) {
			return "Invalid Credentials";
			
		}
		if (entity.getAccStatus().equals("LOCKED")) {
			return "Your Account Locked";
		}
		//create Session and store user data in session
		
		session.setAttribute("userId", entity.getUserId());
		
		return "SUCCESS";
}

	@Override
	public Boolean signUp(SignUpForm form) {

		UserDtlsEntity userDtlsEntity = userRepo.findByEmail(form.getEmail());

		if (userDtlsEntity != null) {

			return false;
		}

		// copy data from binding object to entity object

		UserDtlsEntity user = new UserDtlsEntity();

		BeanUtils.copyProperties(form, user);

		// Generate the Random password and set to the object

		String tempPwd = pwdUtils.generateRandompwdUUID();

		user.setPwd(tempPwd);
		// Set the account Status Locked

		user.setAccStatus("LOCKED");

		// insert record

		userRepo.save(user);

		// sand email to unlock the account
		String to = form.getEmail();
		String subject = "Unlock Your Account | Ashok IT";
		StringBuffer body = new StringBuffer("");
		body.append("<h1>Use below temporary password to unlock your account</h1>");
		body.append("Temporary pwd : " + tempPwd);
		body.append("<br/>");
		body.append("<a href=\"http://localhost:9091/unlock?email=" + to + "\">Click Here To unlock Your Account</a>");

		emailUtils.sandMail(subject, body.toString(), to);

		return true;

	}

	//first way 
	
//	@Override
//	public String unlockAccount(UnlockForm form) {
//
//		UserDtlsEntity user = userRepo.findByEmail(form.getEmail());
//		
//		System.out.println(user);
//		
//		if(form.getTempPwd().equals(user.getPwd())) {
//			
//		if(form.getNewPwd().equals(form.getConfirmPwd())) {
//		
//			user.setPwd(form.getNewPwd());
//			user.setAccStatus("UNLOCKED");
//			
//			userRepo.save(user);
//			
//			System.out.println(user);
//			return "Account is Unlocked";
//		}else {
//			return "new password and Conform password not same";
//			
//		}
//		
//		}
//		else {
//			return "Temp password is incorrect ";
//		}
//			
//	}
//	
	
	
	//second way (sir way)
	@Override
	public boolean unlockAccount(UnlockForm form) {
		
		UserDtlsEntity entity = userRepo.findByEmail(form.getEmail());
		
		if (entity.getPwd().equals(form.getTempPwd())) {
			entity.setPwd(form.getNewPwd());
			entity.setAccStatus("UNLOCKED");
			userRepo.save(entity);
			return true;
		} else {
			
			return false;
		}
		
	}

	@Override
	public String forgotPwd(String email) {
		
         UserDtlsEntity entity = userRepo.findByEmail(email);
		
		Optional<UserDtlsEntity> optional = Optional.ofNullable(entity);
		
		if(optional.isEmpty()) {
		
			return "FAILED";
		}else {
			
			String to=email;
			String subject="Your Password @ | Ashok It is";
			String body="<h2>"+"password :"+entity.getPwd()+"</h2>";
			emailUtils.sandMail(subject, body, to);
			
			return "Password sand to your Email  "+entity.getEmail();
		}
		
		
	}

}
