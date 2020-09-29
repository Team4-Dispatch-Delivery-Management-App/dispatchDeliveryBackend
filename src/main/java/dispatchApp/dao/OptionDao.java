
package dispatchApp.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dispatchApp.model.Option;


@Repository
public class OptionDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void addOption(Option option) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.saveOrUpdate(option);
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
	
	public Option getOptionById(int optionId) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			Option option = (Option) session.get(Option.class, optionId);
			session.getTransaction().commit();
			return option;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
