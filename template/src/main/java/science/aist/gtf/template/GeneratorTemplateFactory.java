/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Factory to create {@link GeneratorTemplate}s</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
public interface GeneratorTemplateFactory {
    /**
     * Loads the generator template for a given template resource
     *
     * @param templateResource the template resource
     * @return the loaded generator template
     */
    GeneratorTemplate loadGeneratorTemplate(TemplateResource templateResource);

    /**
     * Loads all available generator template
     *
     * @return a list of all available generator templates
     */
    default List<GeneratorTemplate> loadAllGeneratorTemplates() {
        return listAvailableTemplateResources().stream()
                .map(this::loadGeneratorTemplate)
                .collect(Collectors.toList());
    }

    /**
     * Lists all available generator templates
     *
     * @return a template resource list with all available templates
     */
    default List<TemplateResource> listAvailableTemplateResources() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

}
