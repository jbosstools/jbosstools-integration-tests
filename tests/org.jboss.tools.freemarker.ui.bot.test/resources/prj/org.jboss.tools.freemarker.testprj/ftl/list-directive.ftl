<#assign users=["Alan", "Benjamin", "Cecile"]>
<#list users>
	<#items as user>
<p>${user}</p>
  	</#items>
</#list>
<#assign empty=[]>
<#list empty>
	<#items as user>
<p>${user}</p>
  	</#items>
<#else>
<p>No users</p>
</#list>