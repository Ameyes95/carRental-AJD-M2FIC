package univ.descartes.ameyesti.CarRental;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Car extends Vehicule {
	
	private String type;
	
	public Car () {
		super();
	}

	public Car(String plaque, int nbPlaces, double dailyPrice, double caution, String marque, String modele, String type) {
		super(plaque, nbPlaces, dailyPrice, caution, marque, modele);
		this.type = type;
	}
	
	public String getPlaque() {
		return plaque;
	}
	public void setPlaque(String plaque) {
		this.plaque = plaque;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isAvailable(Date dateDebut, Date dateFin){

		boolean rep = false ;
		int compteur = 0 ;
		
		if(dateDebut.after(dateFin)){
			
			System.out.println("la date de début de location est postérieure à la date de fin");
		
		}else{ // la date de début est avant la date de fin dans la prochaine boucle

			for(Rent locationEnregistré : this.getRents()){
				
				if(dateFin.after(locationEnregistré.getBeginRent()) || (dateDebut.before(locationEnregistré.getEndRent()) && dateDebut.after(locationEnregistré.getBeginRent())))
					compteur ++ ;
			}
			
		}
		
		if (compteur==0)
			rep = true ;
		
		return rep ;
		
	}

}
