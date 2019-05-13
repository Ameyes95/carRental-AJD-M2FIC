package univ.descartes.ameyesti.CarRental;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person,Long> {
	
	Person findById(String id);

}
