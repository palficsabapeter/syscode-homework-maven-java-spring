package hu.syscode.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.syscode.entities.Student;
import hu.syscode.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {
	private static final Logger logger = LogManager.getLogger(StudentService.class);

	@Autowired
	private StudentRepository studentRepository;

	public List<Student> getAllStudents() {
		logger.debug("Running getAllStudents");
		return studentRepository.findAll();
	}

	public Optional<Student> getStudentById(UUID id) {
		logger.debug("Running getStudentById");
		return studentRepository.findById(id);
	}

	public Student createStudent(String name, String email) {
		logger.debug("Running createStudent");
		Student student = new Student();
		student.setName(name);
		student.setEmail(email);
		return studentRepository.save(student);
	}

	public Optional<Student> updateStudent(UUID id, String name, String email) {
		logger.debug("Running updateStudent");
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
		logger.debug("Running deleteStudent");
		studentRepository.deleteById(id);
	}
}
