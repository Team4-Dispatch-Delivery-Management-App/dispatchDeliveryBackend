package dispatchDeliveryBackend.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Order")
public class Order {
	private static final long serialVersionUID = -6571020025726257848L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	private User user;

	@OneToOne
	private Carrier carrier;

	@OneToOne
	private Option option;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	public void setOption(Option option) {
		this.option = option;
	}
	public Option getOption() {
		return option;
	}
	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}
	public Carrier getCarrier() {
		return carrier;
	}
	public float setTotalPrice() {
		
	}

}
