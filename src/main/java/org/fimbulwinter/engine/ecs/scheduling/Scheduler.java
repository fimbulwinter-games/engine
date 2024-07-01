package org.fimbulwinter.engine.ecs.scheduling;

import org.fimbulwinter.engine.ecs.Component;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.system.RegisterSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Scheduler {
    private final Set<Method> systems = new HashSet<>();

    public Scheduler() {
    }

    public void tick(Entity entity, Set<? extends Component> components) {
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
            try {
                system.invoke(null, arguments.toArray());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addSystem(Method method) {
        systems.add(method);
    }

    public void addSystems(Class<?> systemContainer) {
        List<Method> systems = getAnnotatedMethods(systemContainer);
        systems.forEach(this::addSystem);
    }

    private Map<Class<?>, ? extends Component> getComponentTypeMap(Set<? extends Component> components) {
        final var componentTypeMap = new HashMap<Class<?>, Component>();
        for (final var component : components) {
            componentTypeMap.put(component.getClass(), component);
        }
        return componentTypeMap;
    }

    private List<Method> getAnnotatedMethods(Class<?> systemContainer) {
        return Arrays.stream(systemContainer.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RegisterSystem.class))
                .toList();
    }
}
