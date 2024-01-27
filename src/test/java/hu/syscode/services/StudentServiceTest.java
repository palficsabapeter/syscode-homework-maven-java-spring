package hu.syscode.services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import hu.syscode.entities.Student;
import hu.syscode.repositories.StudentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(Collections.singletonList(new Student()));

        List<Student> students = studentService.getAllStudents();

        assertEquals(1, students.size());
    }

    @Test
    void testGetStudentById() {
        UUID studentId = UUID.randomUUID();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(new Student()));

        Optional<Student> student = studentService.getStudentById(studentId);

        assertTrue(student.isPresent());
    }

    @Test
    void testCreateStudent() {
    	when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
    		Student createdStudent = invocation.getArgument(0);
    		return createdStudent;
    	});
    	
    	Student student = studentService.createStudent("Teszt István", "tesztistvan@email.hu");
    	
    	assertEquals("Teszt István", student.getName());
    	assertEquals("tesztistvan@email.hu", student.getEmail());
    }
    
    @Test
    void testUpdateStudent() {
    	when(studentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
    	Optional<Student> emptyStudent = studentService.updateStudent(UUID.randomUUID(), "Teszt István", "tesztistvan@email.hu");
    	assertTrue(emptyStudent.isEmpty());
    	
    	when(studentRepository.findById(any(UUID.class))).thenReturn(Optional.of(new Student()));
    	when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
    		Student student = invocation.getArgument(0);
    		return student;
    	});
    	Optional<Student> updatedStudent = studentService.updateStudent(UUID.randomUUID(), "Teszt István", "tesztistvan@email.hu");
    	assertTrue(updatedStudent.isPresent());
    	assertEquals("Teszt István", updatedStudent.get().getName());
    	assertEquals("tesztistvan@email.hu", updatedStudent.get().getEmail());
    }
}

