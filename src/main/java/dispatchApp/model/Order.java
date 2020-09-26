package dispatchApp.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Entity
@Table(name = "order")
public @Data class Order implements Serializable{

	/**
	 * auto generate
	 */
	private static final long serialVersionUID = -100109796376862901L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	
	private String startAddress;
	private String endAddress;
	private String carrierType;
	private String deliveryTime; // user only care abput when their package arrive
	private float fee;
	private String status;
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private User user;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Carrier carrier;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Option option;
}
