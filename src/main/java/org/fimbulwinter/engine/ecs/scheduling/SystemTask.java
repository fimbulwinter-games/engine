package org.fimbulwinter.engine.ecs.scheduling;

import org.fimbulwinter.engine.ecs.AutoInjectable;
import org.fimbulwinter.engine.ecs.scheduling.exception.TaskInvocationRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SystemTask implements Runnable {
    final Method system;
    final AutoInjectable[] parameters;

    public SystemTask(Method system, List<AutoInjectable> parameters) {
        this(system, parameters.toArray(new AutoInjectable[0]));
    }

    public SystemTask(Method system, AutoInjectable[] parameters) {
        Objects.requireNonNull(system);
        Objects.requireNonNull(parameters);
        this.system = system;
        this.parameters = parameters;
    }

    public SystemTask(Method system) {
        this(system, List.of());
    }

    @Override
    public void run() {
        try {
            system.invoke(null, (Object[]) parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new TaskInvocationRuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemTask systemTask = (SystemTask) o;
        return system.equals(systemTask.system) && Arrays.equals(parameters, systemTask.parameters);
    }

    @Override
    public int hashCode() {
        int result = system.hashCode();
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }
}
