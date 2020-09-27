package dispatchApp.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "account")
public @Data class Account implements Serializable {
	private static final long serialVersionUID = 2681531852204068105L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String email;
	private String password;
	private boolean status;
	private String type;
	private float balance;

	@OneToOne(mappedBy = "account")
	private User user;
	

}
