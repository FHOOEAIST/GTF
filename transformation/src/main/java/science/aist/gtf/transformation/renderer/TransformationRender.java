/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.transformation.renderer;

/**
 * <p>Interface to represent a renderer inside a transformation. A renderer is responsible for processing a single
 * element or a group of elements inside a transformation and is considered as the smallest part inside a complete
 * transformation process.</p>
 *
 * @param <Container> type of the container element
 * @param <Input>     type of the current element to process
 * @param <Element>   type of the interim element that used to map the properties
 * @param <Result>    resulting type of the rendering
 * @author Andreas Schuler
 * @since 1.0
 */
public interface TransformationRender<Result, Element, Container, Input> {
    /**
     * Renders a given element
     *
     * @param container      the container
     * @param currentElement the current element
     * @return the rendered element
     */
    Result renderElement(Container container, Input currentElement);

    /**
     * Creates a new element same as the one which is rendered
     *
     * @return the new element
     */
    Element createElement();

    /**
     * maps properties
     *
     * @param element        the element, same as which is rendered
     * @param container      the container
     * @param currentElement the current element
     * @return the rendered element
     */
    Element mapProperties(Element element, Container container, Input currentElement);
}
