/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import science.aist.gtf.template.GeneratorTemplate;
import science.aist.gtf.template.GeneratorTemplateFactory;
import science.aist.gtf.template.TemplateResource;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * <p>Factory Implementation for Generator Templates</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
public class GeneratorTemplateFactoryImpl implements GeneratorTemplateFactory {
    @Override
    @SneakyThrows
    public GeneratorTemplate loadGeneratorTemplate(TemplateResource templateResource) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Properties.class, new PropertiesDeserializer());
        objectMapper.registerModule(module);
        return objectMapper.readValue(templateResource.getTemplateResourceLocation(), GeneratorTemplate.class);
    }

    static class PropertiesDeserializer extends StdDeserializer<Properties> {

        public PropertiesDeserializer() {
            super((Class<?>) null);
        }

        private static <T> Iterable<T> iteratorToIterable(Iterator<T> iterator) {
            return () -> iterator;
        }

        @Override
        public Properties deserialize(JsonParser p, DeserializationContext context) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            Properties prop = new Properties();
            for (Map.Entry<String, JsonNode> stringJsonNodeEntry : iteratorToIterable(node.fields())) {
                Object value;
                if (stringJsonNodeEntry.getValue().isArray()) {
                    value = StreamSupport.stream(iteratorToIterable(stringJsonNodeEntry.getValue().iterator()).spliterator(), false)
                            .map(JsonNode::textValue)
                            .collect(Collectors.toList());
                } else if (stringJsonNodeEntry.getValue().isInt()) {
                    value = stringJsonNodeEntry.getValue().intValue();
                } else {
                    value = stringJsonNodeEntry.getValue().textValue();
                }
                if (value == null) {
                    throw new IllegalStateException("Empty values are not supported for property: " + stringJsonNodeEntry.getKey());
                }
                prop.put(stringJsonNodeEntry.getKey(), value);
            }
            return prop;
        }
    }
}
