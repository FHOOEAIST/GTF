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
import science.aist.jack.data.Pair;

import java.io.StringWriter;
import java.util.List;

/**
 * <p>Test class for {@link ExceptionPrinter}</p>
 *
 * @author Andreas Pointner
 */

public class ExceptionPrinterTest {
    @Test
    public void testPrint() {
        // given
        StringWriter sw = new StringWriter();
        ExceptionPrinter ep = new ExceptionPrinter(null);
        ep.setWriter(sw);

        // when
        ep.print(List.of(Pair.of("key1", new Exception("2")), Pair.of("key2", new Exception("3"))));

        // then
        String s = sw.toString();
        Assert.assertNotNull(s);
        Assert.assertTrue(s.length() > 0);
    }
}