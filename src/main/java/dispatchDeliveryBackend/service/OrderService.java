package dispatchDeliveryBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dispatchDeliveryBackend.dao.OrderDao;
import dispatchDeliveryBackend.model.Order;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    
    public void addOrder(Order order) {
   	 orderDao.addOrder(order);
    }
}

