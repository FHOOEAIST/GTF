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
public class Prism extends Object3D {
    public double a;
    public double b;
    public double c;

    public Prism(double a, double b, double c, double h) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.height = h;
    }

    @Override
    public String toString() {
        return "Prism{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", height=" + height +
                '}';
    }
}