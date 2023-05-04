package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.binding.ForgotPwd;
import com.example.binding.LoginForm;
import com.example.binding.SignUpForm;
import com.example.binding.UnlockForm;
import com.example.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public String handelSignUp(@ModelAttribute("user") SignUpForm form, Model model) {

		Boolean status = userService.signUp(form);

		if (status) {
			model.addAttribute("succMsg", "Account Created ,Check Your Email");
		} else {
			model.addAttribute("errMsg", "Choose Unique Email");
		}

		return "signup";
	}

	@GetMapping("/login")
	public String loginPage(Model model) {

		model.addAttribute("loginForm", new LoginForm());
		return "login";
	}
	
	
	
	@PostMapping("/login")
	public String login(@ModelAttribute("loginForm") LoginForm loginForm ,Model model) {

		System.out.println(loginForm);
		
		String status = userService.login(loginForm);
		
		if (status.contains("SUCCESS")) {
			return "redirect:/dashboard";
		}else {
			
			model.addAttribute("errMsg", status);
			return "login";
		}
		
		
	}

	@GetMapping("/signup")
	public String signUpPage(Model model) {
		model.addAttribute("user", new SignUpForm());
		return "signup";
	}
	
	
	

	@GetMapping("/unlock")
	public String unlockPage(@RequestParam String email, Model model) {

		UnlockForm unlockForm = new UnlockForm();
		unlockForm.setEmail(email);
		model.addAttribute("unlock", unlockForm);

		return "unlock";
	}

	// first way to implement unlock
//	  @PostMapping("/unlock")
//	  public String unlockUserAccount(UnlockForm unlock ,Model model) {
//	  
//	  System.out.println(unlock);
//	  
//	  String str = userService.unlockAccount(unlock);
//	  
//	  bindingObj(model);
//	
//	  model.addAttribute("msg", str);
//	  
//	  return "unlock";
//	  
//	  }

	// second way to implement unlock (sir way)
	@PostMapping("/unlock")
	public String unlockUserAccount(UnlockForm unlock, Model model) {

		if (unlock.getNewPwd().equals(unlock.getConfirmPwd())) {

			boolean status = userService.unlockAccount(unlock);
			if (status) {
				
				bindingObj(model);
				
				model.addAttribute("succMsg", "Account Unlocked successful");

			} else {
				bindingObj(model);
				
				model.addAttribute("msg", "Temp password is not correct");

			}

		} else {
			bindingObj(model);
			
			model.addAttribute("msg", "new password and conform password not matched");
		}

		return "unlock";
	}

	@GetMapping("/forgot")
	public String forgotpwdPage(Model model) {

		//model.addAttribute("forgotpwd", new ForgotPwd());
		
		return "forgotPwd";
	}
	
	@PostMapping("/forgotPwd")
	public String forgotpwd( @RequestParam("email") String email,Model model) {
		
		System.out.println(email);
		
		String status = userService.forgotPwd(email);
		if (status.contains("FAILED")) {
		
		model.addAttribute("errMsg", "Account not Exist with Given Email please check your Email");
		//model.addAttribute("forgotpwd", new ForgotPwd());
		return "forgotPwd";
		}
		else {
			
			model.addAttribute("succMsg",status );
			//model.addAttribute("forgotpwd", new ForgotPwd());
			return "forgotPwd";	
		}

		
	}
	
	private void bindingObj(Model model) {
		UnlockForm unlockForm=new UnlockForm();
		model.addAttribute("unlock",unlockForm);
	}

}
