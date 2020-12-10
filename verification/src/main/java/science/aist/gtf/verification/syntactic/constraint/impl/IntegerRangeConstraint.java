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
 * <p>Constraint for checking if an integer is in between the bounds (inclusive)</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
@Getter
@Setter
public class IntegerRangeConstraint implements Constraint<Integer> {
    public static final ConstraintError IntegerRangeError = new ConstraintError("integerRangeError");

    private int lowerBound;
    private int upperBound;

    @Override
    public ConstraintError apply(Integer integer, Field field) {
        return (integer >= lowerBound && integer <= upperBound) ? ConstraintError.NoError : IntegerRangeError;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public boolean isBreakingConstraint() {
        return false;
    }
}
