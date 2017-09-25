package com.trios.collaborate.RestController;

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
	
}
