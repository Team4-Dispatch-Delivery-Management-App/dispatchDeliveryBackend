package dispatchApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dispatchApp.dao.OrderDao;
import dispatchApp.model.Order;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    
    public void addOrder(Order order) {
   	 	orderDao.addOrder(order);
    }
    
    public Order getOrderById(int orderId) {
    	return orderDao.getOrderById(orderId);
    }
    public List<Order> getHistoryById(int userId) {
    	return orderDao.getOrderByUserId(userId);
    }
}