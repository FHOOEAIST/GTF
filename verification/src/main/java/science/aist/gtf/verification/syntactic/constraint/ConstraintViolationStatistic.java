/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.constraint;

import lombok.Getter;
import lombok.ToString;
import science.aist.gtf.verification.syntactic.PropertyVerificatorResult;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>Statistic class representing number of different violations of a property verification</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
@ToString
public class ConstraintViolationStatistic {
    @Getter
    private final int invalidObjects;
    @ToString.Exclude
    private final Map<ConstraintError, Map<Object, List<Field>>> constraintErrors = new HashMap<>();
    @Getter
    private int invalidProperties;

    /**
     * @param result {@link PropertyVerificatorResult} for which the statistic object should be created
     */
    public ConstraintViolationStatistic(PropertyVerificatorResult result) {
        Map<Object, Map<Field, ConstraintError>> map = result.getVerificationResult();
        invalidObjects = map.size();
        for (Map.Entry<Object, Map<Field, ConstraintError>> entry : map.entrySet()) {
            for (Map.Entry<Field, ConstraintError> t : entry.getValue().entrySet()) {
                if (constraintErrors.containsKey(t.getValue())) {
                    if (constraintErrors.get(t.getValue()).containsKey(entry.getKey())) {
                        constraintErrors.get(t.getValue()).get(entry.getKey()).add(t.getKey());
                    } else {
                        List<Field> l = new ArrayList<>();
                        l.add(t.getKey());
                        constraintErrors.get(t.getValue()).put(entry.getKey(), l);
                    }
                } else {
                    Map<Object, List<Field>> innerMap = new HashMap<>();
                    List<Field> l = new ArrayList<>();
                    l.add(t.getKey());
                    innerMap.put(entry.getKey(), l);
                    constraintErrors.put(t.getValue(), innerMap);
                }
                invalidProperties++;
            }
        }
    }

    private static int countViolatingFields(Map<Object, List<Field>> violatingObjects) {
        return violatingObjects.values().stream().map(List::size).reduce(0, Integer::sum);
    }

    /**
     * Returns a map of objects and corresponding fields which are violating the given ConstraintError
     *
     * @param ce the ConstraintError for which the violators should be returned
     * @return map of objects and corresponding fields
     */
    public Map<Object, List<Field>> getConstraintViolators(ConstraintError ce) {
        Map<Object, List<Field>> objectListMap = constraintErrors.get(ce);
        if (objectListMap == null || objectListMap.isEmpty()) {
            return Collections.emptyMap();
        } else {
            return Collections.unmodifiableMap(objectListMap);
        }
    }

    /**
     * @return List of object/field tuples which violating {@link ConstraintError#IsNull}
     */
    public Map<Object, List<Field>> getNullViolators() {
        return getConstraintViolators(ConstraintError.IsNull);
    }

    /**
     * @return List of object/field tuples which violating {@link ConstraintError#IsDefaultInitialized}
     */
    public Map<Object, List<Field>> getDefaultViolators() {
        return getConstraintViolators(ConstraintError.IsDefaultInitialized);
    }

    /**
     * @return List of object/field tuples which violating {@link ConstraintError#IsEmpty}
     */
    public Map<Object, List<Field>> getEmptyViolators() {
        return getConstraintViolators(ConstraintError.IsEmpty);
    }

    /**
     * @return Number of fields which are violating {@link ConstraintError#IsNull}
     */
    @ToString.Include
    public int getViolatedIsNull() {
        return countViolatingFields(getNullViolators());
    }

    /**
     * @return Number of field which are violating {@link ConstraintError#IsDefaultInitialized}
     */
    @ToString.Include
    public int getViolatedIsDefaultInitialized() {
        return countViolatingFields(getDefaultViolators());
    }

    /**
     * @return Number of fields which are violating {@link ConstraintError#IsEmpty}
     */
    @ToString.Include
    public int getViolatedIsEmpty() {
        return countViolatingFields(getEmptyViolators());
    }
}
