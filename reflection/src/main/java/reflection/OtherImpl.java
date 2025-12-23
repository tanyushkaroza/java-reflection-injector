package reflection;

import reflection.SomeInterface;

/**
 * Вторая реализация интерфейса SomeInterface.
 * Выводит "B" при выполнении.
 */
public class OtherImpl implements SomeInterface {
    /**
     * {@inheritDoc}
     * Выводит "B" в консоль.
     */
    @Override
    public void doSomething() {
        System.out.println("B");
    }
}
