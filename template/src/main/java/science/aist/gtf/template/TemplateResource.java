/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;

import java.net.URL;

/**
 * <p>A Template Resource</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
@Getter
@Setter
public class TemplateResource {
    /**
     * The location of the url
     */
    private URL templateResourceLocation;

    /**
     * Initializes the location of the resource
     *
     * @param templateResourceLocation the location of the resource
     */
    public TemplateResource(String templateResourceLocation) {
        this.templateResourceLocation = getClass().getResource(templateResourceLocation);
    }

    /**
     * Initializes the location of the resource
     * <pre>
     *     // Example
     *     new TemplateResource(new ClassPathResource("xyz"));
     * </pre>
     *
     * @param resource the resource
     */
    @SneakyThrows
    public TemplateResource(Resource resource) {
        this.templateResourceLocation = resource.getURL();
    }
}
