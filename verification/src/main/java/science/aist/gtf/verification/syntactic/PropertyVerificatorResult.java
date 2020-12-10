/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import science.aist.gtf.verification.syntactic.constraint.ConstraintError;
import science.aist.gtf.verification.syntactic.constraint.ConstraintViolationStatistic;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Result class of a property verification process</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PropertyVerificatorResult {
    private Map<Object, Map<Field, ConstraintError>> verificationResult = new HashMap<>();

    /**
     * Merge two nested-maps into a new one
     *
     * @param res1 to merge
     * @param res2 to merge
     * @return New, merged verificationResult
     */
    public static PropertyVerificatorResult mergeVerificationResults(PropertyVerificatorResult res1, PropertyVerificatorResult res2) {
        Map<Object, Map<Field, ConstraintError>> map1 = res1.getVerificationResult();
        Map<Object, Map<Field, ConstraintError>> map2 = res2.getVerificationResult();
        Map<Object, Map<Field, ConstraintError>> result = new HashMap<>(map1);

        for (Map.Entry<Object, Map<Field, ConstraintError>> entries : map2.entrySet()) {
            if (map1.containsKey(entries.getKey())) {
                Map<Field, ConstraintError> innerMap = entries.getValue();
                for (Map.Entry<Field, ConstraintError> innerEntries : innerMap.entrySet()) {
                    if (!result.get(entries.getKey()).containsKey(innerEntries.getKey())) {
                        result.get(entries.getKey()).put(innerEntries.getKey(), innerEntries.getValue());
                    }
                }
            } else {
                result.put(entries.getKey(), entries.getValue());
            }
        }

        PropertyVerificatorResult verificatorResult = new PropertyVerificatorResult();
        verificatorResult.verificationResult = result;

        return verificatorResult;
    }

    /**
     * @return If Result is empty or not
     */
    public boolean isEmpty() {
        return verificationResult.isEmpty();
    }

    /**
     * @return new Statistic object
     */
    public ConstraintViolationStatistic createStatistic() {
        return new ConstraintViolationStatistic(this);
    }

    /**
     * Add a constraintError violating field
     *
     * @param field           field which violating the constraintError
     * @param constraintError the violated constraintError
     * @param obj             the object containing the field which is violating the constraintError
     */
    public void addField(Field field, ConstraintError constraintError, Object obj) {
        if (field == null || obj == null) return;

        Map<Field, ConstraintError> list;
        if (verificationResult.containsKey(obj)) {
            list = verificationResult.get(obj);
        } else {
            list = new HashMap<>();
            verificationResult.put(obj, list);
        }
        list.put(field, constraintError);
    }

    /**
     * Method which returns a map of objects with its fields which are violating a given ConstraintError
     *
     * @param error the violated error which is the basis for filtering
     * @return a map of objects with fields violating the given ConstraintError
     */
    public Map<Object, List<Field>> getFieldsByConstraint(ConstraintError error) {
        Map<Object, List<Field>> result = new HashMap<>();

        for (Map.Entry<Object, Map<Field, ConstraintError>> objectMapEntry : verificationResult.entrySet()) {
            List<Field> collect = objectMapEntry.getValue().entrySet()
                    .stream()
                    .filter(fieldConstraintErrorEntry -> fieldConstraintErrorEntry.getValue().equals(error))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            if (!collect.isEmpty()) {
                result.put(objectMapEntry.getKey(), Collections.unmodifiableList(collect));
            }
        }

        return Collections.unmodifiableMap(result);
    }
}
