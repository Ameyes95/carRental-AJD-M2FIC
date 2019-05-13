package univ.descartes.ameyesti.CarRental;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Person {
	
	@Id
	private String idDriverLicense; // Numero de permis de conduire
	private String firstName;
	private String lastName;
	private String nationality;
	
	public Person(String id, String fName, String lastName, String nationality) {
		super();
		this.idDriverLicense = id;
		this.firstName = fName;
		this.lastName = lastName;
		this.nationality = nationality;
	}
	
	
	public Person () {
		super();
	}
	
	
	@Id
	public String getId() {
		return idDriverLicense;
	}


	public void setId(String id) {
		this.idDriverLicense = id;
	}

	public String getfirstName() {
		return firstName;
	}

	public void setfirstName(String name) {
		this.firstName = name;
	}

	public String getLastName() {
		return lastName;
	}


	public String getNationality() {
		return nationality;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public void setNationality(String nationality) {
		this.nationality = nationality;
	}


	public String toString () {
		return "Nom : "+this.getLastName()+"\rPrenom : "+this.getfirstName()+"\rNumero de permis : "+this.idDriverLicense+"\rNationalité : "+this.getNationality();
	}


}
