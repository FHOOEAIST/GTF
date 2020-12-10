/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic.constraint;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Enum for property verificator to name the violated constraint</p>
 *
 * @author Christoph Praschl
 * @since 1.0
 */
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
public class ConstraintError {
    public static final ConstraintError IsNull = new ConstraintError("IsNull");
    public static final ConstraintError IsDefaultInitialized = new ConstraintError("IsDefaultInitialized");
    public static final ConstraintError IsEmpty = new ConstraintError("IsEmpty");
    public static final ConstraintError NoError = new ConstraintError("NoError");

    private String constraintName;
}
