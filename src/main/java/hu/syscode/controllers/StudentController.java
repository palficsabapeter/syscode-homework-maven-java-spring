package hu.syscode.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hu.syscode.entities.Student;
import hu.syscode.services.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable UUID id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> createStudent(@RequestBody Map<String, String> studentData) {
    	String name = studentData.get("name");
        String email = studentData.get("email");
        
        if (name != null && email != null) {
        	if (this.validateEmail(email)) {
        		Student createdStudent = studentService.createStudent(name, email);
                Map<String, UUID> response = new HashMap<String, UUID>();
                response.put("id", createdStudent.getId());
                return ResponseEntity.ok(response);
        	} else {
        		return ResponseEntity.badRequest().build();
        	}
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, UUID>> updateStudent(@PathVariable UUID id, @RequestBody Map<String, String> studentData) {
        String name = studentData.get("name");
        String email = studentData.get("email");
        Optional<Student> student = Optional.empty();
        
        if (name != null && email != null) {
        	if (this.validateEmail(email)) {
        		student = studentService.updateStudent(id, name, email);
        	} else {
        		return ResponseEntity.badRequest().build();
        	}
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
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
    
    private static boolean validateEmail(String email) {
    	// regex from https://emailregex.com/
    	String emailRegex = new String("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    	return Pattern.compile(emailRegex)
    			.matcher(email)
    			.matches();
    }
}
