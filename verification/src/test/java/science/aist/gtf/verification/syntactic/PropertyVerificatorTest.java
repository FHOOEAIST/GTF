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
import science.aist.gtf.verification.syntactic.constraint.ConstraintError;
import science.aist.gtf.verification.syntactic.constraint.ConstraintViolationStatistic;
import science.aist.gtf.verification.syntactic.constraint.impl.*;
import science.aist.gtf.verification.syntactic.domain.threed.Model3D;
import science.aist.gtf.verification.syntactic.domain.threed.Object3D;
import science.aist.gtf.verification.syntactic.domain.transformation.Model2DToModel3DTransformer;
import science.aist.gtf.verification.syntactic.domain.twod.Model2D;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Test class for {@link PropertyVerificator}</p>
 *
 * @author Andreas Pointner
 * @author Christoph Praschl
 */
public class PropertyVerificatorTest {
    @Test
    public void testConstraintViolationStatistic() {
        // given
        PropertyVerificator<Vehicle> propertyVerificator = new PropertyVerificator<>();
        Vehicle c = new Vehicle(1, 2, 3);
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // when
        ConstraintViolationStatistic statistic = result.createStatistic();

        // then
        Assert.assertNotNull(statistic);
        Assert.assertEquals(statistic.getInvalidObjects(), 0);
        Assert.assertEquals(statistic.getInvalidProperties(), 0);
        Assert.assertEquals(statistic.getViolatedIsNull(), 0);
        Assert.assertEquals(statistic.getViolatedIsDefaultInitialized(), 0);
        Assert.assertEquals(statistic.getViolatedIsEmpty(), 0);
    }

    @Test
    public void testConstraintViolationStatistic2() {
        // given
        PropertyVerificator<Vehicle> propertyVerificator = new PropertyVerificator<>();
        Vehicle c = new Vehicle();
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // when
        ConstraintViolationStatistic statistic = result.createStatistic();

        // then
        Assert.assertNotNull(statistic);
        Assert.assertEquals(statistic.getInvalidObjects(), 1);
        Assert.assertEquals(statistic.getInvalidProperties(), 3);
        Assert.assertEquals(statistic.getViolatedIsNull(), 0);
        Assert.assertEquals(statistic.getViolatedIsDefaultInitialized(), 3);
        Assert.assertEquals(statistic.getViolatedIsEmpty(), 0);
        Assert.assertEquals(statistic.getViolatedIsDefaultInitialized(), 3);
        statistic.getDefaultViolators().values().forEach(fields -> fields.forEach(field -> Assert.assertTrue(field.getName().startsWith("tmp"))));
    }


