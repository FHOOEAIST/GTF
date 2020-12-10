/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.visitor.factory;

import science.aist.gtf.verification.syntactic.PropertyRestrictor;
import science.aist.gtf.verification.syntactic.visitor.ConstraintVisitor;

/**
 * <p>Factory Interface for creating {@link ConstraintVisitor} and using a {@link PropertyRestrictor}</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
public interface RestrictedVisitorFactory extends ConstraintVisitorFactory {
    /**
     * @return the used property restrictor
     */
    PropertyRestrictor getRestrictor();

    /**
     * @param restrictor The restrictor which should be injected
     */
    void setRestrictor(PropertyRestrictor restrictor);
}
