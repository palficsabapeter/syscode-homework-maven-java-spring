package hu.syscode.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import hu.syscode.controllers.AddressController;
import hu.syscode.dtos.StudentDto;
import hu.syscode.entities.Address;
import hu.syscode.entities.Student;
import hu.syscode.services.AddressService;

@ExtendWith(MockitoExtension.class)
public class AddressControllerTest {
	@Mock
	private AddressService addressService;
	
	@InjectMocks
	private AddressController addressController;
	
	@Test
	void testGetAddress() {
		Address addr = new Address();
		addr.setId(UUID.fromString("957ac384-fab8-4fa0-992f-e28a63b32e3c"));
		addr.setAddress("Budapest, 1014, Fő utca 2");
		
		when(addressService.getAddress()).thenReturn(addr);

		ResponseEntity<Address> responseEntity = addressController.getAddress();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}
}
