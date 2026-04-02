package org.dummy.calc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(value = "*")
public class App {
	public static void main(String[] args) {
		org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
		SpringApplication.run(App.class, args);
	}
}
