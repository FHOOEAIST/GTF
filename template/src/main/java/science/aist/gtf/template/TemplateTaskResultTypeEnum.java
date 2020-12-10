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

import java.util.Arrays;

/**
 * <p>The different available results of a template task</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TemplateTaskResultTypeEnum {

    FAILED("failed"),
    SUCCESS("success");

    private final String templateTaskResultType;

    @JsonCreator
    public static TemplateTaskResultTypeEnum fromString(String value) {
        return Arrays.stream(values())
                .filter(type -> type.templateTaskResultType.equals(value))
                .findFirst()
                .orElse(null);
    }

    public static TemplateTaskResultTypeEnum fromBoolean(boolean result) {
        return result ? SUCCESS : FAILED;
    }

    @Override
    @JsonValue
    public String toString() {
        return templateTaskResultType;
    }

}