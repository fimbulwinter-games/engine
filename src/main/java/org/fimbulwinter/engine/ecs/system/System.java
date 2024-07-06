package org.fimbulwinter.engine.ecs.system;

import org.fimbulwinter.engine.ecs.component.Component;
import org.fimbulwinter.engine.ecs.entity.Entity;

import java.util.Set;

@FunctionalInterface
public interface System {
    void run(Entity entity, Set<? extends Component> components);
}
