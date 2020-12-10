/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import science.aist.gtf.graph.MetaTag;

/**
 * <p>Implementation of a meta tag, to add additional info to other objects</p>
 *
 * @param <T> the type of the value of the tag
 * @author Andreas Schuler
 * @since 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MetaTagImpl<T> implements MetaTag<T> {
    /**
     * Key of the Meta Tag
     */
    private String key;
    /**
     * Value of the Meta Tag
     */
    private T value;
}
