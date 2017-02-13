package edu.sjsu.cmpe275.lab2;

import edu.sjsu.cmpe275.lab2.domain.Address;
import edu.sjsu.cmpe275.lab2.domain.Phone;
import edu.sjsu.cmpe275.lab2.domain.User;
import edu.sjsu.cmpe275.lab2.repositories.PhoneRepository;
import edu.sjsu.cmpe275.lab2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import java.util.Arrays;

@SpringBootApplication
public class SpringMvcJpaApplication extends SpringBootServletInitializer implements CommandLineRunner {
//public class SpringMvcJpaApplication {


	@Autowired
	UserRepository userRepository;
	@Autowired
	PhoneRepository phoneRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringMvcJpaApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringMvcJpaApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {
		// User 1 with 2 phones
		User joji = new User();
		joji.setId("1");
		joji.setFirstname("Joji");
		joji.setLastname("Kubota");
		joji.setTitle("Project Manager");
		Address jojiAdd = new Address();
		jojiAdd.setStreet("123 Main St.");
		jojiAdd.setCity("San Jose");
		jojiAdd.setState("CA");
		jojiAdd.setZip("90998");
		joji.setAddress(jojiAdd);
		Phone jojiPhone1 = new Phone();
		jojiPhone1.setId("A");
		jojiPhone1.setNumber("123-456-7890");
		jojiPhone1.setDescription("Work phone");
		jojiPhone1.setAddress(jojiAdd);
		jojiPhone1.getUsers().add(joji);
		phoneRepository.save(jojiPhone1);
		Phone jojiPhone2 = new Phone();
		jojiPhone2.setId("B");
		jojiPhone2.setNumber("987-654-3210");
		jojiPhone2.setDescription("Home phone");
		jojiPhone2.setAddress(jojiAdd);
		jojiPhone2.getUsers().add(joji);
		phoneRepository.save(jojiPhone2);
		joji.setPhones(Arrays.asList(new Phone[] {jojiPhone1, jojiPhone2}));
		userRepository.save(joji);

		// User 2 with 2 phones shounak gujarathi
		User shounak = new User();
		shounak.setId("2");
		shounak.setFirstname("Shounak");
		shounak.setLastname("Gujarathi");
		shounak.setTitle("Senior Engineer");
		Address shounakAdd = new Address();
		shounakAdd.setStreet("555 Central Ave.");
		shounakAdd.setCity("San Francisco");
		shounakAdd.setState("CA");
		shounakAdd.setZip("99889");
		shounak.setAddress(shounakAdd);
		Phone shounakPhone1 = new Phone();
		shounakPhone1.setId("C");
		shounakPhone1.setNumber("888-999-4455");
		shounakPhone1.setDescription("Home phone");
		shounakPhone1.setAddress(shounakAdd);
		shounakPhone1.getUsers().add(shounak);
		phoneRepository.save(shounakPhone1);
		Phone shounakPhone2 = new Phone();
		shounakPhone2.setId("D");
		shounakPhone2.setNumber("222-333-2222");
		shounakPhone2.setDescription("Cell phone");
		shounakPhone2.setAddress(shounakAdd);
		shounakPhone2.getUsers().add(shounak);
		phoneRepository.save(shounakPhone2);
		shounak.setPhones(Arrays.asList(new Phone[] {shounakPhone1, shounakPhone2}));
		userRepository.save(shounak);

		Phone standalonePhone = new Phone();
		standalonePhone.setId("XYZ");
		standalonePhone.setNumber("101-010-1100");
		standalonePhone.setDescription("iPhone");
		standalonePhone.setAddress(jojiAdd);
		phoneRepository.save(standalonePhone);

	}

}
