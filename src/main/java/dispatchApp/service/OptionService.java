package dispatchApp.service;

import dispatchApp.dao.CarrierDao;
import dispatchApp.external.GoogleMapAPIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dispatchApp.dao.OptionDao;
import dispatchApp.model.Option;

import dispatchApp.model.*;
import dispatchApp.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class OptionService {
	private final String DEFAULT_STATION = "San Francisco";
	private final String DEFAULT_DRONE = "DRONE";
	private final String DEFAULT_ROBOT = "ROBOT";
	private final GoogleMapAPIClient googleMapAPIClient = new GoogleMapAPIClient();
	private int userOptionCounter = 0;

	@Autowired
	private CarrierDao carrierDao;

	@Autowired
	private  OptionDao optionDao;


	public UserOption getDroneOption(String start, String end) {
		//create route

		Route stationToStart = googleMapAPIClient.getDroneRoute(DEFAULT_STATION, start);
		Route startToEnd = googleMapAPIClient.getDroneRoute(start, end);
		Route endToStation = googleMapAPIClient.getDroneRoute(end, DEFAULT_STATION);
		if (stationToStart == null || startToEnd == null || endToStation == null) {
			return null;
		}
		Route[] routes = new Route[]{stationToStart, startToEnd, endToStation};

		//add into heap


		return getUserOption(routes, DEFAULT_DRONE);
	}

	public UserOption getRobotOption(String start, String end) {

		Route stationToStart = googleMapAPIClient.getRobotRoute(DEFAULT_STATION, start);
		Route startToEnd = googleMapAPIClient.getRobotRoute(start, end);
		Route endToStation = googleMapAPIClient.getRobotRoute(end, DEFAULT_STATION);
		if (stationToStart == null || startToEnd == null || endToStation == null) {
			return null;
		}
		Route[] routes = new Route[]{stationToStart, startToEnd, endToStation};
		return getUserOption(routes, DEFAULT_ROBOT);
	}

	//get option with three routes and carrier type
	private UserOption getUserOption(Route[] routes, String carrierType) {
		Integer carrierID = carrierDao.getAvailableCarrierId(carrierType);

		//case1: no available carrier
		if (carrierID == null) {
			return null;
		}
		//case2: found available carrier
		Carrier carrier = carrierDao.getCarrierById(carrierID);

		carrierDao.setStatus(carrierID, "Busy");


		//add into heap

		double duration = 0;
		for (int i = 0; i < routes.length; i++) {
			duration += routes[i].getDuration();
		}
		float fee = getFee(duration, carrierType);


		//time points calculation
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date dateStart = new Date();
		Date dateDeparture = getTime(dateStart, routes[0].getDuration());
		Date dateDelivery = getTime(dateDeparture, routes[1].getDuration());
		Date dateEnd = getTime(dateDelivery, routes[2].getDuration());

		String startTime = formatter.format(dateStart);
		String departureTime = formatter.format(dateDeparture);
		String deliveryTime = formatter.format(dateDelivery);
		String endTime = formatter.format(dateEnd);


		Option option = new Option();
		option.setCarrier(carrier);
		option.setStartAddress(routes[1].getStartAddress());
		option.setEndAddress(routes[1].getEndAddress());
		option.setFee(fee);
		option.setDepartureTime(departureTime);
		option.setDeliveryTime(deliveryTime);
		option.setEndTime(endTime);

		optionDao.addOption(option);

		UserOption userOption = UserOption.builder()
				.option(option)
				.stationToStart(routes[0])
				.startToEnd(routes[1])
				.endToStation(routes[2])
				.startAddress(routes[1].getStartAddress())
				.endAddress(routes[1].getEndAddress())
				.stationId(option.getCarrier().getStationId())
				.carrierType(option.getCarrier().getCarrierType())
				.carrierId(option.getCarrier().getId())
				.startTime(startTime)
				.departureTime(departureTime)
				.deliveryTime(deliveryTime)
				.endTime(endTime)
				.build();

		return userOption;
	}

	private Date getTime(Date start, double duration) {
		Calendar c = Calendar.getInstance();
		c.setTime(start);
		int hour = (int) duration;
		int minute = (int) ((duration - hour) * 60);
		c.add(Calendar.HOUR, hour);
		c.add(Calendar.MINUTE, minute);
		return c.getTime();
	}


	private UserOption getUserOption(Route[] routes, Option option) {
		UserOption userOption = UserOption.builder()
				.id(userOptionCounter++)
				.option(option)
				.stationToStart(routes[0])
				.startToEnd(routes[1])
				.endToStation(routes[2])
				.carrierId(option.getCarrier().getId())
				.carrierType(option.getCarrier().getCarrierType())
				.build();
		return userOption;
	}

	private float getFee(double duration, String carrierType) {
		return carrierType.equals("DRONE") ?
				(float) (duration * Constants.DEFAULT_DRONE_COST) : (float) (duration * Constants.DEFAULT_ROBOT_COST);
	}

	public UserOption[] getUserOptions(String from, String to) {
		UserOption droneUserOption = getDroneOption(from, to);
		UserOption robotUserOption = getRobotOption(from, to);

		return new UserOption[]{droneUserOption, robotUserOption};
	}

	//add selected option
	public void addSelectedOption(int optionId){
		Option option = optionDao.getOptionById(optionId);
		carrierDao.setStatus(option.getCarrier().getId(), "Busy");
		//add into heap
	}

	public Option getOptionById(int id) {
		Option option = optionDao.getOptionById(id);
		return option;
	}

}