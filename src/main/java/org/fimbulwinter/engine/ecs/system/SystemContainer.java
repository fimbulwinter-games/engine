package org.fimbulwinter.engine.ecs.system;

import org.fimbulwinter.engine.ecs.Component;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.system.exception.DuplicateTypeRuntimeException;
import org.fimbulwinter.engine.ecs.system.exception.InvalidTypeRuntimeException;
import org.joor.Reflect;

import java.lang.reflect.Method;
import java.util.*;

public abstract class SystemContainer implements System {
    private Map<Class<?>, ? extends Component> getComponentTypeMap(Set<? extends Component> components) {
        final var componentTypeMap = new HashMap<Class<?>, Component>();
        for (final var component : components) {
            componentTypeMap.put(component.getClass(), component);
        }
        return componentTypeMap;
    }

    public void run(Entity entity, Set<? extends Component> components) {
        final var componentTypeMap = getComponentTypeMap(components);
        final var methods = getAnnotatedMethods();
        for (final var method : methods) {
            final var arguments = new ArrayList<Component>();
            for (var parameter : method.getParameters()) {
                arguments.add(componentTypeMap.get(parameter.getType()));
            }
            Reflect.on(this).call(method.getName(), arguments.toArray());
        }

    }


    private List<Method> getAnnotatedMethods() {
        return Arrays.stream(getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AutoInject.class))
                .toList();
    }

    public void validateSystems() {
        getAnnotatedMethods().forEach(this::validateSystem);
    }

    private void validateSystem(Method method) {
        Objects.requireNonNull(method);
        validateDuplicateParameters(method);
        validateParameterTypes(method);
    }

    private void validateParameterTypes(Method method) {
        for (var parameter : method.getParameters()) {
            final var parameterType = parameter.getType();
            final var parameterInterfaces = Arrays.stream(parameterType.getInterfaces()).toList();

            if (!parameterInterfaces.contains(Component.class)) {
                throw new InvalidTypeRuntimeException(method, getClass(), Component.class);
            }
        }
    }

    private void validateDuplicateParameters(Method method) {
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
