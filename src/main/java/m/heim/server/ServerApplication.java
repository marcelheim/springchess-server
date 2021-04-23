package m.heim.server;

import m.heim.server.repository.GameRepository;
import m.heim.server.repository.UserRepository;
import m.heim.server.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerApplication {
	Logger logger = LoggerFactory.getLogger(ServerApplication.class);
	UserRepository userRepository;
	GameService gameService;
	GameRepository gameRepository;

	public ServerApplication(UserRepository userRepository, GameService gameService, GameRepository gameRepository) {
		this.userRepository = userRepository;
		this.gameService = gameService;
		this.gameRepository = gameRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner main(){
		return args -> {

		};
	}
}
