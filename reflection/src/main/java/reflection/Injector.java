package reflection;

import reflection.AutoInjectable;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Properties;

public class Injector {
    private Properties properties;

    public Injector() {
        this("config.properties");
    }

    public Injector(String configPath) {
        properties = new Properties();
        
        // Сначала ищем в classpath (работает и для тестов, и для main)
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(configPath)) {
            if (input != null) {
                properties.load(input);
                System.out.println("Config loaded from classpath: " + configPath);
                return;
            }
        } catch (IOException e) {
           
        }
        
        
        File configFile = new File(configPath);
        if (configFile.exists()) {
            try (InputStream input = new FileInputStream(configFile)) {
                properties.load(input);
                System.out.println("Config loaded from file: " + configFile.getAbsolutePath());
                return;
            } catch (IOException e) {
                throw new RuntimeException("Error loading config from: " + configPath, e);
            }
        }
        
        throw new RuntimeException("Config file not found: " + configPath);
    }

    public <T> T inject(T object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                Class<?> fieldType = field.getType();
                String interfaceName = fieldType.getName();
                String implementationClassName = properties.getProperty(interfaceName);
                
                if (implementationClassName == null || implementationClassName.trim().isEmpty()) {
                    throw new RuntimeException(
                        "No implementation configured for: " + interfaceName + 
                        ". Properties keys: " + properties.stringPropertyNames()
                    );
                }
                
                try {
                    System.out.println("Injecting " + interfaceName + " -> " + implementationClassName);
                    
                    Class<?> implementationClass = Class.forName(implementationClassName.trim());
                    Object implementationInstance = implementationClass.getDeclaredConstructor().newInstance();
                    
                    field.setAccessible(true);
                    field.set(object, implementationInstance);
                } catch (Exception e) {
                    throw new RuntimeException(
                        "Error creating instance of: " + implementationClassName, e
                    );
                }
            }
        }
        
        return object;
    }
}