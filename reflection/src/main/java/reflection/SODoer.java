package reflection;

import reflection.SomeOtherInterface;

/**
 * Реализация интерфейса SomeOtherInterface.
 * Выводит "C" при выполнении.
 */
public class SODoer implements SomeOtherInterface {
    /**
     * {@inheritDoc}
     * Выводит "C" в консоль.
     */
    @Override
    public void doSomeOther() {
        System.out.println("C");
    }
}