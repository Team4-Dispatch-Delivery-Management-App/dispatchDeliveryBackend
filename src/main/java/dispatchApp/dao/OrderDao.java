package dispatchApp.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dispatchApp.model.Option;
import dispatchApp.model.Order;

@Repository
public class OrderDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void addOrder(Order order) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.saveOrUpdate(order);
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
	
	public Order getOrderById(int orderId) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			Order order = (Order) session.get(Order.class, orderId);
			session.getTransaction().commit();
			return order;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

