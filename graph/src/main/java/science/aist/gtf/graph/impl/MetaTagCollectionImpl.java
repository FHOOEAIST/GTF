/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import science.aist.gtf.graph.MetaTag;
import science.aist.gtf.graph.MetaTagCollection;
import science.aist.jack.general.util.CastUtils;

import java.util.Collection;
import java.util.HashSet;

/**
 * <p>Handles MetaTags</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
public class MetaTagCollectionImpl implements MetaTagCollection {
    /**
     * meta-information about a specific vertex
     */
    private final Collection<MetaTag<?>> metaTags = new HashSet<>();

    @Override
    public <T> Collection<MetaTag<T>> getMetaTags() {
        return CastUtils.cast(metaTags);
    }

    @Override
    public <T> boolean addMetaTag(MetaTag<T> metaTag) {
        if (metaTag == null)
            return false;
        return this.metaTags.add(metaTag);
    }

    @Override
    public <T> boolean removeMetaTag(MetaTag<T> metaTag) {
        if (metaTag == null)
            return false;
        return this.metaTags.remove(metaTag);
    }
}
