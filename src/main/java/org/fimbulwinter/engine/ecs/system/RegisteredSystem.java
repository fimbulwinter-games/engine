package org.fimbulwinter.engine.ecs.system;

import org.fimbulwinter.engine.ecs.AutoInjectable;
import org.fimbulwinter.engine.ecs.Component;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.scheduling.SystemTask;
import org.fimbulwinter.engine.ecs.scheduling.exception.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class RegisteredSystem {
    final Method system;
//    final AutoInjectable[] parameters;

    public RegisteredSystem(Method system) {
        this(system, List.of());
    }

    public RegisteredSystem(Method system, List<AutoInjectable> parameters) {
        this(system, parameters.toArray(new AutoInjectable[0]));
    }

    public RegisteredSystem(Method system, AutoInjectable[] parameters) {
        validateSystem(system);
//        validateInjectedParameters(system, parameters);
        this.system = system;
//        this.parameters = parameters;
    }


    private void validateSystemParameterTypes(Method method) {
        for (var parameter : method.getParameters()) {
            final var parameterType = parameter.getType();
            final var parameterInterfaces = Arrays.stream(parameterType.getInterfaces()).toList();

            if (!parameterInterfaces.contains(Component.class) && parameter.getType() != Entity.class) {
                throw new InvalidTypeRuntimeException(method, Component.class);
            }
        }
    }

    private void validateSystem(Method system) {
        Objects.requireNonNull(system);
        validateStatic(system);
        validateDuplicateSystemParameters(system);
        validateSystemParameterTypes(system);
    }

    private void validateStatic(Method method) {
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new NotStaticRuntimeException(method);
        }
    }

    private void validateDuplicateSystemParameters(Method method) {
        final var typeCounter = new HashSet<Class<?>>();

        for (var parameter : method.getParameters()) {
            final var parameterType = parameter.getType();

            if (typeCounter.contains(parameterType)) {
                throw new DuplicateTypeRuntimeException(method);
            }
            typeCounter.add(parameterType);
        }
    }

    private void validateInjectedParameters(Method system, AutoInjectable[] parameters) {
        Objects.requireNonNull(system);
        Objects.requireNonNull(parameters);
        validateCorrectGivenParameterCount(system, parameters);

    }

    private void validateCorrectGivenParameterCount(Method system, AutoInjectable[] parameters) {
        final int parameterCount = parameters.length;
        if (system.getParameterCount() != parameterCount) {
            throw new IllegalParameterCountRuntimeException(system, parameterCount);
        }
    }

    public SystemTask generateSystemTask(List<AutoInjectable> arguments) {
        return new SystemTask(system, arguments.toArray(new AutoInjectable[0]));
    }

    public void run(AutoInjectable[] parameters) {
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

        RegisteredSystem registeredSystem = (RegisteredSystem) o;
        return system.equals(registeredSystem.system);
//        return system.equals(systemTask.system) && Arrays.equals(parameters, systemTask.parameters);
    }

    @Override
    public int hashCode() {
        int result = system.hashCode();
        result = 31 * result;
//        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }

    public Parameter[] getParameters() {
        return system.getParameters();
    }
}
