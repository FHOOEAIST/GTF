/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template;

import java.io.IOException;

/**
 * <p>Baseclass for File Operations</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@FunctionalInterface
public interface FilePathOperation<C> {
    /**
     * Performs an operation on a FilePath
     *
     * @param context    the context in which the operation if performed
     * @param sourcePath the source path of the file on that the operation should be executed
     * @param targetPath the target path where the resulting file should be stored
     * @throws IOException if an error during the file operations occurs
     */
    void perform(C context, String sourcePath, String targetPath) throws IOException;
}
