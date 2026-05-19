package Capstone.capstoneProject;

import Capstone.capstoneProject.config.QuerydslConfiguration;
import Capstone.capstoneProject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
class CapstoneProjectApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void repositoryLoads() {
		assertThat(userRepository).isNotNull();
	}
}
