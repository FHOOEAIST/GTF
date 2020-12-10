/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic;

import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.verification.syntactic.constraint.impl.DefaultInitializedConstraint;
import science.aist.jack.data.Pair;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Test class for {@link PropertyRestrictor}</p>
 *
 * @author Christoph Praschl
 */
public class PropertyRestrictorTest {
    @Test
    public void testSetIgnoredClasses() {
        // given
        PropertyRestrictor restrictor = new PropertyRestrictor();
        List<Class<?>> classes = new ArrayList<>();
        classes.add(Integer.class);
        classes.add(Pair.class);
        classes.add(Boolean.class);

        // when
        restrictor.setIgnoredClasses(classes);

        // then
        Assert.assertFalse(restrictor.getIgnoredClasses().isEmpty());
        Assert.assertTrue(restrictor.getIgnoredClasses().contains(Integer.class));
        Assert.assertTrue(restrictor.getIgnoredClasses().contains(Pair.class));
        Assert.assertTrue(restrictor.getIgnoredClasses().contains(Boolean.class));
    }

    @Test
    public void testSetIgnoredFieldsInClass() {
        // given
        PropertyRestrictor restrictor = new PropertyRestrictor();
        Collection<Pair<Class<?>, String>> ignoredFields = new HashSet<>();
        ignoredFields.add(Pair.of(Pair.class, "first"));
        ignoredFields.add(Pair.of(Pair.class, "second"));
        ignoredFields.add(Pair.of(Integer.class, "SIZE"));
        ignoredFields.add(Pair.of(Integer.class, "BYTES"));

        // when
        restrictor.setIgnoredFieldsInClass(ignoredFields);

        // then
        Assert.assertEquals(restrictor.getIgnoredFieldsInClass().size(), 4);

        AtomicInteger IntegerFields = new AtomicInteger(0);
        AtomicInteger PairField = new AtomicInteger(0);
        AtomicInteger otherFields = new AtomicInteger(0);

        restrictor.getIgnoredFieldsInClass().forEach(field -> {
            switch (field.getDeclaringClass().getSimpleName()) {
                case "Pair":
                    PairField.incrementAndGet();
                    break;
                case "Integer":
                    IntegerFields.incrementAndGet();
                    break;
                default:
                    otherFields.incrementAndGet();
                    break;
            }
        });

        Assert.assertEquals(IntegerFields.get(), 2);
        Assert.assertEquals(PairField.get(), 2);
        Assert.assertEquals(otherFields.get(), 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetIgnoredFieldsInClassWithException() {
        // given
        PropertyRestrictor restrictor = new PropertyRestrictor();
        Collection<Pair<Class<?>, String>> ignoredFields = new HashSet<>();
        ignoredFields.add(Pair.of(Pair.class, "test"));

        // when
        restrictor.setIgnoredFieldsInClass(ignoredFields);

        // then
        // Exception
    }

    @Test
    public void testSetUserdefinedDefaultValues() {
        // given
        PropertyRestrictor restrictor = new PropertyRestrictor();
        Map<Class<?>, Map<String, Object>> userdefinedDefaultValues = new HashMap<>();

        Map<String, Object> tupleDefaultValues = new HashMap<>();
        tupleDefaultValues.put("first", 5);
        tupleDefaultValues.put("second", 10);
        userdefinedDefaultValues.put(Pair.class, tupleDefaultValues);

        Map<String, Object> integerDefaultValues = new HashMap<>();
        integerDefaultValues.put("SIZE", 1);
        userdefinedDefaultValues.put(Integer.class, integerDefaultValues);
        restrictor.addGeneralConstraint(Integer.MIN_VALUE + 1, new DefaultInitializedConstraint());

        // when
        ((DefaultInitializedConstraint) restrictor.getGeneralConstraints().get(Integer.MIN_VALUE + 1)).setUserdefinedDefaultValues(userdefinedDefaultValues);

        // then
        DefaultInitializedConstraint defaultInitializedConstraint = ((DefaultInitializedConstraint) restrictor.getGeneralConstraints().get(Integer.MIN_VALUE + 1));
        Assert.assertEquals(defaultInitializedConstraint.getUserdefinedDefaultValues().size(), 3);
        Set<Field> fieldSet = defaultInitializedConstraint.getUserdefinedDefaultValues().keySet();

        AtomicInteger IntegerFields = new AtomicInteger(0);
        AtomicInteger PairField = new AtomicInteger(0);
        AtomicInteger otherFields = new AtomicInteger(0);

        fieldSet.forEach(field -> {
            switch (field.getDeclaringClass().getSimpleName()) {
                case "Pair":
                    PairField.incrementAndGet();
                    if (field.getName().equals("first")) {
                        Assert.assertEquals(defaultInitializedConstraint.getUserdefinedDefaultValues().get(field), 5);
                    } else if (field.getName().equals("second")) {
                        Assert.assertEquals(defaultInitializedConstraint.getUserdefinedDefaultValues().get(field), 10);
                    }
                    break;
                case "Integer":
                    if (field.getName().equals("SIZE")) {
                        Assert.assertEquals(defaultInitializedConstraint.getUserdefinedDefaultValues().get(field), 1);
                    }
                    IntegerFields.incrementAndGet();
                    break;
                default:
                    otherFields.incrementAndGet();
                    break;
            }
        });

        Assert.assertEquals(IntegerFields.get(), 1);
        Assert.assertEquals(PairField.get(), 2);
        Assert.assertEquals(otherFields.get(), 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetUserdefinedDefaultValuesWithException() {
        // given
        Map<Class<?>, Map<String, Object>> userdefinedDefaultValues = new HashMap<>();

        Map<String, Object> tupleDefaultValues = new HashMap<>();
        tupleDefaultValues.put("test", 5);
        userdefinedDefaultValues.put(Pair.class, tupleDefaultValues);
        DefaultInitializedConstraint constraint = new DefaultInitializedConstraint();

        // when
        constraint.setUserdefinedDefaultValues(userdefinedDefaultValues);

        // then
        // Exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetUserdefinedDefaultValuesWithException2() {
        // given
        Map<Class<?>, Map<String, Object>> userdefinedDefaultValues = new HashMap<>();

        Map<String, Object> integerDefaultValues = new HashMap<>();
        integerDefaultValues.put("SIZE", "test");
        userdefinedDefaultValues.put(Integer.class, integerDefaultValues);
        DefaultInitializedConstraint constraint = new DefaultInitializedConstraint();

        // when
        constraint.setUserdefinedDefaultValues(userdefinedDefaultValues);

        // then
        // Exception
    }
}
