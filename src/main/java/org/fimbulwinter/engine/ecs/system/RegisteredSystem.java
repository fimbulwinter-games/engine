package org.fimbulwinter.engine.ecs.system;

import org.fimbulwinter.engine.ecs.AutoInjectable;
import org.fimbulwinter.engine.ecs.Component;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.scheduling.SystemTask;
import org.fimbulwinter.engine.ecs.scheduling.exception.DuplicateTypeRuntimeException;
import org.fimbulwinter.engine.ecs.scheduling.exception.InvalidTypeRuntimeException;
import org.fimbulwinter.engine.ecs.scheduling.exception.NotStaticRuntimeException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class RegisteredSystem {
    final Method system;

    public RegisteredSystem(Method system) {
        this(system, List.of());
    }

    public RegisteredSystem(Method system, List<AutoInjectable> parameters) {
        this(system, parameters.toArray(new AutoInjectable[0]));
    }

    public RegisteredSystem(Method system, AutoInjectable[] parameters) {
        validateSystem(system);
        this.system = system;
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

    public SystemTask generateSystemTask(List<AutoInjectable> arguments) {
        return new SystemTask(system, arguments.toArray(new AutoInjectable[0]));
    }
}
