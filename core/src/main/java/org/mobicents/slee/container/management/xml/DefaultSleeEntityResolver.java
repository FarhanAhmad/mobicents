/*
 * The Open SLEE project.
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.management.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

import org.jboss.logging.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * The class is used as a Default Entity Resolver when parsing slee XML files
 * such as deployment descriptors for example.
 * @author Emil Ivov
 * @author Tim Fox - refactored to use ClassLoader of slee
 */
public class DefaultSleeEntityResolver implements EntityResolver{

    private Hashtable resources = null;
    
    private static DefaultSleeEntityResolver instance;
    private static Logger log = Logger.getLogger(DefaultSleeEntityResolver.class);
    
    private ClassLoader sleeClassLoader;
    
    public static void init(ClassLoader sleeClassLoader) {
        instance = new DefaultSleeEntityResolver(sleeClassLoader);
    }
    
    private DefaultSleeEntityResolver(ClassLoader sleeClassLoader) {
        this.sleeClassLoader = sleeClassLoader;
        
        resources = new Hashtable();
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.0//EN", "dtd/slee-deployable-unit_1_0.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN", "dtd/slee-sbb-jar_1_0.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.0//EN", "dtd/slee-service_1_0.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.0//EN", "dtd/slee-resource-adaptor-type-jar_1_0.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.0//EN", "dtd/slee-resource-adaptor-jar_1_0.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.0//EN", "dtd/slee-profile-spec-jar_1_0.dtd");
        registerResource("-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.0//EN", "dtd/slee-event-jar_1_0.dtd");
    }
    
   
    /**
     * Returns a singleton instance of the resolver.
     * @return a singleton instance of the resolver.
     */
    static DefaultSleeEntityResolver getInstance()
    {
        if (instance == null) throw new IllegalStateException("First create instance with slee classloader");
        return instance;
    }

    /**
     * Adds a URL to the specified resource (as returned by the system classloader).
     * to the resource table of the resolver.
     * @param publicID the public id of the resource
     * @param resourceName the path (starting from a location in the class path)
     * and name of the dtd that should be used by the resolver for documents
     * with the specified public id.
     */
    private void registerResource(String publicID, String resourceName)
    {
        URL url = this.sleeClassLoader.getResource(resourceName);
        if (url != null) {
            resources.put(publicID, url);
        }
        else {
            //All the slee dtds should be packaged locally in sar of slee itself
            throw new IllegalStateException("Cannot find resource:" + resourceName);
        }
    }

    /**
     * Creates an InputSource with a SystemID corresponding to a local dtd file.
     * @param publicId The public identifier of the external entity
     *        being referenced, or null if none was supplied.
     * @param systemId The system identifier of the external entity
     *        being referenced (This is a dummy parameter and is overridden by
     *        the resource names earlier specified by the
     *        <code>registrerResource</code>) method to correspond to the publicID.
     * @return An InputSource object describing the new input source,
     *         or null to request that the parser open a regular
     *         URI connection to the system identifier.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @exception java.io.IOException A Java-specific IO exception,
     *            possibly the result of creating a new InputStream
     *            or Reader for the InputSource.
     */
    public InputSource resolveEntity(String publicId, String systemId)
        throws IOException
    {
        URL resourceURL = (URL)resources.get(publicId);
        if(resourceURL != null)
        {
            InputStream resourceStream = null;
            resourceStream = resourceURL.openStream();
            InputSource is = new InputSource(resourceStream);
            is.setPublicId(publicId);
            is.setSystemId(resourceURL.toExternalForm());
            return is;
        }
        return null;
    }

}
