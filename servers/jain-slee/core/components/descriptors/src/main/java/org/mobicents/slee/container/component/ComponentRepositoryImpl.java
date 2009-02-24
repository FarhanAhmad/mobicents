package org.mobicents.slee.container.component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedComponentException;
import javax.slee.management.AlreadyDeployedException;
import javax.slee.management.DeploymentMBean;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * Implementation of the repository to manage all SLEE components.
 * 
 * @author martins
 * 
 */
public class ComponentRepositoryImpl implements ComponentRepository {

	/**
	 * the {@link EventTypeComponent}s stored in the repository
	 */
	private final ConcurrentHashMap<EventTypeID, EventTypeComponent> eventTypeComponents = new ConcurrentHashMap<EventTypeID, EventTypeComponent>();

	/**
	 * the {@link ProfileSpecificationComponent}s stored in the repository
	 */
	private final ConcurrentHashMap<ProfileSpecificationID, ProfileSpecificationComponent> profileSpecificationComponents = new ConcurrentHashMap<ProfileSpecificationID, ProfileSpecificationComponent>();

	/**
	 * the {@link LibraryComponent}s stored in the repository
	 */
	private final ConcurrentHashMap<LibraryID, LibraryComponent> libraryComponents = new ConcurrentHashMap<LibraryID, LibraryComponent>();

	/**
	 * the {@link ResourceAdaptorTypeComponent}s stored in the repository
	 */
	private final ConcurrentHashMap<ResourceAdaptorID, ResourceAdaptorComponent> resourceAdaptorComponents = new ConcurrentHashMap<ResourceAdaptorID, ResourceAdaptorComponent>();

	/**
	 * the {@link ResourceAdaptorTypeComponent}s stored in the repository
	 */
	private final ConcurrentHashMap<ResourceAdaptorTypeID, ResourceAdaptorTypeComponent> resourceAdaptorTypeComponents = new ConcurrentHashMap<ResourceAdaptorTypeID, ResourceAdaptorTypeComponent>();

	/**
	 * the {@link SbbComponent}s stored in the repository
	 */
	private final ConcurrentHashMap<SbbID, SbbComponent> sbbComponents = new ConcurrentHashMap<SbbID, SbbComponent>();

	/**
	 * the {@link ServiceComponent}s stored in the repository
	 */
	private final ConcurrentHashMap<ServiceID, ServiceComponent> serviceComponents = new ConcurrentHashMap<ServiceID, ServiceComponent>();

	// ----

	public EventTypeComponent getComponentByID(EventTypeID id) {
		return eventTypeComponents.get(id);
	}

	public ProfileSpecificationComponent getComponentByID(
			ProfileSpecificationID id) {
		return profileSpecificationComponents.get(id);
	}

	public LibraryComponent getComponentByID(LibraryID id) {
		return libraryComponents.get(id);
	}

