Quickstart Examples - Scope
===========================

This example focuses on the Scope activity: its uses, parts and syntax. Scopes
provide a way to organize the activities within a BPEL process. They also
provide context for variables, fault handling, compensation, event handling
and correlation sets.

Three possible messages can be used to trigger the process. These are:
    - ok.xml         - demonstrates a standard process execution path
    - fail.xml       - demonstrates throw and rethrow activities and a fault handler
    - compensate.xml - demonstrates scope compensation.

To deploy the example, open a command line window in the example's folder,
and simply type 'ant deploy'. To undeploy, use the command 'ant undeploy'.

To test the example, for example using the SOAPUI client, an example 'ok',
'fail' and 'compensate' message can be found in the messages sub-folder, with the
relevant WSDL being located in the bpel sub-folder.

Alternatively, use the 'ant sendok', 'ant sendfail' or 'ant sendcompensate' commands
(from a command line window) to send either message.

NOTE: The "ant deploy" command will by default deploy version 1 of the
example to the server. If you wish to change the example, and redeploy,
then you will need to update the version number in the build.xml, or
override the version property, e.g. "ant -Dversion=2 deploy" (and similarly
when undeploying "ant -Dversion=2 undeploy").

Some handy URLs:
http://localhost:8080/bpel-console is the BPEL console
http://localhost:8080/Quickstart_bpel_scope_pickWS?wsdl is the URL to the BPEL process' WSDL
