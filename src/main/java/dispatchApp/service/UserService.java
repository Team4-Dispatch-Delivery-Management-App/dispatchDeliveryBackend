package dispatchApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dispatchApp.dao.UserDao;
import dispatchApp.model.User;

@Service
public class UserService {
	@Autowired
    private UserDao userDao;
	
	public void addUser(User user) {
		userDao.addUser(user);
	}
	
	public User gerUserByName(String userName) {
	   	 return userDao.getUserByName(userName);
	    }
	
	public User gerUserById(int userId) {
	   	 return userDao.getUserById(userId);
	    }

}


