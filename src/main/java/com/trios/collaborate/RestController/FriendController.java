package com.trios.collaborate.RestController;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trios.collaborate.dao.FriendDAO;
import com.trios.collaborate.model.Error;
import com.trios.collaborate.model.Friend;
import com.trios.collaborate.model.User;

@Controller
public class FriendController {
	@Autowired
	FriendDAO friendDAO;
	
	@RequestMapping(value="/getsuggestedusers",method=RequestMethod.GET)
	public ResponseEntity<?>getsuggestedusers(HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		List<User>suggestedUser=friendDAO.getFriends(userId);
		return new  ResponseEntity<List<User>>(suggestedUser,HttpStatus.OK);
	}
	
	@RequestMapping(value="/friendRequest/{toId}",method=RequestMethod.GET)
	public ResponseEntity<?>friendRequest(@PathVariable String toId,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		Friend friend=new Friend();
		friend.setFromId(userId);
		friend.setToId(toId);
		friend.setStatus('P');
		friendDAO.friendRequest(friend);
		return new  ResponseEntity<Friend>(friend,HttpStatus.OK);
	}

	@RequestMapping(value="/pendingRequest",method=RequestMethod.GET)
	public ResponseEntity<?>pendingRequest(HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		List<Friend>pendingRequest=friendDAO.pendingRequest(userId);
		return new  ResponseEntity<List<Friend>>(pendingRequest,HttpStatus.OK);
	}
	@RequestMapping(value="/updateRequest",method=RequestMethod.PUT)
	public ResponseEntity<?>updateRequest(@RequestBody Friend friend,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
			}
		friendDAO.updateRequest(friend);
		return new  ResponseEntity<Friend>(friend,HttpStatus.OK);
		
	}
	
	
}
