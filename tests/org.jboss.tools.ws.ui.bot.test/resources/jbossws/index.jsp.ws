<%@page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@page import="test.ws.EchoService"%>
<%@page import="test.ws.Echo"%>
<%@page import="test.ws.Person"%>
<html>
 <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Web service client...</title>
 </head>
 <body>
  <p>operation "test()":
   <%=personToString(getPort().test())%></p>
  <p>operation "echo(Person("Homer", 44)":
   <%=personToString(getPort().echo(getHomer()))%></p>
 </body>
</html>
 
<%!
    private Echo getPort() {
        EchoService service1 = new EchoService();
        return service1.getEchoPort();
    }
 
    private String personToString(Person p) {
        return p.getLogin() + "(age: " + p.getAge() + ")";
    }
 
    private Person getHomer() {
        Person homer = new Person();
        homer.setLogin("Homer");
        homer.setAge(44);
        return homer;
    }
%>
