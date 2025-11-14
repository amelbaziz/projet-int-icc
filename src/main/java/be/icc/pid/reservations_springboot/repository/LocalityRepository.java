package be.icc.pid.reservations_springboot.repository;

import org.springframework.data.repository.CrudRepository;

import be.icc.pid.reservations_springboot.model.Locality;

public interface LocalityRepository extends CrudRepository<Locality, Long> {
	Locality findByPostalCode(String postalCode);
	Locality findByLocality(String locality);
}

