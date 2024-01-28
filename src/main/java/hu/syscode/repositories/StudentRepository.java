package hu.syscode.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import hu.syscode.entities.Student;

public interface StudentRepository extends JpaRepository<Student, UUID> {
}
