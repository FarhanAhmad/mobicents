---------< Mobicents SIP Presence Service >----------

This module is a full SIP Presence Service, including servers:

- XDMS (XML Document Management Server)
- PS (Presence Server)

----> REQUIREMENTS:

The XDMS and PS depends on the Mobicents Http-Servlet and SIP11 RAs.
You need to deploy those RAs prior to the installation of  
the servers. 
File dependencies.xml can deploy/undeploy those components, use:

* "ant -f dependencies.xml deploy" to hard deploy SIP11 and HTTP-SERVLET RAs to Mobicents AS
* "ant -f dependencies.xml deploy-jmx" to deploy SIP11 and HTTP-SERVLET RAs to Mobicents AS, via JMX
* "ant -f dependencies.xml undeploy" to hard undeploy SIP11 and HTTP-SERVLET RAs from Mobicents AS
* "ant -f dependencies.xml undeploy-jmx" to undeploy SIP11 and HTTP-SERVLET RAs from Mobicents AS, via JMX

IMPORTANT, at the moment the XDM Server is not compatible with JDK 1.6!

----> HARD DEPLOY:

Option 1) For both servers integrated on same Mobicents do:

"ant integrated-deploy" on this directory

Option 2) For independent servers (in different Mobicents hosts):

- XDMS : do "ant xdms-deploy" on this directory
- PS : <not available yet>

----> HARD UNDEPLOY:

Option 1) For both servers integrated on same Mobicents do:

"ant integrated-undeploy" on this directory

Option 2) For independent servers:

- XDMS : do "ant xdms-undeploy" on this directory
- PS : <not available yet>

----> DEPLOY VIA JMX:

Option 1) For both servers integrated on same Mobicents do:

"ant integrated-deploy-jmx" on this directory

Option 2) For independent servers (in different Mobicents hosts):

- XDMS : do "ant xdms-deploy-jmx" on this directory
- PS : <not available yet>

----> UNDEPLOY VIA JMX:

Option 1) For both servers integrated on same Mobicents do:

"ant integrated-undeploy-jmx" on this directory

Option 2) For independent servers:

- XDMS : do "ant xdms-undeploy-jmx" on this directory
- PS : <not available yet>

NOTE: For management via JMX you can configure JNP host and port
in the build.xml script, through properties.

-----> JAIN SLEE "PRESENCE AWARE" APP EXAMPLES:

Inside "examples" directory you can find JAIN SLEE applications,
which take advantage of the integrated Mobicents SIP Presence
Service. For more information on each example look at the
readme.txt file inside the specific example's directory.
 
Author: Eduardo Martins, JBoss R&D