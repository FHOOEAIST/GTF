<#-- @ftlvariable name="exception" type="java.util.Collection<science.aist.jack.data.Pair<java.lang.String, java.lang.Exception>>" -->
# Implementation Report

## first groupings
<#assign lastKey = "">
<#list exception?sort_by("first") as pair>
    <#if lastKey != pair.first>
        ### Key ${pair.first}
        | Key | Exception |
        | --- | --------- |
    </#if>
    | ${source(pair.second.getStackTrace())} | <@stacktrace pair.second.getStackTrace() /> | <#assign lastKey = pair.first>
</#list>

## LOCATION groupings
<#assign lastSource = "">
<#list exception as pair>
    <#assign src = source(pair.second.getStackTrace()) />
    <#if !lastSource?contains(src)>
        <#assign lastSource = lastSource + src>
        ### Location ${src}
        | Key | Exception |
        | --- | --------- |
        <#list exception as subPair>
            <#assign subSrc = source(subPair.second.getStackTrace()) />
            <#if src == subSrc>
                | ${subPair.first} | <@stacktrace subPair.second.getStackTrace() /> |
            </#if>
        </#list>
    </#if>
</#list>

## EXCEPTION groupings
<#assign lastException = "">
<#list exception as pair>
    <#assign exc = pair.second.class.name />
    <#if !lastException?contains(exc)>
        <#assign lastException = lastException + exc>
        ### Exception ${exc}
        | Key | Source | Exception |
        | --- | ------ | --------- |
        <#list exception as subPair>
            <#assign subExc = subPair.second.class.name />
            <#if exc == subExc>
                | ${subPair.first} | ${source(subPair.second.getStackTrace())} /> | <@stacktrace subPair.second.getStackTrace() /> |
            </#if>
        </#list>
    </#if>
</#list>

<#function source trace><#return trace[0][trace[0]?index_of("(")+1..trace[0]?index_of(".java")-1] /></#function>
<#macro stacktrace trace><#list trace as line>${line}<br></#list></#macro>
