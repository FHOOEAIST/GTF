/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.transformation;

import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.Vertex;
import science.aist.gtf.graph.builder.GraphBuilder;
import science.aist.gtf.graph.builder.impl.GraphBuilderImpl;
import science.aist.gtf.graph.impl.traversal.DepthFirstSearchTraversalStrategy;
import science.aist.gtf.transformation.renderer.AbstractGraphTransformationRenderer;
import science.aist.gtf.transformation.renderer.MultiGraphTransformationRenderer;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

/**
 * <p>Test class for {@link GraphTransformer}</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
public class GraphTransformerTest {
    /**
     * <p>Test example:</p>
     * <p>Assume a List (int: 3) -&gt; (double: 5.2) -&gt; (int: 7) -&gt; ...</p>
     * <p>We want to transfer it into two different lists one where only int are stored, the other one with only
     * doubles.</p>
     * <pre>{@code Pair<List<Double>, List<Integer>>: [(5.2), (3) -> (7)].}</pre>
     */
    @Test
    public void testApplyTransformation() {
        // given
        GraphBuilder<String, Void> graphBuilder = GraphBuilderImpl.create();
        List<String> list = List.of("int: 3", "double: 5.2", "int: 7");
        String prev = null;
        for (String item : list) {
            if (prev != null) {
                graphBuilder.from(prev).to(item);
            }
            prev = item;
        }
        graphBuilder.from(prev).to(list.get(0)); // Create a cyclic list, just to test this case as well.

        Graph<String, Void> graph = graphBuilder.toGraph();

        IntegerTransformerRenderer itr = new IntegerTransformerRenderer(new IntegerCondition());
        DoubleTransformerRenderer dtr = new DoubleTransformerRenderer(new DoubleCondition());
        // The order of the child renderers matters, because the first one which matches the condition is chosen to render the element
        // double would match for double and int as well. So it's important to add int first.
        MultiGraphTransformationRenderer<Number, String, Void> renderer = new MultiGraphTransformationRenderer<>(Arrays.asList(itr, dtr));
        ListGraphTransformer transformer = new ListGraphTransformer();
        transformer.setElementRenderer(renderer);

        // when
        Pair<List<Double>, List<Integer>> result = transformer.applyTransformation(graph);

        // then
        Assert.assertEquals(result.getFirst().size(), 1);
        Assert.assertEquals(result.getSecond().size(), 2);
    }

    //<editor-fold desc="Inner Classes to create a test for a demo example">
    @SuppressWarnings("WeakerAccess")
    static class Pair<First, Second> {
        private First first;
        private Second second;

        public static <First, Second> Pair<First, Second> of(First first, Second second) {
            Pair<First, Second> p = new Pair<>();
            p.first = first;
            p.second = second;
            return p;
        }

        public First getFirst() {
            return first;
        }

        public Second getSecond() {
            return second;
        }
    }

    static class ListGraphTransformer implements GraphTransformer<String, Void, Pair<List<Double>, List<Integer>>> {
        private MultiGraphTransformationRenderer<Number, String, Void> baseRenderer;

        @Override
        public Pair<List<Double>, List<Integer>> applyTransformation(Graph<String, Void> graph) {
            List<Double> doubleList = new ArrayList<>();
            List<Integer> integerList = new ArrayList<>();

            graph.setVertexTraversalStrategy(new DepthFirstSearchTraversalStrategy<>(graph));

            graph.traverseVertices(element -> {
                Number res = baseRenderer.renderElement(graph, element);
                if (res instanceof AtomicInteger) {
                    integerList.add(((AtomicInteger) res).get());
                } else if (res instanceof AtomicLong) {
                    doubleList.add(Double.longBitsToDouble(((AtomicLong) res).get()));
                } else {
                    throw new IllegalStateException("No supported type");
                }
            });

            return Pair.of(doubleList, integerList);
        }

        public void setElementRenderer(MultiGraphTransformationRenderer<Number, String, Void> baseRenderer) {
            this.baseRenderer = baseRenderer;
        }
    }

    static class DoubleTransformerRenderer extends AbstractGraphTransformationRenderer<AtomicLong, String, Void> {
        public DoubleTransformerRenderer(RendererCondition<Vertex<String, Void>> rendererCondition) {
            super(rendererCondition);
        }

        @Override
        public AtomicLong mapProperties(AtomicLong element, Graph<String, Void> container, Vertex<String, Void> currentElement) {
            String s = currentElement.getElement();
            int idx = s.indexOf(":") + 2;
            if (idx < 0) throw new IllegalStateException("Not valid format!");
            element.set(Double.doubleToLongBits(Double.parseDouble(s.substring(idx))));
            return element;
        }
    }

    static class IntegerTransformerRenderer extends AbstractGraphTransformationRenderer<AtomicInteger, String, Void> {
        public IntegerTransformerRenderer(RendererCondition<Vertex<String, Void>> rendererCondition) {
            super(rendererCondition, AtomicInteger.class, String.class);
        }

        @Override
        public AtomicInteger mapProperties(AtomicInteger element, Graph<String, Void> container, Vertex<String, Void> currentElement) {
            String s = currentElement.getElement();
            int idx = s.indexOf(":") + 2;
            if (idx < 0) throw new IllegalStateException("Not valid format!");
            element.set(Integer.parseInt(s.substring(idx)));
            return element;
        }
    }

    static class IntegerCondition implements RendererCondition<Vertex<String, Void>> {
        Predicate<Vertex<String, Void>> condition = s -> {
            try {
                Integer.parseInt(s.getElement().substring(s.getElement().indexOf(":") + 2));
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
        };

        @Override
        public Predicate<Vertex<String, Void>> createCondition() {
            return condition;
        }
    }

    static class DoubleCondition implements RendererCondition<Vertex<String, Void>> {
        Predicate<Vertex<String, Void>> condition = s -> {
            try {
                Double.parseDouble(s.getElement().substring(s.getElement().indexOf(":") + 1));
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
        };

        @Override
        public Predicate<Vertex<String, Void>> createCondition() {
            return condition;
        }
    }
    //</editor-fold>
}
