package org.fimbulwinter.engine.ecs.system;

import org.fimbulwinter.engine.ecs.Component;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.system.exception.InvalidTypeRuntimeException;

import java.util.Arrays;
import java.util.Set;

public interface SystemContainer {

    default void run(Entity entity, Set<? extends Component> components) {
        final var componentList = components.stream().toList();
    }

    default void validateSystems() {
        final var containerClass = this.getClass();

        for (var method : containerClass.getDeclaredMethods()) {
            final var parameters = method.getParameters();

            if (method.isAnnotationPresent(AutoInject.class))
                for (var parameter : parameters) {
                    final var parameterType = parameter.getType();
                    final var parameterInterfaces = Arrays.stream(parameterType.getInterfaces()).toList();

                    if (!parameterInterfaces.contains(Component.class)) {
                        throw new InvalidTypeRuntimeException(method, containerClass, Component.class);
                    }
                }
        }
    }
}
