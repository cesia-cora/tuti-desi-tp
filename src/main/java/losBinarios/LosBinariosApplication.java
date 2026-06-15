package losBinarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "losBinarios",
        "entidades",
        "accesoDatos",
        "servicios",
        "presentacion"
})
@EntityScan(basePackages = "entidades")
@EnableJpaRepositories(basePackages = "accesoDatos")
public class LosBinariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(LosBinariosApplication.class, args);
    }
}
