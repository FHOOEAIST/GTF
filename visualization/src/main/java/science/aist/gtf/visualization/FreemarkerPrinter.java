/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.visualization;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Getter;
import lombok.Setter;
import science.aist.gtf.template.TemplateEngine;
import science.aist.jack.general.util.CastUtils;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Printer that is based on freemarker. It will persist according to the given template files</p>
 *
 * @param <T> The type of the input context
 * @author Oliver Krauss
 * @author Andreas Pointner
 * @since 1.0
 */
public class FreemarkerPrinter<T> implements TemplateEngine<T> {

    /**
     * Map of all templates loaded into this printer. Per convention the name of the template is also the variable-name
     * of what the object to be transformed will be loaded into.
     */
    @Getter
    private final Map<String, String> templateMap = new HashMap<>();
    /**
     * Additional values to be set manually that the template may need. This also allows overriding any values that the
     * templatePreprocessor may generate.
     */
    @Getter
    private final Map<String, Object> additionalTemplateValues = new HashMap<>();
    /**
     * preprocessors for the template
     */
    private final List<TemplatePreprocessor<?>> templatePreprocessors = new ArrayList<>();
    protected Configuration configuration;
    /**
     * If this printer supports more than one output this list provides the supported ones
     */
    @Getter
    @Setter
    private List<String> supportedFormats = new ArrayList<>();
    /**
     * Selected format out of the supported ones
     */
    @Getter
    private String format = "";
    /**
     * Writer for writing to "something"
     */
    @Getter
    @Setter
    private Writer writer;

    /**
     * Creates the default configuration and set the template loading
     *
     * @param templateDirectory if null then this class will be used for template loading, otherwise the directory which
     *                          was set is used
     */
    public FreemarkerPrinter(String templateDirectory) {
        // default config of Freemarker
        configuration = new Configuration(Configuration.VERSION_2_3_29);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);

        // configure loading of testFiles
        if (templateDirectory == null) {
            configuration.setClassForTemplateLoading(getClass(), "/templates/");
        } else {
            try {
                configuration.setDirectoryForTemplateLoading(new File(templateDirectory));
            } catch (IOException e) {
                throw new IllegalArgumentException("Template directory " + templateDirectory + " does not exist!", e);
            }
        }
    }

    public void transform(String template, Object o) {
        if (!templateMap.containsKey(template)) {
            throw new IllegalStateException("template unknown. Cannot transform");
        }

        Map<String, Object> data = new HashMap<>();
        data.put(template, o);
        applyTemplate(templateMap.get(template), data);
    }


    /**
     * Helper function to actually apply the template
     *
     * @param templateName to be applied
     * @param data         to be printed into the template
     */
    protected void applyTemplate(String templateName, Map<String, Object> data) {
        if (writer == null) {
            // without anywhere to push we don't want to do anything
            return;
        }
        try {
            // Apply preprocessors
            data.values().forEach(dataElement -> templatePreprocessors
                    .stream()
                    .filter(tp -> tp.canPreprocess(dataElement))
                    .map(tp -> tp.process(CastUtils.cast(dataElement)))
                    .forEach(data::putAll)
            );
            data.putAll(additionalTemplateValues);
            Template template = configuration.getTemplate((format.length() > 0 ? format + "/" : "") + templateName + ".ftlh");
            template.process(data, writer);
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }

    public void addTemplate(String name, String templateFile) {
        templateMap.put(name, templateFile);
    }

    /**
     * Adds a value that has to be used by the template (does override if exists)
     *
     * @param key   to be used
     * @param value to be applied to the template
     */
    @SuppressWarnings("unused" /*This may be used from external scopes*/)
    public void addAdditionalTemplateValue(String key, Object value) {
        additionalTemplateValues.put(key, value);
    }

    /**
     * Removes a value from the template
     *
     * @param key to be removed
     */
    @SuppressWarnings("unused" /*This may be used from external scopes*/)
    public void removeAdditionalTemplateValue(String key) {
        additionalTemplateValues.remove(key);
    }

    @SuppressWarnings({"java:S2250" /*Sonar complains over the contains call*/, "unused" /*This may be used from external scopes*/})
    public void setFormat(String format) {
        if (format == null) {
            format = "";
        }
        if (!supportedFormats.contains(format)) {
            throw new IllegalArgumentException("Format " + format + " not supported.");
        }
        this.format = format;
    }

    @SuppressWarnings("unused" /*This may be used from external scopes*/)
    public <S> void addTemplatePreprocessor(TemplatePreprocessor<S> templatePreprocessor) {
        templatePreprocessors.add(templatePreprocessor);
    }

    @SuppressWarnings({"java:S2250" /*Sonar complains over the remove call*/, "unused" /*This may be used from external scopes*/})
    public <S> void removeTemplatePreprocessor(TemplatePreprocessor<S> templatePreprocessor) {
        templatePreprocessors.remove(templatePreprocessor);
    }

    @Override
    public void process(T context, String templateFile, Writer destinationWriter) {
        setWriter(destinationWriter);
        transform(templateFile, context);
    }
}
