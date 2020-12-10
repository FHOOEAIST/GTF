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
import science.aist.gtf.verification.syntactic.FieldExtractor;
import science.aist.gtf.verification.syntactic.constraint.Constraint;
import science.aist.gtf.verification.syntactic.constraint.ConstraintError;
import science.aist.jack.reflection.Autoboxers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>Constraint which checks if an object is default initialized</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public class DefaultInitializedConstraint implements Constraint<Object> {

    /**
     * Map containing user defined default values for certain fields
     */
    @Getter
    private Map<Field, Object> userdefinedDefaultValues;

    /**
     * Helper method for checking if fieldType and defaultValueType are suitable in case of (un)boxing
     *
     * @param fieldType        type of the field
     * @param defaultValueType type of the default value
     * @return true if fieldType and defaultValueType are suitable else false
     */
    private static boolean isSuitableBoxingType(Class<?> fieldType, Class<?> defaultValueType) {
        return (Autoboxers.getBoxingClasses().containsKey(fieldType) && Autoboxers.getBoxingClasses().get(fieldType).equals(defaultValueType)) || (Autoboxers.getBoxingClasses().containsKey(defaultValueType) && Autoboxers.getBoxingClasses().get(defaultValueType).equals(fieldType));
    }

    /**
     * Helper method for checking if fieldType is a sub or super class of defaultValueType
     *
     * @param fieldType        type of the field
     * @param defaultValueType type of the default value
     * @return true if fieldType and defaultValueType share derivation hierarchy else false
     */
    private static boolean shareDerivationHierarchy(Class<?> fieldType, Class<?> defaultValueType) {
        return (fieldType.isAssignableFrom(defaultValueType) || defaultValueType.isAssignableFrom(fieldType));
    }

    /**
     * Get default value of given clazz
     *
     * @param field field to be checked if there is a user defined default value
     * @param <T>   type of clazz
     * @return default value of field; if no user defined value was set returns default value of clazz
     */
    @SuppressWarnings("unchecked")
    public <T> T getDefaultValueForField(Field field) {
        if (userdefinedDefaultValues != null && userdefinedDefaultValues.containsKey(field)) {
            return (T) userdefinedDefaultValues.get(field);
        }

        return FieldExtractor.getDefaultValueForField(field);
    }

    /**
     * Method for adding user defined default values for specific fields of a given class
     * @param field field for which the default value should be defined
     * @param defaultValue the new default value
     * @param <T> Generic type of the default value
     */
    public <T> void addUserdefinedDefaultValue(Field field, T defaultValue) {
        if (userdefinedDefaultValues == null) {
            userdefinedDefaultValues = new HashMap<>();
        }

        userdefinedDefaultValues.put(field, defaultValue);
    }

    /**
     * Method for adding user defined default values for specific fields of a given class
     * @param clazz clazz containing the field
     * @param fieldName name of the field for which the default value should be defined
     * @param defaultValue the new default value
     * @param <T> Generic type of the default value
     */
    public <T> void addUserdefinedDefaultValue(Class<?> clazz, String fieldName, T defaultValue) {
        Optional<Field> f = FieldExtractor.getAllFieldsForClass(clazz).stream().filter(field -> field.getName().equals(fieldName)).findFirst();
        if (f.isEmpty()) {
            throw new IllegalArgumentException("Could not find field (" + fieldName + ") in class (" + clazz.getName() + ")");
        }
        if (!f.get().getType().equals(defaultValue.getClass()) && !isSuitableBoxingType(f.get().getType(), defaultValue.getClass()) && !shareDerivationHierarchy(f.get().getType(), defaultValue.getClass())) {
            throw new IllegalArgumentException("Default value (" + defaultValue + ") with type(" + defaultValue.getClass() + ") is not compatible with field (" + fieldName + ") with type(" + f.get().getType() + ") of class (" + clazz.getName() + ")");
        }

        addUserdefinedDefaultValue(f.get(), defaultValue);
    }

    /**
     * @param userdefinedDefaultValues sets the collection of {@link DefaultInitializedConstraint#userdefinedDefaultValues}
     */
    public void setUserdefinedDefaultValues(Map<Class<?>, Map<String, Object>> userdefinedDefaultValues) {
        this.userdefinedDefaultValues = new HashMap<>();
        userdefinedDefaultValues.forEach((aClass, stringObjectMap) -> stringObjectMap.forEach((s, o) -> addUserdefinedDefaultValue(aClass, s, o)));
    }

    @Override
    public ConstraintError apply(Object o, Field f) {
        if (o.equals(getDefaultValueForField(f))) {
            return ConstraintError.IsDefaultInitialized;
        }

        return ConstraintError.NoError;
    }

    @Override
    public Class<Object> getType() {
        return Object.class;
    }

    @Override
    public boolean isBreakingConstraint() {
        return false;
    }
}
