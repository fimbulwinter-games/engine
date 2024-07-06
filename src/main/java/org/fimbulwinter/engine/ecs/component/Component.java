package org.fimbulwinter.engine.ecs.component;

import org.fimbulwinter.engine.ecs.system.AutoInjectable;

public interface Component extends AutoInjectable {
    default String getComponentIdentifier() {
        return getClass().getName();
    }
}
