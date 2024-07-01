package org.fimbulwinter.engine.ecs.scheduling.exception;

import org.fimbulwinter.engine.ecs.system.exception.AutoinjectionRuntimeException;

import java.lang.reflect.Method;

public class DuplicateTypeRuntimeException extends AutoinjectionRuntimeException {
    public DuplicateTypeRuntimeException(Method method) {
        super("Method '" + method + "' contains duplicate parameter types.");
    }
}
