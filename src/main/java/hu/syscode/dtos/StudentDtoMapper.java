package hu.syscode.dtos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import hu.syscode.entities.Address;
import hu.syscode.entities.Student;
import hu.syscode.dtos.StudentDto;

@Mapper
public interface StudentDtoMapper {
	StudentDtoMapper INSTANCE = Mappers.getMapper(StudentDtoMapper.class);
	
	@Mappings({
	  @Mapping(source = "student.id", target = "id"),
	  @Mapping(source = "student.name", target = "name"),
	  @Mapping(source = "student.email", target = "email"),
	  @Mapping(source = "address.address", target = "address")
	})
    StudentDto convert(Student student, Address address);
}
