package univ.descartes.ameyesti.CarRental;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public abstract class Vehicule {

	@Id
	protected String plaque;
	protected int nombrePlaces;
	protected double dailyPrice;
	protected double caution ;
	protected String marque ;
	protected String modele ;
	@OneToMany(mappedBy="vehicule", cascade = {CascadeType.ALL}) 
	@JsonIgnore
	private List <Rent> rents = new ArrayList<Rent>();

	
	public Vehicule () {
		super();
	}

	public Vehicule(String plaque, int nombrePlaces, double dailyPrice, double caution, String marque, String modele) {
		super();
		this.plaque = plaque;
		this.dailyPrice = dailyPrice;
		this.caution = caution;
		this.marque = marque;
		this.modele = modele;
		this.nombrePlaces = nombrePlaces;
	}

	public String getMarque() {
		return this.marque;
	}

	public void setMarque(String marque) {
		this.marque = marque;
	}
	
	public void setModel(String modele) {
		this.modele = modele;
	}
	
	public String getModele() {
		return this.modele;
	}

	public String getPlateNumber() {
		return this.plaque;
	}

	public void setPlateNumber(String plateNumber) {
		this.plaque = plateNumber;
	}

	public List<Rent> getRents() {
		return this.rents;
	}

	public void setRents(List<Rent> rents) {
		this.rents = rents;
	}

	public int getNombrePlaces() {
		return this.nombrePlaces;
	}
	public void setNombrePlaces(int nombrePlaces) {
		this.nombrePlaces = nombrePlaces;
	}

	public double getDailyPrice() {
		return dailyPrice;
	}

	public double getCaution() {
		return caution;
	}

	public void setDailyPrice(double dailyPrice) {
		this.dailyPrice = dailyPrice;
	}

	public void setCaution(double caution) {
		this.caution = caution;
	}

	abstract public boolean isAvailable(Date dateDebut, Date dateFin);
}
