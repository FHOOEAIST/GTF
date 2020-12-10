/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.domain.twod;

/**
 * <p>Test domain class</p>
 *
 * @author Christoph Praschl
 */
public class Model2D {
    public Rectangle rectangle;
    public Circle circle;
    public Triangle triangle;

    public static Model2D createSample() {
        Model2D model = new Model2D();
        model.rectangle = new Rectangle(1, 2);
        model.circle = new Circle(5);
        model.triangle = new Triangle(3, 4, 5);
        return model;
    }
}