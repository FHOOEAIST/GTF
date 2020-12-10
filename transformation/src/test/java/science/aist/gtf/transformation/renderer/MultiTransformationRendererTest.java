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

import java.util.Arrays;

/**
 * <p>Tests {@link MultiTransformationRenderer}</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
public class MultiTransformationRendererTest {
    @Test
    public void testSuccessWithTwoRenderers() {
        // given
        AbstractConditionalTransformationRenderer<String, String, String> renderer = new AbstractConditionalTransformationRenderer<>(() -> (x) -> true) {
            @Override
            public String createElement() {
                return "Test1";
            }

            @Override
            public String mapProperties(String s, String s2, String currentElement) {
                return s.concat(currentElement);
            }
        };
        AbstractConditionalTransformationRenderer<String, String, String> renderer2 = new AbstractConditionalTransformationRenderer<>(() -> (x) -> true) {
            @Override
            public String createElement() {
                return "Test2";
            }

            @Override
            public String mapProperties(String s, String s2, String currentElement) {
                return s.concat(currentElement);
            }
        };

        MultiTransformationRenderer<String, String, String> multiTransformationRenderer = new MultiTransformationRenderer<>(Arrays.asList(renderer, renderer2));

        // when
        String abc = multiTransformationRenderer.renderElement("", "ABC");

        // then
        Assert.assertEquals(abc, "Test1ABC");
    }

    @Test
    public void testSuccessWithTwoRenderers2() {
        // given
        AbstractConditionalTransformationRenderer<String, String, String> renderer = new AbstractConditionalTransformationRenderer<>(() -> (x) -> false) {
            @Override
            public String createElement() {
                return "Test1";
            }

            @Override
            public String mapProperties(String s, String s2, String currentElement) {
                return s.concat(currentElement);
            }
        };
        AbstractConditionalTransformationRenderer<String, String, String> renderer2 = new AbstractConditionalTransformationRenderer<>(() -> (x) -> true) {
            @Override
            public String createElement() {
                return "Test2";
            }

            @Override
            public String mapProperties(String s, String s2, String currentElement) {
                return s.concat(currentElement);
            }
        };

        MultiTransformationRenderer<String, String, String> multiTransformationRenderer = new MultiTransformationRenderer<>(Arrays.asList(renderer, renderer2));

        // when
        String abc = multiTransformationRenderer.renderElement("", "ABC");

        // then
        Assert.assertEquals(abc, "Test2ABC");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testFailure() {
        // given
        AbstractConditionalTransformationRenderer<String, String, String> renderer = new AbstractConditionalTransformationRenderer<>(() -> (x) -> false) {
            @Override
            public String createElement() {
                return "Test1";
            }

            @Override
            public String mapProperties(String s, String s2, String currentElement) {
                return s.concat(currentElement);
            }
        };
        AbstractConditionalTransformationRenderer<String, String, String> renderer2 = new AbstractConditionalTransformationRenderer<>(() -> (x) -> false) {
            @Override
            public String createElement() {
                return "Test2";
            }

            @Override
            public String mapProperties(String s, String s2, String currentElement) {
                return s.concat(currentElement);
            }
        };

        MultiTransformationRenderer<String, String, String> multiTransformationRenderer = new MultiTransformationRenderer<>(Arrays.asList(renderer, renderer2));

        // when
        multiTransformationRenderer.renderElement("", "ABC");

        // then
        // Exception
    }
}
