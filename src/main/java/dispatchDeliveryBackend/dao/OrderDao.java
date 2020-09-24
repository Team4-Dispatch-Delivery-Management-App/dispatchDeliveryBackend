package dispatchDeliveryBackend.dao;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import dispatchDeliveryBackend.model.Order;


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

	public double getOrderFee(Order order) {// assume Option have fee calculated 
		
	}
	public void update(Order order) {//Assume we call getOrderFee in this method
		double total = getOrderFee(order);
		order.setTotalPrice(total);

		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.saveOrUpdate(order);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public Order validate(int orderId) throws IOException{ // if order !=null && order.getOption()!= null : call update
		Order order = getOrderById(orderId);
		if (order !=null && order.getOption()!= null) {
			throw new IOException(orderId + "");
		}
		update(order);
		return order;

	}
	public Order getOrderById(int orderID) {
		Order order = null;
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			order = (Order) session.get(Order.class, orderID);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order;
	}


}
