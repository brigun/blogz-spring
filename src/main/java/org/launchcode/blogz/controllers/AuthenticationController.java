package org.launchcode.blogz.controllers;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup 21:39 in video
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		
		if (User.isValidUsername(username) && User.isValidPassword(password) && verify.equals(password))
		{
			User newUser = new User(username, password);
			userDao.save(newUser);
			
			logInUser(request, newUser);
		}
		
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login 23:20 in video
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		User visitor = userDao.findByUsername(username);
		
		if (visitor == null)
		{
			model.addAttribute("error", "User does not exist");
			return "redirect: /login";
		}
		
		
		if (visitor.isMatchingPassword(password))
		{
			logInUser(request, visitor);
		}
		else
		{
			model.addAttribute("error", "The password is incorrect");
			return "redirect: /login";
		}

		
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
	
	public void logInUser(HttpServletRequest request, User user)
	{
		HttpSession session = request.getSession();
		setUserInSession(session, user);
	}
}
