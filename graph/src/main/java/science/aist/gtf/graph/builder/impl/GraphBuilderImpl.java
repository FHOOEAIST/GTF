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
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import science.aist.gtf.graph.*;
import science.aist.gtf.graph.builder.*;
import science.aist.gtf.graph.factory.GraphFactory;
import science.aist.gtf.graph.factory.GraphFactoryFactory;
import science.aist.gtf.graph.impl.AbstractEdge;
import science.aist.gtf.graph.impl.AbstractVertex;
import science.aist.gtf.graph.impl.GraphStateImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>Implementation of a graph builder (it creates directed weighted edges)</p>
 * <p>Vertices are identified using an ID, which is extracted using a key mapper. the default key mapper tries to
 * extract the key from a method that is annotation with {@link GraphId}, make sure, that this method has a consistent
 * state. If no {@link GraphId} can be found the decorated vertex value is used as a key. If there are multiple methods
 * that are annotated with {@link GraphId} the first one is used. Please be aware this could lead to unwanted behaviour,
 * and should therefore be avoided.</p>
 * <p>If an edge between vertex a and b is created, it first checks if there is already an edge, if this is the case
 * the weight of the edge is incremented by one. The provided data element will be ignored, but callback functions are
 * called.</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
@CustomLog
public class GraphBuilderImpl<V, E> implements GraphBuilder<V, E> {
    /**
     * The graph state to save the vertices and edges
     */
    private final GraphState<V, E> graphState;
    /**
     * The mapper between the decorated vertex element and it's key
     */
    private final Function<V, Object> keyMapper;
    /**
     * <p>The graph factory that is used to create the vertices and edges.</p>
     * <p>Note: the created vertices and edge must derive from {@link AbstractVertex} respectively {@link AbstractEdge}
     * in order to be accepted. If that is not the case, the adding of vertices and/or edges will fail</p>
     */
    private final GraphFactory graphFactory;
    /**
     * Map that store the created vertices + the key of the decorated element. This could of course be calculated for
     * every vertex everytime a new vertex is added, but that would result in bad performance. So this can be seen as
     * some sort of cache.
     */
    private final Map<Object, Vertex<V, E>> vertices;
    /**
     * Stores the callback methods that are executed on graph creation.
     */
    private final List<Consumer<Graph<V, E>>> creationCallbacks;
    /**
     * Flag that indicates if the GraphBuilder allows any further changes. This is set to false, once a graph is
     * created.
     */
    private boolean allowChanges = true;

    /**
     * Initializes the graph builder with a key mapper and a graph factory
     *
     * @param keyMapper    {@link GraphBuilderImpl#keyMapper}
     * @param graphFactory {@link GraphBuilderImpl#graphFactory}
     */
    private GraphBuilderImpl(Function<V, Object> keyMapper, GraphFactory graphFactory) {
        this.keyMapper = keyMapper;
        this.graphFactory = graphFactory;
        graphState = graphFactory.createGraphState();
        vertices = new HashMap<>();
        creationCallbacks = new ArrayList<>();
    }

