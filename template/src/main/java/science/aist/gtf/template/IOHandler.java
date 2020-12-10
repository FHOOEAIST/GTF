/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template;

/**
 * <p>Handler for IO Operations</p>
 *
 * @param <C> the current context
 * @author Andreas Schuler
 * @since 1.0
 */
public interface IOHandler<C> {
    /**
     * Copies the full directly tree
     *
     * @param context         the context in which this happens
     * @param sourceDirectory the source directory
     * @param targetDirectory the target directory
     * @param overwrite       flag whether there target directory should be overwritten if it already exists
     * @param excludes        the files to exclude
     */
    void handleDirectoryCopy(C context, String sourceDirectory, String targetDirectory, boolean overwrite, String... excludes);

    /**
     * Copies a file from source to target
     *
     * @param context    the context in which this happens
     * @param sourceFile the source file path
     * @param targetFile the target file path
     */
    void handleFileCopy(C context, String sourceFile, String targetFile);

    /**
     * Creates a directory
     *
     * @param context       the context in which this should happen
     * @param directoryPath the directory that should be created
     */
    void handleDirectoryCreate(C context, String directoryPath);

    /**
     * Unzips a file
     *
     * @param context         the context in which this should happen
     * @param sourceFile      the source file that should be unzipped
     * @param targetDirectory the target directory where to unzip the file to
     */
    void unzipFile(C context, String sourceFile, String targetDirectory);
}
