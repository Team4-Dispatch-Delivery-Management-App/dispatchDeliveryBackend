package dispatchApp.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Table(name="address")
public @Data class Address implements Serializable{

	/**
	 * auto generation
	 */
	private static final long serialVersionUID = 5452467653824560048L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String address;
	private String city;
	private String state;
	private int zipcode;
	private String country;
	
	@OneToOne(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
	private User user;
}