	public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id) {
		return resourceAdaptorComponents.get(id);
	}

	public ResourceAdaptorTypeComponent getComponentByID(
			ResourceAdaptorTypeID id) {
		return resourceAdaptorTypeComponents.get(id);
	}

	public SbbComponent getComponentByID(SbbID id) {
		return sbbComponents.get(id);
	}

	public ServiceComponent getComponentByID(ServiceID id) {
		return serviceComponents.get(id);
	}

	// ----

	public Set<EventTypeID> getEventComponentIDs() {
		return eventTypeComponents.keySet();
	}

	public Set<ProfileSpecificationID> getProfileSpecificationIDs() {
		return profileSpecificationComponents.keySet();
	}

	public Set<LibraryID> getLibraryIDs() {
		return libraryComponents.keySet();
	}

	public Set<ResourceAdaptorID> getResourceAdaptorIDs() {
		return resourceAdaptorComponents.keySet();
	}

	public Set<ResourceAdaptorTypeID> getResourceAdaptorTypeIDs() {
		return resourceAdaptorTypeComponents.keySet();
	}

	public Set<SbbID> getSbbIDs() {
		return sbbComponents.keySet();
	}

	public Set<ServiceID> getServiceIDs() {
		return serviceComponents.keySet();
	}

	public boolean isInstalled(ComponentID componentID) {

		SleeComponent component = null;
		if (componentID instanceof EventTypeID) {
			component = getComponentByID((EventTypeID) componentID);
		} else if (componentID instanceof LibraryID) {
			component = getComponentByID((LibraryID) componentID);
		} else if (componentID instanceof ProfileSpecificationID) {
			component = getComponentByID((ProfileSpecificationID) componentID);
		} else if (componentID instanceof ResourceAdaptorID) {
			component = getComponentByID((ResourceAdaptorID) componentID);
		} else if (componentID instanceof ResourceAdaptorTypeID) {
			component = getComponentByID((ResourceAdaptorTypeID) componentID);
		} else if (componentID instanceof SbbID) {
			component = getComponentByID((SbbID) componentID);
		} else if (componentID instanceof ServiceID) {
			component = getComponentByID((ServiceID) componentID);
		}

		return component != null;
	}

	/**
	 * @see DeploymentMBean#getReferringComponents(ComponentID)
	 * @param componentID
	 * @return
	 */
	public ComponentID[] getReferringComponents(ComponentID componentID)
			throws NullPointerException, UnrecognizedComponentException {
		if (componentID == null) {
			throw new NullPointerException("null componentID");
		} else {
			SleeComponent component = null;
			if (componentID instanceof EventTypeID) {
				component = getComponentByID((EventTypeID) componentID);
			} else if (componentID instanceof LibraryID) {
				component = getComponentByID((LibraryID) componentID);
			} else if (componentID instanceof ProfileSpecificationID) {
				component = getComponentByID((ProfileSpecificationID) componentID);
			} else if (componentID instanceof ResourceAdaptorID) {
				component = getComponentByID((ResourceAdaptorID) componentID);
			} else if (componentID instanceof ResourceAdaptorTypeID) {
				component = getComponentByID((ResourceAdaptorTypeID) componentID);
			} else if (componentID instanceof SbbID) {
				component = getComponentByID((SbbID) componentID);
			} else if (componentID instanceof ServiceID) {
				component = getComponentByID((ServiceID) componentID);
			}

			if (component == null) {
				throw new UnrecognizedComponentException(componentID.toString());
			} else {
				Set<SleeComponent> referringComponents = getReferringComponents(component);
				ComponentID[] result = new ComponentID[referringComponents.size()];
				int i = 0;
				for (SleeComponent referringComponent : referringComponents) {
					result[i] = referringComponent.getComponentID();
					i++;
				}
				return result;
			}
		}
	}

	/**
	 * Retrieves the component ids for components that refers the specified component
	 */
	public Set<SleeComponent> getReferringComponents(SleeComponent component) {

		Set<SleeComponent> result = new HashSet<SleeComponent>();
		for (EventTypeComponent otherComponent : eventTypeComponents.values()) {
			if (!otherComponent.getComponentID().equals(
					component.getComponentID())) {
				if (otherComponent.getDependenciesSet().contains(
						component.getComponentID())) {
					result.add(otherComponent);
				}
			}
		}
		for (LibraryComponent otherComponent : libraryComponents.values()) {
			if (!otherComponent.getComponentID().equals(
					component.getComponentID())) {
				if (otherComponent.getDependenciesSet().contains(
						component.getComponentID())) {
					result.add(otherComponent);
				}
			}
		}
		for (ProfileSpecificationComponent otherComponent : profileSpecificationComponents
				.values()) {
			if (!otherComponent.getComponentID().equals(
					component.getComponentID())) {
				if (otherComponent.getDependenciesSet().contains(
						component.getComponentID())) {
					result.add(otherComponent);
				}
			}
		}
		for (ResourceAdaptorComponent otherComponent : resourceAdaptorComponents
				.values()) {
			if (!otherComponent.getComponentID().equals(
					component.getComponentID())) {
				if (otherComponent.getDependenciesSet().contains(
						component.getComponentID())) {
					result.add(otherComponent);
				}
			}
		}
		for (ResourceAdaptorTypeComponent otherComponent : resourceAdaptorTypeComponents
				.values()) {
			if (!otherComponent.getComponentID().equals(
					component.getComponentID())) {
				if (otherComponent.getDependenciesSet().contains(
						component.getComponentID())) {
					result.add(otherComponent);
				}
			}
		}
		for (SbbComponent otherComponent : sbbComponents.values()) {
			if (!otherComponent.getComponentID().equals(
					component.getComponentID())) {
				if (otherComponent.getDependenciesSet().contains(
						component.getComponentID())) {
					result.add(otherComponent);
				}
			}
		}
		for (ServiceComponent otherComponent : serviceComponents.values()) {
			if (!otherComponent.getComponentID().equals(
					component.getComponentID())) {
				if (otherComponent.getDependenciesSet().contains(
						component.getComponentID())) {
					result.add(otherComponent);
				}
			}
		}
		return result;
	}

	// ----

	public void putComponent(EventTypeComponent component)
			throws AlreadyDeployedException {
		if (eventTypeComponents.putIfAbsent(component.getEventTypeID(),
				component) != null) {
			throw new AlreadyDeployedException("the component "
					+ component.getComponentID() + " is already deployed");
		}
	}

	public boolean putComponent(ProfileSpecificationComponent component)
			throws AlreadyDeployedException {
		return profileSpecificationComponents.putIfAbsent(component
				.getProfileSpecificationID(), component) == null;
	}

	public boolean putComponent(LibraryComponent component)
			throws AlreadyDeployedException {
		return libraryComponents.putIfAbsent(component.getLibraryID(),
				component) == null;
	}

	public boolean putComponent(ResourceAdaptorComponent component)
			throws AlreadyDeployedException {
		return resourceAdaptorComponents.putIfAbsent(component
				.getResourceAdaptorID(), component) == null;
	}

	public boolean putComponent(ResourceAdaptorTypeComponent component) {
		return resourceAdaptorTypeComponents.putIfAbsent(component
				.getResourceAdaptorTypeID(), component) == null;
	}

	public boolean putComponent(SbbComponent component) {
		return sbbComponents.putIfAbsent(component.getSbbID(), component) == null;
	}

	public boolean putComponent(ServiceComponent component) {
		return serviceComponents.putIfAbsent(component.getServiceID(),
				component) == null;
	}

	// ----

	public void removeComponent(EventTypeID componentID) {
		eventTypeComponents.remove(componentID);
	}

	public void removeComponent(ProfileSpecificationID componentID) {
		profileSpecificationComponents.remove(componentID);
	}

	public void removeComponent(LibraryID componentID) {
		libraryComponents.remove(componentID);
	}

	public void removeComponent(ResourceAdaptorID componentID) {
		resourceAdaptorComponents.remove(componentID);
	}

	public void removeComponent(ResourceAdaptorTypeID componentID) {
		resourceAdaptorTypeComponents.remove(componentID);
	}

	public void removeComponent(SbbID componentID) {
		sbbComponents.remove(componentID);
	}

	public void removeComponent(ServiceID componentID) {
		serviceComponents.remove(componentID);
	}
}