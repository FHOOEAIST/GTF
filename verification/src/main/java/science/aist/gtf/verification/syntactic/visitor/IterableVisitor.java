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
import science.aist.gtf.verification.syntactic.constraint.ConstraintError;
import science.aist.gtf.verification.syntactic.visitor.factory.RestrictedVisitorFactory;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * <p>Visitor for an iterable</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public class IterableVisitor extends AbstractVisitor<Iterable<?>> {
    public IterableVisitor(RestrictedVisitorFactory visitorFactory) {
        super(visitorFactory);
    }

    /**
     * Visit method for visiting an iterable
     *
     * @param toVisit                An iterable which should be visited
     * @param fieldOfToVisitInParent the corresponding field in the parent of which toVisit contains the value
     * @param parent                 The parent object wrapping the toVisit object
     * @param visitedObjs            A list of already visited objects to avoid circular calls
     * @return A map of objects and its fields which break a constraint
     */
    @Override
    public PropertyVerificatorResult visit(Iterable<?> toVisit, Field fieldOfToVisitInParent, Object parent, Collection<Object> visitedObjs) {
        PropertyVerificatorResult result = new PropertyVerificatorResult();
        // check if iterable is initialized and has elements; if not it is an invalid field
        if (toVisit == null) {
            result.addField(fieldOfToVisitInParent, ConstraintError.IsNull, parent);
        } else {
            if (!toVisit.iterator().hasNext()) {
                result.addField(fieldOfToVisitInParent, ConstraintError.IsEmpty, parent);
            } else {
                for (Object o : toVisit) {
                    result = PropertyVerificatorResult.mergeVerificationResults(result, visitorFactory.createObjectVisitor().visit(o, fieldOfToVisitInParent, parent, visitedObjs));
                }
            }
        }

        return result;
    }
}
