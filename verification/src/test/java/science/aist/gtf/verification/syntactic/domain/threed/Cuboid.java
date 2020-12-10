/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.domain.threed;

/**
 * <p>Test domain class</p>
 *
 * @author Christoph Praschl
 */
public class Cuboid extends Object3D {
    public double length;
    public double width;

    public Cuboid(double length, double width, double h) {
        this.length = length;
        this.width = width;
        height = h;
    }

    @Override
    public String toString() {
        return "Cuboid{" +
                "width=" + width +
                ", length=" + length +
                ", height=" + height +
                '}';
    }
}