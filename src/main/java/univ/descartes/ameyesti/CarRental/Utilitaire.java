package univ.descartes.ameyesti.CarRental;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Utilitaire extends Vehicule {
	
	protected int maxWeight;

	public int getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(int maxWeight) {
		this.maxWeight = maxWeight;
	}
		
	public Utilitaire(String plateNumber, int nbPlaces, double dailyPrice, double caution, String marque, String modele, int maxWeight) {
		super(plateNumber, nbPlaces, dailyPrice, caution, marque, modele);
		this.maxWeight = maxWeight;
	}

	public Utilitaire ( ) {
		super();
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
