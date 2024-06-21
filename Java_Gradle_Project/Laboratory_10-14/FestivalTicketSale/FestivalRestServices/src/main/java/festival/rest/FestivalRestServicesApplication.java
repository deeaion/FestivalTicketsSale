package festival.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:App.xml")
//@ComponentScan(basePackages = "festival.rest")
public class FestivalRestServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(FestivalRestServicesApplication.class, args);
    }

}
