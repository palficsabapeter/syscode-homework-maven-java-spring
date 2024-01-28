package hu.syscode.dtos;

import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class StudentDto {
	private UUID id;
	private String name;
	private String email;
	private String address;
}
