package org.fimbulwinter.engine.ecs.system;

import org.fimbulwinter.engine.ecs.Component;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.system.exception.DuplicateTypeRuntimeException;
import org.fimbulwinter.engine.ecs.system.exception.InvalidTypeRuntimeException;
import org.joor.Reflect;

import java.lang.reflect.Method;
import java.util.*;

public abstract class SystemContainer implements System {

    private final List<Method> systems = new ArrayList<>();

    private static void validateSystems(List<Method> systems) {
        systems.forEach(SystemContainer::validateSystem);
    }

    private static void validateSystem(Method method) {
        Objects.requireNonNull(method);
        validateDuplicateParameters(method);
        validateParameterTypes(method);
    }

    private static void validateDuplicateParameters(Method method) {
        final var typeCounter = new HashSet<Class<?>>();

        for (var parameter : method.getParameters()) {
            final var parameterType = parameter.getType();

            if (typeCounter.contains(parameterType)) {
                throw new DuplicateTypeRuntimeException(method, SystemContainer.class);
            }
            typeCounter.add(parameterType);
        }
    }

    private static void validateParameterTypes(Method method) {
        for (var parameter : method.getParameters()) {
            final var parameterType = parameter.getType();
            final var parameterInterfaces = Arrays.stream(parameterType.getInterfaces()).toList();

            if (!parameterInterfaces.contains(Component.class) && parameter.getType() != Entity.class) {
                throw new InvalidTypeRuntimeException(method, SystemContainer.class, Component.class);
            }
        }
    }

    private Map<Class<?>, ? extends Component> getComponentTypeMap(Set<? extends Component> components) {
        final var componentTypeMap = new HashMap<Class<?>, Component>();
        for (final var component : components) {
            componentTypeMap.put(component.getClass(), component);
        }
        return componentTypeMap;
    }

    public void scanSystems() {
        final List<Method> systems = getAnnotatedMethods();
        validateSystems(systems);
        this.systems.clear();
        this.systems.addAll(systems);
    }

    @Override
    public void run(Entity entity, Set<? extends Component> components) {
        final var componentTypeMap = getComponentTypeMap(components);
        for (final var system : systems) {
            final var arguments = new ArrayList<>();
            for (var parameter : system.getParameters()) {
                if (parameter.getType() == Entity.class) {
                    arguments.add(entity);
                } else {
                    arguments.add(componentTypeMap.get(parameter.getType()));
                }
            }
            Reflect.on(this).call(system.getName(), arguments.toArray());
        }
    }

    private List<Method> getAnnotatedMethods() {
        return Arrays.stream(getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RegisterSystem.class))
                .toList();
    }
}
