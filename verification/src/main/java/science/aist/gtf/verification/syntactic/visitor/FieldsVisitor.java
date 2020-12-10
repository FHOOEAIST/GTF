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
import science.aist.gtf.verification.syntactic.visitor.factory.RestrictedVisitorFactory;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * <p>Visitor for visiting a collection of fields</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public class FieldsVisitor extends AbstractVisitor<Collection<Field>> {
    public FieldsVisitor(RestrictedVisitorFactory visitorFactory) {
        super(visitorFactory);
    }

    /**
     * Visit method for visiting a collection of fields
     *
     * @param toVisit                The collection of fields which should be visited
     * @param fieldOfToVisitInParent the corresponding field in the parent of which toVisit contains the value
     * @param parent                 The parent object wrapping the toVisit object
     * @param visitedObjs            A list of already visited objects to avoid circular calls
     * @return A map of objects and its fields which break a constraint
     */
    @Override
    public PropertyVerificatorResult visit(Collection<Field> toVisit, Field fieldOfToVisitInParent, Object parent, Collection<Object> visitedObjs) {
        PropertyVerificatorResult result = new PropertyVerificatorResult();
        for (Field f : toVisit) {
            ConstraintVisitor<Field> visitor = visitorFactory.createFieldVisitor();
            result = PropertyVerificatorResult.mergeVerificationResults(result, visitor.visit(f, f, parent, visitedObjs));
        }

        return result;
    }
}
