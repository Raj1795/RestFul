package com.trios.collaborate.RestController;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trios.collaborate.dao.UserDAO;
import com.trios.collaborate.model.User;
import com.trios.collaborate.model.Error;

@Controller
public class UserController {

	@Autowired(required=true)
	UserDAO userDAO;
	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public	ResponseEntity<?>registeruser(@RequestBody User user){
		if(!userDAO.isUsernameValid(user.getUserId()))
		{
			Error error=new Error(2,"USERNAME ALREADY EXISTS");
			return new ResponseEntity<Error>(error,HttpStatus.NOT_ACCEPTABLE);
		}
		if(!userDAO.isEmailValid(user.getEmail())){
			Error error=new Error(3,"EMAIL ALREADY EXISTS");
			return new ResponseEntity<Error>(error,HttpStatus.NOT_ACCEPTABLE);
		}
		boolean result=userDAO.createUser(user);
		
		if(result){
			return new ResponseEntity<User>(user,HttpStatus.OK);
		}
		
		else{
			Error error= new Error(1,"ERROR IN CREATING USER");
			return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
			
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ResponseEntity<?>login(@RequestBody User user,HttpSession session)
	{
		User validUser=userDAO.login(user);
		if(validUser==null){
			Error error=new Error(4,"INVALID LOGIN DATA");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		validUser.setIsOnline("Online");
		userDAO.update(validUser);
		session.setAttribute("userId",validUser.getUserId());
		return new ResponseEntity<User>(validUser,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/logout",method=RequestMethod.PUT)
	public ResponseEntity<?>logout(HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		User user=userDAO.getUser(userId);
		user.setIsOnline("Offline");
		userDAO.update(user);
		session.removeAttribute("userId");
		session.invalidate();
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	@RequestMapping(value="/getUser",method=RequestMethod.GET)
	public ResponseEntity<?>getUser(HttpSession session){
		String userId=(String)session.getAttribute("userId");
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		User user=userDAO.getUser(userId);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	@RequestMapping(value="/updateUser",method=RequestMethod.PUT)
	public ResponseEntity<?>updateUser(@RequestBody User user,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		if(!userDAO.isupdateEmailValid(user.getEmail(),user.getUserId())){
			Error error=new Error(3,"EMAIL ALREADY EXISTS");
			return new ResponseEntity<Error>(error,HttpStatus.NOT_ACCEPTABLE);
		}
		try{
			userDAO.update(user);
			return new ResponseEntity<User>(user,HttpStatus.OK);
		}catch(Exception e){
			Error error=new Error(4,"UNABLE TO UPDATE ");
			return new  ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
