/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.constraint.impl;

import lombok.Getter;
import lombok.Setter;
import science.aist.gtf.verification.syntactic.constraint.Constraint;
import science.aist.gtf.verification.syntactic.constraint.ConstraintError;

import java.lang.reflect.Field;

/**
 * <p>Constraint for checking if a boolean value is of given value</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public class BooleanConstraint implements Constraint<Boolean> {
    public static final ConstraintError BooleanConstraintError = new ConstraintError("booleanConstraint");

    @Setter
    @Getter
    private boolean expectedValue = false;

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public boolean isBreakingConstraint() {
        return false;
    }

    @Override
    public ConstraintError apply(Boolean aBoolean, Field field) {
        return Boolean.valueOf(expectedValue).equals(aBoolean) ? ConstraintError.NoError : BooleanConstraintError;
    }
}
