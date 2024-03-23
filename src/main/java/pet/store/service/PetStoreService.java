package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service

public class PetStoreService {

@Autowired
	
	private PetStoreDao petStoreDao;

@Autowired

	private CustomerDao customerDao;

@Autowired

private EmployeeDao employeeDao;

@Transactional(readOnly = false)
public PetStoreData savePetStore(PetStoreData petStoreData) {
	Long petStoreId = petStoreData.getPetStoreId();
	PetStore petStore = findOrCreatePetStore(petStoreId);
	
	setFieldsInPetStore(petStore, petStoreData);
	return new PetStoreData(petStoreDao.save(petStore));
}

private void setFieldsInPetStore(PetStore petStore, PetStoreData petStoreData) {
	petStore.setPetStoreId(petStoreData.getPetStoreId());
	petStore.setPetStoreName(petStoreData.getPetStoreName());
	petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
	petStore.setPetStoreCity(petStoreData.getPetStoreCity());
	petStore.setPetStoreState(petStoreData.getPetStoreState());
	petStore.setPetStoreZip(petStoreData.getPetStoreZip());
	petStore.setPetStorePhone(petStoreData.getPetStorePhone());
}

private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;
	
	if(Objects.isNull(petStoreId)) {
		petStore = new PetStore();
	}
	
	else {
		petStore = findPetStoreById(petStoreId);
	}
	
	return petStore;
}

private PetStore findPetStoreById(Long petStoreId) {
	return petStoreDao.findById(petStoreId)
			.orElseThrow(() -> new NoSuchElementException(
					"PetStore with ID=" + petStoreId + " does not exist."));
}

@Transactional(readOnly = true)
public List<PetStoreData> retrieveAllPetStore() {
	List<PetStore> petStores = petStoreDao.findAll();
	
	List<PetStoreData> result = new LinkedList<>(); 

		for (PetStore petstore : petStores) {

			PetStoreData psd = new PetStoreData (petstore);

			psd.getCustomers ().clear ();

			psd.getEmployees () .clear () ;

			result.add (psd) ;
		}
		
		return result;
		
	}

@Transactional(readOnly = true)
public PetStoreData retrievePetStoreById(Long petStoreId) {
	PetStore petStore = findPetStoreById(petStoreId);
	return new PetStoreData(petStore);
}

@Transactional(readOnly = false)
public void deletePetStoreById(Long petStoreId) {
	PetStore petStore = findPetStoreById(petStoreId);
	petStoreDao.delete(petStore);
	
}

@Transactional(readOnly = false)
public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
	PetStore petStore = findPetStoreById(petStoreId);
			Long employeeId = petStoreEmployee.getEmployeeId();
			Employee employee = findOrCreateEmployee(petStoreId, employeeId);
			copyEmployeeFields (employee, petStoreEmployee);
			employee.setPetStore(petStore);
			petStore.getEmployees().add(employee);
			Employee dbEmployee = employeeDao.save(employee);
			return new PetStoreEmployee (dbEmployee);

}

private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
	employee.setEmployeeId(petStoreEmployee.getEmployeeId());
	employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
	employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
	employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
	employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
}

private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
	Employee employee;
	
	if(Objects.isNull(employeeId)) {
		employee = new Employee();
	}
	
	else {
		employee = findEmployeeById(petStoreId, employeeId);
	}
	
	return employee;
}

private Employee findEmployeeById(Long petStoreId, Long employeeId) {
	return employeeDao.findById(employeeId)
			.orElseThrow(() -> new NoSuchElementException(
					"Employee with ID=" + employeeId + " does not exist."));
}

@Transactional(readOnly = false)
public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
	PetStore petStore = findPetStoreById(petStoreId);
			Long customerId = petStoreCustomer.getCustomerId();
			Customer customer = findOrCreateCustomer(petStoreId, customerId);
			copyCustomerFields (customer, petStoreCustomer);
			customer.getPetStores().add(petStore);
			petStore.getCustomers().add(customer);
			Customer dbCustomer = customerDao.save(customer);
			return new PetStoreCustomer (dbCustomer);
}

private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
	customer.setCustomerId(petStoreCustomer.getCustomerId());
	customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
	customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
	customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
}

private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
	Customer customer;
	
	if(Objects.isNull(customerId)) {
		customer = new Customer();
	}
	
	else {
		customer = findCustomerById(petStoreId, customerId);
	
	}
	
	return customer;

}

private Customer findCustomerById(Long petStoreId, Long customerId) {
	Customer customer = customerDao.findById(customerId)
			.orElseThrow(() -> new NoSuchElementException(
					"Customer with ID=" + customerId + " does not exist.")); 
	
	boolean found = false;
	
	for (PetStore petStore : customer.getPetStores()) {
		
		if(petStore.getPetStoreId() == petStoreId) {
		
			found = true;
			
			break;
		}
	}
		
	if(!found) {
		throw new IllegalArgumentException(" customer with ID=" + customerId 
				+ " is not a member of the petStore with ID=" + petStoreId);
	}
	
	return customer;
}
}


	



