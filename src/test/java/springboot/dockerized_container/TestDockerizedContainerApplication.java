package springboot.dockerized_container;

import org.springframework.boot.SpringApplication;

public class TestDockerizedContainerApplication {

	public static void main(String[] args) {
		SpringApplication.from(DockerizedContainerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
