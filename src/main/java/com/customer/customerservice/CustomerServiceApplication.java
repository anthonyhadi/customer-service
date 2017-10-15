package com.customer.customerservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;
import java.util.stream.Stream;

@EnableDiscoveryClient
@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}
}

@RestController
@RefreshScope
class MessageRestController {

	private final String value;

	@Autowired
	public MessageRestController(@Value("${message}") String value) {
		this.value = value;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/message")
	String read() {
		return this.value;
	}
}

@Component
class SampleDataCLR implements CommandLineRunner {

	private final CustomerRepository customerRepository;

	@Autowired
	public SampleDataCLR(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		Stream.of("anthonyhadi,Anthony,Hadi", "yennifebriati,Yenni,Febriati", "susanwijayaa,Susan,Wijaya")
				.map(tpl -> tpl.split(","))
				.forEach(tpl -> customerRepository.save(new Customer(tpl[0], tpl[1], tpl[2])));
		customerRepository.findAll().forEach(System.out::println);
	}
}

@RepositoryRestResource
interface CustomerRepository extends JpaRepository<Customer, Long> {

}

@Entity
class Customer {

	@Id
	@GeneratedValue
	private Long id;
	private String username;
	private String firstName;
	private String lastName;

	public Customer() {
	}

	public Customer(String username, String firstName, String lastName) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Customer{" +
				"id=" + id +
				", username='" + username + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				'}';
	}
}
