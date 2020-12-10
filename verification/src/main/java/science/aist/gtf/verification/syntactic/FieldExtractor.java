/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import science.aist.jack.reflection.Autoboxers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>Class for extracting fields of class</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldExtractor {

    public static List<Field> getAllFieldsForClass(Class<?> clazz) {
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
     * Method for getting a field by name and given class
     *
     * @param clazz     Class which contains the field
     * @param fieldName Name of the Field
     * @return the field
     * @throws IllegalArgumentException if there is no field with given name
     */
    public static Field getClassField(Class<?> clazz, String fieldName) {
        Optional<Field> f = FieldExtractor.getAllFieldsForClass(clazz).stream().filter(field -> field.getName().equals(fieldName)).findFirst();
        if (f.isEmpty()) {
            throw new IllegalArgumentException("Could not find field (" + fieldName + ") in class (" + clazz.getName() + ")");
        }

        return f.get();
    }

    /**
     * Get default value of given clazz
     *
     * @param field field to be checked if there is a user defined default value
     * @param <T>   type of clazz
     * @return default value of field; if no user defined value was set returns default value of clazz
     */
    @SuppressWarnings("unchecked")
    public static <T> T getDefaultValueForField(Field field) {
        return (T) getDefaultValueForClass(field.getType());
    }

    /**
     * Get default value of given clazz
     *
     * @param clazz clazz for which default value should be get
     * @param <T>   type of clazz
     * @return default value of clazz
     */
    @SuppressWarnings("unchecked")
    public static <T> T getDefaultValueForClass(Class<T> clazz) {
        return (T) Autoboxers.getPrimitiveDefaultValues().get(clazz);
    }
}
