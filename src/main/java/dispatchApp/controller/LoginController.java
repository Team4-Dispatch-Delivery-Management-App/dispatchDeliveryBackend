package dispatchApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import dispatch.model.User;
import dispatch.service.UserService;



@Controller
public class HomePageController {
	@Autowired
	private UserService userService;

	

	@RequestMapping(value = "/login")
	public ResponseEntity<?> login(@RequestParam(value = "email")  String userEmail, @RequestParam(value = "password") String password ) {
		
		User userExist = userService.gerUserByName(userEmail);
		String passwordExist = userExist.getAccount().getPassword();
		if(userExist == null) {
			return new ResponseEntity<>("Login failed! User do not exist!", HttpStatus.UNAUTHORIZED);//用户不存在
		} else if(!password.equals(passwordExist)){
			return new ResponseEntity<>("Login failed! Password is incorrect!",HttpStatus.UNAUTHORIZED);// 密码错误	
		}
		return new ResponseEntity<>(userService.gerUserByName(userEmail),HttpStatus.OK);// 以json格式将user给前端；
	}
	
	@RequestMapping(value = "/logout")
	public ResponseEntity<String> logout(){
		return new ResponseEntity<>("Logout successful.", HttpStatus.OK);
		
	}
}
