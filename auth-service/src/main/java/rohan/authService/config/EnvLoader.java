package rohan.authService.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class EnvLoader implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();


        File envFile = new File(".env");

        if (envFile.exists()) {
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(envFile));

                Map<String, Object> envMap = new HashMap<>();
                properties.forEach((key, value) -> {
                    envMap.put(key.toString(), value);
                });

                MapPropertySource propertySource = new MapPropertySource("envFileProperties", envMap);
                environment.getPropertySources().addFirst(propertySource);

                System.out.println("✅ .env file loaded successfully!");
            } catch (IOException e) {
                System.err.println("❌ Error loading .env file: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️  .env file not found. Using system environment variables.");
        }
    }
}