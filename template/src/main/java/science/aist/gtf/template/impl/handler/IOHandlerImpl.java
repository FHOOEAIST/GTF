/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl.handler;

import lombok.CustomLog;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import science.aist.gtf.template.GeneratorTemplate;
import science.aist.gtf.template.IOHandler;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * <p>Implementation of {@link IOHandler}</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
@CustomLog
public class IOHandlerImpl implements IOHandler<GeneratorTemplate> {

    @Override
    @SneakyThrows
    public void handleDirectoryCopy(GeneratorTemplate context, String src, String target, boolean overwrite, String... exclude) {
        Path fromPath = Paths.get(src + File.separator);
        Path targetPath = Paths.get(target);
        if (overwrite && targetPath.toFile().exists()) {
            FileUtils.deleteDirectory(targetPath.getParent().toFile());
        }

        Files.createDirectories(targetPath);
        try (Stream<Path> stream = Files.walk(fromPath)) {
            stream.forEach(sourcePath -> {
                try {
                    if (exclude != null) {
                        if (Arrays.stream(exclude).noneMatch(element -> element.equals(sourcePath.getFileName().toString()))) {
                            Files.copy(sourcePath, targetPath.resolve(fromPath.relativize(sourcePath)), StandardCopyOption.REPLACE_EXISTING);
                        }
                    } else {
                        Files.copy(sourcePath, targetPath.resolve(fromPath.relativize(sourcePath)), StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @SneakyThrows
    public void handleFileCopy(GeneratorTemplate context, String sourceFile, String targetFile) {
        Files.copy(Paths.get(sourceFile), Paths.get(targetFile));
    }

    @Override
    @SneakyThrows
    public void handleDirectoryCreate(GeneratorTemplate context, String directoryPath) {
        Files.createDirectories(Paths.get(directoryPath));
    }

    @Override
    @SneakyThrows
    public void unzipFile(GeneratorTemplate context, String sourceFile, String targetDirectory) {
        UnzipFile.unzip(sourceFile.startsWith("classpath:") ? new ClassPathResource(sourceFile.substring(sourceFile.indexOf(':') + 1)).getInputStream() : new FileInputStream(new File(sourceFile)), targetDirectory);
    }
}