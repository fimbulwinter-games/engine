package org.fimbulwinter.engine.ecs.scheduling;

import org.fimbulwinter.engine.ecs.AutoInjectable;
import org.fimbulwinter.engine.ecs.Component;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.system.RegisterSystem;
import org.fimbulwinter.engine.ecs.system.RegisteredSystem;

import java.lang.reflect.Method;
import java.util.*;

public class Scheduler {
    private final Set<RegisteredSystem> registeredSystems = new HashSet<>();

    public Scheduler() {
    }

    public void tick(Entity entity, Set<? extends Component> components) {
        final var componentTypeMap = getComponentTypeMap(components);
        registeredSystems.forEach(system -> {
            final List<AutoInjectable> arguments = new ArrayList<>();
            final var parameters = system.getParameters();

            for (var parameter : parameters) {
                if (parameter.getType() == Entity.class) {
                    arguments.add(entity);
                } else {
                    arguments.add(componentTypeMap.get(parameter.getType()));
                }
            }

            SystemTask task = system.generateSystemTask(arguments);
            task.run();
        });
    }

    private void registerSystem(RegisteredSystem system) {
        registeredSystems.add(system);
    }

    public void registerSystems(Class<?> systemContainer) {
        List<Method> systems = findAnnotatedMethods(systemContainer);
        systems.forEach(system -> this.registerSystem(new RegisteredSystem(system)));
    }

    private Map<Class<?>, ? extends Component> getComponentTypeMap(Set<? extends Component> components) {
        final var componentTypeMap = new HashMap<Class<?>, Component>();
        for (final var component : components) {
            componentTypeMap.put(component.getClass(), component);
        }
        return componentTypeMap;
    }

    private List<Method> findAnnotatedMethods(Class<?> systemContainer) {
        return Arrays.stream(systemContainer.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RegisterSystem.class))
                .toList();
    }
}
