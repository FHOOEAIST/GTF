/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl.renderer;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import lombok.CustomLog;
import org.springframework.util.PropertyPlaceholderHelper;
import science.aist.gtf.template.TemplateTask;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

import java.io.File;

/**
 * <p>Uses a file with a graphviz representation and creates a PNG file out of it</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@CustomLog
public class GraphVizImageRenderer extends FileOperationRenderer {
    public GraphVizImageRenderer(RendererCondition<TemplateTask> rendererCondition, PropertyPlaceholderHelper propertyPlaceholderHelper) {
        super(rendererCondition, propertyPlaceholderHelper,
                (c, source, dest) -> Graphviz.fromFile(new File(source)).render(Format.PNG).toFile(new File(dest)));
    }
}
