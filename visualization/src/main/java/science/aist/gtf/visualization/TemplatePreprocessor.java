/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.visualization;

import java.util.Map;

/**
 * <p>Preprocessor of Template Arguments</p>
 *
 * @param <T> The type of the element that should be processed
 * @author Andreas Pointner
 * @since 1.0
 */
public interface TemplatePreprocessor<T> {
    /**
     * Inputs any type of object, that should be processed. This method must check if the object is of type T, as well
     * as if this object should be processed by the TemplatePreprocessor.
     *
     * @param toProcess the object to be checked
     * @return whether if the processor should be called for a given object or not.
     */
    boolean canPreprocess(Object toProcess);

    /**
     * Processes the object and extracts additional properties
     *
     * @param toProcess the object to be processed
     * @return a map with additional template properties
     */
    Map<String, Object> process(T toProcess);
}
