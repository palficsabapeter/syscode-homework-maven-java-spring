package hu.syscode.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.syscode.entities.Address;
import hu.syscode.entities.Student;
import hu.syscode.services.StudentService;
import hu.syscode.dtos.StudentDto;
import hu.syscode.dtos.StudentDtoMapper;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/students")
public class StudentController {
	private static final Logger logger = LogManager.getLogger(StudentController.class);
	
	private final WebClient webClient;
    
    @Value("${spring.security.user.name}")
    private String addressAuthUser;

    @Value("${spring.security.user.password}")
    private String addressAuthPw;

    @Autowired
    private StudentService studentService;
    
    
	public StudentController(WebClient.Builder webClientBuilder) {
		webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
	}

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
    	logger.debug("Running getAllStudents");
    	
    	String jsonAddrRes = webClient.get()
                .uri("/address")
                .header("Authorization", StudentController.getBasicAuthenticationHeader(addressAuthUser, addressAuthPw))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    	
    	List<Student> students = studentService.getAllStudents();
    	List<StudentDto> studentDtos = new ArrayList<StudentDto>();
    	
    	ObjectMapper om = new ObjectMapper();
    	Optional<Address> address = Optional.empty();
    	try {
    		address = Optional.of(om.readValue(jsonAddrRes, Address.class));
    		if (address.isEmpty()) return ResponseEntity.notFound().build();
    		for (Student student : students) {
        		StudentDto studentDto = StudentDtoMapper.INSTANCE.convert(student, address.get());
        		studentDtos.add(studentDto);
        	}
    		
    		return ResponseEntity.ok(studentDtos);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
                
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable UUID id) {
    	logger.debug("Running getStudentById");
    	String jsonAddrRes = webClient.get()
                .uri("/address")
                .header("Authorization", StudentController.getBasicAuthenticationHeader(addressAuthUser, addressAuthPw))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    	
        Optional<Student> student = studentService.getStudentById(id);
        
        ObjectMapper om = new ObjectMapper();
    	Optional<Address> address = Optional.empty();
    	try {
    		address = Optional.of(om.readValue(jsonAddrRes, Address.class));
    		if (student.isEmpty() || address.isEmpty()) return ResponseEntity.notFound().build();
    		StudentDto studentDto = StudentDtoMapper.INSTANCE.convert(student.get(), address.get());
    		
    		return ResponseEntity.ok(studentDto);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> createStudent(@RequestBody Map<String, String> studentData) {
    	logger.debug("Running createStudent");
    	String name = studentData.get("name");
        String email = studentData.get("email");
        
        if (nameAndEmailValid(name, email)) {
        	Student createdStudent = studentService.createStudent(name, email);
            Map<String, UUID> response = new HashMap<String, UUID>();
            response.put("id", createdStudent.getId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, UUID>> updateStudent(@PathVariable UUID id, @RequestBody Map<String, String> studentData) {
    	logger.debug("Running updateStudent");
        String name = studentData.get("name");
        String email = studentData.get("email");
        Optional<Student> student = Optional.empty();
        
        if (nameAndEmailValid(name, email)) {
        	student = studentService.updateStudent(id, name, email);
        } else {
        	return ResponseEntity.badRequest().build();
        }
        
        if (student.isPresent()) {
        	Student updatedStudent = student.get();
        	Map<String, UUID> response = new HashMap<String, UUID>();
        	response.put("id", updatedStudent.getId());
        	return ResponseEntity.ok(response);
        } else {
        	return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID id) {
    	logger.debug("Running deleteStudent");
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
    
    private static boolean nameAndEmailValid(String name, String email) {
    	if (name == null || email == null) {
    		return false;
    	}
    	
    	// regex from https://emailregex.com/
    	String emailRegex = new String("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    	return Pattern.compile(emailRegex)
    			.matcher(email)
    			.matches();
    }
    
    private static String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}
