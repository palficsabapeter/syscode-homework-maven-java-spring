package hu.syscode.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.syscode.entities.Student;
import hu.syscode.repositories.StudentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class StudentControllerIT {
	@Autowired
	private WebApplicationContext webApplicationContext;
	
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;
    
    @BeforeEach
    public void setUp() {
    	mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    	
    	try (Connection connection = DriverManager.getConnection(dataSourceUrl, username, password);
    		Statement statement = connection.createStatement()) {
               String cleanupSql = "DELETE FROM student";
               statement.executeUpdate(cleanupSql);
        } catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testCreateStudent() throws Exception {
        String name = "Teszt István";
        String email = "tesztistvan@mail.hu";
        Student testStudent = new Student();
        testStudent.setName(name);
        testStudent.setEmail(email);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty());

        UUID studentId = UUID.fromString(studentRepository.findAll().get(0).getId().toString());
        mockMvc.perform(MockMvcRequestBuilders
                .get("/students/" + studentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(studentId.toString()))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.address").value("Budapest, 1014, Fő utca 2"));
    }
}
