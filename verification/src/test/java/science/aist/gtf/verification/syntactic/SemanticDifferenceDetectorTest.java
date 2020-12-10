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
import science.aist.gtf.verification.syntactic.domain.threed.*;
import science.aist.gtf.verification.syntactic.domain.transformation.Model2DToModel3DTransformer;
import science.aist.gtf.verification.syntactic.domain.twod.Model2D;

/**
 * <p>Test class for {@link SemanticDifferenceDetector}</p>
 *
 * @author Christoph Praschl
 */
public class SemanticDifferenceDetectorTest {

    private void addStandardFields(SemanticDifferenceDetector detector) {
        detector.addAffectedField(Cuboid.class, "length");
        detector.addAffectedField(Cuboid.class, "width");

        detector.addAffectedField(Cylinder.class, "radius");

        detector.addAffectedField(Model3D.class, "cuboid");
        detector.addAffectedField(Model3D.class, "cylinder");
        detector.addAffectedField(Model3D.class, "prism");

        detector.addAffectedField(Prism.class, "a");
        detector.addAffectedField(Prism.class, "b");
        detector.addAffectedField(Prism.class, "c");
    }

    /**
     * Tests the SemanticDifferenceDetector with a list of fields which should be affected by transformation
     */
    @Test
    public void testDetect() {
        // given
        final double height = 5;
        PropertyVerificator<Model3D> propertyVerificator = new PropertyVerificator<>();
        DefaultInitializedConstraint constraint = new DefaultInitializedConstraint();
        constraint.addUserdefinedDefaultValue(Object3D.class, "height", height);
        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE + 1, constraint);

        Model2D model = Model2D.createSample();
        Model2DToModel3DTransformer transformer = new Model2DToModel3DTransformer();
        transformer.setHeight(height);
        Model3D result = transformer.applyTransformation(model);
        PropertyVerificatorResult invalidProperties = propertyVerificator.getInvalidProperties(result);

        SemanticDifferenceDetector detector = new SemanticDifferenceDetector();
        addStandardFields(detector);

        // when
        detector.setListContainsAffectedFields(false);
        PropertyVerificatorResult detect = detector.detect(invalidProperties);

        // then
        Assert.assertEquals(detect.createStatistic().getInvalidProperties(), 3);
    }

    /**
     * Tests the SemanticDifferenceDetector with a list of fields which should NOT be affected by transformation if
     * there are invalid properties with such a list --&gt; semantic differences
     */
    @Test
    public void testDetect2() {
        // given
        final double height = 5;
        PropertyVerificator<Model3D> propertyVerificator = new PropertyVerificator<>();
        DefaultInitializedConstraint constraint = new DefaultInitializedConstraint();
        constraint.addUserdefinedDefaultValue(Object3D.class, "height", height);
        propertyVerificator.getRestrictor().addGeneralConstraint(Integer.MIN_VALUE + 1, constraint);

        Model2D model = Model2D.createSample();
        Model2DToModel3DTransformer transformer = new Model2DToModel3DTransformer();
        transformer.setHeight(height);
        Model3D result = transformer.applyTransformation(model);
        PropertyVerificatorResult invalidProperties = propertyVerificator.getInvalidProperties(result);

        SemanticDifferenceDetector detector = new SemanticDifferenceDetector();
        addStandardFields(detector);

        // when
        detector.setListContainsAffectedFields(true);
        detector.addAffectedField(Object3D.class, "height");
        PropertyVerificatorResult detect = detector.detect(invalidProperties);

        // then
        Assert.assertEquals(detect.createStatistic().getInvalidProperties(), 3);
    }

}
