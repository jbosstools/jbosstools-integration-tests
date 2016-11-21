${1.2}
<#setting locale="cs">
${1.2}
<#assign x=true>
<#assign y=false>
${x?c}, ${y?c}
<#setting boolean_format="Y,N">
${x?boolean}, ${y?boolean}