package reflection;

import reflection.SomeBean;
import reflection.Injector;

/**
 * Демонстрационный класс для тестирования инжектора.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Тестирование инжектора зависимостей ===");
        
        // Создаем инжектор с конфигурацией по умолчанию
        Injector injector = new Injector();
        
        // Создаем и инжектируем зависимости
        SomeBean bean = new SomeBean();
        SomeBean injectedBean = injector.inject(bean);
        
        System.out.println("Выполнение SomeBean.foo():");
        injectedBean.foo();
        
        System.out.println("\n=== Проверка типов внедренных объектов ===");
        System.out.println("field1 тип: " + injectedBean.getField1().getClass().getName());
        System.out.println("field2 тип: " + injectedBean.getField2().getClass().getName());
    }
}
