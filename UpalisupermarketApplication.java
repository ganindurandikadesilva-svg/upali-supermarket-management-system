package lk.upalisupermarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;


@RestController//api implement krn mapping siyalla servlet container ekt add wenne me annotation eken

@SpringBootApplication
public class UpalisupermarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpalisupermarketApplication.class, args);


		System.out.println("Hello World");


		 
	}

	/* @RequestMapping("/dashboard")
	public String dashboard(){
		return "<h1>Hello</h1>";
	 } */



}
