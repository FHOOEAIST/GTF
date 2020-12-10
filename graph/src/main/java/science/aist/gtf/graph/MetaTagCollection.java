/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph;

import science.aist.jack.general.util.CastUtils;

import java.util.*;

/**
 * <p>Allows adding MetaTag information to a specific class</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
public interface MetaTagCollection {
    /**
     * @param <T> returns a collection of all the meta tags
     * @return the collection of all current meta tags
     */
    <T> Collection<MetaTag<T>> getMetaTags();

    /**
     * Returns the value for a meta tag with a given key
     *
     * @param key   the key of the meta tag
     * @param clazz the class of the meta tag value
     * @param <T>   the type of the meta tag value
     * @return the value of the meta tag, if there are multiple elements with the same key and class type, the first one
     * which is matching will be returned
     * @throws IllegalStateException if no meta tag with the given key exists, or if the meta tag value does not match
     *                               the clazz type.
     */
    default <T> T getMetaTagValue(String key, Class<T> clazz) {
        return tryGetMetaTagValue(key, clazz)
                .orElseThrow(() -> new IllegalStateException("Did not find any element with given key and given class type"));
    }

    /**
     * Tries to get a value for a given meta tag with a given key
     *
     * @param key   the key of the meta tag
     * @param clazz the class of the meta tag value
     * @param <T>   the type of the meta tag value
     * @return the value of the meta tag, if there are multiple elements with the same key class type, the first one
     * which is matching will be returned
     */
    default <T> Optional<T> tryGetMetaTagValue(String key, Class<T> clazz) {
        return getMetaTags().stream()
                .filter(Objects::nonNull)
                .filter(mt -> mt.getKey().equals(key))
                .map(MetaTag::getValue)
                .filter(Objects::nonNull)
                .filter(val -> clazz.isAssignableFrom(val.getClass()))
                .map(clazz::cast)
                .findFirst();
    }

    /**
     * Returns the first value for a meta tag with a given key or throws an exception if not present
     *
     * @param key the key
     * @param <T> the type of the meta tag
     * @return the value if present
     * @see MetaTagCollection#getMetaTagValue(String, Class)
     */
    default <T> T getMetaTagValue(String key) {
        return CastUtils.cast(getMetaTagValue(key, Object.class));
    }

    /**
     * Try to return the first value for a meta tag with a given key
     *
     * @param key the key
     * @param <T> the type of the meta tag
     * @return the value if present
     * @see MetaTagCollection#tryGetMetaTagValue(String, Class)
     */
    default <T> Optional<T> tryGetMetaTagValue(String key) {
        return CastUtils.cast(tryGetMetaTagValue(key, Object.class));
    }

    /**
     * Adds a meta tag to the current vertex
     *
     * @param <T>     the type of the meta tag to add
     * @param metaTag the meta tag
     * @return a flag which indicates if the element was added
     */
    <T> boolean addMetaTag(MetaTag<T> metaTag);

    /**
     * Removes a meta tag from the current vertex
     *
     * @param <T>     the type of the meta tag to remove
     * @param metaTag the meta tag
     * @return a flag which indicates if the element was removed
     */
    <T> boolean removeMetaTag(MetaTag<T> metaTag);

    /**
     * <p>Removes all meta that, where the key of the meta tag equals the key parameter. Optionally it also checks if
     * the value type is one of the given classes. If classes is null or empty it will only check for the key. After
     * calling this method the meta tag with the associated key will be removed. In case the key does not exist, no
     * error will occur.</p>
     *
     * @param key   the key of the meta tag to be removed
     * @param clazz optional: a list of possible types where the meta tag must be one of them.
     */
    default void removeMetaTag(String key, Class<?>... clazz) {
        List<Class<?>> filterClasses = clazz != null && clazz.length > 0 ? Arrays.asList(clazz) : Collections.emptyList();
        new ArrayList<>(getMetaTags()).stream()
                .filter(mt -> mt.getKey().equals(key))
                .filter(mt -> filterClasses.isEmpty() || filterClasses.contains(mt.getValue().getClass()))
                .forEach(this::removeMetaTag);
    }
}
