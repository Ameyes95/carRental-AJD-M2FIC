package univ.descartes.ameyesti.CarRental;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;



@Entity
public class Rent {
	
	@Id
	private String id;
	private Date beginRent;
	private Date endRent;
	private Person person;
	private Vehicule vehicule;
	private double price;
	
	public Rent(String id, Date beginRent, Date endRent, Vehicule vehicule, Person pers) {
		super();
		this.id = id;
		this.beginRent = beginRent;
		this.endRent = endRent;
		this.vehicule = vehicule;
		this.setPrice((getDifferenceDays(beginRent,endRent) * vehicule.dailyPrice));
		person = pers ;
	}
	
	public Rent() {
		super ();
	}
	
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getBeginRent() {
		return beginRent;
	}
	public void setBeginRent(Date beginRent) {
		this.beginRent = beginRent;
	}
	public Date getEndRent() {
		return endRent;
	}
	public void setEndRent(Date endRent) {
		this.endRent = endRent;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@ManyToOne
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	public Vehicule getVehicule() {
		return vehicule;
	}

	public void setVehicule(Vehicule vehicule) {
		this.vehicule = vehicule;
	}

	public static long getDifferenceDays(Date d1, Date d2) {
	    long diff = d2.getTime() - d1.getTime();
	    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

}
