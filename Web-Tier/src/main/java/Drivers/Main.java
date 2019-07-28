package Drivers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import DataPersistence.Logger;
import Drivers.LoadBalancer;

@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		
		Logger.getLogger().log("Server is Up");
		SpringApplication.run(Main.class, args);
		
		Logger.getLogger().log("Starting load balancer");
		LoadBalancer load = new LoadBalancer();
		load.loadBalance();

	}
}
