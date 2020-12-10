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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Visitor for an object</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public class ObjectVisitor extends AbstractVisitor<Object> {
    public ObjectVisitor(RestrictedVisitorFactory visitorFactory) {
        super(visitorFactory);
    }

    private static List<Field> getAllFieldsForClass(Class<?> clazz) {
        List<Field> res = new ArrayList<>();
        getAllFieldsForClass(res, clazz);
        return res;
    }

    private static void getAllFieldsForClass(List<Field> fields, Class<?> clazz) {
        if (clazz == null) return;
        fields.addAll(Arrays.stream(clazz.getDeclaredFields()).filter(f -> !f.getName().startsWith("this$")).collect(Collectors.toList()));
        getAllFieldsForClass(fields, clazz.getSuperclass());
    }

    /**
     * Visit method for visiting an object
     *
     * @param toVisit                An object which should be visited
     * @param fieldOfToVisitInParent the corresponding field in the parent of which toVisit contains the value
     * @param parent                 The parent object wrapping the toVisit object
     * @param visitedObjs            A list of already visited objects to avoid circular calls
     * @return A map of objects and its fields which break a constraint
     */
    @Override
    public PropertyVerificatorResult visit(Object toVisit, Field fieldOfToVisitInParent, Object parent, Collection<Object> visitedObjs) {
        if (toVisit == null && fieldOfToVisitInParent == null) {
            return new PropertyVerificatorResult();
        } else if (toVisit == null && parent != null) {
            return new PropertyVerificatorResult();
        } else if (toVisit == null) {
            throw new IllegalArgumentException("toVisit must not be null");
        }

        if (visitedObjs.contains(toVisit)) {
            return new PropertyVerificatorResult();
        } else {
            visitedObjs.add(toVisit);
        }

        // get all fields of obj´s class to check them
        Collection<Field> fields = getAllFieldsForClass(toVisit.getClass());

        return visitorFactory.createFieldsVisitor().visit(fields, null, toVisit, visitedObjs);
    }
}
