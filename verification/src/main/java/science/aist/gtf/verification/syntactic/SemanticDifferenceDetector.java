/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic;


import lombok.Getter;
import lombok.Setter;
import science.aist.gtf.verification.syntactic.constraint.ConstraintError;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Class for detecting semantic differences of a verification result</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
@Getter
@Setter
public class SemanticDifferenceDetector {
    private List<Field> transformationAffectedFields = new ArrayList<>();
    private boolean listContainsAffectedFields = true;

    /**
     * Method for adding a field to the affected fields list
     *
     * @param field field to be added
     */
    public void addAffectedField(Field field) {
        transformationAffectedFields.add(field);
    }

    /**
     * Method for adding a class field by name
     *
     * @param clazz     clazz containing the field
     * @param fieldname name of the field
     */
    public void addAffectedField(Class<?> clazz, String fieldname) {
        addAffectedField(FieldExtractor.getClassField(clazz, fieldname));
    }

    /**
     * Method which filters a {@link PropertyVerificatorResult} based on the transformation affected fields
     *
     * @param input The result which should be filtered
     * @return the filtered result
     */
    public PropertyVerificatorResult detect(PropertyVerificatorResult input) {
        Map<Object, Map<Field, ConstraintError>> collect = input.getVerificationResult().entrySet().stream().filter(fieldConstraintErrorMap -> {
            if (listContainsAffectedFields) {
                return transformationAffectedFields.stream().anyMatch(transformationAffectedField -> fieldConstraintErrorMap.getValue().containsKey(transformationAffectedField));
            } else {
                return transformationAffectedFields.stream().noneMatch(transformationAffectedField -> fieldConstraintErrorMap.getValue().containsKey(transformationAffectedField));
            }
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new PropertyVerificatorResult(collect);
    }
}
