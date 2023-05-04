package com.example.utils;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class PwdUtils {

	public String generateRandompwdUUID() {
		
		String string = UUID.randomUUID().toString();
		
		return string;
		
	}
	
	public static String  generateRandompwdApacheCommans() {
		
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String pwd = RandomStringUtils.random( 6, characters );
		
		
		return pwd;
	}
	
}
