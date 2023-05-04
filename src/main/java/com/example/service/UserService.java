package com.example.service;

import com.example.binding.LoginForm;
import com.example.binding.SignUpForm;
import com.example.binding.UnlockForm;

public interface UserService {

	public String login(LoginForm form);
	
	public Boolean signUp(SignUpForm form);
	
	//public String unlockAccount(UnlockForm form);
	
	public boolean unlockAccount(UnlockForm form);
	
	public String forgotPwd(String email);
	
	
}
