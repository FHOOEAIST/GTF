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
public class Model3D {
    public Cuboid cuboid;
    public Cylinder cylinder;
    public Prism prism;

    @Override
    public String toString() {
        return "Model3D{" +
                "cuboid=" + cuboid +
                ", cylinder=" + cylinder +
                ", prism=" + prism +
                '}';
    }
}
