/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl.handler;

import org.springframework.core.io.ClassPathResource;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Test class for {@link UnzipFile}</p>
 *
 * @author Andreas Pointner
 */

public class UnzipFileTest {

    @Test
    public void testUnzip() throws IOException {
        // given
        InputStream inputStream = new ClassPathResource("test.zip").getInputStream();
        String dest = System.getProperty("user.dir") + "/target/test/unzip/";

        // when
        UnzipFile.unzip(inputStream, dest);

        // then
        Assert.assertTrue(new File(dest + "test.txt").exists());
        Assert.assertTrue(new File(dest + "test/").exists());
        Assert.assertTrue(new File(dest + "test/test.txt").exists());
    }
}