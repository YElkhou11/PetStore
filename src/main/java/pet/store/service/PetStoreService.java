package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
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
	List<PetStoreData> response = new LinkedList<>();
	
	
	for(PetStore petStore : petStores) {
		
		response.add(new PetStoreData(petStore));
	}
		
		return response;
	
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
}


