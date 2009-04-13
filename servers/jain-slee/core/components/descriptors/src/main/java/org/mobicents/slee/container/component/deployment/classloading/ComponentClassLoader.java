package org.mobicents.slee.container.component.deployment.classloading;

import javax.slee.ComponentID;

/**
 * The SLEE component class loader implementation.
 * 
 * A component needs to have it's own class loader due to unique JNDI context
 * but in reality it just delegates to the related component jar class loader.
 * 
 * @author martins
 * 
 */
public class ComponentClassLoader extends ClassLoader {

	/**
	 * the component id, used to make this class loader unique
	 */
	private final ComponentID componentID;
	
	public ComponentClassLoader(ComponentID componentID, URLClassLoaderDomain parent) {
		super(parent);		
		this.componentID = componentID;
	}

	@Override
	public String toString() {
		return "ComponentClassLoader[ componentID = " + componentID	+ " ]";
	}

}
