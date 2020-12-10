/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.visitor.factory.impl;

import lombok.Getter;
import lombok.Setter;
import science.aist.gtf.verification.syntactic.PropertyRestrictor;
import science.aist.gtf.verification.syntactic.visitor.*;
import science.aist.gtf.verification.syntactic.visitor.factory.RestrictedVisitorFactory;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * <p>Factory for creating singleton instances of {@link ConstraintVisitor}</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public class SingletonVisitorFactory implements RestrictedVisitorFactory {
    private final ConstraintVisitor<Object> objectVisitor;
    private final ConstraintVisitor<Iterable<?>> iterableVisitor;
    private final ConstraintVisitor<Collection<Field>> fieldsVisitor;
    private final ConstraintVisitor<Field> fieldVisitor;
    private final ConstraintVisitor<Object> arrayVisitor;
    @Getter
    @Setter
    private PropertyRestrictor restrictor;


    public SingletonVisitorFactory(PropertyRestrictor restrictor) {
        this.restrictor = restrictor;
        objectVisitor = new ObjectVisitor(this);
        iterableVisitor = new IterableVisitor(this);
        fieldsVisitor = new FieldsVisitor(this);
        fieldVisitor = new FieldVisitor(this);
        arrayVisitor = new ArrayVisitor(this);
    }

    @Override
    public ConstraintVisitor<Object> createObjectVisitor() {
        return objectVisitor;
    }

    @Override
    public ConstraintVisitor<Iterable<?>> createIterableVisitor() {
        return iterableVisitor;
    }

    @Override
    public ConstraintVisitor<Collection<Field>> createFieldsVisitor() {
        return fieldsVisitor;
    }

    @Override
    public ConstraintVisitor<Field> createFieldVisitor() {
        return fieldVisitor;
    }

    @Override
    public ConstraintVisitor<Object> createArrayVisitor() {
        return arrayVisitor;
    }
}
