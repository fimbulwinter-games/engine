package org.fimbulwinter.engine.ecs.system;

import org.fimbulwinter.engine.ecs.Component;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.system.exception.DuplicateTypeRuntimeException;
import org.fimbulwinter.engine.ecs.system.exception.InvalidTypeRuntimeException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public interface SystemContainer {

    default void run(Entity entity, Set<? extends Component> components) {
        final var componentList = components.stream().toList();
    }

    default void validateSystems() {
        Arrays.stream(getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AutoInject.class))
                .forEach(this::validateSystem);
    }

    default void validateSystem(Method method) {
        Objects.requireNonNull(method);
        validateDuplicateParameters(method);
        validateParameterTypes(method);
    }

    default void validateParameterTypes(Method method) {
        for (var parameter : method.getParameters()) {
            final var parameterType = parameter.getType();
            final var parameterInterfaces = Arrays.stream(parameterType.getInterfaces()).toList();

            if (!parameterInterfaces.contains(Component.class)) {
                throw new InvalidTypeRuntimeException(method, getClass(), Component.class);
            }
        }
    }

    default void validateDuplicateParameters(Method method) {
        final var typeCounter = new HashSet<Class<?>>();

        for (var parameter : method.getParameters()) {
            final var parameterType = parameter.getType();

            if (typeCounter.contains(parameterType)) {
                throw new DuplicateTypeRuntimeException(method, getClass());
            }
            typeCounter.add(parameterType);
        }
    }
}
