package be.icc.pid.reservations_springboot.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import be.icc.pid.reservations_springboot.model.Type;



public interface TypeRepository extends CrudRepository<Type, Long> {
	Type findByType(String type);
	Optional<Type> findById(long id);
}

