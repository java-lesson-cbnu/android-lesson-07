package kr.easw.lesson07;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class Lesson07Example {
public static void main(String[] args) {
        new SpringApplicationBuilder(Lesson07Example.class)
                .registerShutdownHook(true)
                .run(args);
    }
}
