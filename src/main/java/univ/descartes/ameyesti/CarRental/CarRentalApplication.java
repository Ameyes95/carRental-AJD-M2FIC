package univ.descartes.ameyesti.CarRental;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CarRentalApplication {
	
	private static final Logger log = LoggerFactory.getLogger(CarRentalApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CarRentalApplication.class, args);
	}
	
	
	@Bean
	public CommandLineRunner demo(PersonRepository repository) {
		return (args) -> {
			
		 /* -------------------------> Initialiser un client au lancement de l'application
			Person client = new Person("1","Ameyes","Ticilia","Française");
			repository.save(client);
			
			-------------------------> Initialiser une voiture au lancement de l'application	
			
			Car voiture = new Car ("75LDC19",5,20.0,2500.0,"Audi","RS6","Break supersport");
			
			-------------------------> Initialiser une location au lancement de l'application 
					---------------------(Executez au préalable les deux précédentes étapes)
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Rent location = new Rent(dateFormat.parse("2019-05-08"),dateFormat.parse("2019-05-15"),voiture,client);

			voiture.getRents().add(location);
			garagePrincipal.save(voiture);

			*/

		};
	}
	

}

