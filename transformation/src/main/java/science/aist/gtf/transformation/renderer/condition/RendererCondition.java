/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.transformation.renderer.condition;

import java.util.function.Predicate;

/**
 * <p>Interface to create a renderer condition</p>
 *
 * @param <T> input type of the predicate
 * @author Andreas Schuler
 * @since 1.0
 */
public interface RendererCondition<T> {
    /**
     * Creates a condition for a specific type
     *
     * @return the condition
     */
    Predicate<T> createCondition();
}
