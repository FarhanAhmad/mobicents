/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.deployment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.Sbb;
import javax.slee.SbbLocalObject;
import javax.slee.management.DeploymentException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MEventEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetChildRelationMethod;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetProfileCMPMethod;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbAbstractClass;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbCMPField;
import org.mobicents.slee.container.component.sbb.AbstractSbbClassInfo;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.sbb.SbbAbstractMethodHandler;
import org.mobicents.slee.runtime.sbb.SbbConcrete;
import org.mobicents.slee.runtime.sbb.SbbLocalObjectImpl;
import org.mobicents.slee.runtime.sbb.SbbObjectState;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;

/**
 * Class generating the sbb concrete class from a sbb abstract class provided by
 * a sbb developer
 * 
 * 
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author Stefano Gamma
 * @author Eduardo Martins (v2)
 * 
 */
public class ConcreteSbbGenerator {

	/**
	 * the sbb component
	 */
	private final SbbComponent sbbComponent;

	/**
	 * the sbb abstract class used to generate the concrete class
	 */
	private CtClass sbbAbstractClass = null;

	/**
	 * the sbb concrete class to generate
	 */
	private CtClass sbbConcreteClass = null;

	/**
	 * Logger to logg information
	 */
	private static Logger logger = null;

	/**
	 * the sbb abstract methods used to generate the concrete class methods
	 */
	private Map abstractMethods = null;

	/**
	 * Generator of the concrete activity context interface in case the sbb
	 * developer specified a narrow method in th descriptor
	 */
	private ConcreteActivityContextInterfaceGenerator concreteActivityContextInterfaceGenerator = null;

	/**
	 * Pool to generate or read classes with javassist
	 */
	private ClassPool pool = null;

	private Map superClassesAbstractMethods;

	/**
	 * The path where classes will reside
	 */
	private final String deployDir;

	static {
		logger = Logger.getLogger(ConcreteSbbGenerator.class);
	}

	/**
	 * Constructor
	 */
	public ConcreteSbbGenerator(SbbComponent sbbComponent) {
		this.sbbComponent = sbbComponent;
		this.deployDir = sbbComponent.getDeploymentDir().getAbsolutePath();
		;
		this.pool = sbbComponent.getClassPool();
	}