    @Test
    public void testNoInvalidFields() {
        // given
        PropertyVerificator<Vehicle> propertyVerificator = new PropertyVerificator<>();
        Vehicle c = new Vehicle(1, 2, 3);

        // when
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testNoneInitializedFields() {
        // given
        PropertyVerificator<Vehicle> propertyVerificator = new PropertyVerificator<>();
        Vehicle c = new Vehicle();

        // when
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        result.getVerificationResult().forEach((o, tuples) -> tuples.forEach((field, constraint) -> Assert.assertSame(constraint, ConstraintError.IsDefaultInitialized)));
    }

    @Test
    public void testSomeInitializedFields() {
        // given
        PropertyVerificator<Vehicle> propertyVerificator = new PropertyVerificator<>();
        Vehicle c = new Vehicle(1);

        // when
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        result.getVerificationResult()
                .forEach((o, tuples) ->
                        tuples.forEach((field, constraint) ->
                                Assert.assertSame(constraint, ConstraintError.IsDefaultInitialized)));

        result.getVerificationResult().forEach((o, tuples) -> Assert.assertEquals(tuples.size(), 2));
    }

    @Test
    public void testWithClassWithNoProperties() {
        // given
        PropertyVerificator<ClassWithoutProperties> propertyVerificator = new PropertyVerificator<>();
        ClassWithoutProperties c = new ClassWithoutProperties();

        // when
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testWithComplexClass() {
        // given
        PropertyVerificator<Car> propertyVerificator = new PropertyVerificator<>();
        Car c = new Car();

        // when
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        ConstraintViolationStatistic statistic = result.createStatistic();

        Assert.assertEquals(statistic.getInvalidObjects(), 4);
        Assert.assertEquals(statistic.getInvalidProperties(), 18);
        Assert.assertEquals(statistic.getViolatedIsNull(), 4);
        Assert.assertEquals(statistic.getViolatedIsDefaultInitialized(), 13);
        Assert.assertEquals(statistic.getViolatedIsEmpty(), 1);
    }

    @Test
    public void testWithComplexClass2() {
        // given
        PropertyVerificator<WheelList> propertyVerificator = new PropertyVerificator<>();
        WheelList wl = new WheelList();

        // when
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(wl);

        // then
        ConstraintViolationStatistic statistic = result.createStatistic();
        Assert.assertEquals(statistic.getInvalidObjects(), 3);
        Assert.assertEquals(statistic.getInvalidProperties(), 3);
        Assert.assertEquals(statistic.getViolatedIsNull(), 0);
        Assert.assertEquals(statistic.getViolatedIsDefaultInitialized(), 3);
        Assert.assertEquals(statistic.getViolatedIsEmpty(), 0);
    }

    @Test
    public void testWithUserdefinedDefaultValue1() {
        // given
        PropertyVerificator<Car> propertyVerificator = new PropertyVerificator<>();
        Car c = new Car();
        c.srn = "Test";
        PropertyRestrictor restrictor = new PropertyRestrictor();
        propertyVerificator.setRestrictor(restrictor);

        DefaultInitializedConstraint constraint = new DefaultInitializedConstraint();
        constraint.addUserdefinedDefaultValue(Car.class, "srn", "Test");
        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE + 1, constraint);

        // when
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);


        // then
        ConstraintViolationStatistic statistic = result.createStatistic();

        Assert.assertEquals(statistic.getInvalidObjects(), 4);
        Assert.assertEquals(statistic.getInvalidProperties(), 18);
        Assert.assertEquals(statistic.getViolatedIsNull(), 3);
        Assert.assertEquals(statistic.getViolatedIsDefaultInitialized(), 14);
        Assert.assertEquals(statistic.getViolatedIsEmpty(), 1);
    }

    @Test
    public void testWithUserdefinedDefaultValue2() {
        // given
        PropertyVerificator<Car> propertyVerificator = new PropertyVerificator<>();
        Car c = new Car();
        c.srn = "Test";
        PropertyRestrictor restrictor = new PropertyRestrictor();
        propertyVerificator.setRestrictor(restrictor);


        DefaultInitializedConstraint constraint = new DefaultInitializedConstraint();
        constraint.addUserdefinedDefaultValue(Car.class, "srn", "somethingElse");
        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE + 1, constraint);

        // when
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        ConstraintViolationStatistic statistic = result.createStatistic();

