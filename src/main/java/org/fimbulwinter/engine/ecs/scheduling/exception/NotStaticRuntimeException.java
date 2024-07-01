package org.fimbulwinter.engine.ecs.scheduling.exception;

import org.fimbulwinter.engine.ecs.system.exception.AutoinjectionRuntimeException;

import java.lang.reflect.Method;

public class NotStaticRuntimeException extends AutoinjectionRuntimeException {
    public NotStaticRuntimeException(Method system) {
        super("System '" + system + "' is not marked as static");
    }
}
