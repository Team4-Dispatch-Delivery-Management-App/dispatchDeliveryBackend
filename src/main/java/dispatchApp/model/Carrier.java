package dispatchApp.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
@Entity
@Table(name="carrier")
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
	private double speed;
	private String status;
	
	@OneToOne(mappedBy="carrier")
	@JsonIgnore
	private Order order;
	
	@OneToOne(mappedBy="carrier")
	@JsonIgnore
	private Option option;
}
