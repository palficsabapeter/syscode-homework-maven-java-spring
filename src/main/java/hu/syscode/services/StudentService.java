package hu.syscode.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.syscode.entities.Student;
import hu.syscode.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(UUID id) {
        return studentRepository.findById(id);
    }

    public Student createStudent(String name, String email) {
    	Student student = new Student();
    	student.setName(name);
    	student.setEmail(email);
        return studentRepository.save(student);
    }

    public Optional<Student> updateStudent(UUID id, String name, String email) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            Student savedStudent = existingStudent.get();
            savedStudent.setName(name);
            savedStudent.setEmail(email);
            return Optional.of(studentRepository.save(savedStudent));
        }
        return Optional.empty();
    }

    public void deleteStudent(UUID id) {
        studentRepository.deleteById(id);
    }
}
