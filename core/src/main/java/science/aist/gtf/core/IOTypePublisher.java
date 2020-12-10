/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.core;

/**
 * <p>Interface which allows getting Input and Output Types</p>
 *
 * @param <I> the input type
 * @param <O> the output type
 * @author Christoph Praschl
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface IOTypePublisher<I, O> {
    /**
     * @return OutputType of class
     */
    Class<O> getOutType();

    /**
     * @return InputType of class
     */
    Class<I> getInType();
}
