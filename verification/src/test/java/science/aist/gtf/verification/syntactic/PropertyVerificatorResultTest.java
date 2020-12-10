/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic;

import lombok.CustomLog;
import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.verification.syntactic.constraint.ConstraintError;
import science.aist.gtf.verification.syntactic.constraint.impl.DefaultInitializedConstraint;
import science.aist.gtf.verification.syntactic.domain.threed.Cuboid;
import science.aist.gtf.verification.syntactic.domain.threed.Model3D;
import science.aist.gtf.verification.syntactic.domain.transformation.Model2DToModel3DTransformer;
import science.aist.gtf.verification.syntactic.domain.twod.Model2D;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * <p>Test class for {@link PropertyVerificatorResult}</p>
 *
 * @author Christoph Praschl
 */
@CustomLog
public class PropertyVerificatorResultTest {
    @Test
    public void testGetFieldsByConstraint() {
        // given
        final double height = 5;
        PropertyVerificator<Model3D> propertyVerificator = new PropertyVerificator<>();
        PropertyRestrictor restrictor = new PropertyRestrictor(true, false, true);
        DefaultInitializedConstraint constraint = new DefaultInitializedConstraint();
        constraint.addUserdefinedDefaultValue(Cuboid.class, "height", height);
        restrictor.addGeneralConstraint(Integer.MIN_VALUE + 1, constraint);
        propertyVerificator.setRestrictor(restrictor);

        Model2D model = Model2D.createSample();
        Model2DToModel3DTransformer transformer = new Model2DToModel3DTransformer();
        transformer.setHeight(5);
        Model3D result = transformer.applyTransformation(model);
        log.info(result.toString());
        PropertyVerificatorResult invalidProperties = propertyVerificator.getInvalidProperties(result);

        // when
        Map<Object, List<Field>> fieldsByConstraint = invalidProperties.getFieldsByConstraint(ConstraintError.IsDefaultInitialized);

        // then
        Assert.assertEquals(fieldsByConstraint.size(), 3);
        fieldsByConstraint.forEach((o, fields) -> Assert.assertEquals(fields.size(), 1));
    }

    @Test
    public void testGetFieldsByConstraint2() {
        // given
        final double height = 5;
        PropertyVerificator<Model3D> propertyVerificator = new PropertyVerificator<>();
        PropertyRestrictor restrictor = new PropertyRestrictor(true, false, true);
        DefaultInitializedConstraint constraint = new DefaultInitializedConstraint();
        constraint.addUserdefinedDefaultValue(Cuboid.class, "height", height);
        restrictor.addGeneralConstraint(Integer.MIN_VALUE + 1, constraint);
        propertyVerificator.setRestrictor(restrictor);

        Model2D model = Model2D.createSample();
        Model2DToModel3DTransformer transformer = new Model2DToModel3DTransformer();
        transformer.setHeight(height);
        Model3D result = transformer.applyTransformation(model);
        PropertyVerificatorResult invalidProperties = propertyVerificator.getInvalidProperties(result);


        // when
        Map<Object, List<Field>> fieldsByConstraint = invalidProperties.getFieldsByConstraint(ConstraintError.IsNull);

        // then
        Assert.assertTrue(fieldsByConstraint.isEmpty());
    }
}
