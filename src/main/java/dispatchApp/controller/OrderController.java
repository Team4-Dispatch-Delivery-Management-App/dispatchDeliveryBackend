package dispatchApp.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	@RequestMapping(value = "/order/{optionId}/user/{userId}", method = RequestMethod.GET) // check

	public String createOrder(@PathVariable("optionId") int optionId, @PathVariable("userId") int userId) {

		Order order = new Order();
		Option option = optionService.getOptionById(optionId);
		Carrier carrier = option.getCarrier();
		User user = userService.gerUserById(userId);

		order.setOption(option);
		order.setUser(user);
		order.setStartAddress(option.getStartAddress());
		order.setEndAddress(option.getEndAddress());
		order.setDeliveryTime(option.getDeliveryTime());
		order.setCarrierType(option.getCarrierType());
		order.setFee(option.getFee());
		// There are 3 status of order: Pending, Departed, Delivered
		// There are 4 status of carrier: pending, Departed, Delivered
		order.setStatus("Pending");
		carrier.setStatus("Pending");

		// update delivery date
		orderService.addOrder(order);
		/*TODO add the function to update the time when place order*/
		
		// heap addition
		template.getCarrierpq().add(new Element.ElementBuilder().orderId(order.getId()).carrierId(option.getCarrierId())
				.endTime(new DateTime(option.getEndTime())).build());
		template.getDepartpq().add(new Element.ElementBuilder().orderId(order.getId()).carrierId(option.getCarrierId())
				.departureTime(new DateTime(option.getDepartureTime())).build());
		template.getUserpq().add(new Element.ElementBuilder().orderId(order.getId()).carrierId(option.getCarrierId())
				.deliveryTime(new DateTime(option.getDeliveryTime())).build());
		return "redirect:/checkout?optionId=" + optionId;
	}
}
