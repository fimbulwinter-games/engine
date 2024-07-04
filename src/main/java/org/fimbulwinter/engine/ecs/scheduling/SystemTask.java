package org.fimbulwinter.engine.ecs.scheduling;

import org.fimbulwinter.engine.ecs.AutoInjectable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class SystemTask implements Runnable {
    private final Method system;
    private final AutoInjectable[] arguments;

    public SystemTask(Method system, AutoInjectable[] arguments) {
        Objects.requireNonNull(system);
        Objects.requireNonNull(arguments);

        this.system = system;
        this.arguments = arguments;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemTask that)) return false;

        return system.equals(that.system) && Arrays.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return 31 * system.hashCode() + Arrays.hashCode(arguments);
    }

    @Override
    public void run() {
        try {
            system.invoke(null, (Object[]) arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e); // More specialized exception
        }
    }
}
