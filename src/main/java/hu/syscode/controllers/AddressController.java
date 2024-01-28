package hu.syscode.controllers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.syscode.entities.Address;
import hu.syscode.services.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {
	private static final Logger logger = LogManager.getLogger(AddressController.class);
	
    @Autowired
    private AddressService addressService;
    
	@GetMapping
    public ResponseEntity<Address> getAddress() {
		logger.debug("Running getAddress");
        return ResponseEntity.ok(addressService.getAddress());
    }
}
