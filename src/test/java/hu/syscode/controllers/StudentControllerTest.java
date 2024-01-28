package hu.syscode.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
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
import hu.syscode.services.StudentService;
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

	private UUID st1Id;
	private UUID notFoundId;
	private String name;
	private String modName;
	private String email;
	private String modEmail;
	private Student st1;
	private Student st1Mod;
	private Student st2;
	private Student st3;

	@BeforeEach
	public void setUp() {
		when(webClientBuilder.build()).thenReturn(webClient);
		when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);

		String mockJsonResponse = "{\"id\": \"a755a627-6d09-4c5f-af56-88c3b2d763c1\", \"address\": \"Budapest, 1014, Fő utca 2\"}";

		lenient().when(webClient.get()).thenReturn(reqHeadUriSpec);
		lenient().when(reqHeadUriSpec.uri(anyString())).thenReturn(reqHeadSpec);
		lenient().when(reqHeadSpec.header(anyString(), anyString())).thenReturn(reqHeadSpec);
		lenient().when(reqHeadSpec.retrieve()).thenReturn(resSpec);
		lenient().when(resSpec.bodyToMono(String.class)).thenReturn(Mono.just(mockJsonResponse));

		st1Id = UUID.fromString("957ac384-fab8-4fa0-992f-e28a63b32e3c");
		name = new String("Teszt István");
		modName = new String("Tesztes Isti");
		email = new String("tesztistvan@email.hu");
		modEmail = new String("tesztesisti@email.hu");
		st1 = new Student();
		st1.setId(st1Id);
		st1.setName(name);
		st1.setEmail(email);

		st1Mod = new Student();
		st1Mod.setId(st1Id);
		st1Mod.setName(modName);
		st1Mod.setEmail(modEmail);

		st2 = new Student();
		st2.setId(UUID.fromString("afe40d02-7044-41ed-b823-704b59648436"));
		st2.setName("Teszt Jakab");
		st2.setEmail("tesztjakab@email.hu");

		st3 = new Student();
		st3.setId(UUID.fromString("cbcc3639-2074-4aad-8bfa-711432437a22"));
		st3.setName("Teszt Kata");
		st3.setEmail("tesztkata@email.hu");

		List<Student> mockStudents = Arrays.asList(st1, st2, st3);
		lenient().when(studentService.getAllStudents()).thenReturn(mockStudents);

		notFoundId = UUID.fromString("00000000-0000-0000-0000-000000000000");
		lenient().when(studentService.getStudentById(eq(notFoundId))).thenReturn(Optional.empty());
		lenient().when(studentService.getStudentById(eq(st1Id))).thenReturn(Optional.of(st1));

		lenient().when(studentService.createStudent(eq(name), eq(email))).thenReturn(st1);

		lenient().when(studentService.updateStudent(eq(notFoundId), anyString(), anyString()))
				.thenReturn(Optional.empty());
		lenient().when(studentService.updateStudent(eq(st1Id), eq(modName), eq(modEmail)))
				.thenReturn(Optional.of(st1Mod));

		studentController = new StudentController(webClientBuilder);
		ReflectionTestUtils.setField(studentController, "studentService", studentService);
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAllStudents() {
		StudentDto stDto1 = new StudentDto();
		stDto1.setId(UUID.fromString("957ac384-fab8-4fa0-992f-e28a63b32e3c"));
		stDto1.setName("Teszt István");
		stDto1.setEmail("tesztistvan@email.hu");
		stDto1.setAddress("Budapest, 1014, Fő utca 2");

		StudentDto stDto2 = new StudentDto();
		stDto2.setId(UUID.fromString("afe40d02-7044-41ed-b823-704b59648436"));
		stDto2.setName("Teszt Jakab");
		stDto2.setEmail("tesztjakab@email.hu");
		stDto2.setAddress("Budapest, 1014, Fő utca 2");

		StudentDto stDto3 = new StudentDto();
		stDto3.setId(UUID.fromString("cbcc3639-2074-4aad-8bfa-711432437a22"));
		stDto3.setName("Teszt Kata");
		stDto3.setEmail("tesztkata@email.hu");
		stDto3.setAddress("Budapest, 1014, Fő utca 2");

		ResponseEntity<List<StudentDto>> responseEntity = studentController.getAllStudents();
		List<StudentDto> resBody = responseEntity.getBody();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(resBody);
		assertEquals(3, resBody.size());
		assertEquals(stDto1, resBody.get(0));
		assertEquals(stDto2, resBody.get(1));
		assertEquals(stDto3, resBody.get(2));
	}

	@Test
	void testGetStudentById() {
		// test with Nil UUID
		ResponseEntity<StudentDto> responseEntity = studentController.getStudentById(notFoundId);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());

		// test with existing UUID
		ResponseEntity<StudentDto> responseEntity2 = studentController.getStudentById(st1Id);
		StudentDto resBody = responseEntity2.getBody();

		assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
		assertNotNull(resBody);

		StudentDto stDto = new StudentDto();
		stDto.setId(st1Id);
		stDto.setName("Teszt István");
		stDto.setEmail("tesztistvan@email.hu");
		stDto.setAddress("Budapest, 1014, Fő utca 2");

		assertEquals(stDto, resBody);
	}

	@Test
	void testCreateStudent() {
		// test with missing params
		Map<String, String> reqBody = new HashMap<String, String>();
		ResponseEntity<Map<String, UUID>> responseEntity = studentController.createStudent(reqBody);
		Map<String, UUID> resBody = responseEntity.getBody();

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNull(resBody);

		// test with missing email param
		reqBody.put("name", name);

		responseEntity = studentController.createStudent(reqBody);
		resBody = responseEntity.getBody();

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNull(resBody);

		// test with wrong email param
		reqBody.put("email", "asdá$ł[Ä[");

		responseEntity = studentController.createStudent(reqBody);
		resBody = responseEntity.getBody();

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNull(resBody);

		// happy case test
		reqBody.remove("email");
		reqBody.put("email", email);

		responseEntity = studentController.createStudent(reqBody);
		resBody = responseEntity.getBody();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(resBody);
		Map<String, UUID> res = new HashMap<String, UUID>();
		res.put("id", st1Id);
		assertEquals(res, resBody);
	}

	@Test
	void testUpdateStudent() {
		// test with missing params
		Map<String, String> reqBody = new HashMap<String, String>();
		ResponseEntity<Map<String, UUID>> responseEntity = studentController.updateStudent(notFoundId, reqBody);
		Map<String, UUID> resBody = responseEntity.getBody();

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNull(resBody);

		// test with missing email param
		reqBody.put("name", modName);

		responseEntity = studentController.updateStudent(notFoundId, reqBody);
		resBody = responseEntity.getBody();

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNull(resBody);

		// test with wrong email param
		reqBody.put("email", "asdá$ł[Ä[");

		responseEntity = studentController.updateStudent(notFoundId, reqBody);
		resBody = responseEntity.getBody();

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNull(resBody);

		// test for not found id
		reqBody.remove("email");
		reqBody.put("email", modEmail);

		responseEntity = studentController.updateStudent(notFoundId, reqBody);
		resBody = responseEntity.getBody();

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(resBody);

		// happy case test
		responseEntity = studentController.updateStudent(st1Id, reqBody);
		resBody = responseEntity.getBody();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(resBody);
		Map<String, UUID> res = new HashMap<String, UUID>();
		res.put("id", st1Id);
		assertEquals(res, resBody);
	}
	
	@Test
	void testDeleteStudent() {
		// test with not found id
		ResponseEntity<Void> responseEntity = studentController.deleteStudent(notFoundId);

		assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());

		// test with existing id
		responseEntity = studentController.deleteStudent(st1Id);

		assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}
}
