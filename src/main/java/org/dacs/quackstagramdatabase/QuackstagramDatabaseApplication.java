package org.dacs.quackstagramdatabase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class QuackstagramDatabaseApplication {

    public static void main(String[] args) {
        // Disable headless mode
        System.setProperty("java.awt.headless", "false");
        ConfigurableApplicationContext context = SpringApplication.run(QuackstagramDatabaseApplication.class, args);
        Handler handler = context.getBean(Handler.class);
        handler.start();
    }

}
