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
import science.aist.jack.general.util.CastUtils;

import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * <p>Constraint for checking if a collection is empty</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public class EmptyContainerConstraint implements Constraint<Iterator<?>> {
    @Override
    public ConstraintError apply(Iterator<?> iterable, Field f) {
        return iterable == null ? ConstraintError.IsNull : iterable.hasNext() ? ConstraintError.NoError : ConstraintError.IsEmpty;
    }

    @Override
    public Class<Iterator<?>> getType() {
        return CastUtils.cast(Iterator.class);
    }

    @Override
    public boolean isBreakingConstraint() {
        return false;
    }
}
