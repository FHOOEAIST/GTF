/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.builder.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.graph.*;
import science.aist.gtf.graph.builder.GraphBuilder;
import science.aist.gtf.graph.factory.GraphFactory;
import science.aist.gtf.graph.impl.GraphStateImpl;
import science.aist.gtf.graph.impl.MetaTagImpl;
import science.aist.gtf.graph.impl.VertexImpl;
import science.aist.jack.general.util.CastUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * <p>Test class for {@link GraphBuilderImpl}</p>
 *
 * @author Andreas Pointner
 */

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class GraphBuilderImplTest {
    @Test
    public void testAddVertexAndFromTo() {
        //given

        // when
        GraphStateAccessor<String, Void> graphState = GraphBuilderImpl.<String, Void>create()
                .addVertex("3")
                .from("1").to("2")
                .getGraphState();

        // then
        Assert.assertNotNull(graphState);
        Assert.assertEquals(graphState.getVertices().size(), 3);
        Collection<Vertex<String, Void>> vertices = graphState.getVertices();
        Assert.assertTrue(vertices.stream().anyMatch(v -> v.getElement().equals("1")));
        Assert.assertTrue(vertices.stream().anyMatch(v -> v.getElement().equals("2")));
        Assert.assertTrue(vertices.stream().anyMatch(v -> v.getElement().equals("3")));
        Assert.assertTrue(vertices.stream().filter(v -> v.getElement().equals("1")).anyMatch(v -> v.getEdges().size() == 1));
        Assert.assertTrue(vertices.stream().filter(v -> v.getElement().equals("1")).anyMatch(v -> v.getOutgoingEdges().size() == 1));
        Assert.assertTrue(vertices.stream().filter(v -> v.getElement().equals("1")).anyMatch(v -> v.getIncomingEdges().size() == 0));
        Assert.assertTrue(vertices.stream().filter(v -> v.getElement().equals("1")).anyMatch(v -> v.getEdges().stream().findFirst().get().getSource().getElement().equals("1")));
        Assert.assertTrue(vertices.stream().filter(v -> v.getElement().equals("1")).anyMatch(v -> v.getEdges().stream().findFirst().get().getTarget().getElement().equals("2")));
        Assert.assertTrue(vertices.stream().filter(v -> v.getElement().equals("2")).anyMatch(v -> v.getEdges().size() == 1));
        Assert.assertTrue(vertices.stream().filter(v -> v.getElement().equals("2")).anyMatch(v -> v.getOutgoingEdges().size() == 0));
        Assert.assertTrue(vertices.stream().filter(v -> v.getElement().equals("2")).anyMatch(v -> v.getIncomingEdges().size() == 1));
        Assert.assertTrue(vertices.stream().filter(v -> v.getElement().equals("2")).anyMatch(v -> v.getEdges().stream().findFirst().get().getSource().getElement().equals("1")));
        Assert.assertTrue(vertices.stream().filter(v -> v.getElement().equals("2")).anyMatch(v -> v.getEdges().stream().findFirst().get().getTarget().getElement().equals("2")));
        Assert.assertTrue(vertices.stream().filter(v -> v.getElement().equals("3")).anyMatch(v -> v.getEdges().size() == 0));
    }

    @Test
    public void testAddVertexWith() {
        //given

        // when
        GraphStateAccessor<String, Void> graphState = GraphBuilderImpl.<String, Void>create()
                .addVertexWith("1").with(v -> v.addMetaTag(new MetaTagImpl<>("test", "test")))
                .getGraphState();

        // then
        Optional<Vertex<String, Void>> first = graphState.getVertices().stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Assert.assertEquals(first.get().getElement(), "1");
        Assert.assertTrue(first.get().tryGetMetaTagValue("test").isPresent());
        Assert.assertEquals(first.get().tryGetMetaTagValue("test").get(), "test");
    }

    @Test
    public void testFromToData() {
        //given

        // when
        GraphStateAccessor<String, String> graphState = GraphBuilderImpl.<String, String>create()
                .from("1").toData("2").data("edgeData")
                .getGraphState();

        // then
        Optional<Edge<String, String>> first = graphState.getEdges(graphState.getVertices().stream().findFirst().get()).stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Assert.assertEquals(first.get().getElement(), "edgeData");
    }

    @Test
    public void testFromToWith() {
        //given

        // when
        GraphStateAccessor<String, String> graphState = GraphBuilderImpl.<String, String>create()
                .from("1").toWith("2").with(e -> e.addMetaTag(new MetaTagImpl<>("test", "test")))
                .getGraphState();

        // then
        Optional<Edge<String, String>> first = graphState.getEdges(graphState.getVertices().stream().findFirst().get()).stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Assert.assertTrue(first.get().tryGetMetaTagValue("test").isPresent());
        Assert.assertEquals(first.get().tryGetMetaTagValue("test").get(), "test");
    }

    @Test
    public void testFromToDataWith() {
        // given

        // when
        GraphStateAccessor<String, String> graphState = GraphBuilderImpl.<String, String>create()
                .from("1").toData("2").dataWith("edgeData").with(e -> e.addMetaTag(new MetaTagImpl<>("test", "test")))
                .getGraphState();

        // then
        Optional<Edge<String, String>> first = graphState.getEdges(graphState.getVertices().stream().findFirst().get()).stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Assert.assertEquals(first.get().getElement(), "edgeData");
        Assert.assertTrue(first.get().tryGetMetaTagValue("test").isPresent());
        Assert.assertEquals(first.get().tryGetMetaTagValue("test").get(), "test");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testFromByError() {
        // given

        // when
        GraphBuilderImpl.<String, Void>create()
                .fromByKey("1").to("2")
                .getGraphState();

        // then
        // exception expected because key 1 is unknown
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testWrongFactoryVertex() {
        // given
        Vertex<Object, Object> mockVertex = CastUtils.cast(Mockito.mock(Vertex.class));
        GraphFactory mockFactory = Mockito.mock(GraphFactory.class);
        Mockito.when(mockFactory.createVertex(ArgumentMatchers.any())).thenReturn(mockVertex);
        Mockito.when(mockFactory.createGraphState()).thenReturn(new GraphStateImpl<>());

        // when
        GraphBuilderImpl.<String, Void>create(mockFactory)
                .from("1").to("2")
                .getGraphState();

        // then
        // exception expected
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testWrongFactoryEdge() {
        // given
        Edge<Object, Object> mockEdge = CastUtils.cast(Mockito.mock(Edge.class));
        GraphFactory mockFactory = Mockito.mock(GraphFactory.class);
        Mockito.when(mockFactory.createGraphState()).thenReturn(new GraphStateImpl<>());
        Mockito.when(mockFactory.createVertex(ArgumentMatchers.any())).thenAnswer(x -> new VertexImpl<>(x.getArguments()[0]));
        Mockito.when(mockFactory.createEdge(ArgumentMatchers.any())).thenReturn(mockEdge);

        // when
        GraphBuilderImpl.<String, Void>create(mockFactory)
                .from("1").to("2")
                .getGraphState();

        // then
        // exception expected
    }

    @Test
    public void testDuplicateEdge() {
        // given

        // when
        GraphStateAccessor<String, Void> graphState = GraphBuilderImpl.<String, Void>create()
                .from("1").to("2")
                .from("1").to("2")
                .getGraphState();

        // then
        Set<Edge<String, Void>> collect = graphState.getVertices().stream().map(graphState::getEdges).flatMap(Collection::stream).collect(Collectors.toSet());
        Optional<Edge<String, Void>> first = graphState.getEdges(graphState.getVertices().stream().findFirst().get()).stream().findFirst();
        Assert.assertEquals(collect.size(), 1);
        Assert.assertTrue(first.isPresent());
        Assert.assertEquals(first.get().getWeight(), 2.0);
    }

    @Test
    public void testFromToByKeyData() {
        //given
        A a1 = new A(1);
        A a2 = new A(2);

        // when
        GraphStateAccessor<A, String> graphState = GraphBuilderImpl.<A, String>create()
                .addVertex(a1).addVertex(a2)
                .fromByKey(1).toDataByKey(2).data("edgeData")
                .getGraphState();

        // then
        Optional<Edge<A, String>> first = graphState.getEdges(graphState.getVertices().stream().findFirst().get()).stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Assert.assertEquals(first.get().getElement(), "edgeData");
    }

    @Test
    public void testFromToByKeyWith() {
        //given
        A a1 = new A(1);
        A a2 = new A(2);

        // when
        GraphStateAccessor<A, String> graphState = GraphBuilderImpl.<A, String>create()
                .addVertex(a1).addVertex(a2)
                .fromByKey(1).toWithByKey(2).with(e -> e.addMetaTag(new MetaTagImpl<>("test", "test")))
                .getGraphState();

        // then
        Optional<Edge<A, String>> first = graphState.getEdges(graphState.getVertices().stream().findFirst().get()).stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Assert.assertTrue(first.get().tryGetMetaTagValue("test").isPresent());
        Assert.assertEquals(first.get().tryGetMetaTagValue("test").get(), "test");
    }

    @Test
    public void testFromToByKeyDataWith() {
        //given
        A a1 = new A(1);
        A a2 = new A(2);

        // when
        GraphStateAccessor<A, String> graphState = GraphBuilderImpl.<A, String>create()
                .addVertex(a1).addVertex(a2)
                .fromByKey(1).toDataByKey(2).dataWith("edgeData").with(e -> e.addMetaTag(new MetaTagImpl<>("test", "test")))
                .getGraphState();

        // then
        Optional<Edge<A, String>> first = graphState.getEdges(graphState.getVertices().stream().findFirst().get()).stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Assert.assertEquals(first.get().getElement(), "edgeData");
        Assert.assertTrue(first.get().tryGetMetaTagValue("test").isPresent());
        Assert.assertEquals(first.get().tryGetMetaTagValue("test").get(), "test");
    }

    @Test
    public void testKeyExtractionByGraphId() {
        // given
        A a1 = new A(1);
        A a2 = new A(2);

        // when
        GraphStateAccessor<A, Void> graphState = GraphBuilderImpl.<A, Void>create()
                .addVertex(a1)
                .addVertex(a2)
                .fromByKey(1).toByKey(2)
                .getGraphState();

        // then
        Assert.assertEquals(graphState.getVertices().size(), 2);
        Optional<Edge<A, Void>> first = graphState.getEdges(graphState.getVertices().stream().findFirst().get()).stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Assert.assertEquals(first.get().getSource().getElement(), a1);
        Assert.assertEquals(first.get().getTarget().getElement(), a2);
    }

    @Test
    public void testKeyExtractionByKeyExtractor() {
        // given
        B b1 = new B(1);
        B b2 = new B(2);

        // when
        GraphStateAccessor<B, Void> graphState = GraphBuilderImpl.<B, Void>create(B::getKey)
                .addVertex(b1)
                .addVertex(b2)
                .fromByKey(1).toByKey(2)
                .getGraphState();

        // then
        Assert.assertEquals(graphState.getVertices().size(), 2);
        Optional<Edge<B, Void>> first = graphState.getEdges(graphState.getVertices().stream().findFirst().get()).stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Assert.assertEquals(first.get().getSource().getElement(), b1);
        Assert.assertEquals(first.get().getTarget().getElement(), b2);
    }

    @Test
    public void testKeyExtractionByGraphIdWithPrivateMethod() {
        // given
        C c1 = new C(1);
        C c2 = new C(2);

        // when
        GraphStateAccessor<C, Void> graphState = GraphBuilderImpl.<C, Void>create()
                .addVertex(c1)
                .addVertex(c2)
                .fromByKey(c1).toByKey(c2) // needs c1 and c2 here, as the key @GraphId is on a private method
                .getGraphState();

        // then
        Assert.assertEquals(graphState.getVertices().size(), 2);
        Optional<Edge<C, Void>> first = graphState.getEdges(graphState.getVertices().stream().findFirst().get()).stream().findFirst();
        Assert.assertTrue(first.isPresent());
        Assert.assertEquals(first.get().getSource().getElement(), c1);
        Assert.assertEquals(first.get().getTarget().getElement(), c2);
    }

    @Test
    public void testToGraph() {
        // given
        GraphBuilder<C, Void> graphBuilder = GraphBuilderImpl.create();

        // when
        Graph<C, Void> graph = graphBuilder.toGraph();

        // then
        Assert.assertNotNull(graph);
        Assert.assertNotNull(graph.getGraphState());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testToGraphFailAlreadyCreated() {
        // given
        GraphBuilder<C, Void> graphBuilder = GraphBuilderImpl.create();
        graphBuilder.toGraph();

        // when
        graphBuilder.from(null);

        // then
        // Exception expected - graph was already created
    }

    @Test
    public void testUpdateKey() {
        // given
        GraphBuilder<C, Void> graphBuilder = GraphBuilderImpl.create(C::getKey);
        C c = new C(1);
        graphBuilder.getOrAddVertex(c);

        // when
        graphBuilder.updateKey(c, 2, C::setKey);

        // then
        Assert.assertEquals(graphBuilder.getGraphState().getVertices().size(), 1);
        Assert.assertEquals(c.getKey(), 2);
        Assert.assertEquals(graphBuilder.getOrAddVertex(c).getElement(), c);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testUpdateKeyMappingDoesNotReplaceKey() {
        // given
        GraphBuilder<C, Void> graphBuilder = GraphBuilderImpl.create(C::getKey);
        C c = new C(1);
        graphBuilder.getOrAddVertex(c);

        // when
        graphBuilder.updateKey(c, 2, C::doNothing);

        // then
        // exception - mapping does not correctly replace the key
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testUpdateKeyAlreadyExists() {
        // given
        GraphBuilder<C, Void> graphBuilder = GraphBuilderImpl.create(C::getKey);
        C c = new C(1);
        graphBuilder.getOrAddVertex(c);
        graphBuilder.getOrAddVertex(new C(2));

        // when
        graphBuilder.updateKey(c, 2, C::setKey);

        // then
        // exception - a mapping for the given key already exists
    }

    @Test
    public void testCallback() {
        // given
        AtomicBoolean ab = new AtomicBoolean(false);
        GraphBuilder<C, Void> graphBuilder = GraphBuilderImpl.create();

        // when
        graphBuilder.addGraphCreationCallback(g -> ab.set(true));
        graphBuilder.addGraphCreationCallback(g -> g.addMetaTag(new MetaTagImpl<>("key", "value")));

        // then
        var graph = graphBuilder.toGraph();
        Assert.assertTrue(ab.get());
        Assert.assertEquals(graph.getMetaTagValue("key"), "value");
    }

    @Test
    public void testAddSubGraph() {
        // given
        GraphBuilder<String, Void> graphBuilder = GraphBuilderImpl.create();

        Graph<String, Void> subGraph = GraphBuilderImpl.<String, Void>create()
                .from("A").to("B")
                .from("B").to("C")
                .toGraph();

        graphBuilder.from("X").to("Y");

        // when
        Graph<String, Void> subGraphView = graphBuilder.addSubGraph(subGraph);

        // then
        Graph<String, Void> graph = graphBuilder.toGraph();
        Assert.assertEquals(graph.getVertices().size(), 5);
        Assert.assertEquals(graph.getEdges().size(), 3);
        Assert.assertNotNull(subGraphView);
        Assert.assertEquals(subGraphView.getVertices().size(), 3);
        Assert.assertEquals(subGraphView.getEdges().size(), 2);
    }

    @Test
    public void testAddSubGraph2() {
        // given
        GraphBuilder<String, Void> graphBuilder = GraphBuilderImpl.create();

        Graph<String, Void> subGraph = GraphBuilderImpl.<String, Void>create()
                .from("A").to("B")
                .from("B").to("C")
                .toGraph();

        graphBuilder.from("X").to("Y");
        Graph<String, Void> subGraphView = graphBuilder.addSubGraph(subGraph);

        // when
        graphBuilder.from("Y").to("A");

        // then
        Graph<String, Void> graph = graphBuilder.toGraph();
        Assert.assertEquals(graph.getVertices().size(), 5);
        Assert.assertEquals(graph.getEdges().size(), 4);
        Assert.assertNotNull(subGraphView);
        Assert.assertEquals(subGraphView.getVertices().size(), 3);
        Assert.assertEquals(subGraphView.getEdges().size(), 2);
    }

    @Test
    public void testAddSubGraph3() {
        // given
        GraphBuilder<String, Void> graphBuilder = GraphBuilderImpl.create();

        Graph<String, Void> subGraph = GraphBuilderImpl.<String, Void>create()
                .from("A").to("B")
                .from("B").to("C")
                .toGraph();

        graphBuilder.from("X").to("Y");
        Graph<String, Void> subGraphView = graphBuilder.addSubGraph(subGraph);
        graphBuilder.from("Y").to("A");

        // when
        graphBuilder.from("Y").to("D");

        // then
        Graph<String, Void> graph = graphBuilder.toGraph();
        Assert.assertEquals(graph.getVertices().size(), 6);
        Assert.assertEquals(graph.getEdges().size(), 5);
        Assert.assertNotNull(subGraphView);
        Assert.assertEquals(subGraphView.getVertices().size(), 3);
        Assert.assertEquals(subGraphView.getEdges().size(), 2);
    }

    @Test
    public void testAddSubGraph4() {
        // given
        GraphBuilder<String, Void> graphBuilder = GraphBuilderImpl.create();

        Graph<String, Void> subGraph = GraphBuilderImpl.<String, Void>create()
                .from("A").to("B")
                .from("B").to("C")
                .toGraph();
        subGraph.addMetaTag(new MetaTagImpl<>("a", "b"));

        graphBuilder.from("X").to("Y");

        // when
        Graph<String, Void> subGraphView = graphBuilder.addSubGraph(subGraph);

        // then
        Graph<String, Void> graph = graphBuilder.toGraph();
        Assert.assertEquals(graph.getVertices().size(), 5);
        Assert.assertEquals(graph.getEdges().size(), 3);
        Assert.assertNotNull(subGraphView);
        Assert.assertEquals(subGraphView.getVertices().size(), 3);
        Assert.assertEquals(subGraphView.getEdges().size(), 2);
        Assert.assertEquals(subGraphView.getMetaTagValue("a"), "b");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testAddSubGraphFail() {
        // given
        GraphBuilder<String, Void> graphBuilder = GraphBuilderImpl.create();

        Graph<String, Void> subGraph = GraphBuilderImpl.<String, Void>create()
                .from("A").to("B")
                .from("B").to("C")
                .toGraph();

        graphBuilder.from("X").to("Y");
        graphBuilder.from("Y").to("A");

        // when
        graphBuilder.addSubGraph(subGraph);

        // then
        // Exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testAddSubGraphFail2() {
        // given
        GraphBuilder<A, Void> graphBuilder = GraphBuilderImpl.create(A::getKey);

        Graph<A, Void> subGraph = GraphBuilderImpl.<A, Void>create(x -> x)
                .from(new A(1)).to(new A(2))
                .from(new A(1)).to(new A(3))
                .toGraph();

        // when
        graphBuilder.addSubGraph(subGraph);

        // then
        // Exception
    }

    @Test
    public void testGraphBuilderMerging() {
        // given
        @AllArgsConstructor
        @Getter
        class Person {
            private int id;
            private String firstname;
            private String lastname;
        }
        Person p1 = new Person(1, "Max", "Mustermann");
        Person p2 = new Person(2, "Erika", "Mustermann");
        Person p3 = new Person(1, "Dr. Max", "Mustermann BSc.");

        // when
        GraphBuilder<Person, Void> graphBuilder = GraphBuilderImpl.create(Person::getId, (x1, x2) -> new Person(x1.getId(), x1.getFirstname(), x2.getLastname()));
        var graph = graphBuilder.from(p1).to(p2)
                .from(p3).to(p2)
                .toGraph();

        // then
        boolean isPresent = graph.getVertices().stream()
                .filter(x -> x.getElement().getFirstname().equals("Max"))
                .filter(x -> x.getElement().getLastname().equals("Mustermann BSc."))
                .filter(x -> x.getElement().getId() == 1)
                .filter(x -> x.getElement() != p1)
                .anyMatch(x -> x.getElement() != p3);
        Assert.assertTrue(isPresent);
    }

    @AllArgsConstructor
    private static class A {
        private final int key;

        @GraphId
        public int getKey() {
            return key;
        }
    }

    @Getter
    @AllArgsConstructor
    private static class B {
        private final int key;
    }

    @AllArgsConstructor
    @Setter
    private static class C {
        private int key;

        @GraphId
        private int getKey() {
            return key;
        }

        public void doNothing(int x) {
        }
    }
}