/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mobicents.servlet.sip.startup.jboss;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletContext;

import org.apache.catalina.Host;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.core.StandardWrapper;
import org.apache.catalina.startup.Constants;
import org.apache.catalina.startup.DigesterFactory;
import org.apache.catalina.startup.ExpandWar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.naming.resources.FileDirContext;
import org.apache.tomcat.util.digester.Digester;
import org.jboss.web.tomcat.security.config.JBossContextConfig;
import org.mobicents.servlet.sip.annotations.ClassFileScanner;
import org.mobicents.servlet.sip.startup.SipContext;
import org.mobicents.servlet.sip.startup.SipContextConfig;
import org.mobicents.servlet.sip.startup.SipEntityResolver;
import org.mobicents.servlet.sip.startup.SipRuleSet;
import org.mobicents.servlet.sip.startup.SipStandardContext;
import org.mobicents.servlet.sip.startup.loading.SipServletImpl;
import org.xml.sax.EntityResolver;

/**
 * Startup event listener for a the <b>SipStandardContext</b> that configures
 * the properties of that Context, and the associated defined servlets.
 * It extends the JbossContextConfig to be able to load sip servlet applications.
 * 
 * @author Jean Deruelle
 *
 */
public class SipJBossContextConfig extends JBossContextConfig 
	implements LifecycleListener{

	private static transient Log logger = LogFactory
			.getLog(SipContextConfig.class);

	@Override
	public void lifecycleEvent(LifecycleEvent event) {
		// logger.info("got lifecycle event : " + event.getType());
		
		try {
			super.lifecycleEvent(event);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		// logger.info("lifecycle event handled");
	}

	@Override
	protected synchronized void start() {		
		if(context instanceof SipContext) {
			if(logger.isDebugEnabled()) {
				logger.debug("starting sipContextConfig");
			}
			ServletContext servletContext = context.getServletContext();
			// calling start on the parent to initialize web resources of the web
			// app if any. That mean that this is a converged application.
			InputStream webXmlInputStream = servletContext
					.getResourceAsStream(Constants.ApplicationWebXml);
			if(logger.isDebugEnabled()) {
				logger.debug(Constants.ApplicationWebXml + " has been found, calling super.start() !");
			}
			context.setWrapperClass(StandardWrapper.class.getName());						
			if (webXmlInputStream != null) {
				super.start();
			}				
			InputStream sipXmlInputStream = servletContext
					.getResourceAsStream(SipContext.APPLICATION_SIP_XML);
			// processing of the sip.xml file
			if (sipXmlInputStream != null) {
				if(logger.isDebugEnabled()) {
					logger.debug(SipContext.APPLICATION_SIP_XML + " has been found !");
				}
				context.setWrapperClass(SipServletImpl.class.getName());
				//annotations scanning
				SipStandardContext sipctx = (SipStandardContext) context;
				ClassFileScanner scanner = new ClassFileScanner(sipctx.getJbossBasePath() + "/WEB-INF/classes/", sipctx);
				scanner.scan();
				//
				Digester sipDigester =  DigesterFactory.newDigester(xmlValidation,
	                    xmlNamespaceAware,
	                    new SipRuleSet());
				EntityResolver entityResolver = new SipEntityResolver();
				sipDigester.setValidating(false);		
				sipDigester.setEntityResolver(entityResolver);				
				//push the context to the digester
				sipDigester.push(context);
				sipDigester.setClassLoader(context.getClass().getClassLoader());
				//parse the sip.xml and populate the context with it
				try {
					sipDigester.resolveEntity(null, null);
					sipDigester.parse(sipXmlInputStream);
				} catch (Throwable e) {
					logger.error("Impossible to parse the sip deployment descriptor",
							e);
					ok = false;
				}
				// Use description from the annotations no matter if sip.xml parsing failed. TODO: making sense?
				if(scanner.isApplicationParsed()) { 
					ok = true;
				}
			} else {
				logger.info(SipContext.APPLICATION_SIP_XML + " has not been found !");
				ok = false;
			}			
			// Make our application available if no problems were encountered
			if (ok) {
				if(logger.isDebugEnabled()) {
					logger.debug("sipContextConfig started");
				}
				context.setConfigured(true);						
			} else {
				logger.warn("contextConfig.unavailable");
				context.setAvailable(false);
			}			
		} else {
			super.start();
		}				
	}

	@Override
	protected synchronized void stop() {
		if(logger.isDebugEnabled()) {
			logger.debug("stopping sipContextConfig");
		}
		super.stop();
		if(logger.isDebugEnabled()) {
			logger.debug("sipContextConfig stopped");
		}
	}

	/**
	 * Adjust docBase.
	 */
	protected void fixDocBase() throws IOException {
		if(context instanceof SipContext) {
			Host host = (Host) context.getParent();
			String appBase = host.getAppBase();
			boolean unpackWARs = true;
			if (host instanceof StandardHost) {
				unpackWARs = ((StandardHost) host).isUnpackWARs()
						&& ((StandardContext) context).getUnpackWAR();
			}
			File canonicalAppBase = new File(appBase);
			if (canonicalAppBase.isAbsolute()) {
				canonicalAppBase = canonicalAppBase.getCanonicalFile();
			} else {
				canonicalAppBase = new File(System.getProperty("catalina.base"),
						appBase).getCanonicalFile();
			}
			String docBase = context.getDocBase();
			if (docBase == null) {
				// Trying to guess the docBase according to the path
				String path = context.getPath();
				if (path == null) {
					return;
				}
				if (path.equals("")) {
					docBase = "ROOT";
				} else {
					if (path.startsWith("/")) {
						docBase = path.substring(1);
					} else {
						docBase = path;
					}
				}
			}
			File file = new File(docBase);
			if (!file.isAbsolute()) {
				docBase = (new File(canonicalAppBase, docBase)).getPath();
			} else {
				docBase = file.getCanonicalPath();
			}
			file = new File(docBase);
			String origDocBase = docBase;
			if ((docBase.toLowerCase().endsWith(".sar") || docBase.toLowerCase()
					.endsWith(".war"))
					&& !file.isDirectory() && unpackWARs) {
				URL war = new URL("jar:" + (new File(docBase)).toURL() + "!/");
				String contextPath = context.getPath();
				if (contextPath.equals("")) {
					contextPath = "ROOT";
				}
				docBase = ExpandWar.expand(host, war, contextPath);
				file = new File(docBase);
				docBase = file.getCanonicalPath();
				if (context instanceof SipStandardContext) {
					FileDirContext fileDirContext =new FileDirContext();
					fileDirContext.setDocBase(docBase);
	                ((SipStandardContext) context).setResources(fileDirContext );
	            }
			} else {
				File docDir = new File(docBase);
				if (!docDir.exists()) {
					String[] extensions = new String[] { ".sar", ".war" };
					for (String extension : extensions) {
						File archiveFile = new File(docBase + extension);
						if (archiveFile.exists()) {
							if (unpackWARs) {
								URL war = new URL("jar:" + archiveFile.toURL()
										+ "!/");
								docBase = ExpandWar.expand(host, war, context
										.getPath());
								file = new File(docBase);
								docBase = file.getCanonicalPath();
							} else {
								docBase = archiveFile.getCanonicalPath();
							}
	
							break;
						}
					}
					if (context instanceof SipContext) {
						FileDirContext fileDirContext =new FileDirContext();
						fileDirContext.setDocBase(docBase);
		                ((SipContext) context).setResources(fileDirContext );
	                }
				}
			}
			if (docBase.startsWith(canonicalAppBase.getPath())) {
				docBase = docBase.substring(canonicalAppBase.getPath().length());
				docBase = docBase.replace(File.separatorChar, '/');
				if (docBase.startsWith("/")) {
					docBase = docBase.substring(1);
				}
			} else {
				docBase = docBase.replace(File.separatorChar, '/');
			}
			context.setDocBase(docBase);
		} else {
			super.fixDocBase();
		}
	}

}
