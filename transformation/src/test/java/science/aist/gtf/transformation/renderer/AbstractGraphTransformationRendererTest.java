/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.transformation.renderer;

import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.Vertex;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

/**
 * <p>Test class for {@link AbstractGraphTransformationRenderer}</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
public class AbstractGraphTransformationRendererTest {
    @Test
    public void testPositive() {
        // given

        // when
        AbstractGraphTransformationRenderer<String, String, Void> renderer = createAbstractGraphTransformationRendererStringString(() -> v -> true);

        // then
        Assert.assertTrue(renderer.renderElement(null, null).isPresent());
    }

    @Test
    public void testNegative() {
        // given


        // when
        AbstractGraphTransformationRenderer<String, String, Void> renderer = createAbstractGraphTransformationRendererStringString(() -> v -> false);

        // then
        Assert.assertFalse(renderer.renderElement(null, null).isPresent());
    }

    @Test
    public void testCreateElement() {
        // given
        AbstractGraphTransformationRenderer<String, String, Void> renderer = createAbstractGraphTransformationRendererStringString(() -> v -> true);

        // when
        String element = renderer.createElement();

        // then
        Assert.assertNotNull(element);
    }

    @Test(expectedExceptions = NoSuchMethodException.class)
    public void testCreateElementNegative() {
        // given
        AbstractGraphTransformationRenderer<Class<?>, Class<?>, Void> renderer = new AbstractGraphTransformationRenderer<>(() -> v -> true) {
            @Override
            public Class<?> mapProperties(Class<?> aClass, Graph<Class<?>, Void> classGraph, Vertex<Class<?>, Void> currentElement) {
                return null;
            }
        };

        // when
        renderer.createElement();

        // then
        // Exception awaited.
    }

    private AbstractGraphTransformationRenderer<String, String, Void> createAbstractGraphTransformationRendererStringString(RendererCondition<Vertex<String, Void>> rendererCondition) {
        return new AbstractGraphTransformationRenderer<>(rendererCondition) {
            @Override
            public String mapProperties(String element, Graph<String, Void> container, Vertex<String, Void> currentElement) {
                return element;
            }
        };
    }
}
