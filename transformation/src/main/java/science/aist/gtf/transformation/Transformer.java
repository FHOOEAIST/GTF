/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.transformation;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * <p>Interface to apply a transformation on a given input and output</p>
 *
 * @param <Input>  input type
 * @param <Output> output type
 * @author Andreas Pointner
 * @since 1.0
 */
@FunctionalInterface
public interface Transformer<Input, Output> {
    /**
     * Transforms an input into an output
     *
     * @param input the input
     * @return the result
     */
    Output applyTransformation(Input input);

    /**
     * Verifies before applying the transformation
     *
     * @param inputVerificator input verificator
     * @return a new transformer which calls the verificator first on input only, and then executes the
     * "applyTransformation()" method of this transformer
     */
    default Transformer<Input, Output> verificateBefore(Predicate<Input> inputVerificator) {
        Objects.requireNonNull(inputVerificator);
        return input -> {
            if (!inputVerificator.test(input)) {
                throw new IllegalStateException("Before Verification with input (" + input + ") failed!");
            }
            return applyTransformation(input);
        };
    }

    /**
     * Verifies after applying the transformation
     *
     * @param inputOutputVerificator input-/output verificator
     * @return a new transformer which execute the applyTransformation of this transformer first, and then calls the
     * verificator
     */
    default Transformer<Input, Output> verificateAfter(BiPredicate<Input, Output> inputOutputVerificator) {
        Objects.requireNonNull(inputOutputVerificator);
        return andThenConsumeResult((input, output) -> {
            if (!inputOutputVerificator.test(input, output)) {
                throw new IllegalStateException("After Verification failed!");
            }
        });
    }

    /**
     * Applies the current transformer first, and then executes the after transformer
     *
     * @param after       the transformer to be executed next
     * @param <NewOutput> the new output of the next transformer
     * @return a new transformer which apply this transformer first, and then the after transformer
     */
    default <NewOutput> Transformer<Input, NewOutput> andThen(Transformer<? super Output, ? extends NewOutput> after) {
        Objects.requireNonNull(after);
        return input -> after.applyTransformation(applyTransformation(input));
    }

    /**
     * Applies the current transformer first, and then executes the consumer.
     *
     * @param consumer the consumer to be executed after the transformation.
     * @return new transformer which execute the applyTransformation of this transformer first, and then calls the
     * consumer
     */
    default Transformer<Input, Output> andThenConsumeResult(BiConsumer<Input, Output> consumer) {
        Objects.requireNonNull(consumer);
        return input -> {
            Output output = applyTransformation(input);
            consumer.accept(input, output);
            return output;
        };
    }
}