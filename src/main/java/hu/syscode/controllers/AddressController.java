package hu.syscode.controllers;

import java.util.List;

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
	
    @Autowired
    private AddressService addressService;
    
	@GetMapping
    public ResponseEntity<Address> getAddress() {
        return ResponseEntity.ok(addressService.getAddress());
    }
}
