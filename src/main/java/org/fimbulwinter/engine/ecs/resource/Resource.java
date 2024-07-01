package org.fimbulwinter.engine.ecs.resource;

import org.fimbulwinter.engine.ecs.AutoInjectable;

public interface Resource extends AutoInjectable {
    default String getResourceIdentifier() {
        return this.getClass().getName();
    }
}
