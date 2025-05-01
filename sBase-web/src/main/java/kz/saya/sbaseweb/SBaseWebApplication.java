package kz.saya.sbaseweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "kz.saya.sbasecore",
        "kz.saya.sbasesecurity",
        "kz.saya.sbaseweb"
})
public class SBaseWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SBaseWebApplication.class, args);
    }

}
