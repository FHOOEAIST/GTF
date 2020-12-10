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
 * <p>Implementation of a meta tag, to add additional info to other objects</p>
 *
 * @param <T> the type of the value of the tag
 * @author Andreas Schuler
 * @since 1.0
 */
public interface MetaTag<T> {
    /**
     * gets value of field key
     *
     * @return value of field key
     */
    String getKey();

    /**
     * sets value of field key
     *
     * @param key value of field key
     */
    void setKey(String key);

    /**
     * gets value of field value
     *
     * @return value of field value
     */
    T getValue();

    /**
     * sets value of field value
     *
     * @param value value of field value
     */
    void setValue(T value);
}
