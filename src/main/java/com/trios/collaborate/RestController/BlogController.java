package com.trios.collaborate.RestController;



import java.util.Date;
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


import com.trios.collaborate.dao.BlogDAO;
import com.trios.collaborate.dao.UserDAO;
import com.trios.collaborate.model.Blog;
import com.trios.collaborate.model.BlogComment;
import com.trios.collaborate.model.Error;
import com.trios.collaborate.model.User;

@Controller
public class BlogController {
	
	@Autowired(required=true)
	BlogDAO blogDAO;

	@Autowired(required=true)
	UserDAO userDAO;
	
	@RequestMapping(value="/createBlog",method=RequestMethod.POST)
	public ResponseEntity<?>createBlog(@RequestBody Blog blog,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		blog.setPostedOn(new Date());
		User postedBy=userDAO.getUser(userId);
		blog.setPostedBy(postedBy);
		try{
			blogDAO.createBlog(blog);
			return new ResponseEntity<Blog>(blog,HttpStatus.OK);
		}catch(Exception e){
			Error error=new Error(7,"Unable to Post the Blog");
			return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value="/getBlogs/{approved}",method=RequestMethod.GET)
	public ResponseEntity<?>getBlogs(@PathVariable int approved,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		List<Blog>blogs=blogDAO.getBlogs(approved);
		System.out.println("Returning");
		return new ResponseEntity<List<Blog>>(blogs,HttpStatus.OK);
		
	}
	@RequestMapping(value="/getBlogById/{blogId}",method=RequestMethod.GET)
	public ResponseEntity<?>getBlogById(@PathVariable int blogId,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		Blog blog=blogDAO.getBlog(blogId);
		return new ResponseEntity<Blog>(blog,HttpStatus.OK);
	}
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	public ResponseEntity<?>updateBlog(@RequestBody Blog blog,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		if(!blog.isApproved() && blog.getRejectionReason()==null)
			blog.setRejectionReason("Not Mentioned");
		blogDAO.updateBlog(blog);
		return new ResponseEntity<Blog>(blog,HttpStatus.OK);
	}
	@RequestMapping(value="/addComment",method=RequestMethod.POST)
	public ResponseEntity<?>addBlogComment(@RequestBody BlogComment blogComment,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		User user=userDAO.getUser(userId);
		blogComment.setCommentedBy(user);
		blogComment.setCommentedOn(new Date());
		try{
			blogDAO.addBlogComment(blogComment);
			return new ResponseEntity<BlogComment>(blogComment,HttpStatus.OK);
		}catch(Exception e){
			Error error=new Error(7,"Unable to Post the BlogComment");
			return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value="/getComments/{Id}",method=RequestMethod.GET)
	public ResponseEntity<?>getComments(@PathVariable int Id,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
	List<BlogComment>blogComments=blogDAO.getBlogComments(Id);
		return new  ResponseEntity<List<BlogComment>>(blogComments,HttpStatus.OK);
	}
	@RequestMapping(value="/getnotification",method=RequestMethod.GET)
	   public ResponseEntity<?> getNotification(HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			System.out.println("PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
			List<Blog> blogNotification=blogDAO.getNotification(userId);
			System.out.println(blogNotification);
			return new ResponseEntity<List<Blog>>(blogNotification,HttpStatus.OK);
	   }
}
