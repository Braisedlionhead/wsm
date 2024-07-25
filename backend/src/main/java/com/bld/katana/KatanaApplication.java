package com.bld.katana;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.bld")
public class KatanaApplication {


    public static void main(String[] args) {
        SpringApplication.run(KatanaApplication.class,args);
    }
}
