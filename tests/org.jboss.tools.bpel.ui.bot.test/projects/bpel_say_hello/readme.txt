Quickstart Examples - Level 1 - Say Hello
============================================

This example is simmilar to Hello World examples but uses input data
to influence the output.

The user calls the sayHello operation and provides his name. The process
replys with 'Hello <name>'.

To deploy the example, open a command line window in the example's folder,
and simply type 'ant deploy'. To undeploy, use the command 'ant undeploy'.

To test the example, for example using the SOAPUI client, an example
'sayHello' message can be found in the messages sub-folder, with the
relevant WSDL being located in the bpel sub-folder.

Alternatively, use the 'ant sayhello' command (from a command line window)
to send the message.

NOTE: The "ant deploy" command will by default deploy version 1 of the
example to the server. If you wish to change the example, and redeploy,
then you will need to update the version number in the build.xml, or
override the version property, e.g. "ant -Dversion=2 deploy" (and similarly
when undeploying "ant -Dversion=2 undeploy").

Some handy URLs:
http://localhost:8080/bpel-console is the BPEL console
http://localhost:8080/SayHelloProcess?wsdl is the URL to the BPEL process' WSDL
