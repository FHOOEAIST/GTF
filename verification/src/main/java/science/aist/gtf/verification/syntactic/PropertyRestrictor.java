/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic;

import lombok.Getter;
import lombok.Setter;
import science.aist.gtf.verification.syntactic.constraint.Constraint;
import science.aist.gtf.verification.syntactic.constraint.impl.DefaultInitializedConstraint;
import science.aist.gtf.verification.syntactic.constraint.impl.EmptyContainerConstraint;
import science.aist.gtf.verification.syntactic.constraint.impl.NullConstraint;
import science.aist.jack.data.Pair;
import science.aist.jack.reflection.Autoboxers;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>Property restrictor which consists of collections of classes, fields, default values which should be considered in
 * property verification process</p>
 * <p>
 * General Constraint Convention: Normally you will need {@link NullConstraint}, {@link DefaultInitializedConstraint}
 * and {@link EmptyContainerConstraint}. If default constructor is used those will be initialized at {@link
 * PropertyRestrictor#generalConstraints} indices Integer.MIN_VALUE, Integer.MIN_VALUE + 1 and Integer.MIN_VALUE + 2.
 * <p>
 * You can use the PropertyRestrictor in a spring configuration file like:
 * <pre>
 *    {@code     <bean name="restrictor" class="science.aist.gtf.verification.syntactic.PropertyRestrictor">
 *         <!-- ignored classes -->
 *         <property name="ignoredClasses">
 *             <list>
 *                 <value>java.lang.String</value>
 *                 <value>java.lang.Integer</value>
 *                 <value>science.aist.gtf.core.FieldTuple</value>
 *             </list>
 *         </property>
 *
 *         <!-- ignored fields of class -->
 *         <property name="ignoredFieldsInClass">
 *             <list value-type="science.aist.gtf.core.FieldTuple">
 *                 <bean class="science.aist.gtf.core.FieldTuple">
 *                     <constructor-arg name="first" value="java.lang.Integer"/>
 *                     <constructor-arg name="second" value="SIZE"/>
 *                 </bean>
 *                 <bean class="science.aist.gtf.core.FieldTuple">
 *                     <constructor-arg name="first" value="java.lang.Integer"/>
 *                     <constructor-arg name="second" value="BYTES"/>
 *                 </bean>
 *                 <bean class="science.aist.gtf.core.FieldTuple">
 *                     <constructor-arg name="first" value="science.aist.gtf.core.FieldTuple"/>
 *                     <constructor-arg name="second" value="first"/>
 *                 </bean>
 *                 <bean class="science.aist.gtf.core.FieldTuple">
 *                     <constructor-arg name="first" value="science.aist.gtf.core.FieldTuple"/>
 *                     <constructor-arg name="second" value="second"/>
 *                 </bean>
 *             </list>
 *         </property>
 *
 *         <!-- general constraints -->
 *
 *         <property name="generalConstraints">
 *             <map>
 *                 <entry key="#{ T(java.lang.Integer).MIN_VALUE }">
 *                     <bean class="science.aist.gtf.verification.syntactic.constraint.impl.NullConstraint"/>
 *                 </entry>
 *                 <entry key="#{ T(java.lang.Integer).MIN_VALUE + 1 }">
 *                     <bean class="science.aist.gtf.verification.syntactic.constraint.impl.DefaultInitializedConstraint">
 *                         <property name="userdefinedDefaultValues">
 *                             <map>
 *                                 <entry key="science.aist.gtf.core.Tuple">
 *                                     <map>
 *                                         <entry key="first" value="10"/>
 *                                         <entry key="second" value="5"/>
 *                                     </map>
 *                                 </entry>
 *                                 <entry key="java.lang.Integer">
 *                                     <map>
 *                                         <entry key="SIZE" value-type="java.lang.Integer" value="10"/>
 *                                     </map>
 *                                 </entry>
 *                             </map>
 *                         </property>
 *                     </bean>
 *                 </entry>
 *                 <entry key="#{ T(java.lang.Integer).MIN_VALUE + 2 }">
 *                     <bean class="science.aist.gtf.verification.syntactic.constraint.impl.EmptyContainerConstraint"/>
 *                 </entry>
 *             </map>
 *         </property>
 *
 *         <!-- class constraints -->
 *         <property name="classConstraints">
 *             <map>
 *                 <entry key="java.lang.Integer">
 *                     <map>
 *                         <entry key="0">
 *                             <bean class="science.aist.gtf.verification.syntactic.constraint.impl.IntegerRangeConstraint">
 *                                 <property name="lowerBound" value="0"/>
 *                                 <property name="upperBound" value="5"/>
 *                             </bean>
 *                         </entry>
 *                     </map>
 *                 </entry>
 *             </map>
 *         </property>
 *
 *         <!-- fields per class constraints -->
 *
 *         <property name="fieldConstraints">
 *             <map>
 *                 <entry>
 *                     <key>
 *                         <bean name="keyBean1" class="science.aist.gtf.core.FieldTuple">
 *                             <constructor-arg name="first" value="test.something.SomeClass"/>
 *                             <constructor-arg name="second" value="someBooleanField"/>
 *                         </bean>
 *                     </key>
 *                     <map>
 *                         <entry key="0">
 *                             <bean class="science.aist.gtf.verification.syntactic.constraint.impl.BooleanConstraint">
 *                                 <property name="expectedValue" value="true"/>
 *                             </bean>
 *                         </entry>
 *                     </map>
 *                 </entry>
 *             </map>
 *         </property>
 *     </bean>}
 * </pre>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public class PropertyRestrictor {
    /**
     * Collection with classes which should be ignored
     */
    @Setter
    private Collection<Class<?>> ignoredClasses;

    /**
     * Collection with tuples for a field in a given class which should be ignored
     */
    private Collection<Field> ignoredFieldsInClass;

    /**
     * General constraints checked for every field if suitable
     */
    @Getter
    @Setter
    private Map<Integer, Constraint<?>> generalConstraints = new HashMap<>();

    /**
     * Constraints for a specific field
     */
    @Getter
    private Map<Field, Map<Integer, Constraint<?>>> fieldConstraints = new HashMap<>();

    /**
     * Constraints for a specific class
     */
    @Getter
    @Setter
    private Map<Class<?>, Map<Integer, Constraint<?>>> classConstraints = new HashMap<>();

    /**
     * Constructs a property restrictor with initialized general constraints containing: {@link NullConstraint} at index
     * {@link Integer#MIN_VALUE}, {@link DefaultInitializedConstraint}  at index {@link Integer#MIN_VALUE} + 1 and
     * {@link EmptyContainerConstraint} at index {@link Integer#MIN_VALUE} + 2.
     */
    public PropertyRestrictor() {
        this(true);
    }

    /**
     * Constructs a property restrictor with initialized general constraints containing if initializeGeneralConstraints
     * is true: {@link NullConstraint} at index {@link Integer#MIN_VALUE}, {@link DefaultInitializedConstraint}  at
     * index {@link Integer#MIN_VALUE} + 1 and {@link EmptyContainerConstraint} at index {@link Integer#MIN_VALUE} + 2.
     *
     * @param initializeGeneralConstraints if true initializes the three general constraints
     */
    public PropertyRestrictor(boolean initializeGeneralConstraints) {
        this(initializeGeneralConstraints, initializeGeneralConstraints, initializeGeneralConstraints);
    }

    /**
     * Constructs a property restrictor with initialized general constraints containing {@link NullConstraint}, {@link
     * DefaultInitializedConstraint} and {@link EmptyContainerConstraint}.
     *
     * @param initializeNullConstraint               if true NullConstraint will be initialized at position {@link
     *                                               Integer#MIN_VALUE}
     * @param initializeDefaultInitializedConstraint if true DefaultInitializedConstraint will be initialized at
     *                                               position {@link Integer#MIN_VALUE} + 1
     * @param initializeEmptyContainerConstraint     if true EmptyContainerConstraint will be initialized at position
     *                                               {@link Integer#MIN_VALUE} + 2
     */
    public PropertyRestrictor(boolean initializeNullConstraint, boolean initializeDefaultInitializedConstraint, boolean initializeEmptyContainerConstraint) {
        if (initializeNullConstraint) {
            generalConstraints.put(Integer.MIN_VALUE, new NullConstraint());
        }
        if (initializeDefaultInitializedConstraint) {
            generalConstraints.put(Integer.MIN_VALUE + 1, new DefaultInitializedConstraint());
        }

        if (initializeEmptyContainerConstraint) {
            generalConstraints.put(Integer.MIN_VALUE + 2, new EmptyContainerConstraint());
        }
    }

    private static void checkConstraint(Field f, Constraint<?> c) {
        Class<?> aClass = Autoboxers.getPrimitiveClasses().get(f.getType());
        if (aClass != null && !aClass.equals(c.getType()) && !f.getType().equals(c.getType())) {
            throw new IllegalArgumentException("Adding Field constraint not possible. Non matching types");
        }

        if (aClass == null && !f.getType().equals(c.getType())) {
            throw new IllegalArgumentException("Adding Field constraint not possible. Non matching types");
        }
    }

    /**
     * helper method for adding to a constraint map
     *
     * @param map map to which constraint should be added
     * @param key key position to which constraint should be added
     * @param c   constraint which should be added
     * @param <T> type of map key
     */
    private static <T> void addToMap(Map<T, Map<Integer, Constraint<?>>> map, T key, Constraint<?> c) {
        if (map.containsKey(key)) {
            map.get(key).keySet().stream().max(Integer::compareTo).ifPresent(integer -> addToMap(map, key, c, integer + 1));
        } else {
            addToMap(map, key, c, 0);
        }
    }

    /**
     * helper method for adding to a constraint map
     *
     * @param map      map to which constraint should be added
     * @param key      key position to which constraint should be added
     * @param c        constraint which should be added
     * @param priority of the constraint
     * @param <T>      type of map key
     */
    private static <T> void addToMap(Map<T, Map<Integer, Constraint<?>>> map, T key, Constraint<?> c, Integer priority) {
        if (map.containsKey(key)) {
            map.get(key).put(priority, c);
        } else {
            Map<Integer, Constraint<?>> m = new HashMap<>();
            m.put(priority, c);
            map.put(key, m);
        }
    }

    /**
     * @return Collection with classes which should be ignored
     */
    public Collection<Class<?>> getIgnoredClasses() {
        if (ignoredClasses == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(ignoredClasses);
    }

    /**
     * @return Collection with Tuples for a field in a given class which should be ignored
     */
    public Collection<Field> getIgnoredFieldsInClass() {
        if (ignoredFieldsInClass == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(ignoredFieldsInClass);
    }

    /**
     * @param ignoredFieldsInClass sets the collection of {@link PropertyRestrictor#ignoredFieldsInClass}
     */
    public void setIgnoredFieldsInClass(Iterable<Pair<Class<?>, String>> ignoredFieldsInClass) {
        this.ignoredFieldsInClass = new HashSet<>();
        ignoredFieldsInClass.forEach(classStringTuple -> addIgnoredClassField(classStringTuple.getFirst(), classStringTuple.getSecond()));
    }

    /**
     * Method to ignore a field in a class  (helpful for fields which are not touched by transformation for some
     * reason)
     *
     * @param field Field which should be ignored
     */
    public void addIgnoredClassField(Field field) {
        if (ignoredFieldsInClass == null) {
            ignoredFieldsInClass = new HashSet<>();
        }

        ignoredFieldsInClass.add(field);
    }

    /**
     * Method to ignore a field in a class (helpful for fields which are not touched by transformation for some reason)
     *
     * @param clazz     Class which contains the field
     * @param fieldName Name of the Field which should be ignored
     */
    public void addIgnoredClassField(Class<?> clazz, String fieldName) {
        addIgnoredClassField(FieldExtractor.getClassField(clazz, fieldName));
    }

    /**
     * Method to add a class to be ignored (helpful for e.q. boolean which has often the default value `false` and is so
     * often misinterpreted as IsDefaultInitialized)
     *
     * @param clazz clazz which should be ignored
     */
    public void addIgnoredClass(Class<?> clazz) {
        if (ignoredClasses == null) {
            ignoredClasses = new HashSet<>();
        }

        ignoredClasses.add(clazz);
    }

    /**
     * @param fieldConstraints Constraints for a specific field
     */
    @SuppressWarnings("unused" /*This is used in other scopes*/)
    public void setFieldConstraints(Map<Pair<Class<?>, String>, Map<Integer, Constraint<?>>> fieldConstraints) {
        Map<Field, Map<Integer, Constraint<?>>> res = new HashMap<>();
        fieldConstraints.forEach((classStringTuple, integerConstraintMap) ->
                res.put(FieldExtractor.getClassField(classStringTuple.getFirst(), classStringTuple.getSecond()), integerConstraintMap)
        );

        this.fieldConstraints = res;
    }

    /**
     * Adds a general constraint checked for every field if suitable
     *
     * @param priority priority of the constraint
     * @param c        constraint
     */
    public void addGeneralConstraint(Integer priority, Constraint<?> c) {
        generalConstraints.put(priority, c);
    }

    /**
     * Add a constraint for a given field (takes the next possible priority)
     *
     * @param f field for which the constraint should be defined
     * @param c constraint which should be added
     */
    public void addFieldConstraint(Field f, Constraint<?> c) {
        checkConstraint(f, c);
        addToMap(fieldConstraints, f, c);
    }

    /**
     * Add a constraint for a given field (takes the next possible priority)
     *
     * @param clazz     Class which contains the field
     * @param fieldName Name of the Field for which constraint is added
     * @param c         constraint which should be added
     */
    public void addFieldConstraint(Class<?> clazz, String fieldName, Constraint<?> c) {
        addFieldConstraint(FieldExtractor.getClassField(clazz, fieldName), c);
    }

    /**
     * Add a constraint for a given field with a given priority Attention: Only one constraint per priority! Calling
     * multiple times with same priority will always replace the constraint.
     *
     * @param f        field for which the constraint should be defined
     * @param c        constraint which should be added
     * @param priority the priority of the constraint
     */
    public void addFieldConstraint(Field f, Constraint<?> c, Integer priority) {
        checkConstraint(f, c);
        addToMap(fieldConstraints, f, c, priority);
    }

    /**
     * Add a constraint for a given class (takes the next possible priority)
     *
     * @param clazz class for which the constraint should be defined
     * @param c     constraint which should be added
     * @param <T>   type parameter of class
     */
    public <T> void addClassConstraint(Class<T> clazz, Constraint<T> c) {
        addToMap(classConstraints, clazz, c);
    }

    /**
     * Add a constraint for a given class with a given priority Attention: Only one constraint per priority! Calling
     * multiple times with same priority will always replace the constraint.
     *
     * @param clazz    class for which the constraint should be defined
     * @param c        constraint which should be added
     * @param <T>      type parameter of class
     * @param priority the priority of the constraint
     */
    public <T> void addClassConstraint(Class<T> clazz, Constraint<T> c, Integer priority) {
        addToMap(classConstraints, clazz, c, priority);
    }
}
