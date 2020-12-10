/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.domain.transformation;

import science.aist.gtf.transformation.Transformer;
import science.aist.gtf.verification.syntactic.domain.threed.Cuboid;
import science.aist.gtf.verification.syntactic.domain.threed.Cylinder;
import science.aist.gtf.verification.syntactic.domain.threed.Model3D;
import science.aist.gtf.verification.syntactic.domain.threed.Prism;
import science.aist.gtf.verification.syntactic.domain.twod.Model2D;

/**
 * <p>Test transformer implementation</p>
 *
 * @author Christoph Praschl
 */
public class Model2DToModel3DTransformer implements Transformer<Model2D, Model3D> {
    private double height = 5;

    @SuppressWarnings("unused" /*This is accessed fia reflection*/)
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public Model3D applyTransformation(Model2D model2D) {
        Model3D result = new Model3D();
        result.prism = new Prism(model2D.triangle.a, model2D.triangle.b, model2D.triangle.c, height);
        result.cylinder = new Cylinder(model2D.circle.radius, height);
        result.cuboid = new Cuboid(model2D.rectangle.width, model2D.rectangle.length, height); // swapped width and length
        return result;
    }
}