    /**
     * Creates a new instance of the default key mapper, which either uses a method that is annotated with {@link
     * GraphId} or uses the element itself as a fallback.
     *
     * @param <V> the type of the decorated vertex
     * @return an function representing the key mapper.
     */
    public static <V> Function<V, Object> getDefaultKeyMapper() {
        return element -> {
            try {
                Optional<Method> method = Arrays.stream(element.getClass().getDeclaredMethods())
                        .filter(m -> m.getAnnotation(GraphId.class) != null)
                        .findFirst();
                if (method.isPresent()) {
                    return method.get().invoke(element);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.debug(e);
                // ignore the exception, because if we can't find a GraphKey, we use the element itself
            }
            // per default we will use the element itself as a key
            return element;
        };
    }

    /**
     * Creates a new graph builder using the default key mapper as well as the default factory ({@link
     * GraphFactoryFactory#getDefaultFactory()}
     *
     * @param <V> the type of the decorated vertex value
     * @param <E> the type of the decorated edge value
     * @return a new instance of the GraphBuilder
     */
    public static <V, E> GraphBuilder<V, E> create() {
        return create(getDefaultKeyMapper(), GraphFactoryFactory.getDefaultFactory());
    }

    /**
     * Creates a new graph builder using the default key mapper
     *
     * @param graphFactory specify the graph factory that should be used
     * @param <V>          the type of the decorated vertex value
     * @param <E>          the type of the decorated edge value
     * @return a new instance of the GraphBuilder
     */
    public static <V, E> GraphBuilder<V, E> create(GraphFactory graphFactory) {
        return create(getDefaultKeyMapper(), graphFactory);
    }

    /**
     * Creates a new graph builder using the default factory ({@link GraphFactoryFactory#getDefaultFactory()}
     *
     * @param keyMapper specify the key mapper that should be used
     * @param <V>       the type of the decorated vertex value
     * @param <E>       the type of the decorated edge value
     * @return a new instance of the GraphBuilder
     */
    public static <V, E> GraphBuilder<V, E> create(Function<V, Object> keyMapper) {
        return create(keyMapper, GraphFactoryFactory.getDefaultFactory());
    }

    /**
     * Creates a new graph builder
     *
     * @param graphFactory specify the graph factory that should be used
     * @param keyMapper    specify the key mapper that should be used
     * @param <V>          the type of the decorated vertex value
     * @param <E>          the type of the decorated edge value
     * @return a new instance of the GraphBuilder
     */
    public static <V, E> GraphBuilder<V, E> create(Function<V, Object> keyMapper, GraphFactory graphFactory) {
        return new GraphBuilderImpl<>(keyMapper, graphFactory);
    }

    @Override
    public GraphStateAccessor<V, E> getGraphState() {
        return graphState.readonly();
    }

    @Override
    public Vertex<V, E> getOrAddVertex(V value) {

        Object key = keyMapper.apply(value);
        if (vertices.containsKey(key)) {
            return vertices.get(key);
        }

        // Check only needs to be performed on writing operations, the above case is readonly.
        checkChangeAllowed();

        Vertex<V, E> vertex = graphFactory.createVertex(value);
        vertices.put(key, vertex);
        graphState.addVertex(vertex);
        return vertex;
    }

    @Override
    public GraphBuilder<V, E> addVertex(V value) {
        checkChangeAllowed();
        getOrAddVertex(value);
        return this;
    }

    @Override
    public VertexBuilderWith<V, E> addVertexWith(V value) {
        checkChangeAllowed();
        return new VertexBuilderWithImpl(getOrAddVertex(value));
    }

    @Override
    public EdgeBuilder<V, E> from(V value) {
        checkChangeAllowed();
        return new EdgeBuilderImpl(getOrAddVertex(value));
    }

    @Override
    public EdgeBuilder<V, E> fromByKey(Object key) {
        checkChangeAllowed();
        return new EdgeBuilderImpl(getVertexByKey(key));
    }

    @Override
    public Graph<V, E> toGraph() {
        allowChanges = false;
        Graph<V, E> graph = graphFactory.createGraph(getGraphState());
        creationCallbacks.forEach(c -> c.accept(graph));
        return graph;
    }

    @Override
    public <T> void updateKey(V element, T key, BiConsumer<V, T> mapping) {
        checkChangeAllowed();
        if (vertices.containsKey(key)) {
            throw new IllegalStateException("There is already a mapping with the provided key");
        }
        Object oldKey = keyMapper.apply(element);
        Vertex<V, E> vertexByKey = getVertexByKey(oldKey);
        vertices.remove(oldKey);
        mapping.accept(element, key);
        Object newKey = keyMapper.apply(element);
        if (key != newKey) {
            throw new IllegalStateException("The mapping function did not correctly replace the oldKey with the newKey");
        }
        vertices.put(newKey, vertexByKey);
    }

    @Override
    public void addGraphCreationCallback(Consumer<Graph<V, E>> callback) {
        checkChangeAllowed();
        creationCallbacks.add(callback);
    }

    @Override
    public Graph<V, E> addSubGraph(Graph<V, E> subGraph) {
        // validate if possible:
        List<Object> keys = subGraph.getVertices()
                .stream()
                .map(Vertex::getElement)
                .map(keyMapper)
                .collect(Collectors.toList());
        if (keys.stream().anyMatch(vertices::containsKey)) {
            throw new IllegalStateException("Cannot add sub graph to graph, because at least a single element in the " +
                    "subgraph has the same key as an element in the graph.");
        }
        if (keys.size() != new HashSet<>(keys).size()) {
            throw new IllegalStateException("Different elements in the subgraph have the same key.");
        }
        // New State for new resulting graph
        GraphState<V, E> subGraphState = new GraphStateImpl<>();

        Map<Vertex<V, E>, VertexView<V, E>> cache = new HashMap<>();

        // Add the vertices to the graph builder and add the vertices to the sub graph state.
        var edges = new HashSet<>(subGraph.getEdges());

        for (Vertex<V, E> vertex : subGraph.getVertices()) {// Add to graph builder
            Object key = keyMapper.apply(vertex.getElement());
            vertices.put(key, vertex);
            graphState.addVertex(vertex);

            // Add to resulting graph state but decorated from an VertexView
            VertexView<V, E> vertexView = graphFactory.createVertexView(vertex);
            cache.put(vertex, vertexView);
            subGraphState.addVertex(vertexView);
        }

        // Add edges to the graph and add the edges to the sub graph state
        for (Edge<V, E> edge : edges) {
            Vertex<V, E> source = edge.getSource();
            Vertex<V, E> target = edge.getTarget();
            graphState.addEdge(edge, source, target);
            subGraphState.addEdge(graphFactory.createEdgeView(edge), cache.get(source), cache.get(target));
        }

        // Creating resulting view on the subgraph
        Graph<V, E> resultGraph = graphFactory.createGraph(subGraphState.readonly());
        subGraph.getMetaTags().forEach(resultGraph::addMetaTag);
        return resultGraph;
    }

    private Vertex<V, E> getVertexByKey(Object key) {
        if (!vertices.containsKey(key)) {
            throw new IllegalStateException("Cannot start a vertex from an unknown key");
        }
        return vertices.get(key);
    }

    private Edge<V, E> createEdge(Vertex<V, E> source, Vertex<V, E> target, E value) {
        return graphState.getEdges(source)
                .stream()
                .filter(e -> target == e.getTarget())
                .findAny()
                .map(edge -> {
                    if (edge instanceof AbstractEdge)
                        ((AbstractEdge<V, E>) edge).setWeight(edge.getWeight() + 1);
                    return edge;
                }).orElseGet(() -> {
                    Edge<V, E> edge = graphFactory.createEdge(value);
                    graphState.addEdge(edge, source, target);
                    return edge;
                });
    }

    private void checkChangeAllowed() {
        if (!allowChanges)
            throw new IllegalStateException("Graph Builder already created graph, no further manipulations allowed");
    }


    // non-static inner classes are used, to be able to access properties and private methods from graph builder
    @RequiredArgsConstructor
    private class VertexBuilderWithImpl implements VertexBuilderWith<V, E> {
        private final Vertex<V, E> vertex;

        @Override
        public GraphBuilder<V, E> with(Consumer<Vertex<V, E>> callback) {
            checkChangeAllowed();
            callback.accept(vertex);
            return GraphBuilderImpl.this;
        }
    }

    @RequiredArgsConstructor
    private class EdgeBuilderImpl implements EdgeBuilder<V, E> {
        private final Vertex<V, E> source;

        @Override
        public GraphBuilder<V, E> to(V value) {
            checkChangeAllowed();
            Vertex<V, E> target = getOrAddVertex(value);
            createEdge(source, target, null);
            return GraphBuilderImpl.this;
        }

        @Override
        public GraphBuilder<V, E> toByKey(Object key) {
            checkChangeAllowed();
            Vertex<V, E> target = getVertexByKey(key);
            createEdge(source, target, null);
            return GraphBuilderImpl.this;
        }

        @Override
        public EdgeDataBuilder<V, E> toData(V value) {
            checkChangeAllowed();
            Vertex<V, E> target = getOrAddVertex(value);
            return new EdgeDataBuilderImpl(source, target);
        }

        @Override
        public EdgeDataBuilder<V, E> toDataByKey(Object key) {
            checkChangeAllowed();
            Vertex<V, E> target = getVertexByKey(key);
            return new EdgeDataBuilderImpl(source, target);
        }

        @Override
        public EdgeWithBuilder<V, E> toWith(V value) {
            checkChangeAllowed();
            Vertex<V, E> target = getOrAddVertex(value);
            return new EdgeWithBuilderImpl(source, target);
        }

        @Override
        public EdgeWithBuilder<V, E> toWithByKey(Object key) {
            checkChangeAllowed();
            Vertex<V, E> target = getVertexByKey(key);
            return new EdgeWithBuilderImpl(source, target);
        }
    }

    @RequiredArgsConstructor
    private class EdgeDataBuilderImpl implements EdgeDataBuilder<V, E> {
        private final Vertex<V, E> source;
        private final Vertex<V, E> target;

        @Override
        public GraphBuilder<V, E> data(E value) {
            checkChangeAllowed();
            createEdge(source, target, value);
            return GraphBuilderImpl.this;
        }

        @Override
        public EdgeWithBuilder<V, E> dataWith(E value) {
            checkChangeAllowed();
            return new EdgeWithBuilderImpl(source, target, value);
        }
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    private class EdgeWithBuilderImpl implements EdgeWithBuilder<V, E> {

        private final Vertex<V, E> source;
        private final Vertex<V, E> target;
        private E value;

        @Override
        public GraphBuilder<V, E> with(Consumer<Edge<V, E>> callback) {
            checkChangeAllowed();
            Edge<V, E> edge = createEdge(source, target, value);
            callback.accept(edge);
            return GraphBuilderImpl.this;
        }
    }
}