        Assert.assertEquals(statistic.getInvalidObjects(), 4);
        Assert.assertEquals(statistic.getInvalidProperties(), 17);
        Assert.assertEquals(statistic.getViolatedIsNull(), 3);
        Assert.assertEquals(statistic.getViolatedIsDefaultInitialized(), 13);
        Assert.assertEquals(statistic.getViolatedIsEmpty(), 1);
    }

    @Test
    public void testWithUserdefinedDefaultValue3() {
        // given
        PropertyVerificator<Car> propertyVerificator = new PropertyVerificator<>();
        Car c = new Car();
        c.srn = "Test";
        c.tmp4 = 10.5f;
        PropertyRestrictor restrictor = new PropertyRestrictor();
        propertyVerificator.setRestrictor(restrictor);

        DefaultInitializedConstraint constraint = new DefaultInitializedConstraint();
        constraint.addUserdefinedDefaultValue(Car.class, "srn", "somethingElse");
        constraint.addUserdefinedDefaultValue(Car.class, "tmp4", 10.0f);
        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE + 1, constraint);

        //when
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        ConstraintViolationStatistic statistic = result.createStatistic();

        Assert.assertEquals(statistic.getInvalidObjects(), 4);
        Assert.assertEquals(statistic.getInvalidProperties(), 16);
        Assert.assertEquals(statistic.getViolatedIsNull(), 2);
        Assert.assertEquals(statistic.getViolatedIsDefaultInitialized(), 13);
        Assert.assertEquals(statistic.getViolatedIsEmpty(), 1);
    }

    @Test
    public void testWithUserdefinedDefaultValue4() {
        // given
        PropertyVerificator<WheelList> propertyVerificator = new PropertyVerificator<>();
        PropertyRestrictor propertyRestrictor = new PropertyRestrictor();
        propertyVerificator.setRestrictor(propertyRestrictor);
        WheelList wl = new WheelList();


        DefaultInitializedConstraint constraint = new DefaultInitializedConstraint();
        constraint.addUserdefinedDefaultValue(Wheel.class, "dimension", 4);
        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE + 1, constraint);

        //when
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(wl);

        // then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testWithOutIgnore() {
        // given
        PropertyVerificator<SomeClass> propertyVerificator = new PropertyVerificator<>();
        SomeClass c = new SomeClass();

        // when
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then

        Assert.assertEquals(result.getVerificationResult().size(), 1);
        final Optional<Map.Entry<Object, Map<Field, ConstraintError>>> first = result.getVerificationResult().entrySet().stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Assert.assertEquals(first.get().getValue().size(), 3);
    }

    @Test
    public void testWithIgnoreBooleans() {
        // given
        PropertyVerificator<SomeClass> propertyVerificator = new PropertyVerificator<>();
        SomeClass c = new SomeClass();
        PropertyRestrictor restrictor = new PropertyRestrictor();
        propertyVerificator.setRestrictor(restrictor);

        // when
        restrictor.addIgnoredClass(Boolean.class);
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        Assert.assertEquals(result.getVerificationResult().size(), 1);
        final Optional<Map.Entry<Object, Map<Field, ConstraintError>>> first = result.getVerificationResult().entrySet().stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Map<Field, ConstraintError> innermap = first.get().getValue();
        Assert.assertEquals(innermap.size(), 1);
        final Optional<Map.Entry<Field, ConstraintError>> first1 = innermap.entrySet().stream().findFirst();
        Assert.assertTrue(first1.isPresent());
        Assert.assertEquals(first1.get().getKey().getType(), SomeEnum.class);
    }

    @Test
    public void testWithIgnoreFieldByName1() {
        // given
        PropertyVerificator<SomeClass> propertyVerificator = new PropertyVerificator<>();
        SomeClass c = new SomeClass();
        PropertyRestrictor restrictor = new PropertyRestrictor();
        propertyVerificator.setRestrictor(restrictor);

        // when
        restrictor.addIgnoredClassField(SomeClass.class, "b2");
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        Assert.assertEquals(result.getVerificationResult().size(), 1);
        final Optional<Map.Entry<Object, Map<Field, ConstraintError>>> first = result.getVerificationResult().entrySet().stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Map<Field, ConstraintError> innermap = first.get().getValue();

        Assert.assertEquals(innermap.size(), 2);
    }

    @Test
    public void testWithIgnoreFieldByName2() {
        // given
        PropertyVerificator<SomeClass> propertyVerificator = new PropertyVerificator<>();
        SomeClass c = new SomeClass();
        PropertyRestrictor restrictor = new PropertyRestrictor();
        propertyVerificator.setRestrictor(restrictor);

        // when
        restrictor.addIgnoredClassField(SomeClass.class, "s");
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        Assert.assertEquals(result.getVerificationResult().size(), 1);
        final Optional<Map.Entry<Object, Map<Field, ConstraintError>>> first = result.getVerificationResult().entrySet().stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Map<Field, ConstraintError> innermap = first.get().getValue();
        Assert.assertEquals(innermap.size(), 2);
    }

    @Test
    public void testWithIgnoreBooleansAndFieldByName() {
        // given
        PropertyVerificator<SomeClass> propertyVerificator = new PropertyVerificator<>();
        SomeClass c = new SomeClass();
        PropertyRestrictor restrictor = new PropertyRestrictor();
        propertyVerificator.setRestrictor(restrictor);

        // when
        restrictor.addIgnoredClass(Boolean.class);
        restrictor.addIgnoredClassField(SomeClass.class, "s");
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        Assert.assertEquals(result.getVerificationResult().size(), 0);
    }

    @Test
    public void testWithClassRestriction() {
        // given
        PropertyVerificator<Car> propertyVerificator = new PropertyVerificator<>();
        Car c = new Car();
        IntegerRangeConstraint integerRangeConstraint = new IntegerRangeConstraint();
        integerRangeConstraint.setLowerBound(-2);
        integerRangeConstraint.setUpperBound(-1);

        // when
        propertyVerificator.getRestrictor().addClassConstraint(Integer.class, integerRangeConstraint);
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        Map<Object, Map<Field, ConstraintError>> verificationResult = result.getVerificationResult();
        AtomicInteger x = new AtomicInteger();
        verificationResult.forEach((o, fieldConstraintErrorMap) -> fieldConstraintErrorMap.forEach((field, constraintError) -> {
            if (constraintError.equals(IntegerRangeConstraint.IntegerRangeError)) {
                x.incrementAndGet();
            }
        }));
        Assert.assertEquals(x.get(), 7);
    }

    @Test
    public void testWithFieldRestriction() {
        // given
        PropertyVerificator<Vehicle> propertyVerificator = new PropertyVerificator<>();
        Vehicle c = new Vehicle(1, 2, 3);
        Field fieldTmp1 = FieldExtractor.getClassField(Vehicle.class, "tmp1");

        IntegerRangeConstraint integerRangeConstraint = new IntegerRangeConstraint();
        integerRangeConstraint.setLowerBound(2);
        integerRangeConstraint.setUpperBound(3);

        // when
        propertyVerificator.getRestrictor().addFieldConstraint(fieldTmp1, integerRangeConstraint);
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        Map<Object, Map<Field, ConstraintError>> verificationResult = result.getVerificationResult();
        AtomicInteger x = new AtomicInteger();
        verificationResult.forEach((o, fieldConstraintErrorMap) -> fieldConstraintErrorMap.forEach((field, constraintError) -> {
            if (constraintError.equals(IntegerRangeConstraint.IntegerRangeError)) {
                x.incrementAndGet();
                Assert.assertEquals(field, fieldTmp1);
            }
        }));
        Assert.assertEquals(x.get(), 1);
    }

    @Test
    public void testWithFieldRestriction2() {
        // given
        PropertyVerificator<SomeClass> propertyVerificator = new PropertyVerificator<>();
        SomeClass c = new SomeClass(true, false, SomeEnum.first);
        Field fieldTmp1 = FieldExtractor.getClassField(SomeClass.class, "b");

        BooleanConstraint constraint = new BooleanConstraint();
        constraint.setExpectedValue(true);

        // when
        propertyVerificator.getRestrictor().addFieldConstraint(fieldTmp1, constraint);
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        Map<Object, Map<Field, ConstraintError>> verificationResult = result.getVerificationResult();
        AtomicInteger x = new AtomicInteger();
        verificationResult.forEach((o, fieldConstraintErrorMap) -> fieldConstraintErrorMap.forEach((field, constraintError) -> {
            if (constraintError.equals(BooleanConstraint.BooleanConstraintError)) {
                x.incrementAndGet();
                Assert.assertEquals(field, fieldTmp1);
            }
        }));
        Assert.assertEquals(x.get(), 0);
    }


    @Test
    public void testWithNotMatchingFieldRestriction() {
        // given
        PropertyVerificator<Vehicle> propertyVerificator = new PropertyVerificator<>();
        Vehicle c = new Vehicle(1, 2, 3);
        Field fieldTmp1 = FieldExtractor.getClassField(Vehicle.class, "tmp1");

        IntegerRangeConstraint integerRangeConstraint = new IntegerRangeConstraint();
        integerRangeConstraint.setLowerBound(1);
        integerRangeConstraint.setUpperBound(3);

        // when
        propertyVerificator.getRestrictor().addFieldConstraint(fieldTmp1, integerRangeConstraint);
        PropertyVerificatorResult result = propertyVerificator.getInvalidProperties(c);

        // then
        Map<Object, Map<Field, ConstraintError>> verificationResult = result.getVerificationResult();
        AtomicInteger x = new AtomicInteger();
        verificationResult.forEach((o, fieldConstraintErrorMap) -> fieldConstraintErrorMap.forEach((field, constraintError) -> {
            if (constraintError.equals(IntegerRangeConstraint.IntegerRangeError)) {
                x.incrementAndGet();
                Assert.assertEquals(field, fieldTmp1);
            }
        }));
        Assert.assertEquals(x.get(), 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWithNotSuitableFieldRestriction() {
        // given
        PropertyVerificator<Vehicle> propertyVerificator = new PropertyVerificator<>();
        Vehicle c = new Vehicle(1, 2, 3);
        Field fieldTmp1 = FieldExtractor.getClassField(SomeClass.class, "b");

        IntegerRangeConstraint integerRangeConstraint = new IntegerRangeConstraint();
        integerRangeConstraint.setLowerBound(2);
        integerRangeConstraint.setUpperBound(3);

        // when
        propertyVerificator.getRestrictor().addFieldConstraint(fieldTmp1, integerRangeConstraint);
        propertyVerificator.getInvalidProperties(c);

        // then - Exception
    }

    /**
     * Test for c.praschl masterthesis
     */
    @Test
    public void testModel2DToModel3D() {
        // given
        final double height = 5;
        PropertyVerificator<Model3D> propertyVerificator = new PropertyVerificator<>();
        DefaultInitializedConstraint constraint = new DefaultInitializedConstraint();
        constraint.addUserdefinedDefaultValue(Object3D.class, "height", height);
        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE + 1, constraint);

        Model2D model = Model2D.createSample();
        Model2DToModel3DTransformer transformer = new Model2DToModel3DTransformer();
        transformer.setHeight(height);

        // when
        Model3D result = transformer.applyTransformation(model);

        // then
        PropertyVerificatorResult invalidProperties = propertyVerificator.getInvalidProperties(result);
        Assert.assertEquals(invalidProperties.createStatistic().getViolatedIsDefaultInitialized(), 3);
        Assert.assertEquals(invalidProperties.createStatistic().getInvalidObjects(), 3);
    }

    /**
     * Test for c.praschl masterthesis
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void testModel2DToModel3D2() {
        // given
        final double height = 5;
        PropertyVerificator<?> propertyVerificator = new PropertyVerificator<>();
        DefaultInitializedConstraint constraint = new DefaultInitializedConstraint();
        constraint.addUserdefinedDefaultValue(Object3D.class, "height", height);
        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE + 1, constraint);

        Model2D model = Model2D.createSample();
        Model2DToModel3DTransformer transformer = new Model2DToModel3DTransformer();
        transformer.setHeight(height);

        // when
        transformer
                .verificateBefore(i -> i != null &&
                        i.rectangle != null &&
                        i.circle != null &&
                        i.triangle != null)
                .verificateAfter((i, o) -> o != null &&
                        o.cuboid != null &&
                        o.cylinder != null &&
                        o.prism != null &&
                        i.rectangle.length == o.cuboid.length &&  // throws exception
                        i.rectangle.width == o.cuboid.width &&
                        i.triangle.a == o.prism.a &&
                        i.triangle.b == o.prism.b &&
                        i.triangle.c == o.prism.c &&
                        i.circle.radius == o.cylinder.radius)
                .applyTransformation(model);

        // then
    }


    @Test
    public void testGeneralConstraintTest() {
        // given
        PropertyVerificator<Vehicle> propertyVerificator = new PropertyVerificator<>();
        IntegerRangeConstraint constraint = new IntegerRangeConstraint();
        constraint.setLowerBound(1);
        constraint.setUpperBound(6);

        propertyVerificator.getRestrictor().addGeneralConstraint(0, constraint);

        Vehicle v = new Vehicle(2, 3, 5);

        // when
        PropertyVerificatorResult invalidProperties = propertyVerificator.getInvalidProperties(v);

        // then
        Map<Object, List<Field>> fieldsByConstraint = invalidProperties.getFieldsByConstraint(IntegerRangeConstraint.IntegerRangeError);
        Assert.assertTrue(fieldsByConstraint.isEmpty());
    }

    @Test
    public void testGeneralConstraintTest2() {
        // given
        PropertyVerificator<Vehicle> propertyVerificator = new PropertyVerificator<>();
        IntegerRangeConstraint constraint = new IntegerRangeConstraint();
        constraint.setLowerBound(1);
        constraint.setUpperBound(6);

        propertyVerificator.getRestrictor().addGeneralConstraint(0, constraint);

        Vehicle v = new Vehicle(4, 9, 10);

        // when
        PropertyVerificatorResult invalidProperties = propertyVerificator.getInvalidProperties(v);

        // then
        Map<Object, List<Field>> fieldsByConstraint = invalidProperties.getFieldsByConstraint(IntegerRangeConstraint.IntegerRangeError);
        Assert.assertFalse(fieldsByConstraint.isEmpty());
        Assert.assertEquals(fieldsByConstraint.get(v).size(), 2);
    }

    @Test
    public void testGeneralConstraintTest3() {
        // given
        PropertyVerificator<SomeClass> propertyVerificator = new PropertyVerificator<>();

        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE, new NullConstraint());
        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE + 1, new DefaultInitializedConstraint());
        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE + 2, new EmptyContainerConstraint());

        SomeClass c = new SomeClass(null, true, SomeEnum.first);

        // when
        PropertyVerificatorResult invalidProperties = propertyVerificator.getInvalidProperties(c);

        // then
        Map<Object, List<Field>> fieldsByConstraint = invalidProperties.getFieldsByConstraint(ConstraintError.IsNull);
        Assert.assertEquals(fieldsByConstraint.size(), 1);
    }

    @Test
    public void testComplexUserDefinedDefaultValue() {
        // given
        C c = new C();
        B b1 = new B(c);
        B b2 = new B(c);
        B b3 = new B(c);
        A a = new A(b1, b2, b3);

        PropertyVerificator<A> propertyVerificator = new PropertyVerificator<>();

        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE, new NullConstraint());

        DefaultInitializedConstraint defaultInitializedConstraint = new DefaultInitializedConstraint();
        defaultInitializedConstraint.addUserdefinedDefaultValue(B.class, "c", c);
        defaultInitializedConstraint.addUserdefinedDefaultValue(B.class, "y", 5);
        defaultInitializedConstraint.addUserdefinedDefaultValue(A.class, "j", -2);
        defaultInitializedConstraint.addUserdefinedDefaultValue(C.class, "x", 10);

        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE + 1, defaultInitializedConstraint);
        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE + 2, new EmptyContainerConstraint());

        // when
        PropertyVerificatorResult invalidProperties = propertyVerificator.getInvalidProperties(a);

        // then
        ConstraintViolationStatistic statistic = invalidProperties.createStatistic();
        Assert.assertEquals(statistic.getInvalidObjects(), 5);
        Assert.assertEquals(statistic.getViolatedIsDefaultInitialized(), 8);
    }

    /**
     * Nested enum just for test
     */
    @SuppressWarnings("unused" /*Accessed fia reflection*/)
    private enum SomeEnum {
        first, second, third, fourth
    }

    /**
     * Nested enum just for test
     */
    private enum Type {BUS, CAR}

    /**
     * Nested class just for test
     */
    @SuppressWarnings("unused" /*Accessed fia reflection*/)
    private static class B {
        C c;
        int y = 5;

        public B(C c) {
            this.c = c;
        }
    }

    /**
     * Nested class just for test
     */
    @SuppressWarnings("unused" /*Accessed fia reflection*/)
    private static class C {
        int x = 10;
    }

    /**
     * Nested class just for test
     */
    @SuppressWarnings({"FieldCanBeLocal" /*Accessed fia reflection*/, "unused" /*Accessed fia reflection*/})
    private static class SomeClass {
        private Boolean b;
        private Boolean b2;
        private SomeEnum s;

        public SomeClass() {
        }

        public SomeClass(Boolean b, Boolean b2, SomeEnum s) {
            this.b = b;
            this.b2 = b2;
            this.s = s;
        }
    }

    /**
     * Nested class just for test
     */
    @SuppressWarnings("unused" /*Accessed fia reflection*/)
    private static class Vehicle {
        public int tmp3;
        protected int tmp2;
        private int tmp1;

        public Vehicle() {
        }

        public Vehicle(int tmp1) {
            this.tmp1 = tmp1;
        }

        public Vehicle(int tmp1, int tmp2, int tmp3) {
            this.tmp1 = tmp1;
            this.tmp2 = tmp2;
            this.tmp3 = tmp3;
        }
    }

    /**
     * Nested class just for test
     */
    @SuppressWarnings({"FieldCanBeLocal" /*Accessed fia reflection*/, "unused" /*Accessed fia reflection*/, "FieldMayBeFinal" /*Tests non final fields*/, "MismatchedQueryAndUpdateOfCollection" /*Accessed fia reflection*/, "MismatchedReadAndWriteOfArray" /*Accessed fia reflection*/})
    private static class Car extends Vehicle {
        private double tmp1;
        private boolean tmp2;
        private float tmp3;
        private Float tmp4;
        private long tmp5;
        private short tmp6;
        private byte tmp7;
        private Collection<Wheel> collection = new ArrayList<>();
        private Collection<Wheel> collection2 = new ArrayList<>(3);
        private Collection<Wheel> collection3 = null;
        private Wheel[] wheels2 = new Wheel[3];

        private Wheel[] wheels;

        private Wheel[] wheels3 = new Wheel[5];

        private String srn;

        private Type type;

        private Type type2 = Type.BUS;

        public Car() {
            collection.add(new Wheel());
            collection.add(new Wheel());
            wheels2[0] = new Wheel(1);
            wheels2[1] = new Wheel();
            type = Type.CAR;
        }
    }

    /**
     * Nested class just for test
     */
    @SuppressWarnings({"FieldCanBeLocal", "unused" /*Accessed fia reflection*/})
    private static class Wheel {
        private int dimension;

        public Wheel() {
        }

        public Wheel(int dimension) {
            this.dimension = dimension;
        }
    }

    private static class WheelList {
        public List<Wheel> wheels = new ArrayList<>();

        public WheelList() {
            wheels.add(new Wheel());
            wheels.add(new Wheel());
            wheels.add(new Wheel());
        }
    }

    /**
     * Nested class just for test; Simulates class without any properties (e.q. class only containing some utility
     * methods)
     */
    private static class ClassWithoutProperties {

    }

    /**
     * Nested class just for test
     */
    @SuppressWarnings({"InnerClassMayBeStatic" /*Special Test if it works with non static classes, too*/, "unused" /*Accessed fia reflection*/})
    private class A {
        List<B> bs;
        int j = -2;

        public A(B... bs) {
            this.bs = Arrays.asList(bs);
        }
    }
}
