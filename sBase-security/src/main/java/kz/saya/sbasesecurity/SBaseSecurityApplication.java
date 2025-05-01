package kz.saya.sbasesecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "kz.saya.sbasecore",
        "kz.saya.sbasesecurity"
})
public class SBaseSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SBaseSecurityApplication.class, args);
    }

}
