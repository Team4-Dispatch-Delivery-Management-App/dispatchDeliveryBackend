package dispatchApp.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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
//import static dispatchApp.utils.Element.*;
import dispatchApp.utils.HeapClean;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@Controller
public class OrderController {

	@Autowired
	private OptionService optionService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private HeapClean template;

	@RequestMapping(value = "Login/order/user", method = RequestMethod.POST) // check

	public ResponseEntity<?> createOrder(@RequestBody String json){

		JSONObject jsonObj=new JSONObject(json);
		int optionId =  jsonObj.getInt("optionID");
		String userEmail = jsonObj.getString("email");
		
		Order order = new Order();
		Option option = optionService.getOptionById(optionId);
		Carrier carrier = option.getCarrier();
		User user = userService.gerUserByName(userEmail);

		order.setOption(option);
		order.setUser(user);
		order.setStartAddress(option.getStartAddress());
		order.setEndAddress(option.getEndAddress());
		order.setDeliveryTime(option.getDeliveryTime());
		order.setCarrier(option.getCarrier());
		order.setFee(option.getFee());
		order.setExternalUserEmail(user.getAccount().getEmail());
		// There are 3 status of order: Pending, Departed, Delivered
		// There are 4 status of carrier: pending, Departed, Delivered
		order.setStatus("Pending");
		//DateTime dt = new DateTime();
		
		order.setStartTime(DateTime.now().toString());
		carrier.setStatus("Pending");

		// update delivery date
		orderService.addOrder(order);
		/*TODO add the function to update the time when place order*/
		
		//heap addition
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		DateTime endTime = formatter.parseDateTime(option.getEndTime());
		
		DateTime departureTime = formatter.parseDateTime(option.getDepartureTime());
		DateTime deliveryTime = formatter.parseDateTime(option.getDeliveryTime());
//		
//		System.out.println(order.getId());
//		System.out.println(option.getCarrier().getId());
//		System.out.println(endTime.toString("yyyy-MM-dd HH:mm"));
		
		Element carrierElement = Element.builder().orderId(order.getId()).carrierId(option.getCarrier().getId())
				.endTime(endTime).build();
		Element departElement = Element.builder().orderId(order.getId()).carrierId(option.getCarrier().getId())
				.departureTime(departureTime).build();
		Element userElement = Element.builder().orderId(order.getId()).carrierId(option.getCarrier().getId())
				.deliveryTime(deliveryTime).build();
		
//		Element carrierElement = new Element.ElementBuilder().orderId(order.getId()).carrierId(option.getCarrier().getId())
//				.endTime(endTime).build();
//		System.out.println(carrierElement.toString());
		
		
		template.getCarrierpq().add(carrierElement);
		template.getDepartpq().add(departElement);
		template.getUserpq().add(userElement);
		
//		System.out.print(template.getCarrierpq().peek());
//		System.out.print(template.getDepartpq().peek());
//		System.out.print(template.getUserpq().peek());
		
		JSONObject result = new JSONObject();		
		result.put("Your order id", order.getId());		
		return new ResponseEntity<>(result.toString(), HttpStatus.OK);	}
	@RequestMapping(value = "Login/history/user", method = RequestMethod.POST)
	public ResponseEntity<?> getHistory(@RequestBody String json) {
		//ResponseEntity<?> ret = null;
		JSONObject jsonObj = new JSONObject(json);
		String userEmail = jsonObj.getString("email");
		List<Order> res = orderService.getHistoryById(userEmail);
		JSONArray te = new JSONArray();

		for (Order o : res) {
			JSONObject temp = new JSONObject();
			temp.put("Order Id", o.getId());
			temp.put("From", o.getStartAddress());
			temp.put("To", o.getEndAddress());
			temp.put("Fee", o.getFee());
			temp.put("Carrier Type", o.getCarrier().getCarrierType());
			temp.put("Status", o.getStatus());
			temp.put("StartTime", o.getStartTime());
			te.put(temp);			
		}
	
		return new ResponseEntity<>(te.toString(), HttpStatus.OK);

		
	}
}
