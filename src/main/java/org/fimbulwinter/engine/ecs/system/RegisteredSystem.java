package org.fimbulwinter.engine.ecs.system;

import org.fimbulwinter.engine.ecs.Component;
import org.fimbulwinter.engine.ecs.ComponentSet;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.resource.Resource;
import org.fimbulwinter.engine.ecs.scheduling.SystemTask;
import org.fimbulwinter.engine.ecs.scheduling.exception.DuplicateTypeRuntimeException;
import org.fimbulwinter.engine.ecs.scheduling.exception.InvalidTypeRuntimeException;
import org.fimbulwinter.engine.ecs.scheduling.exception.NotStaticRuntimeException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class RegisteredSystem {
    final Method system;

    public RegisteredSystem(Method system) {
        validateSystem(system);
        this.system = system;
    }

    private void validateSystemParameterTypes(Method method) {
        for (var parameter : method.getParameters()) {
            final var parameterType = parameter.getType();
            final var parameterInterfaces = Arrays.stream(parameterType.getInterfaces()).toList();

            if (!(parameter.getType() == Entity.class || parameterInterfaces.contains(Component.class) || parameterInterfaces.contains(Resource.class))) {
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

    public Set<SystemTask> generateSystemTasks(Map<Entity, ComponentSet> entities, Collection<? extends Resource> resources) {
        Set<SystemTask> tasks = new HashSet<>();

        for (var entry : entities.entrySet()) {
            final var entity = entry.getKey();
            final var componentSet = entry.getValue();

            SystemTask.generateTask(system, entity, componentSet, resources).ifPresent(tasks::add);
        }
        return tasks;
    }
}
