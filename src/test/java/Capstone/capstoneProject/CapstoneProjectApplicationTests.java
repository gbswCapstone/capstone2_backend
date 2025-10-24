package Capstone.capstoneProject;

import Capstone.capstoneProject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EntityScan(basePackages = "Capstone.capstoneProject.entity")
@EnableJpaRepositories(basePackages = "Capstone.capstoneProject.repository")
class CapstoneProjectApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void repositoryLoads() {
		assert userRepository != null;
	}
}

