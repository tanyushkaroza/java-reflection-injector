package reflection;

import reflection.SomeInterface;

/**
 * Первая реализация интерфейса SomeInterface.
 * Выводит "A" при выполнении.
 */
public class SomeImpl implements SomeInterface {
    /**
     * {@inheritDoc}
     * Выводит "A" в консоль.
     */
    @Override
    public void doSomething() {
        System.out.println("A");
    }
}