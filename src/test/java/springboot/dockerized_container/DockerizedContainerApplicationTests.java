package springboot.dockerized_container;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DockerizedContainerApplicationTests {

	@Test
	void contextLoads() {
	}

}
