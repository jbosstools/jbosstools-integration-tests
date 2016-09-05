<#assign x=1>
<#if x == 1>
x=1
<#else>
x!=1
</#if>
<#assign x=2 y=1>
<#if x == 1>
x=1
<#elseif y=1>
x!=1, y=1
<#else>
0
</#if>
