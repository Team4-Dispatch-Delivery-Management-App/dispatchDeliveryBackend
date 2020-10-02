package dispatchApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



import dispatchApp.model.User;
import dispatchApp.service.UserService;


@CrossOrigin("*")
@Controller
public class LoginController {
	@Autowired
	private UserService userService;

	

	@RequestMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody String json ) {
		System.out.println("new login received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!");
		JSONObject jsonObj=new JSONObject(json);
		String email =  jsonObj.getString("email");
		String password =  jsonObj.getString("password");
		User userExist = userService.gerUserByName(email);
		JSONObject result = new JSONObject();
		if(userExist == null) {
			result.put("fail1","Login failed! User do not exist!");
			return new ResponseEntity<>(result.toString(), HttpStatus.UNAUTHORIZED);
		} else if(!password.equals(userExist.getAccount().getPassword())){
			result.put("fali2","Login failed! Password is incorrect!");
			return new ResponseEntity<>(result.toString(),HttpStatus.UNAUTHORIZED);}
		result.put("email", email);
		result.put("firstName", userExist.getFirstName());
		result.put("lastName", userExist.getLastName());
		return new ResponseEntity<>(result.toString(),HttpStatus.OK);
	    }
	
	@RequestMapping(value = "Login/logout")
	public ResponseEntity<String> logout(){
		System.out.println("new logout received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!received!!!!!!!!!");
		
		JSONObject result = new JSONObject();
		result.put("logout", "Logout successful.");
		return new ResponseEntity<>(result.toString(), HttpStatus.OK);
		
	}
}
