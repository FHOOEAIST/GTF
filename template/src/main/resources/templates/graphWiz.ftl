<#-- @ftlvariable name="graph" type="science.aist.gtf.graph.Graph" -->
digraph G {
  <#list graph.getVertices() as vertex>
    ${vertex.element}[label="${vertex.element}"];
  </#list>

  <#list graph.edges as edge>
    ${edge.source.element}->${edge.target.element};
  </#list>
}
