package hu.syscode.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import hu.syscode.controllers.StudentController;
import hu.syscode.dtos.StudentDto;
import hu.syscode.entities.Student;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {
	@Mock
	private StudentService studentService;
	
	@Mock
	private WebClient webClient;

	@Mock
	private WebClient.Builder webClientBuilder;

	@Mock
	private RequestHeadersUriSpec reqHeadUriSpec;

	@Mock
	private RequestHeadersSpec reqHeadSpec;

	@Mock
	private ResponseSpec resSpec;

	private StudentController studentController;
	
	public void setUp() {
		when(webClientBuilder.build()).thenReturn(webClient);
		when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);

		String mockJsonResponse = "{\"id\": \"a755a627-6d09-4c5f-af56-88c3b2d763c1\", \"address\": \"Budapest, 1014, Fő utca 2\"}";

		when(webClient.get()).thenReturn(reqHeadUriSpec);
		when(reqHeadUriSpec.uri(anyString())).thenReturn(reqHeadSpec);
		when(reqHeadSpec.header(anyString(), anyString())).thenReturn(reqHeadSpec);
		when(reqHeadSpec.retrieve()).thenReturn(resSpec);
		when(resSpec.bodyToMono(String.class)).thenReturn(Mono.just(mockJsonResponse));
		
		studentController = new StudentController(webClientBuilder);
        ReflectionTestUtils.setField(studentController, "studentService", studentService);
	    MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAllStudents() {
		setUp();
		
		Student st1 = new Student();
		st1.setId(UUID.fromString("957ac384-fab8-4fa0-992f-e28a63b32e3c"));
		st1.setName("Teszt István");
		st1.setEmail("tesztistvan@email.hu");

		Student st2 = new Student();
		st2.setId(UUID.fromString("afe40d02-7044-41ed-b823-704b59648436"));
		st2.setName("Teszt Jakab");
		st2.setEmail("tesztjakab@email.hu");

		Student st3 = new Student();
		st3.setId(UUID.fromString("cbcc3639-2074-4aad-8bfa-711432437a22"));
		st3.setName("Teszt Kata");
		st3.setEmail("tesztkata@email.hu");

		List<Student> mockStudents = Arrays.asList(st1, st2, st3);
		lenient().when(studentService.getAllStudents()).thenReturn(mockStudents);

		ResponseEntity<List<StudentDto>> responseEntity = studentController.getAllStudents();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}
}
