package org.fimbulwinter.engine.ecs.system.exception;

import java.lang.reflect.Method;

public class NotStaticRuntimeException extends AutoinjectionRuntimeException {
    public NotStaticRuntimeException(Method system) {
        super("System '" + system + "' is not marked as static");
    }
}
