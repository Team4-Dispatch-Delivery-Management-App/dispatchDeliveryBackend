package dispatchApp.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;
import dispatchApp.model.Carrier;
import dispatchApp.model.Option;
import dispatchApp.model.Order;
import dispatchApp.model.User;
import dispatchApp.service.OptionService;
import dispatchApp.service.OrderService;
import dispatchApp.service.UserService;
import dispatchApp.utils.Element;
import dispatchApp.utils.HeapClean;

@Controller
public class OrderController {

	@Autowired
	private OptionService optionService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	private HeapClean template;

	@RequestMapping(value = "Login/order/{optionId}/user/{userId}", method = RequestMethod.GET) // check

	public ResponseEntity<String> createOrder(@PathVariable("optionId") int optionId, @PathVariable("userId") int userId) {

		Order order = new Order();
		Option option = optionService.getOptionById(optionId);
		Carrier carrier = option.getCarrier();
		User user = userService.gerUserById(userId);

		order.setOption(option);
		order.setUser(user);
		order.setStartAddress(option.getStartAddress());
		order.setEndAddress(option.getEndAddress());
		order.setDeliveryTime(option.getDeliveryTime());
		order.setCarrier(option.getCarrier());
		order.setFee(option.getFee());
		order.setExternalUserId(user.getId());
		// There are 3 status of order: Pending, Departed, Delivered
		// There are 4 status of carrier: pending, Departed, Delivered
		order.setStatus("Pending");
		//DateTime dt = new DateTime();
		
		order.setStartTime(DateTime.now().toString());
		carrier.setStatus("Pending");

		// update delivery date
		orderService.addOrder(order);
		/*TODO add the function to update the time when place order*/
		
		// heap addition
		//template.getCarrierpq().add(new Element.ElementBuilder().orderId(order.getId()).carrierId(option.getCarrier().getId())
				//.endTime(new DateTime(option.getEndTime())).build());
		//template.getDepartpq().add(new Element.ElementBuilder().orderId(order.getId()).carrierId(option.getCarrier().getId())
				//.departureTime(new DateTime(option.getDepartureTime())).build());
		//template.getUserpq().add(new Element.ElementBuilder().orderId(order.getId()).carrierId(option.getCarrier().getId())
				//.deliveryTime(new DateTime(option.getDeliveryTime())).build());
		JSONObject result = new JSONObject();		
		result.put("Your order id", order.getId());		
		return new ResponseEntity<>(result.toString(), HttpStatus.OK);	}
	@RequestMapping(value = "Login/history/user/{userId}", method = RequestMethod.GET)
	public ResponseEntity<String> getHistory(@PathVariable("userId") int userId) {
		//ResponseEntity<?> ret = null;
		List<Order> res = orderService.getHistoryById(userId);
		JSONArray te = new JSONArray();

		for (Order o : res) {
			JSONObject temp = new JSONObject();
			temp.put("Order Id", o.getId());
			temp.put("From", o.getStartAddress());
			temp.put("To", o.getEndAddress());
			temp.put("Fee", o.getFee());
			temp.put("Carrier Type", o.getCarrier().getCarrierType());
			temp.put("Status", o.getStatus());
			temp.put("", o.getStartTime());
			te.put(temp);			
		}
	
		return new ResponseEntity<>(te.toString(), HttpStatus.OK);

		
	}
}
