# GTF - Graph Transformation Framework

[![javadoc](https://javadoc.io/badge2/science.aist.gtf/gtf/javadoc.svg)](https://javadoc.io/doc/science.aist.gtf/gtf) 
[![Maven Central](https://img.shields.io/maven-central/v/science.aist.gtf/gtf.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:"science.aist.gtf")
[![GitHub release](https://img.shields.io/github/v/release/fhooeaist/gtf.svg)](https://github.com/fhooeaist/gtf/releases)
[![License: MPL 2.0](https://img.shields.io/badge/License-MPL%202.0-brightgreen.svg)](https://opensource.org/licenses/MPL-2.0)
[![DOI](https://zenodo.org/badge/303987926.svg)](https://zenodo.org/badge/latestdoi/303987926)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FFHOOEAIST%2FGTF.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2FFHOOEAIST%2FGTF?ref=badge_shield)

The Graph Transformation Framework is as the name suggests, all about graphs and their transformation into desired
target models. It provides different modules, like ...
 - **graph**: This module contains all the domain objects, that are required to represent a graph and its state.
 - **template**: This module contains classes for various templating and automation tasks.
 - **transformation**: This module allows creating transformations on graph structures.
 - **verification**: With this module a verification on graph models can be performed.
 - **visualization**: This module contains classes for visualization using Freemarker template engine. 

## Getting Started

There is no general getting started, as it strongly depends on what you want to achieve with the framework. The next few
sections provide some examples for the different modules. For more details you can checkout our test classes, which 
deal with a few different examples and problems.

### Graph

If you want to use the graph structure itself, you have to add the following maven dependency to your project:

```xml
<dependency>
    <groupId>science.aist.gtf</groupId>
    <artifactId>graph</artifactId>
    <version>${gtf.version}</version> <!-- e.g. 1.0.0 -->
</dependency>
```

Using graph builder offers you an intuitive, declarative way to define and create your graph, which is shown in the 
following example.

```java
@lombok.AllArgsConstructor
class Person {
    @lombok.Getter
  int id;
  String name;
}

Function<Person, Object> keyExtractor = Person::getId;
Graph<Person, String> graph = GraphBuilderImpl.<Person, String>create(keyExtractor)
   .from(new Person(1, "Max Mustermann")).toData(new Person(2, "Maxine Musterfrau")).data("married")
   .from(new Person(3, "John Smith")).toDataByKey(2).data("friends")
   .fromByKey(3).toByKey(2)
   .toGraph();
```

### Template

```xml
<dependency>
    <groupId>science.aist.gtf</groupId>
    <artifactId>template</artifactId>
    <version>${gtf.version}</version> <!-- e.g. 1.0.0 -->
</dependency>
```

For a more thorough description of how to use templates refer to 
[template documentation](https://fhooeaist.github.io/GTF/template.html).

### Transformation

```xml
<dependency>
    <groupId>science.aist.gtf</groupId>
    <artifactId>transformation</artifactId>
    <version>${gtf.version}</version> <!-- e.g. 1.0.0 -->
</dependency>
```

The transformation module basically consists of interface and abstract helper classes that allow to transform any input
into any output. The base concept consists of Transformers and Renderers, where a Transformer is used to transform the 
whole input into an output whereas the renderers are applied on small parts of the given input. A renderer can have a condition
which allows to use a list of renderers with their condition inside MultiRenderers. This form of abstraction allows for 
a clean separation of concerns.


Example (Note: this example only shows the interaction of a single component).

```java
RendererCondition<String> upperCaseCondition = () -> x -> x.equals("a");
TransformationRender<Optional<String>, String, Graph<String, Void>, String> uppercaseTransformer =
        new AbstractConditionalTransformationRenderer<>(upperCaseCondition) {
            @Override
            public String createElement() {
                return null; // not needed in this case can be used to create the resulting element
            }

            @Override
            public String mapProperties(String s, Graph<String, Void> vertices, String currentElement) {
                return currentElement.toUpperCase();
            }
        };
TransformationRender<Optional<String>, String, Graph<String, Void>, String> lowercaseTransformer = ...;
var renderer = new MultiTransformationRenderer<>(List.of(uppercaseTransformer, lowercaseTransformer));

Graph<String, Void> graph = ...;
Transformer<Graph<String, Void>, List<String>> transformer = g -> {
    List<String> res = new ArrayList<>();
    g.traverseVertices(vv -> res.add(renderer.renderElement(g, vv.getElement())));
    return res;
};
List<String> result = transformer.applyTransformation(graph);
```

In addition to that transformation can be split into multiple parts and then joined again.

```java
Transformer<Input, Interim> t1 = ...;
Transformer<Interim, Output> t2 = ...;
Transformer<Input, Output> finalTransformation = t1.andThen(t2);
```

### Verification

```xml
<dependency>
    <groupId>science.aist.gtf</groupId>
    <artifactId>verification</artifactId>
    <version>${gtf.version}</version> <!-- e.g. 1.0.0 -->
</dependency>
```

Different approaches for verification are implemented in the verification module. You may checkout the test classes to 
get an overview over the functionality.

### Visualization

 ```xml
 <dependency>
     <groupId>science.aist.gtf</groupId>
     <artifactId>visualization</artifactId>
     <version>${gtf.version}</version> <!-- e.g. 1.0.0 -->
 </dependency>
 ```

The visualization module is based on the template engine and basically provides different implementation for the
TemplateEngine<T> interface. As an example the graph transformation framework provides a freemarker-based approach.

## FAQ

If you have any questions, please checkout our [FAQ](https://fhooeaist.github.io/GTF/faq.html) section.

## Contributing

**First make sure to read our [general contribution guidelines](https://fhooeaist.github.io/CONTRIBUTING.html).**
   
## Licence

Copyright (c) 2020 the original author or authors.
DO NOT ALTER OR REMOVE COPYRIGHT NOTICES.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FFHOOEAIST%2FGTF.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2FFHOOEAIST%2FGTF?ref=badge_large)

## Research

If you are going to use this project as part of a research paper, we would ask you to reference this project by citing
it. 

[![DOI](https://zenodo.org/badge/303987926.svg)](https://zenodo.org/badge/latestdoi/303987926)

If you are going to use parts of the verification module, we would ask you to reference:

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.4450044.svg)](https://doi.org/10.5281/zenodo.4450044)
