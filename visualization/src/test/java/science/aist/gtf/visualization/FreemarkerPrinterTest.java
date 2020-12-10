/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.visualization;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.StringWriter;
import java.util.Map;

/**
 * <p>Test class for {@link FreemarkerPrinter}</p>
 *
 * @author Andreas Pointner
 */
public class FreemarkerPrinterTest {

    @Test
    public void testTransform() {
        // given
        StringWriter sw = new StringWriter();
        FreemarkerPrinter<?> fp = new FreemarkerPrinter<>(null);
        fp.setWriter(sw);
        fp.addTemplate("echoTemplate", "echo");

        // when
        fp.transform("echoTemplate", Map.of("data", "mySampleData"));

        // then
        String s = sw.toString();
        Assert.assertEquals(s, "mySampleData");
    }
}