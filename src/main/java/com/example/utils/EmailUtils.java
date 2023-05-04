package com.example.utils;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;
	
	public Boolean sandMail(String subject ,String body ,String to)  {
		
		boolean isSent=false;
		
		try {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
		
		helper.setSubject(subject);
		helper.setText(body,true);
		helper.setTo(to);
		
		mailSender.send(mimeMessage);
		
		isSent=true;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		return isSent;
	}
	
	
}
