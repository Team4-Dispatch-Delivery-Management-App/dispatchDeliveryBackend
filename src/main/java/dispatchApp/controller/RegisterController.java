package dispatchApp.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import dispatchApp.model.User;
import dispatchApp.service.UserService;

public class RegisterController {
	@Autowired
	private UserService userService;


	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<String> registerUser(@ModelAttribute(value = "user") User user,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		String userEmail = user.getAccount().getEmail();
		User userExist = userService.gerUserByName(userEmail);
		if(userExist == null) {
			userService.addUser(user);
			return new ResponseEntity<> ("Registered Successfully. Login using email and password.", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("The email has been registed. Please log in.", HttpStatus.BAD_REQUEST);
		}
		
	}

}