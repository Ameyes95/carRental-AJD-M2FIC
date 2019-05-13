package univ.descartes.ameyesti.CarRental;

import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car,Long> {
	
	Car findByPlaque(String plaque);

}


