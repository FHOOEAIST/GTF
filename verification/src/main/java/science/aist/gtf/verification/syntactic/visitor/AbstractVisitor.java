/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.visitor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import science.aist.gtf.verification.syntactic.PropertyVerificatorResult;
import science.aist.gtf.verification.syntactic.visitor.factory.RestrictedVisitorFactory;

import java.util.HashSet;

/**
 * <p>Abstract visitor</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
@RequiredArgsConstructor
@Setter
public abstract class AbstractVisitor<T> implements ConstraintVisitor<T> {
    @NonNull
    protected RestrictedVisitorFactory visitorFactory;

    /**
     * Visit method for visiting a generic object
     *
     * @param toVisit The generic object which should be visited
     * @return A map of objects and its fields which break a constraint
     */
    public PropertyVerificatorResult visit(T toVisit) {
        return visit(toVisit, null, null, new HashSet<>());
    }
}
