/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * <p>The different template tasks</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TemplateTaskTypeEnum {

    MKDIR("createDirectory"),
    COPY("copyFile"),
    COPYDIR("copyDirectory"),
    TEMPLATE("instantiateTemplate"),
    GRAPHVIZ("graphvizimage"),
    DELETE("deleteFile"),
    UNZIP("unzip"),
    SHELL("shell"),

    // This is used to allow custom extensions in other projects.
    // type can be set to extension and be distinguished using other elements in the template task
    EXTENSION("extension");

    @Getter
    private final String templateTaskType;

    @JsonCreator
    public static TemplateTaskTypeEnum fromString(String value) {
        return Arrays.stream(values())
                .filter(templateTaskTypeEnum -> templateTaskTypeEnum.templateTaskType.equals(value))
                .findFirst()
                .orElse(null);
    }

    @Override
    @JsonValue
    public String toString() {
        return templateTaskType;
    }

}
