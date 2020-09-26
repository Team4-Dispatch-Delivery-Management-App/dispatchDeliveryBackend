package model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

public @Data class Carrier implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1438320128221365451L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String carrierType; // update
	private int stationId;
	private int speed;
	private String status;
	
	@OneToOne
	private Order order;
	
	@OneToOne
	private Option option;
}
