package dispatchApp.utils;

import java.util.Comparator;
import java.util.PriorityQueue;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dispatchApp.model.Carrier;
import dispatchApp.model.Order;
import dispatchApp.service.OptionService;
import dispatchApp.service.OrderService;
import dispatchApp.utils.Element;
import lombok.Data;;
/*
 * Date & Time Class used here: https://www.joda.org/joda-time/
 * */
@Component
public @Data class HeapClean {
	@Autowired
	private OptionService optionService;
	@Autowired
	private OrderService orderService;
	
	private PriorityQueue<Element> userpq = new PriorityQueue<Element>(new userComparator());
	private PriorityQueue<Element> carrierpq = new PriorityQueue<Element>(new carrierComparator());
	private PriorityQueue<Element> departpq = new PriorityQueue<Element>(new departComparator());
	
	class userComparator implements Comparator<Element> {

		@Override
		public int compare(Element e1, Element e2) {
			if (e1.getDeliveryTime().equals(e2.getDeliveryTime())) {
				return 0;
			}
			return e1.getDeliveryTime().isBefore(e2.getDeliveryTime())? -1: 1;
		}
		
	}
	
	class carrierComparator implements Comparator<Element> {

		@Override
		public int compare(Element e1, Element e2) {
			if (e1.getEndTime().equals(e2.getEndTime())) {
				return 0;
			}
			return e1.getEndTime().isBefore(e2.getEndTime())? -1: 1;
		}
		
	}
	class departComparator implements Comparator<Element> {

		@Override
		public int compare(Element e1, Element e2) {
			if (e1.getDepartureTime().equals(e2.getDepartureTime())) {
				return 0;
			}
			return e1.getDepartureTime().equals(e2.getDepartureTime())? -1: 1;
		}
		
	}
	
	public void check() {
		DateTime currentTime = new DateTime(DateTimeZone.UTC);
//		DateTime currentTime = DateTime.now();
		// update depart status
		while (!departpq.isEmpty() && currentTime.isAfter( departpq.peek().getDepartureTime())) {
			Element depart = departpq.poll();
			Order order = orderService.getOrderById(depart.getOrderId());
			order.setStatus("Departed");
			orderService.updateOrder(order);
			
			Carrier carrier = orderService.getOrderById(depart.getOrderId()).getCarrier();
			carrier.setStatus("Departed");
			orderService.updateCarrier(carrier);
			
//			orderService.getOrderById(depart.getOrderId()).getCarrier().setStatus("Departed");
//			orderService.getOrderById(depart.getOrderId()).setStatus("Departed");
			System.out.println("order + carrier departed");
		}
		// update user related status: delivered
		while (!userpq.isEmpty() && currentTime.isAfter(userpq.peek().getDeliveryTime())) {
			Element finished = userpq.poll();
			Order order = orderService.getOrderById(finished.getOrderId());
			order.setStatus("Delivered");
			orderService.updateOrder(order);
			
			Carrier carrier = orderService.getOrderById(finished.getOrderId()).getCarrier();
			carrier.setStatus("Delivered");
			orderService.updateCarrier(carrier);
			
			System.out.println("order + carrier Delivered");
		}
		// update carrier related status:
		while (!carrierpq.isEmpty() && currentTime.isAfter(carrierpq.peek().getEndTime())) {
			Element available = carrierpq.poll();
			Carrier carrier = orderService.getOrderById(available.getOrderId()).getCarrier();
			carrier.setStatus("Available");
			orderService.updateCarrier(carrier);
//			orderService.getOrderById(available.getOrderId()).getCarrier().setStatus("Available");
			System.out.println("carrier Available");
		}
//		System.out.println(carrierpq.peek());
//		System.out.println(carrierpq.size());
		
	}
}
