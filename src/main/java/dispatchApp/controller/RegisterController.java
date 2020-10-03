package dispatchApp.controller;



import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;

import dispatchApp.model.Account;
import dispatchApp.model.Address;
import dispatchApp.model.User;
import dispatchApp.service.UserService;
@CrossOrigin("*")
@Controller
public class RegisterController {
	@Autowired
	private UserService userService;


	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody String json,
			BindingResult result) {
		System.out.println("new received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!");
		JSONObject jsonObj=new JSONObject(json);
		String email =  jsonObj.getString("email");
		String password =  jsonObj.getString("password");
		String lastName =  jsonObj.getString("lastName");
		String firstName = jsonObj.getString("firstName");
		User user = new User();
		
		System.out.println("test");
		Account account = new Account();
		account.setEmail(email);
		account.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setAccount(account);
		account.setUser(user);
		
		user.setBillingAddress("test");
		Address address = new Address();
		user.setAddress(address);
		address.setUser(user);
//		return new ResponseEntity<> ("Registered Successfully. Login using email and password.", HttpStatus.OK);
//		String userEmail = user.getAccount().getEmail();
//		System.out.println(userEmail);
//		System.out.println("received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!");
//		
//		if (result.hasErrors()) {
//			return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
//		}
		
		JSONObject res = new JSONObject();
		User userExist = userService.gerUserByName(email);
		if(userExist == null) {
			userService.addUser(user);
			res.put("Register Success", "Registered Successfully. Login using email and password.");
			return new ResponseEntity<> (res.toString(), HttpStatus.OK);
		}else {
			res.put("Register Failure", "The email has been registed. Please log in.");
			return new ResponseEntity<>(res.toString(), HttpStatus.BAD_REQUEST);
		}
		
	}

}
