/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl.handler;

import lombok.Cleanup;
import lombok.CustomLog;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <p>Unzips an input stream of a zip file</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@UtilityClass
@CustomLog
public class UnzipFile {
    @SneakyThrows
    @SuppressWarnings("java:S2095" /*complains about not properly closing the FileOutputStream, but this is done using lombok cleanup*/)
    public void unzip(InputStream fileZip, String destDir) {
        byte[] buffer = new byte[1024];
        @Cleanup ZipInputStream zis = new ZipInputStream(fileZip);
        for (ZipEntry zipEntry = zis.getNextEntry(); zipEntry != null; zipEntry = zis.getNextEntry()) {
            File newFile = new File(destDir, zipEntry.getName());
            if (!zipEntry.isDirectory() && newFile.getParentFile().mkdirs()) {
                log.debug("Directory {} created.", newFile.getParentFile().getAbsolutePath());
            } else if (zipEntry.isDirectory() && newFile.mkdirs()) {
                log.debug("Directory {} created.", newFile.getAbsolutePath());
            }
            if (!zipEntry.isDirectory()) {
                @Cleanup FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
        }
        zis.closeEntry();
        zis.close();
    }
}
