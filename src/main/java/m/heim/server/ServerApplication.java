package m.heim.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Eintrittsklasse in das Spring Boot Projekt
 */
@SpringBootApplication
public class ServerApplication {
	/**
	 * Eintrittspunkt in das Spring Boot Projekt
	 * @param args Kommandozeilenargumente
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
