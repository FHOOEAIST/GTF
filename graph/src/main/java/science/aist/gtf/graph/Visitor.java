/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph;

/**
 * <p>Functional Interface as Basis for the Visitor Pattern</p>
 *
 * @param <T> type of the visited element
 * @author Andreas Schuler
 * @since 1.0
 */
@FunctionalInterface
public interface Visitor<T> {
    /**
     * @param element element
     */
    void visit(T element);
}
