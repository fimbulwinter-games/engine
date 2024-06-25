package org.fimbulwinter.engine.ecs.system.exception;

import java.lang.reflect.Method;

public class DuplicateTypeRuntimeException extends AutoinjectionRuntimeException {
    public DuplicateTypeRuntimeException(Method method, Class<?> containingClass) {
        super("Method '" + method.getName() + "' in class '" + containingClass.getName() + "' contains duplicate parameter types.");
    }
}
