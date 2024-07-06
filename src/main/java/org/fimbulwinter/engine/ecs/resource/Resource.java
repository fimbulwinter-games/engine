package org.fimbulwinter.engine.ecs.resource;

import org.fimbulwinter.engine.ecs.system.AutoInjectable;

public interface Resource extends AutoInjectable {
    default String getResourceIdentifier() {
        return this.getClass().getName();
    }
}
