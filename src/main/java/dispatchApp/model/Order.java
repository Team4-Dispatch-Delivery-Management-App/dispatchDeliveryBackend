package dispatchApp.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Entity
@Table(name = "orders")
public @Data class Order implements Serializable{

	/**
	 * auto generate
	 */
	private static final long serialVersionUID = -100109796376862901L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne()
	private User user;
	
	@OneToOne()
	private Option option;
	
	@ManyToOne()
	private Carrier carrier;
	
	private String deliveryTime; // user only care abput when their package arrive
	
	private String startAddress;
	private String endAddress;
	private String startTime;
	private String externalUserEmail;
	
	private float fee;
	private String status;
	
	
	
	
	
}