	/**
	 * Generate the concrete sbb Class
	 * 
	 * @return the concrete sbb class
	 */
	public void generateConcreteSbb() throws DeploymentException {
		String sbbAbstractClassName = sbbComponent.getAbstractSbbClass()
				.getName();
		String sbbConcreteClassName = ConcreteClassGeneratorUtils
				.getSbbConcreteClassName(sbbAbstractClassName);

		sbbConcreteClass = pool.makeClass(sbbConcreteClassName);

		try {

			try {
				sbbAbstractClass = pool.get(sbbAbstractClassName);
			} catch (NotFoundException nfe) {
				throw new DeploymentException(nfe.getMessage(), nfe);
			}
			
			generateAbstractSbbClassInfo();
			
			try {
				ConcreteClassGeneratorUtils.createInterfaceLinks(
						sbbConcreteClass, new CtClass[] { pool
								.get(SbbConcrete.class.getName()) });
			} catch (NotFoundException nfe) {
				throw new DeploymentException(nfe.getMessage(), nfe);
			}

			ConcreteClassGeneratorUtils.createInheritanceLink(sbbConcreteClass,
					sbbAbstractClass);

			abstractMethods = ClassUtils
					.getAbstractMethodsFromClass(sbbAbstractClass);
			superClassesAbstractMethods = ClassUtils
					.getSuperClassesAbstractMethodsFromClass(sbbAbstractClass);
	
			try {
				createFields(new CtClass[] {
						pool.get(SbbEntity.class.getName()),
						pool.get(SbbObjectState.class.getName()) });

				CtClass[] parameters = new CtClass[] { pool.get(SbbEntity.class
						.getName()) };
				createSbbEntityGetterAndSetter(sbbConcreteClass);
				createDefaultUsageParameterGetter(sbbConcreteClass);
				createNamedUsageParameterGetter(sbbConcreteClass);
				createDefaultConstructor();
				createConstructorWithParameter(parameters);

			} catch (NotFoundException nfe) {
				logger.error("Constructor With Parameter not created");
				throw new DeploymentException("Constructor not created.", nfe);
			}
			
			MSbbAbstractClass mSbbAbstractClass = sbbComponent.getDescriptor()
					.getSbbClasses().getSbbAbstractClass();
			createCMPAccessors(mSbbAbstractClass.getCmpFields());
			createGetChildRelationsMethod(mSbbAbstractClass
					.getChildRelationMethods().values());
			createGetProfileCMPMethods(mSbbAbstractClass.getProfileCMPMethods()
					.values());
			createFireEventMethods(sbbComponent.getDescriptor()
					.getEventEntries().values());
			// GetUsageParametersMethod[] usageParameters=
			// sbbDeploymentDescriptor.getUsageParametersMethods();

			// if the activity context interface has been defined in the
			// descriptor
			// file
			// then generates the concrete class of the activity context
			// interface
			// and implements the narrow method
			
			if (sbbComponent.getDescriptor()
					.getSbbActivityContextInterface() != null) {
				Class activityContextInterfaceClass = null;
				try {
					activityContextInterfaceClass = Thread.currentThread()
							.getContextClassLoader().loadClass(
									sbbComponent.getDescriptor()
									.getSbbActivityContextInterface().getInterfaceName());
				} catch (ClassNotFoundException e2) {
					String s = "Error creating constructor -  class not found";
					logger.error(s, e2);
					throw new DeploymentException(s, e2);
				}
				// Check the activity context interface for illegal method
				// names.
				Method[] methods = activityContextInterfaceClass.getMethods();
				ArrayList<String> allSetters = new ArrayList<String>();
				ArrayList<String> missingSetters = new ArrayList<String>();
				if (methods != null) {
					for (int i = 0; i < methods.length; i++) {
						if (!methods[i].getDeclaringClass().getName().equals(
								"javax.slee.ActivityContextInterface")) {
							String methodName = methods[i].getName();
							// setters should have a single parameter and should
							// return void type.
							if (methodName.startsWith("set")) {
								Class[] args = methods[i].getParameterTypes();

								// setter should only have one argument
								if (args.length != 1)
									throw new DeploymentException(
											"Setter method '"
													+ methodName
													+ "' should only have one argument.");

								// setter return type should be void
								Class returnClass = methods[i].getReturnType();
								if (!returnClass.equals(Void.TYPE))
									throw new DeploymentException(
											"Setter method '"
													+ methodName
													+ "' return type should be void.");

								allSetters.add(methodName);
							} else if (methodName.startsWith("get")) {
								Class[] args = methods[i].getParameterTypes();

								// getter should have no parameters.
								if (args != null && args.length != 0)
									throw new DeploymentException(
											"Getter method '"
													+ methodName
													+ "' should have no parameters.");

								// getter return type should not be void
								if (methods[i].getReturnType()
										.equals(Void.TYPE))
									throw new DeploymentException(
											"Getter method '"
													+ methodName
													+ "' return type cannot be void.");

								String setterName = methodName.replaceFirst(
										"get", "set");

								try {
									activityContextInterfaceClass.getMethod(
											setterName, methods[i]
													.getReturnType());
								} catch (NoSuchMethodException nsme) {
									missingSetters.add(setterName);
								}
							} else {
								throw new DeploymentException(
										"Invalid method '"
												+ methodName
												+ "' in SBB Activity Context Interface.");
							}
						}

					}

					// Check if the missing setters aren't defined with
					// different arg
					for (String setter : missingSetters)
						if (allSetters.contains(setter))
							throw new DeploymentException(
									"Getter argument type and"
											+ " setter return type for attribute '"
											+ setter.replaceFirst("set", "")
													.toLowerCase()
											+ "' must be the same.");
				}
				/*
				 * CtMethod[] abstractClassMethods =
				 * sbbAbstractClass.getDeclaredMethods();
				 * 
				 * for ( int i = 0; i < abstractClassMethods.length; i ++ ) {
				 * CtMethod ctMethod = abstractClassMethods[i]; if ( !
				 * Modifier.isAbstract(ctMethod.getModifiers())) {
				 * this.createMethodWrapper(sbbConcreteClass,ctMethod); } }
				 */

				// check if the concrete class has already been generated.
				// if that the case, the guess is that the concrete class is a
				// safe
				// one
				// and so it is not generated again
				// avoid also problems of class already loaded from the class
				// loader
				//  
				CtClass activityContextInterface = null;
				try {
					activityContextInterface = pool
							.get(activityContextInterfaceClass.getName());

					createField(activityContextInterface,
							"sbbActivityContextInterface");

					this
							.createSetActivityContextInterfaceMethod(activityContextInterface);

					ConcreteActivityContextInterfaceGenerator concreteActivityContextInterfaceGenerator = new ConcreteActivityContextInterfaceGenerator(
							activityContextInterfaceClass.getName(), deployDir,
							pool);

					Class concreteActivityContextInterfaceClass = concreteActivityContextInterfaceGenerator
							.generateActivityContextInterfaceConcreteClass();

					createGetSbbActivityContextInterfaceMethod(
							activityContextInterface,
							concreteActivityContextInterfaceClass);
					// set the concrete activity context interface class in
					// the
					// descriptor
					if (logger.isDebugEnabled()) {
						logger.debug("SETTING ACI concrete class  "
								+ concreteActivityContextInterfaceClass
								+ " in " + sbbComponent);
					}
					sbbComponent
							.setActivityContextInterfaceConcreteClass(concreteActivityContextInterfaceClass);

				} catch (NotFoundException nfe) {
					logger
							.error("Narrow Activity context interface method and "
									+ "activity context interface concrete class not created");
					throw new DeploymentException(nfe.getMessage(), nfe);
				} finally {
					/*
					 * if (activityContextInterface != null) {
					 * activityContextInterface.detach(); }
					 */
				}

			}
			// if the sbb local object has been defined in the descriptor file
			// then generates the concrete class of the sbb local object
			// and implements the narrow method
			Class sbbLocalInterfaceClass = sbbComponent
					.getSbbLocalInterfaceClass();
			if (logger.isDebugEnabled()) {
				logger.debug("Sbb Local Object interface :"
						+ sbbLocalInterfaceClass);
			}
			if (sbbLocalInterfaceClass != null
					&& !sbbLocalInterfaceClass.getName().equals(
							"javax.slee.SbbLocalObject")) {
				// check if the concrete class has already been generated.
				// if that the case, the guess is that the concrete class is a
				// safe
				// one
				// and so it is not generated again
				// avoid also problems of class already loaded from the class
				// loader
				if (true /*
							 * !SbbDeployer.concreteClassesGenerated
							 * .contains(sbbAbstractClassName)
							 */) {
					try {
						CtClass sbbLocalInterface = pool
								.get(sbbLocalInterfaceClass.getName());
						ConcreteSbbLocalObjectGenerator concreteSbbLocalObjectGenerator = new ConcreteSbbLocalObjectGenerator(
								sbbLocalInterfaceClass.getName(),
								sbbAbstractClassName, this.deployDir, pool);
						Class concreteSbbLocalObjectClass = concreteSbbLocalObjectGenerator
								.generateSbbLocalObjectConcreteClass();
						// set the sbb Local object class in the descriptor
						sbbComponent
								.setSbbLocalInterfaceConcreteClass(concreteSbbLocalObjectClass);

					} catch (NotFoundException nfe) {
						String s = "sbb Local Object concrete class not created for interface "
								+ sbbLocalInterfaceClass.getName();
						throw new DeploymentException(s, nfe);
					}
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug(sbbLocalInterfaceClass.getName()
								+ " concrete class already "
								+ "generated. No generated a second time.");
					}
				}
			}
			// if there is no interface defined in the descriptor for sbb local
			// object
			// then the slee implementation is taken
			else {

				try {
					sbbComponent
							.setSbbLocalInterfaceClass(SbbLocalObject.class);
					sbbComponent
							.setSbbLocalInterfaceConcreteClass(SbbLocalObjectImpl.class);
				} catch (Exception e) {
					throw new DeploymentException(e.getMessage(), e);
				}
			}
			try {
				sbbConcreteClass.writeFile(deployDir);
				// @@2.4+ -> 3.4+
				// pool.writeFile(sbbConcreteClassName, deployPath);
				if (logger.isDebugEnabled()) {
					logger.debug("Concrete Class " + sbbConcreteClassName
							+ " generated in the following path " + deployDir);
				}
			} catch (Exception e) {
				String s = "Error generating concrete class";
				throw new DeploymentException(s, e);
			}

			Class clazz = null;
			try {
				clazz = Thread.currentThread().getContextClassLoader()
						.loadClass(sbbConcreteClassName);
			} catch (ClassNotFoundException e1) {
				String s = "What the heck?! Could not find generated class. Is it under the chair?";
				throw new DeploymentException(s, e1);
			}
			// set the concrete class in the descriptor
			sbbComponent.setConcreteSbbClass(clazz);

		} finally {
			if (sbbConcreteClass != null) {
				sbbConcreteClass.defrost();

			}
		}

