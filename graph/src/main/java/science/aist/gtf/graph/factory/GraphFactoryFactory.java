/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <p>The factory for graph factories</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GraphFactoryFactory {
    /**
     * Returns an instance of a default factory
     *
     * @return a instance of a graph factory
     */
    public static GraphFactory getDefaultFactory() {
        return new DefaultGraphFactory();
    }
}
