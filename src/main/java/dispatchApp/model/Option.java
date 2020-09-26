package dispatchApp.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Table(name="option")
public @Data class Option implements Serializable{
	
	/**
	 * auto generate
	 */
	private static final long serialVersionUID = 6947716960780322088L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	
	private String startAddress;
	private String endAddress;
	private int carrierId; // 自己分配
	private String carrierType;
	
	private String startTime;
	private String departureTime;
	private String deliveryTime; // 
	private String endTime;
	private float weight;
	
	private float fee;
	
	@OneToOne
	private User user;
	
	@OneToOne
	private Carrier carrier;
	
	@OneToOne
	private Order order;
}