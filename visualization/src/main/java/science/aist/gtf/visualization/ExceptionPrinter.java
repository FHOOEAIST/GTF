/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.visualization;

import science.aist.jack.data.Pair;

import java.util.Collection;

/**
 * <p>The Exception printer generates a report of exceptions provided to it.</p>
 * <p>It automatically groups exceptions according to:</p>
 * <ul>
 *     <li>a KEY provided by a caller</li>
 *     <li>the LOCATION the exception occurred</li>
 *     <li>the EXCEPTION that occurred</li>
 * </ul>
 *
 * @author Oliver Krauss
 * @author Andreas Pointner
 * @since 1.0
 */
public class ExceptionPrinter extends FreemarkerPrinter<Collection<Pair<String, Exception>>> {

    /**
     * Identifier of exception template
     */
    private static final String EXCEPTION_TEMPLATE = "exception";

    public ExceptionPrinter(String templateDirectory) {
        super(templateDirectory);
        this.addTemplate(EXCEPTION_TEMPLATE, EXCEPTION_TEMPLATE);
    }

    /**
     * Prints the exceptions into the template
     *
     * @param exceptions to be printed &lt;Key, Exception&gt;
     */
    public void print(Collection<Pair<String, Exception>> exceptions) {
        super.transform(EXCEPTION_TEMPLATE, exceptions);
    }

    @SuppressWarnings("unused" /*This may be used from external scopes*/)
    public String getExceptionTemplate() {
        return this.getTemplateMap().get(EXCEPTION_TEMPLATE);
    }

    @SuppressWarnings("unused" /*This may be used from external scopes*/)
    public void setExceptionTemplate(String template) {
        this.addTemplate(EXCEPTION_TEMPLATE, template);
    }
}
