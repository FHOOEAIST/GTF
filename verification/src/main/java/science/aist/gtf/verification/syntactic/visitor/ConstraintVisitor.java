/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.visitor;

import science.aist.gtf.verification.syntactic.PropertyVerificatorResult;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * <p>Interface for visiting a generic object T</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public interface ConstraintVisitor<T> {
    /**
     * Visit method for visiting a generic object
     *
     * @param toVisit                The generic object which should be visited
     * @param fieldOfToVisitInParent the corresponding field in the parent of which toVisit contains the value
     * @param parent                 The parent object wrapping the toVisit object
     * @param visitedObjs            A list of already visited objects to avoid circular calls
     * @return A map of objects and its fields which break a constraint
     */
    PropertyVerificatorResult visit(T toVisit, Field fieldOfToVisitInParent, Object parent, Collection<Object> visitedObjs);

    /**
     * Visit method for visiting a generic object
     *
     * @param toVisit The generic object which should be visited
     * @return A map of objects and its fields which break a constraint
     */
    PropertyVerificatorResult visit(T toVisit);
}