		// uh uh
		if (sbbComponent.getConcreteSbbClass() == null) {
			throw new DeploymentException(
					"concrete sbb class generation failed and I don't know why, bug bug ?!? :)");
		}
	}

	/**
	 * Generates info that indicates if a method from {@link Sbb} interface
	 * should be invoked or not, in runtime.
	 */
	private void generateAbstractSbbClassInfo() {

		CtClass sbbClass = null;
		try {
			sbbClass = pool.get(Sbb.class.getName());
		} catch (NotFoundException e) {
			throw new SLEEException(e.getMessage(), e);
		}

		AbstractSbbClassInfo abstractSbbClassInfo = new AbstractSbbClassInfo();

		for (CtMethod sbbClassMethod : sbbClass.getDeclaredMethods()) {
			for (CtMethod sbbAbstractClassMethod : sbbAbstractClass
					.getMethods()) {
				if (sbbAbstractClassMethod.getName().equals(
						sbbClassMethod.getName())
						&& sbbAbstractClassMethod.getSignature().equals(
								sbbClassMethod.getSignature())) {
					// match, save info
					abstractSbbClassInfo.setInvokeInfo(sbbAbstractClassMethod
							.getMethodInfo().getName(), !sbbAbstractClassMethod
							.isEmpty());
					break;
				}
			}
		}

		sbbComponent.setAbstractSbbClassInfo(abstractSbbClassInfo);

	}

	/**
	 * @param class1
	 * @param string
	 */
	private void createField(CtClass parameter, String parameterName) {
		// TODO Auto-generated method stub
		try {
			CtField ctField = new CtField(parameter, parameterName,
					sbbConcreteClass);
			ctField.setModifiers(Modifier.PRIVATE);
			sbbConcreteClass.addField(ctField);
		} catch (CannotCompileException cce) {
			cce.printStackTrace();
		}
	}

	/**
	 * Creates a constructor with parameters <BR>
	 * For every parameter a field of the same class is created in the concrete
	 * class And each field is gonna be initialized with the corresponding
	 * parameter
	 * 
	 * @param parameters
	 *            the parameters of the constructor to add
	 * @throws DeploymentException
	 */
	protected void createConstructorWithParameter(CtClass[] parameters)
			throws DeploymentException {
		CtConstructor constructorWithParameter = new CtConstructor(parameters,
				sbbConcreteClass);
		String constructorBody = "{" + "this();";
		/*
		 * for (int i = 0; i < parameters.length; i++) { String parameterName =
		 * parameters[i].getName(); parameterName =
		 * parameterName.substring(parameterName .lastIndexOf(".") + 1); String
		 * firstCharLowerCase = parameterName.substring(0, 1) .toLowerCase();
		 * parameterName = firstCharLowerCase.concat(parameterName
		 * .substring(1));
		 * 
		 * int paramNumber = i + 1; constructorBody += parameterName + "=$" +
		 * paramNumber + ";"; }
		 */
		constructorBody += "this.setSbbEntity($1);";
		constructorBody += "}";
		try {
			sbbConcreteClass.addConstructor(constructorWithParameter);
			constructorWithParameter.setBody(constructorBody);
			if (logger.isDebugEnabled()) {
				logger.debug("ConstructorWithParameter created");
			}
		} catch (CannotCompileException e) {
			throw new DeploymentException(e.getMessage(), e);
		}
	}

	private void createFields(CtClass[] parameters) throws DeploymentException {
		for (int i = 0; i < parameters.length; i++) {
			String parameterName = parameters[i].getName();
			parameterName = parameterName.substring(parameterName
					.lastIndexOf(".") + 1);
			String firstCharLowerCase = parameterName.substring(0, 1)
					.toLowerCase();
			parameterName = firstCharLowerCase.concat(parameterName
					.substring(1));
			try {
				CtField ctField = new CtField(parameters[i], parameterName,
						sbbConcreteClass);
				ctField.setModifiers(Modifier.PRIVATE);
				sbbConcreteClass.addField(ctField);
			} catch (CannotCompileException e) {
				throw new DeploymentException(e.getMessage(), e);
			}
		}
	}

	/**
	 * Create a default constructor on the Sbb Concrete Class
	 * 
	 * @throws DeploymentException
	 */
	protected void createDefaultConstructor() throws DeploymentException {

		CtConstructor defaultConstructor = new CtConstructor(null,
				sbbConcreteClass);
		// We need a "do nothing" constructor because the
		// convergence name creation method may need to actually
		// create the object instance to run the method that
		// creates the convergence name.

		String constructorBody = "{ }";

		try {
			defaultConstructor.setBody(constructorBody);
			sbbConcreteClass.addConstructor(defaultConstructor);
			logger.debug("DefaultConstructor created");
		} catch (CannotCompileException e) {
			throw new DeploymentException(e.getMessage(), e);
		}
	}

	/**
	 * Create a default usage parameter getter and setter.
	 * 
	 * @param sbbConcrete
	 * @throws DeploymentException
	 */
	private void createDefaultUsageParameterGetter(CtClass sbbConcrete)
			throws DeploymentException {
		String methodName = "getDefaultSbbUsageParameterSet";
		CtMethod method = (CtMethod) abstractMethods.get(methodName);
		if (method == null) {
			method = (CtMethod) superClassesAbstractMethods.get(methodName);
		}
		if (method != null) {
			try {
				// copy method from abstract to concrete class
				CtMethod concreteMethod = CtNewMethod.copy(method,
						sbbConcreteClass, null);
				// create the method body
				String concreteMethodBody = "{ return ($r)"
						+ SbbAbstractMethodHandler.class.getName()
						+ ".getDefaultSbbUsageParameterSet(sbbEntity); }";
				if (logger.isDebugEnabled()) {
					logger.debug("Generated method " + methodName
							+ " , body = " + concreteMethodBody);
				}
				concreteMethod.setBody(concreteMethodBody);
				sbbConcreteClass.addMethod(concreteMethod);
			} catch (CannotCompileException cce) {
				throw new SLEEException("Cannot compile method "
						+ method.getName(), cce);
			}
		}
	}

	/**
	 * Create a named usage parameter getter.
	 * 
	 * @param sbbConcrete
	 * @throws DeploymentException
	 */

	private void createNamedUsageParameterGetter(CtClass sbbConcrete)
			throws DeploymentException {
		String methodName = "getSbbUsageParameterSet";
		CtMethod method = (CtMethod) abstractMethods.get(methodName);
		if (method == null) {
			method = (CtMethod) superClassesAbstractMethods.get(methodName);
		}
		if (method != null) {
			try {
				// copy method from abstract to concrete class
				CtMethod concreteMethod = CtNewMethod.copy(method,
						sbbConcreteClass, null);
				// create the method body
				String concreteMethodBody = "{ return ($r)"
						+ SbbAbstractMethodHandler.class.getName()
						+ ".getSbbUsageParameterSet(sbbEntity,$1); }";
				if (logger.isDebugEnabled()) {
					logger.debug("Generated method " + methodName
							+ " , body = " + concreteMethodBody);
				}
				concreteMethod.setBody(concreteMethodBody);
				sbbConcreteClass.addMethod(concreteMethod);
			} catch (CannotCompileException cce) {
				throw new SLEEException("Cannot compile method "
						+ method.getName(), cce);
			}
		}
	}

	/**
	 * Create a method to retrive the entity from the SbbObject.
	 * 
	 * @param cmpAccessors
	 * @throws DeploymentException
	 */
	private void createSbbEntityGetterAndSetter(CtClass sbbConcrete)
			throws DeploymentException {
		try {
			CtClass sbbEntityClass = pool.get(SbbEntity.class.getName());

			CtMethod getSbbEntity = CtNewMethod
					.make("public " + SbbEntity.class.getName()
							+ " getSbbEntity() { return this.sbbEntity; }",
							sbbConcrete);
			getSbbEntity.setModifiers(Modifier.PUBLIC);
			sbbConcrete.addMethod(getSbbEntity);
			CtMethod setSbbEntity = CtNewMethod.make(
					"public void setSbbEntity ( " + SbbEntity.class.getName()
							+ " sbbEntity )" + "{"
							+ "this.sbbEntity = sbbEntity;" + "}", sbbConcrete);

			setSbbEntity.setModifiers(Modifier.PUBLIC);
			sbbConcrete.addMethod(setSbbEntity);
		} catch (Exception e) {
			throw new DeploymentException(e.getMessage(), e);
		}
	}

	/**
	 * Create the cmp field setters and getters
	 * 
	 * @param cmps
	 *            the description of the cmp fields
	 * @throws DeploymentException
	 */
	protected void createCMPAccessors(Collection<MSbbCMPField> cmps)
			throws DeploymentException {

		for (MSbbCMPField cmp : cmps) {
			String fieldName = cmp.getCmpFieldName();
			// Set the first char of the accessor to UpperCase to follow the
			// javabean requirements
			fieldName = fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1);

			String getterMethodName = "get" + fieldName;
			CtMethod getterMethod = (CtMethod) abstractMethods
					.get(getterMethodName);
			if (getterMethod == null) {
				getterMethod = (CtMethod) this.superClassesAbstractMethods
						.get(getterMethodName);
			}
			if (getterMethod == null) {
				throw new SLEEException("can't find abstract method "
						+ getterMethodName);
			}

			try {
				// copy method from abstract to concrete class
				CtMethod concreteGetterMethod = CtNewMethod.copy(getterMethod,
						sbbConcreteClass, null);
				// create the method body
				String concreteGetterMethodBody = "{ return ($r)"
						+ SbbAbstractMethodHandler.class.getName()
						+ ".getCMPField(sbbEntity,\"" + cmp.getCmpFieldName()
						+ "\","+concreteGetterMethod.getReturnType().getName()+".class); }";
				if (logger.isDebugEnabled()) {
					logger.debug("Generated method " + getterMethodName
							+ " , body = " + concreteGetterMethodBody);
				}
				concreteGetterMethod.setBody(concreteGetterMethodBody);
				sbbConcreteClass.addMethod(concreteGetterMethod);
			} catch (Exception cce) {
				throw new SLEEException("Cannot compile method "
						+ getterMethod.getName(), cce);
			}

			String setterMethodName = "set" + fieldName;
			CtMethod setterMethod = (CtMethod) abstractMethods
					.get(setterMethodName);
			if (setterMethod == null) {
				setterMethod = (CtMethod) this.superClassesAbstractMethods
						.get(setterMethodName);
			}
			if (setterMethod == null) {
				throw new SLEEException("can't find abstract method "
						+ setterMethodName);
			}

			try {
				// copy method from abstract to concrete class
				CtMethod concreteSetterMethod = CtNewMethod.copy(setterMethod,
						sbbConcreteClass, null);
				// create the method body
				String concreteSetterMethodBody = "{ "
						+ SbbAbstractMethodHandler.class.getName()
						+ ".setCMPField(sbbEntity,\"" + cmp.getCmpFieldName()
						+ "\",$1); }";
				if (logger.isDebugEnabled()) {
					logger.debug("Generated method " + setterMethodName
							+ " , body = " + concreteSetterMethodBody);
				}
				concreteSetterMethod.setBody(concreteSetterMethodBody);
				sbbConcreteClass.addMethod(concreteSetterMethod);
			} catch (CannotCompileException cce) {
				throw new SLEEException("Cannot compile method "
						+ getterMethod.getName(), cce);
			}
		}

	}

	private String getEventTypeIDInstantionString(MEventEntry mEventEntry) {
		String eventTypeIDClassName = EventTypeID.class.getName();
		return eventTypeIDClassName + " eventTypeID = new "
				+ eventTypeIDClassName + "(\""
				+ mEventEntry.getEventReference().getEventTypeName() + "\",\""
				+ mEventEntry.getEventReference().getEventTypeVendor()
				+ "\",\""
				+ mEventEntry.getEventReference().getEventTypeVersion()
				+ "\");";
	}

	/**
	 * Create the implementation of the fire event methods
	 * 
	 * @param firedEvents
	 *            the set of fire event
	 */
	protected void createFireEventMethods(Collection<MEventEntry> mEventEntries) {
		if (mEventEntries == null)
			return;
		for (MEventEntry mEventEntry : mEventEntries) {
			if (mEventEntry.isFired()) {
				String methodName = "fire" + mEventEntry.getEventName();
				CtMethod method = (CtMethod) abstractMethods.get(methodName);
				if (method == null) {
					method = (CtMethod) superClassesAbstractMethods
							.get(methodName);
				}
				if (method != null) {
					try {
						// copy method from abstract to concrete class
						CtMethod concreteMethod = CtNewMethod.copy(method,
								sbbConcreteClass, null);
						String eventTypeIDClassName = EventTypeID.class
								.getName();
						// create the method body
						String concreteMethodBody = "{";
						concreteMethodBody += getEventTypeIDInstantionString(mEventEntry);
						concreteMethodBody += SbbAbstractMethodHandler.class
								.getName()
								+ ".fireEvent(sbbEntity,eventTypeID";
						for (int i = 0; i < method.getParameterTypes().length; i++) {
							concreteMethodBody += ",$" + (i + 1);
						}
						concreteMethodBody += ");}";
						if (logger.isDebugEnabled()) {
							logger.debug("Generated method " + methodName
									+ " , body = " + concreteMethodBody);
						}
						concreteMethod.setBody(concreteMethodBody);
						sbbConcreteClass.addMethod(concreteMethod);
					} catch (Exception e) {
						throw new SLEEException("Cannot compile method "
								+ method.getName(), e);
					}
				}
			}
		}
	}

	/**
	 * Create the get child relation method (this method redirects the call to a
	 * child relation interceptor)
	 * 
	 * @param childRelations
	 *            the childRelations method to add to the concrete class
	 */
	protected void createGetChildRelationsMethod(
			Collection<MGetChildRelationMethod> childRelations) {
		if (childRelations == null)
			return;
		for (MGetChildRelationMethod childRelation : childRelations) {
			String methodName = childRelation.getChildRelationMethodName();
			CtMethod method = (CtMethod) abstractMethods.get(methodName);
			if (method == null) {
				method = (CtMethod) superClassesAbstractMethods.get(methodName);
			}
			if (method != null) {
				try {
					// copy method from abstract to concrete class
					CtMethod concreteMethod = CtNewMethod.copy(method,
							sbbConcreteClass, null);
					// create the method body
					String concreteMethodBody = "{ return "
							+ SbbAbstractMethodHandler.class.getName()
							+ ".getChildRelation(sbbEntity,\"" + methodName
							+ "\"); }";
					if (logger.isDebugEnabled()) {
						logger.debug("Generated method " + methodName
								+ " , body = " + concreteMethodBody);
					}
					concreteMethod.setBody(concreteMethodBody);
					sbbConcreteClass.addMethod(concreteMethod);
				} catch (CannotCompileException cce) {
					throw new SLEEException("Cannot compile method "
							+ method.getName(), cce);
				}
			}
		}
	}

	/**
	 * Create the get profile CMP method (this method redirects the call to a
	 * profile cmp interceptor)
	 * 
	 * @param cmpProfiles
	 *            the CMP Profiles method to add to the concrete class
	 */
	protected void createGetProfileCMPMethods(
			Collection<MGetProfileCMPMethod> cmpProfiles) {
		if (cmpProfiles == null) {
			if (logger.isDebugEnabled()) {
				logger
						.debug("no CMP Profile method implementation to generate.");
			}
			return;
		}
		for (MGetProfileCMPMethod cmpProfile : cmpProfiles) {
			String methodName = cmpProfile.getProfileCmpMethodName();
			CtMethod method = (CtMethod) abstractMethods.get(methodName);
			if (method == null)
				method = (CtMethod) superClassesAbstractMethods.get(methodName);
			if (method != null)
				try {
					// copy method from abstract to concrete class
					CtMethod concreteMethod = CtNewMethod.copy(method,
							sbbConcreteClass, null);
					// create the method body
					String concreteMethodBody = "{ return "
							+ SbbAbstractMethodHandler.class.getName()
							+ ".getProfileCMPMethod(sbbEntity,\"" + methodName
							+ "\",$1); }";
					if (logger.isDebugEnabled()) {
						logger.debug("Generated method " + methodName
								+ " , body = " + concreteMethodBody);
					}
					concreteMethod.setBody(concreteMethodBody);
					sbbConcreteClass.addMethod(concreteMethod);
				} catch (CannotCompileException cce) {
					throw new SLEEException("Cannot compile method "
							+ method.getName(), cce);
				}
		}
	}

	protected void createSetActivityContextInterfaceMethod(
			CtClass activityContextInterface) throws DeploymentException {
		String methodToAdd = "public void sbbSetActivityContextInterface( Object aci ) {"
				+ "this.sbbActivityContextInterface = ("
				+ activityContextInterface.getName() + ") aci ; } ";
		CtMethod methodTest;
		try {
			methodTest = CtNewMethod.make(methodToAdd, sbbConcreteClass);
			sbbConcreteClass.addMethod(methodTest);
			logger.debug("Method " + methodToAdd + " added");
		} catch (CannotCompileException e) {
			throw new DeploymentException(e.getMessage(), e);
		}
	}

	/**
	 * Create the narrow method to get the activity context interface
	 * 
	 * @param activityContextInterface
	 *            the activity context interface return type of the narrow
	 *            method
	 * @param concreteActivityContextInterfaceClass
	 * @throws DeploymentException
	 */
	protected void createGetSbbActivityContextInterfaceMethod(
			CtClass activityContextInterface,
			Class concreteActivityContextInterfaceClass)
			throws DeploymentException {
		String methodToAdd = "public "
				+ activityContextInterface.getName()
				+ " asSbbActivityContextInterface(javax.slee.ActivityContextInterface aci) {"
				+ "if(aci==null)"
				+ "     throw new "
				+ IllegalStateException.class.getName()
				+ "(\"Passed argument can not be of null value.\");"
				+ " if(sbbEntity == null || sbbEntity.getSbbObject().getState() != "
				+ SbbObjectState.class.getName() + ".READY) { throw new "
				+ IllegalStateException.class.getName()
				+ "(\"Cannot call asSbbActivityContextInterface\"); } "
				+ "else if ( aci instanceof "
				+ concreteActivityContextInterfaceClass.getName()
				+ ") return aci;" + "else return  new "
				+ concreteActivityContextInterfaceClass.getName() + " ( ("+ActivityContextInterfaceImpl.class.getName()+") $1, "
				+ "sbbEntity.getSbbComponent());" + "}";
		CtMethod methodTest;
		try {
			methodTest = CtNewMethod.make(methodToAdd, sbbConcreteClass);
			sbbConcreteClass.addMethod(methodTest);
			if (logger.isDebugEnabled()) {
				logger.debug("Method " + methodToAdd + " added");
			}
		} catch (CannotCompileException e) {
			throw new DeploymentException(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param usageParameters
	 */
	/*
	 * protected void createSbbUsageParameterInterface(
	 * GetUsageParametersMethod[] usageParameters){ }
	 */
}