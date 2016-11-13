package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		
		// TODO - implement newPost 27:43 in video
		HttpSession session = request.getSession();
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		User author = getUserFromSession(session);
		
		if (title != null && title != "")
		{
			if (body != null && body != "")
			{
				Post newPost = new Post (title, body, author);
				postDao.save(newPost);
				return "redirect:/blog/" + author + "/" + newPost.getUid(); // TODO - this redirect should go to the new post's page
			}
			else
			{
				model.addAttribute("error", "There is no text in the body of your post");
			}
		}
		else
		{
			model.addAttribute("error", "There is no title entered.");
		}
		
		return "redirect:/newpost";  // TODO - this redirect should go to the new post's page  		
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		// TODO - implement singlePost 30:30 in video
		Post post = postDao.findByUid(uid);
		model.addAttribute("post", post);
		
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		
		// TODO - implement userPosts 30:50 in video
		User user = userDao.findByUsername(username);
		List<Post> postsByUser = postDao.findByAuthor(user);
		
		model.addAttribute("posts", postsByUser);
		
		return "blog";
	}
	
}
