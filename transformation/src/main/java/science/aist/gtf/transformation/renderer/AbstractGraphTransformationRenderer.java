/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.transformation.renderer;

import lombok.CustomLog;
import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.Vertex;
import science.aist.gtf.transformation.TransformerUtils;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

import java.lang.reflect.InvocationTargetException;

import static science.aist.jack.exception.ExceptionUtils.unchecked;


/**
 * <p>Abstract implementation of a transformation renderer</p>
 *
 * @param <T> The result type
 * @param <S> The input type of the decorated source vertex element
 * @param <E> the input type of the decorated source edge element
 * @author Andreas Pointner
 * @since 1.0
 */
@CustomLog
public abstract class AbstractGraphTransformationRenderer<T, S, E> extends AbstractConditionalTransformationRenderer<T, Graph<S, E>, Vertex<S, E>> implements GraphTransformationRenderer<T, S, E> {

    /**
     * Type of the reference class
     */
    protected Class<T> classT;

    /**
     * Type of the input class
     */
    protected Class<S> classS;

    public AbstractGraphTransformationRenderer(RendererCondition<Vertex<S, E>> rendererCondition) {
        super(rendererCondition);

        // Try to extract the type arguments. Log a warning if this fails.
        try {
            classT = TransformerUtils.getTypeArgument(getClass(), 0);
            classS = TransformerUtils.getTypeArgument(getClass(), 1);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    public AbstractGraphTransformationRenderer(RendererCondition<Vertex<S, E>> rendererCondition, Class<T> classT, Class<S> classS) {
        super(rendererCondition);
        this.classT = classT;
        this.classS = classS;
    }

    @Override
    public T createElement() {
        if (classT == null)
            throw new UnsupportedOperationException("Override this method as the class type could not be extracted!");

        try {
            // Try to return a new element of the type. This fails, if the class cannot be accessed, or if there is no default constructor.
            return classT.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw unchecked(e);
        }
    }
}
