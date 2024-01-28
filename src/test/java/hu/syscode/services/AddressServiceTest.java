package hu.syscode.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import hu.syscode.entities.Address;

@SpringBootTest
public class AddressServiceTest {

	@InjectMocks
	private AddressService addressService;

	@Test
	void getAddress() {
		Address address = addressService.getAddress();
		assertEquals("Budapest, 1014, Fő utca 2", address.getAddress());
	}
}
