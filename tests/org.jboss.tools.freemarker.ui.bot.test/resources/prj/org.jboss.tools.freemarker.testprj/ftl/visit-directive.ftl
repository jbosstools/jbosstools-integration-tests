<#import "/lib/docbook.ftl" as docbook>

<#--
  We use the docbook library, but we override some handlers
  in this namespace.
-->
<#visit document using [.namespace, docbook]>


<#recurse>
<#--
  Override the "programlisting" handler, but only in the case if
  its "role" attribute is "java"
-->
<#macro programlisting>
  <#if .node.@role[0]!"" == "java">
    <#-- Do something special here... -->
    ...
  <#else>
    <#-- Just use the original (overidden) handler -->
    <#fallback>
  </#if>
</#macro>
<@programlisting/>