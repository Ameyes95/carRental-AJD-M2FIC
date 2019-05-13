package univ.descartes.ameyesti.CarRental;

import java.util.Date;
import java.text.SimpleDateFormat;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import org.json.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;

@RestController
public class LocationServices {

	int nombreDeLocations = 0 ;
	
	@Autowired
	PersonRepository clientRepository; // Répertoire de clients
	
	@Autowired
	CarRepository garage; // Répertoire des voitures
	
/*-----------------------------------------------------Afficher les voitures--------------------------------------------------------------*/
	@RequestMapping(value = "/cars", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String listOfCars(){

		String listOfCars = "";
		
		for (Car gov : garage.findAll()){
			
			listOfCars = listOfCars + printCar(gov.getPlaque())+"<br><br>";	
		}

		return listOfCars ;
	
	}
	
/*-----------------------------------------------------Afficher les clients--------------------------------------------------------------*/
	
	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String listOfClient(){

		String listOfClient = "";
		
		for (Person client : clientRepository.findAll()){
			
			listOfClient = listOfClient + client.toString()+"\n\n";	
		}

		return listOfClient ;
	
	}
	
/*-------------------------------------------------------Afficher les locations enregistrés par voiture------------------------------------------------------------*/	
	
	@RequestMapping(value = "/rentsbycar/{plateNumber}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String listOfRentsByDriver(@PathVariable("plateNumber") String plaque){
		
		String listOfRents = "";
	
		for (Rent location : garage.findByPlaque(plaque).getRents()){
			
			listOfRents = listOfRents + printLocation(location)+"<hr width='100%' color='red'>";	
		}
		
		return listOfRents ;
	
	}
	
/*--------------------------------------------------------Afficher une voiture-----------------------------------------------------------*/	
	
	/**
	 * @param plaque
	 * @return String descritpif du véhicule
	 * @throws Exception
	 */
	@RequestMapping(value = "/cars/{plaque}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String aCar(@PathVariable("plaque") String plaque) throws Exception{
		
			return printCar(plaque);
		
		}
	
/*---------------------------------------------------------Supprimer une voiture----------------------------------------------------------*/	
	
	/**
	 * @param plaque
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cars/{plaque}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public String deleteCar(@PathVariable("plaque") String plaque) throws Exception{

		String rep = "Voiture non trouvée";
		
		if (garage.findByPlaque(plaque) != null){
			Car car = garage.findByPlaque(plaque);
			garage.delete(car);
			rep = ("Voiture supprimée");
		}
		return rep ;
	}
	
/*----------------------------------------------------------Supprimer un client--------------------------------------------------------*/	
	
	/**
	 * @param numeroPermis
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/customers/{idDriverLicense}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public String deleteCustomer(@PathVariable("idDriverLicense") String numeroPermis) throws Exception{

		String rep = "Client non trouvée";
		
		if (clientRepository.findById(numeroPermis) != null){
			Person client = clientRepository.findById(numeroPermis);
			clientRepository.delete(client);
			rep = ("Le client "+ client.getfirstName()+ " "+client.getLastName()+ " d'identifiant "+client.getId()+" a été supprimé de la base.");
		}
		return rep ;
	}
	
/*----------------------------------------------------------Mettre à jour prix d'un véhicule-------------------------------------------------------*/
	
	/**
	 * @param plaque (par URL)
	 * @param price (Par Body)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cars/prixjour/{plaque}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public String rent(@PathVariable("plaque") String plaque, @RequestBody String price) throws Exception{
	
				String rep = "Voiture non trouvée";
		
		if (garage.findByPlaque(plaque) != null){
			garage.findByPlaque(plaque).setDailyPrice((Double.parseDouble(price)));
			garage.save(garage.findByPlaque(plaque));
			rep = ("Mise à jour du prix effectuée");
		}
		return rep ;
	}
	
	@RequestMapping(value = "/cars/add", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public String addCar(@RequestBody Car car) throws Exception{

		String rep = "" ;
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContextJMS.xml");
		QueueConnectionFactory factory = (QueueConnectionFactory) applicationContext.getBean("connectionFactory");
		
		Queue queue = (Queue) applicationContext.getBean("queue");
		
		// Create a connection. See https://docs.oracle.com/javaee/7/api/javax/jms/package-summary.html	
		QueueConnection connection = factory.createQueueConnection();
		// Open a session without transaction and acknowledge automatic
		QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		// Start the connection
		connection.start();
		// Create a sender	
		QueueSender sender = session.createSender(queue);
		
		if (garage.findByPlaque(car.getPlaque()) == null){
			
			garage.save(car);
		// Create a message (ou un objet)
		Message message = session.createTextMessage("Votre "+car.getMarque()+" "+car.getModele()+" immatriculée "+car.getPlaque()+" à été ajouté !");
		System.out.println(message);
		// Send the message
		sender.send(message);
		// Close the session
		session.close();
		// Close the connection
		connection.close();
		
		rep = ("Votre "+car.getMarque()+" "+car.getModele()+" immatriculée "+car.getPlaque()+" à été ajouté !");
		
		}else{
			// Create a message (ou un objet)
			Message message = session.createTextMessage("Votre "+car.getMarque()+" "+car.getModele()+" immatriculée "+car.getPlaque()+" est déja dans la base !");
			System.out.println(message);
			// Send the message
			sender.send(message);
			// Close the session
			session.close();
			// Close the connection
			connection.close();
			
			rep = "Votre "+car.getType()+" "+car.getModele()+" immatriculée "+car.getPlaque()+" est déja dans la base !";
			
		}
		
		return rep ;
	}
	
/*---------------------------------------------------------Ajouter un client----------------------------------------------------------*/	
	
	/**
	 * @param body (Objet client JSON)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/customers/add", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public String addClient(@RequestBody String body) throws Exception{

		String rep = "" ;
		JSONObject infos = new JSONObject(body);
		Person client = new Person(infos.getString("idDriverLicense"),infos.getString("firstName"),infos.getString("lastName"),infos.getString("nationality"));
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContextJMS.xml");
		QueueConnectionFactory factory = (QueueConnectionFactory) applicationContext.getBean("connectionFactory");
		
		Queue queue = (Queue) applicationContext.getBean("queue");
		
		// Create a connection. See https://docs.oracle.com/javaee/7/api/javax/jms/package-summary.html	
		QueueConnection connection = factory.createQueueConnection();
		// Open a session without transaction and acknowledge automatic
		QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		// Start the connection
		connection.start();
		// Create a sender	
		QueueSender sender = session.createSender(queue);
		
		if (clientRepository.findById(client.getId()) == null){
			
			clientRepository.save(client);
		// Create a message (ou un objet)
		Message message = session.createTextMessage("Le client "+client.getfirstName()+" "+client.getLastName()+" a été ajouté !");
		System.out.println(message);
		// Send the message
		sender.send(message);
		// Close the session
		session.close();
		// Close the connection
		connection.close();
		
		rep = ("Le client "+client.getfirstName()+" "+client.getLastName()+" a été ajouté !");
		
		}else{
			// Create a message (ou un objet)
			Message message = session.createTextMessage("Un client possèdant le numéro de permis "+client.getId()+" a déja été enregistré");
			System.out.println(message);
			// Send the message
			sender.send(message);
			// Close the session
			session.close();
			// Close the connection
			connection.close();
			
			rep = "Un client possèdant le numéro de permis "+client.getId()+" a déja été enregistré";
			
		}
		
		return rep ;
	}
	
/*---------------------------------------------------------Enregistrer une location----------------------------------------------------------*/

	/**
	 * @param objJson représentant un objet RENT
	 * @return String resultat de la requête
	 * @throws Exception
	 */
	@RequestMapping(value = "/rents/add", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public String rentACar(@RequestBody String objJson) throws Exception{

		JSONObject infos = new JSONObject(objJson);
		String plaque = infos.getString("plaque");
		String idConducteur = infos.getString("idDriverLicense");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date debutLoc = dateFormat.parse(infos.getString("dateDebut"));
		Date finLoc = dateFormat.parse(infos.getString("dateFin"));
		
		Car car = garage.findByPlaque(plaque);
		Person loueur = clientRepository.findById(idConducteur);
		
		String rep = "";
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContextJMS.xml");
		QueueConnectionFactory factory = (QueueConnectionFactory) applicationContext.getBean("connectionFactory");
		
		Queue queue = (Queue) applicationContext.getBean("queue");
		
		// Create a connection. See https://docs.oracle.com/javaee/7/api/javax/jms/package-summary.html	
				QueueConnection connection = factory.createQueueConnection();
				// Open a session without transaction and acknowledge automatic
				QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
				// Start the connection
				connection.start();
				// Create a sender	
				QueueSender sender = session.createSender(queue);
				// Create a message (ou un objet)
	
	if (car != null && loueur != null && car.isAvailable(debutLoc, finLoc)){
		
		Message message = session.createTextMessage("Demande de location enregistrée");
		// Send the message
		sender.send(message);
		// Close the session
		session.close();
		// Close the connection
		connection.close();
		rep = "Demande de location enregistrée";
		
					nombreDeLocations ++ ;
					Rent newRent = new Rent(Integer.toString(nombreDeLocations), debutLoc,finLoc,car,loueur);			
					car.getRents().add(newRent);
					
				    garage.save(car);
					
		}else{
		
			if(car == null){

				rep = "Le véhicule est introuvable dans le garage";
			
			}
			
			if(loueur == null){
			
				rep = "Le loueur est inconnu";
			
			}
			
			if(car.isAvailable(debutLoc, finLoc) == false){

				rep = "Le véhicule n'est pas disponible pour les dates choisies";
			}
			
		}		
		return rep ;
	}

/*-------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * @param String plaque (immatriculation du véhicule)
	 * @return String affichage détaillé des informations d'un véhicule donné
	 */
	public String printCar(String plaque){

		String rep = "Voiture non trouvée";
		
		if (garage.findByPlaque(plaque) != null){
			Car car = garage.findByPlaque(plaque);
			rep = ("Marque : "+car.getMarque()+"<br>Modèle :"+car.getModele()+"<br>Plaque : "+plaque+"<br>Prix/jour : "+car.getDailyPrice()+" \u20AC");
		}
		return rep;
		
	}
	
	/**
	 * @param location (objet RENT représentant une location)
	 * @return String affichage détaillé des informations d'une location enregistrées
	 */
	public String printLocation(Rent location){
		
		String rep = ("<ul><li>Loueur : <ul>"+location.getPerson().toString()+"</ul></<li><li>Véhicule : <ul>"+printCar(location.getVehicule().getPlateNumber())+"</ul></<li><li>Date de début de location : "+location.getBeginRent()+"</li><li>Date de fin de location : "+location.getEndRent()+"</li><li>Prix total : "+location.getPrice()+" \u20AC</li></ul>");
		return rep;
		
	}
}