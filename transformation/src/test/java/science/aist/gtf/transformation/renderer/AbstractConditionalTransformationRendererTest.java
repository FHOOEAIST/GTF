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

import java.util.Optional;

/**
 * <p>Tests {@link AbstractConditionalTransformationRenderer}</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
public class AbstractConditionalTransformationRendererTest {
    @Test
    public void testTrue() {
        // given
        AbstractConditionalTransformationRenderer<String, String, String> renderer = new AbstractConditionalTransformationRenderer<>(() -> (x) -> true) {
            @Override
            public String createElement() {
                return "Test";
            }

            @Override
            public String mapProperties(String s, String s2, String currentElement) {
                return s.concat(currentElement);
            }
        };

        // when
        Optional<String> result = renderer.renderElement("", "ABC");

        // then
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), "TestABC");
    }

    @Test
    public void testFalse() {
        // given
        AbstractConditionalTransformationRenderer<String, String, String> renderer = new AbstractConditionalTransformationRenderer<>(() -> (x) -> false) {
            @Override
            public String createElement() {
                return "Test";
            }

            @Override
            public String mapProperties(String s, String s2, String currentElement) {
                return s.concat(currentElement);
            }
        };

        // when
        Optional<String> result = renderer.renderElement("", "ABC");

        // then
        Assert.assertFalse(result.isPresent());
    }
}
