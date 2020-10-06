package dispatchApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dispatchApp.dao.CarrierDao;
import dispatchApp.dao.OrderDao;
import dispatchApp.model.Carrier;
import dispatchApp.model.Order;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private CarrierDao carrierDao;
    
    public void addOrder(Order order) {
   	 	orderDao.addOrder(order);
    }
    
    public Order getOrderById(int orderId) {
    	return orderDao.getOrderById(orderId);
    }
    public List<Order> getHistoryById(String userEmail) {
    	return orderDao.getOrderByUserEmail(userEmail);
    }
    public void updateOrder(Order order) {
    	orderDao.updateOrder(order);
    }
    public void updateCarrier(Carrier carrier) {
    	carrierDao.updateCarrier(carrier);
    }
}