/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.constraint;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

/**
 * <p>Constraint which will be checked and result in some ConstraintError or if there is no Error in {@link
 * ConstraintError#NoError}</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public interface Constraint<T> extends BiFunction<T, Field, ConstraintError> {

    /**
     * @return method which returns the type for which the constraint is applied
     */
    Class<T> getType();

    /**
     * @return a flag if constraint is breaking and if violated no further constraints should be checked (e.q. because
     * of a NullPointer)
     */
    boolean isBreakingConstraint();
}
