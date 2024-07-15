package org.fimbulwinter.engine.ecs.scheduling;

import org.fimbulwinter.engine.ecs.component.ComponentSet;
import org.fimbulwinter.engine.ecs.entity.Entity;
import org.fimbulwinter.engine.ecs.resource.Resource;
import org.fimbulwinter.engine.ecs.system.AutoInjectable;
import org.fimbulwinter.engine.ecs.system.RegisterSystem;
import org.fimbulwinter.engine.ecs.system.SystemStage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class SystemTask implements Runnable {
    private final Method system;
    private final SystemStage systemStage;
    private final AutoInjectable[] arguments;

    private SystemTask(Method system, AutoInjectable[] arguments) {
        Objects.requireNonNull(system);
        Objects.requireNonNull(arguments);

        this.system = system;
        this.arguments = arguments;
        this.systemStage = system.getAnnotation(RegisterSystem.class).systemStage();
    }

    public static Optional<SystemTask> generateTask(Method system, Entity entity, ComponentSet components, Collection<? extends Resource> resources) {
        final List<AutoInjectable> args = new ArrayList<>();

        for (Parameter parameter : system.getParameters()) {
            final Class<?> parameterType = parameter.getType();

            if (parameterType.isInstance(entity)) {
                args.add(entity);
                continue;
            }
            boolean isComponent = false;
            for (var component : components) {
                if (parameterType.isInstance(component)) {
                    args.add(component);
                    isComponent = true;
                }
            }
            if (!isComponent) {
                for (var resource : resources) {
                    if (parameterType.isInstance(resource)) {
                        args.add(resource);
                        break;
                    }
                }
            }
        }
        if (system.getParameterCount() == args.size()) {
            return Optional.of(new SystemTask(system, args.toArray(new AutoInjectable[0])));
        } else {
            return Optional.empty();
        }
    }

    public SystemStage getSystemStage() {
        return systemStage;
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
