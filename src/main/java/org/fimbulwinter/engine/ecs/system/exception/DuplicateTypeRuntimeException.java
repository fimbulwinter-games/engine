package org.fimbulwinter.engine.ecs.system.exception;

import java.lang.reflect.Method;

public class DuplicateTypeRuntimeException extends AutoinjectionRuntimeException {
    public DuplicateTypeRuntimeException(Method method) {
        super("Method '" + method + "' contains duplicate parameter types.");
    }
}
