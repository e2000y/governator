package com.netflix.governator.internal;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Set;

import jakarta.validation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import com.netflix.governator.LifecycleAction;
import com.netflix.governator.LifecycleFeature;

/**
 * Special LifecycleFeature to support JSR380 validation
 * 
 * @author e2000y
 */
public final class JSR380ValidationFeature implements LifecycleFeature {
    private static final Logger LOG = LoggerFactory.getLogger(JSR380ValidationFeature.class);

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    @Override
    public List<LifecycleAction> getActionsForType(final Class<?> type) {
        List<LifecycleAction> ls = new ArrayList<LifecycleAction>();

        ls.add(new LifecycleAction() {
            private Validator validator = factory.getValidator();

            @Override
            public void call(Object obj)
                throws Exception {

                ValidationException exception = null;
                Set<ConstraintViolation<Object>> violations = validator.validate(obj);

                if (!violations.isEmpty()) {
                    for ( ConstraintViolation<Object> violation : violations ) {
                        String path = getPath(violation);
                        String message = String.format("%s - %s.%s = %s", violation.getMessage(), obj.getClass().getName(), path, String.valueOf(violation.getInvalidValue()));

                        if ( exception == null ) {
                            exception = new ValidationException(message);
                        } else {
                            exception = new ValidationException(message, exception);
                        }
                    }
                }

                if (exception != null)
                    throw exception;
            }

            private String getPath(ConstraintViolation<Object> violation) {
                Iterable<String> transformed = Iterables.transform(
                        violation.getPropertyPath(),
                        new Function<Path.Node, String>() {
                            @Override
                            public String apply(Path.Node node) {
                                return node.getName();
                            }
                        });

                return Joiner.on(".").join(transformed);
            }
        });

        return ls;
    }

    @Override
    public String toString() {
        return "JSR380Validation";
    }

}

