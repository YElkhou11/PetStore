package pet.store.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.entity.Customer;

public interface CustomerDao extends JpaRepository<Customer, Long> {

	Set<Customer> findAllByCustomerIn(Set<PetStoreCustomer> customers);

}
