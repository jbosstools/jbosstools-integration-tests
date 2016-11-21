<#list users>
	<#items as user>
Full name: ${user.fullName}
Age: ${user.age}
Male: ${user.male?c}
Date of Birth: ${user.birthDate?string('YYYY.MM.DD')}
######
</#items>
</#list>