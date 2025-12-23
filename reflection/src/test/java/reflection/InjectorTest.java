package reflection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class InjectorTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
    
    @Test
    void testSimpleInjection() {
        // Создаем тестовые свойства в памяти
        Properties testProperties = new Properties();
        testProperties.setProperty("reflection.SomeInterface", "reflection.SomeImpl");
        testProperties.setProperty("reflection.SomeOtherInterface", "reflection.SODoer");
        
        // Создаем инжектор и подменяем свойства через reflection
        Injector injector = new Injector();
        try {
            Field propertiesField = Injector.class.getDeclaredField("properties");
            propertiesField.setAccessible(true);
            propertiesField.set(injector, testProperties);
        } catch (Exception e) {
            fail("Failed to set properties: " + e.getMessage());
        }
        
        SomeBean bean = new SomeBean();
        SomeBean injectedBean = injector.inject(bean);
        
        injectedBean.foo();
        String output = outputStream.toString().trim();
        
        // Проверяем вывод
        String[] lines = output.split(System.lineSeparator());
        assertEquals(2, lines.length);
        assertEquals("A", lines[0]);
        assertEquals("C", lines[1]);
        
        // Проверяем типы
        assertTrue(injectedBean.getField1() instanceof SomeImpl);
        assertTrue(injectedBean.getField2() instanceof SODoer);
    }
    
    @Test
    void testInjectionWithOtherImpl() {
        Properties testProperties = new Properties();
        testProperties.setProperty("reflection.SomeInterface", "reflection.OtherImpl");
        testProperties.setProperty("reflection.SomeOtherInterface", "reflection.SODoer");
        
        Injector injector = new Injector();
        try {
            Field propertiesField = Injector.class.getDeclaredField("properties");
            propertiesField.setAccessible(true);
            propertiesField.set(injector, testProperties);
        } catch (Exception e) {
            fail("Failed to set properties: " + e.getMessage());
        }
        
        SomeBean bean = new SomeBean();
        SomeBean injectedBean = injector.inject(bean);
        
        injectedBean.foo();
        String output = outputStream.toString().trim();
        
        String[] lines = output.split(System.lineSeparator());
        assertEquals(2, lines.length);
        assertEquals("B", lines[0]);
        assertEquals("C", lines[1]);
        
        assertTrue(injectedBean.getField1() instanceof OtherImpl);
    }
    
    @Test
    void testMissingConfiguration() {
        Properties testProperties = new Properties();
        // Намеренно не добавляем конфигурацию
        
        Injector injector = new Injector();
        try {
            Field propertiesField = Injector.class.getDeclaredField("properties");
            propertiesField.setAccessible(true);
            propertiesField.set(injector, testProperties);
            
            SomeBean bean = new SomeBean();
            assertThrows(RuntimeException.class, () -> injector.inject(bean));
        } catch (Exception e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }
    
    @Test
    void testInvalidImplementationClass() {
        Properties testProperties = new Properties();
        testProperties.setProperty("reflection.SomeInterface", "reflection.NonExistentClass");
        
        Injector injector = new Injector();
        try {
            Field propertiesField = Injector.class.getDeclaredField("properties");
            propertiesField.setAccessible(true);
            propertiesField.set(injector, testProperties);
            
            SomeBean bean = new SomeBean();
            assertThrows(RuntimeException.class, () -> injector.inject(bean));
        } catch (Exception e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }
}