package hu.syscode.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import hu.syscode.entities.Address;

import java.util.UUID;

@Service
public class AddressService {	
	private static final Logger logger = LogManager.getLogger(AddressService.class);
	
	public Address getAddress() {
		logger.debug("Running getAddress");
		Address address = new Address();
		address.setId(UUID.randomUUID());
		address.setAddress("Budapest, 1014, Fő utca 2");
        return address;
    }
}
