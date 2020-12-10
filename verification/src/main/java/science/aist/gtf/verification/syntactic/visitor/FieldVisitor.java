/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.visitor;

import lombok.CustomLog;
import science.aist.gtf.verification.syntactic.PropertyRestrictor;
import science.aist.gtf.verification.syntactic.PropertyVerificatorResult;
import science.aist.gtf.verification.syntactic.constraint.Constraint;
import science.aist.gtf.verification.syntactic.constraint.ConstraintError;
import science.aist.gtf.verification.syntactic.visitor.factory.RestrictedVisitorFactory;
import science.aist.jack.general.util.CastUtils;
import science.aist.jack.reflection.Autoboxers;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Visitor for a single field</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
@CustomLog
public class FieldVisitor extends AbstractVisitor<Field> {
    public FieldVisitor(RestrictedVisitorFactory visitorFactory) {
        super(visitorFactory);
    }

    /**
     * Helper method for getting a list of constraints ordered by the priority key in the map
     *
     * @param map map containing constraints with there priority as key
     * @return list of constraints ordered by priority
     */
    private static List<Constraint<?>> getConstraintsByPriority(Map<Integer, Constraint<?>> map) {
        return map.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Visit method for visiting a single field
     *
     * @param toVisit                The generic object which should be visited
     * @param fieldOfToVisitInParent the corresponding field in the parent of which toVisit contains the value
     * @param parent                 The parent object wrapping the toVisit object
     * @param visitedObjs            A list of already visited objects to avoid circular calls
     * @return A map of objects and its fields which break a constraint
     */
    @Override
    public PropertyVerificatorResult visit(Field toVisit, Field fieldOfToVisitInParent, Object parent, Collection<Object> visitedObjs) {
        if (toVisit.isSynthetic())
            return new PropertyVerificatorResult(); // necessary for e.q. jacoco because this framework adds fields to object so there is difference between local and jenkins test
        if (visitorFactory.getRestrictor().getIgnoredFieldsInClass().contains(toVisit)) {
            log.debug("found ignored field");
            return new PropertyVerificatorResult();
        }
        if (visitorFactory.getRestrictor().getIgnoredClasses().contains(toVisit.getType())) {
            log.debug("found ignored class");
            return new PropertyVerificatorResult();
        }

        boolean isAccessible = toVisit.canAccess(parent);
        toVisit.setAccessible(true); // change accessibility to get also private fields

        PropertyVerificatorResult result = new PropertyVerificatorResult();

    /* check if field is
       - an iterable
       - an array
       - a Java Boxing class as Integer
       - any other object */
        try {
            // check if toVisit breaks any general constraint
            boolean violatedBreakingConstraint = checkGeneralConstraints(toVisit, parent, result);


            // check if toVisit breaks any field constraint
            if (!violatedBreakingConstraint) {
                violatedBreakingConstraint = checkFieldConstraints(toVisit, parent, result);
            }

            // check if toVisit breaks any class constraint
            if (!violatedBreakingConstraint) {
                violatedBreakingConstraint = checkClassConstraints(toVisit, parent, result);
            }

            if (!violatedBreakingConstraint) {
                if (Iterable.class.isAssignableFrom(toVisit.getType())) { // case field is an iterable
                    result = PropertyVerificatorResult.mergeVerificationResults(result, visitorFactory.createIterableVisitor().visit((Iterable<?>) toVisit.get(parent), toVisit, parent, visitedObjs));
                } else if (toVisit.getType().isArray()) { // case field is an array
                    result = PropertyVerificatorResult.mergeVerificationResults(result, visitorFactory.createArrayVisitor().visit(toVisit.get(parent), fieldOfToVisitInParent, parent, visitedObjs));
                } else if (toVisit.getType().getPackage() != null &&
                        !toVisit.getType().getPackage().getName().startsWith("java")
                        && !toVisit.getType().isEnum()) { // case field is an object (but not a Boxing class from java package e.q. Integer)
                    result = PropertyVerificatorResult.mergeVerificationResults(result, visitorFactory.createObjectVisitor().visit(toVisit.get(parent), toVisit, parent, visitedObjs));
                }
            }
        } catch (IllegalAccessException e) {
            log.debug(String.format("FieldVisitor: could not get Field (%s) from parent (%s)", toVisit.getName(), parent), e);
        }

        toVisit.setAccessible(isAccessible); // reset accessibility

        return result;
    }

    /**
     * Help method for checking all general constraints of {@link PropertyRestrictor#getGeneralConstraints()}
     *
     * @param toVisit field for which constraints should be checked
     * @param parent  object containing the field
     * @param result  property restrictor result to which violated constraints are added
     * @return true if a {@link Constraint#isBreakingConstraint()} was violated
     */
    private boolean checkGeneralConstraints(Field toVisit, Object parent, PropertyVerificatorResult result) {
        for (Constraint<?> constraint : getConstraintsByPriority(visitorFactory.getRestrictor().getGeneralConstraints())) {
            if ((constraint.getType().isAssignableFrom(toVisit.getType()) || Autoboxers.getPrimitiveClasses().containsKey(toVisit.getType())) &&
                    (checkConstraintForField(toVisit, parent, result, constraint) && constraint.isBreakingConstraint())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Help method for checking all field constraints of {@link PropertyRestrictor#getFieldConstraints}
     *
     * @param toVisit field for which constraints should be checked
     * @param parent  object containing the field
     * @param result  property restrictor result to which violated constraints are added
     * @return true if a {@link Constraint#isBreakingConstraint()} was violated
     */
    private boolean checkFieldConstraints(Field toVisit, Object parent, PropertyVerificatorResult result) {
        Map<Field, Map<Integer, Constraint<?>>> fieldConstraints = visitorFactory.getRestrictor().getFieldConstraints();
        if (fieldConstraints.containsKey(toVisit)) {
            for (Constraint<?> constraint : getConstraintsByPriority(fieldConstraints.get(toVisit))) {
                if (checkConstraintForField(toVisit, parent, result, constraint) && constraint.isBreakingConstraint()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Help method for checking all class constraints of {@link PropertyRestrictor#getClassConstraints}
     *
     * @param toVisit field for which constraints should be checked
     * @param parent  object containing the field
     * @param result  property restrictor result to which violated constraints are added
     * @return true if a {@link Constraint#isBreakingConstraint()} was violated
     */
    private boolean checkClassConstraints(Field toVisit, Object parent, PropertyVerificatorResult result) {
        Map<Class<?>, Map<Integer, Constraint<?>>> classConstraints = visitorFactory.getRestrictor().getClassConstraints();
        Class<?> boxingClass = Autoboxers.getPrimitiveClasses().get(toVisit.getType());
        boolean classConstraintsContains = classConstraints.containsKey(toVisit.getType());
        if (classConstraintsContains || (boxingClass != null && classConstraints.containsKey(boxingClass))) {
            Map<Integer, Constraint<?>> map;

            if (classConstraintsContains) {
                map = classConstraints.get(toVisit.getType());
            } else {
                map = classConstraints.get(boxingClass);
            }

            for (Constraint<?> constraint : getConstraintsByPriority(map)) {
                if (checkConstraintForField(toVisit, parent, result, constraint) && constraint.isBreakingConstraint()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method for checking a constraint for a given field
     *
     * @param toVisit    the field to be checked
     * @param parent     the object containing the field
     * @param result     the visitor result to which a ConstraintError will be added if there is some
     * @param constraint The constraint which is checked
     * @return true if constrained is violated else false
     */
    private boolean checkConstraintForField(Field toVisit, Object parent, PropertyVerificatorResult result, Constraint<?> constraint) {
        try {
            Object o;
            Class<?> aClass = Autoboxers.getPrimitiveClasses().get(toVisit.getType());
            if ((constraint.getType().isAssignableFrom(toVisit.getType())) || (aClass != null && constraint.getType().isAssignableFrom(aClass))) {
                o = toVisit.get(parent);
            } else {
                return false;
            }

            ConstraintError error = constraint.apply(CastUtils.cast(o), toVisit);
            if (!error.equals(ConstraintError.NoError)) {
                result.addField(toVisit, error, parent);
                return true;
            }

        } catch (IllegalAccessException e) {
            log.debug("FieldVisitor - not able to access field of parent object to check classConstraints", e);
        }
        return false;
    }
}
