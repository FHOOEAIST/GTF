/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.constraint.impl;

import science.aist.gtf.verification.syntactic.constraint.Constraint;
import science.aist.gtf.verification.syntactic.constraint.ConstraintError;

import java.lang.reflect.Field;

/**
 * <p>Constraint which checks if an object is null</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public class NullConstraint implements Constraint<Object> {
    @Override
    public ConstraintError apply(Object o, Field f) {
        return o == null ? ConstraintError.IsNull : ConstraintError.NoError;
    }

    @Override
    public Class<Object> getType() {
        return Object.class;
    }

    @Override
    public boolean isBreakingConstraint() {
        return true;
    }
}
