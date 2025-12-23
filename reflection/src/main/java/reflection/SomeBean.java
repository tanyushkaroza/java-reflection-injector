package reflection;

import reflection.AutoInjectable;
import reflection.SomeInterface;
import reflection.SomeOtherInterface;

/**
 * Класс, содержащий поля для автоматического внедрения зависимостей.
 * Поля помечены аннотацией @AutoInjectable и должны быть
 * инициализированы инжектором.
 */
public class SomeBean {
    @AutoInjectable
    private SomeInterface field1;
    
    @AutoInjectable
    private SomeOtherInterface field2;

    /**
     * Выполняет действия с внедренными зависимостями.
     * Вызывает методы field1.doSomething() и field2.doSomeOther().
     */
    public void foo() {
        field1.doSomething();
        field2.doSomeOther();
    }

    /**
     * Получает первый внедренный объект.
     * 
     * @return объект SomeInterface
     */
    public SomeInterface getField1() {
        return field1;
    }

    /**
     * Получает второй внедренный объект.
     * 
     * @return объект SomeOtherInterface
     */
    public SomeOtherInterface getField2() {
        return field2;
    }
}