package hu.syscode.services;

import org.springframework.stereotype.Service;

import hu.syscode.entities.Address;

import java.util.UUID;

@Service
public class AddressService {	
	public Address getAddress() {
		Address address = new Address();
		address.setId(UUID.randomUUID());
		address.setAddress("Budapest, 1014, Fő utca 2");
        return address;
    }
}
