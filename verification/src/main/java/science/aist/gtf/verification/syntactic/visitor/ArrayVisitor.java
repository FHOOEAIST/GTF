/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.visitor;

import science.aist.gtf.verification.syntactic.FieldExtractor;
import science.aist.gtf.verification.syntactic.PropertyVerificatorResult;
import science.aist.gtf.verification.syntactic.constraint.ConstraintError;
import science.aist.gtf.verification.syntactic.visitor.factory.RestrictedVisitorFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * <p>A visitor for a array</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public class ArrayVisitor extends AbstractVisitor<Object> {
    public ArrayVisitor(RestrictedVisitorFactory visitorFactory) {
        super(visitorFactory);
    }

    /**
     * Visit method for visiting an array
     *
     * @param toVisit                An array which should be visited
     * @param fieldOfToVisitInParent the corresponding field in the parent of which toVisit contains the value
     * @param parent                 The parent object wrapping the toVisit object
     * @param visitedObjs            A list of already visited objects to avoid circular calls
     * @return A map of objects and its fields which break a constraint
     */
    @Override
    public PropertyVerificatorResult visit(Object toVisit, Field fieldOfToVisitInParent, Object parent, Collection<Object> visitedObjs) {
        if (toVisit != null && !toVisit.getClass().isArray()) {
            throw new IllegalArgumentException("toVisit must be an array");
        }

        PropertyVerificatorResult result = new PropertyVerificatorResult();
        if (toVisit == null) { // if toVisit is null it is an invalid field
            result.addField(fieldOfToVisitInParent, ConstraintError.IsNull, parent);
        } else {
            int length = Array.getLength(toVisit);
            if (length == 0) { // if toVisit is empty it is an invalid field
                result.addField(fieldOfToVisitInParent, ConstraintError.IsEmpty, parent);
            } else {
                // count number of null/default initialized elements in toVisit
                int cnt = 0;
                for (int i = 0; i < length; i++) {
                    Object o = Array.get(toVisit, i);
                    if (o == null || o.equals(FieldExtractor.getDefaultValueForClass(o.getClass()))) {
                        cnt++;
                    }
                }
                if (cnt == length) { // if all elements of toVisit are null or default initialized it is an invalid field
                    result.addField(fieldOfToVisitInParent, ConstraintError.IsDefaultInitialized, parent);
                } else {
                    // also, check nested objects of toVisit elements
                    for (int i = 0; i < length; i++) {
                        ConstraintVisitor<Object> visitor = visitorFactory.createObjectVisitor();
                        result = PropertyVerificatorResult.mergeVerificationResults(result, visitor.visit(Array.get(toVisit, i), fieldOfToVisitInParent, parent, visitedObjs));
                    }
                }
            }
        }

        return result;
    }
}
