package dispatchApp.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dispatchApp.model.Account;
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
			session.save(order);
			System.out.print("Here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//			System.out.print(order.toString());
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

	

	public List<Order> getOrderByUserEmail(String userEmail) {
		List<Order> res = new ArrayList<>();
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Order> criteriaQuery = builder.createQuery(Order.class);
			Root<Order> root = criteriaQuery.from(Order.class);
			criteriaQuery.select(root).where(builder.equal(root.get("externalUserEmail"), userEmail)); 
			res =  session.createQuery(criteriaQuery).getResultList();
			
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}
	public void updateOrder(Order order) {
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
	
}
