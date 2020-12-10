/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.visitor.factory;

import science.aist.gtf.verification.syntactic.visitor.ConstraintVisitor;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * <p>Factory Interface for creating {@link ConstraintVisitor}</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public interface ConstraintVisitorFactory {
    /**
     * Method for creating an object visitor
     *
     * @return an object visitor
     */
    ConstraintVisitor<Object> createObjectVisitor();

    /**
     * Method for creating an iterable visitor
     *
     * @return an iterable visitor
     */
    ConstraintVisitor<Iterable<?>> createIterableVisitor();

    /**
     * Method for creating a fields visitor
     *
     * @return an fields visitor
     */
    ConstraintVisitor<Collection<Field>> createFieldsVisitor();

    /**
     * Method for creating a field visitor
     *
     * @return an field visitor
     */
    ConstraintVisitor<Field> createFieldVisitor();

    /**
     * Method for creating an array visitor
     *
     * @return an array visitor
     */
    ConstraintVisitor<Object> createArrayVisitor();
}
