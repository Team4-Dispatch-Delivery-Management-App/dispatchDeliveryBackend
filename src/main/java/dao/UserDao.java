package dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import model.User;
import model.Account;

@Repository
public class UserDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	public void addUser(User user) {
		//setEnabled： 给注册的用户设置状态
		user.getAccount().setStatus(true);
       
		//给新注册的用户分配权限，默认是“ROLE_USER”
		Account account = new Account();
		account.setType("ROLE_USER");
	
		Session session = null;
		
		
       // 保存用户
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public User getUserByName(String userName) {
		Account account = null;
		try (Session session = sessionFactory.openSession()) {
                    session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Account> criteriaQuery = builder.createQuery(Account.class);
			Root<Account> root = criteriaQuery.from(Account.class);
			criteriaQuery.select(root).where(builder.equal(root.get("email"), userName)); 
			account = session.createQuery(criteriaQuery).getSingleResult();
            session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (account != null) 
			return account.getUser();
		return null;
	}
	
	public User getUserById(int userId) { // UserId or AccountId?
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			User user = (User) session.get(User.class, userId);
			session.getTransaction().commit();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}


}
