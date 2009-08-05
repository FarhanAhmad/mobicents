/**
 * Start time:14:30:39 2009-04-20<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.isup;

import java.util.TreeMap;

import org.mobicents.isup.ParameterRangeInvalidException;
import org.mobicents.isup.messages.AddressCompleteMessage;
import org.mobicents.isup.parameters.*;
import org.mobicents.isup.parameters.accessTransport.*;

/**
 * Start time:14:30:39 2009-04-20<br>
 * Project: mobicents-isup-stack<br>
 * See Table 21/Q.763
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
class AddressCompleteMessageImpl extends ISUPMessageImpl implements AddressCompleteMessage{

	public static final MessageTypeImpl _MESSAGE_TYPE = new MessageTypeImpl(_MESSAGE_CODE_ACM);

	protected static final int _INDEX_F_MessageType = 0;
	protected static final int _INDEX_F_BackwardCallIndicators = 1;
	// FIXME: those can be sent in any order, but we prefer this way, its faster
	// to access by index than by hash ?
	protected static final int _INDEX_O_OptionalBakwardCallIndicators = 0;
	protected static final int _INDEX_O_CallReference = 1;
	protected static final int _INDEX_O_CauseIndicators = 2;
	protected static final int _INDEX_O_UserToUserIndicators = 3;
	protected static final int _INDEX_O_UserToUserInformation = 4;
	protected static final int _INDEX_O_AccessTransport = 5;
	// FIXME: There can be more of those.
	protected static final int _INDEX_O_GenericNotificationIndicator = 6;
	protected static final int _INDEX_O_TransmissionMediumUsed = 7;
	protected static final int _INDEX_O_EchoControlInformation = 8;
	protected static final int _INDEX_O_AccessDeliveryInformation = 9;
	protected static final int _INDEX_O_RedirectionNumber = 10;
	protected static final int _INDEX_O_ParameterCompatibilityInformation = 11;
	protected static final int _INDEX_O_CallDiversionInformation = 12;
	protected static final int _INDEX_O_NetworkSpecificFacility = 13;
	protected static final int _INDEX_O_RemoteOperations = 14;
	protected static final int _INDEX_O_ServiceActivation = 15;
	protected static final int _INDEX_O_RedirectionNumberRestriction = 16;
	protected static final int _INDEX_O_ConferenceTreatmentIndicators = 17;
	protected static final int _INDEX_O_UIDActionIndicators = 18;
	protected static final int _INDEX_O_ApplicationTransportParameter = 19;
	protected static final int _INDEX_O_CCNRPossibleIndicator = 20;
	protected static final int _INDEX_O_HTRInformation = 21;
	protected static final int _INDEX_O_PivotRoutingBackwardInformation = 22;
	protected static final int _INDEX_O_RedirectStatus = 23;
	protected static final int _INDEX_O_EndOfOptionalParameters = 24;

	AddressCompleteMessageImpl(Object source, byte[] b) throws ParameterRangeInvalidException {
		super(source);
		super.f_Parameters = new TreeMap<Integer, ISUPParameter>();
		super.v_Parameters = new TreeMap<Integer, ISUPParameter>();
		super.o_Parameters = new TreeMap<Integer, ISUPParameter>();

		super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
		decodeElement(b);
		super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);
		
		super.mandatoryCodes.add(BackwardCallIndicators._PARAMETER_CODE);
		super.mandatoryCodeToIndex.put(BackwardCallIndicators._PARAMETER_CODE,_INDEX_F_BackwardCallIndicators);

		
		
		super.mandatoryVariableCodes.add(OptionalBakwardCallIndicators._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(CallReference._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(CauseIndicators._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(UserToUserIndicators._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(UserToUserInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(AccessTransport._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(TransmissionMediumUsed._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(EchoControlInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(AccessDeliveryInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(RedirectionNumber._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(CallDiversionInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(RemoteOperations._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(ServiceActivation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(RedirectionNumberRestriction._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(ConferenceTreatmentIndicators._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(UIDActionIndicators._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(ApplicationTransportParameter ._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(CCNRPossibleIndicator._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(HTRInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(PivotRoutingBackwardInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(RedirectStatus._PARAMETER_CODE);
		
		
		
		super.mandatoryVariableCodeToIndex.put(OptionalBakwardCallIndicators._PARAMETER_CODE, _INDEX_O_OptionalBakwardCallIndicators);
		super.mandatoryVariableCodeToIndex.put(CallReference._PARAMETER_CODE, _INDEX_O_CallReference);
		super.mandatoryVariableCodeToIndex.put(CauseIndicators._PARAMETER_CODE, _INDEX_O_CauseIndicators);
		super.mandatoryVariableCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, _INDEX_O_UserToUserIndicators);
		super.mandatoryVariableCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, _INDEX_O_UserToUserInformation);
		super.mandatoryVariableCodeToIndex.put(AccessTransport._PARAMETER_CODE, _INDEX_O_AccessTransport);
		super.mandatoryVariableCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE, _INDEX_O_GenericNotificationIndicator);
		super.mandatoryVariableCodeToIndex.put(TransmissionMediumUsed._PARAMETER_CODE, _INDEX_O_TransmissionMediumUsed);
		super.mandatoryVariableCodeToIndex.put(EchoControlInformation._PARAMETER_CODE, _INDEX_O_EchoControlInformation);
		super.mandatoryVariableCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE, _INDEX_O_AccessDeliveryInformation);
		super.mandatoryVariableCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, _INDEX_O_RedirectionNumber);
		super.mandatoryVariableCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE, _INDEX_O_ParameterCompatibilityInformation);
		super.mandatoryVariableCodeToIndex.put(CallDiversionInformation._PARAMETER_CODE, _INDEX_O_CallDiversionInformation);
		super.mandatoryVariableCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, _INDEX_O_NetworkSpecificFacility);
		super.mandatoryVariableCodeToIndex.put(RemoteOperations._PARAMETER_CODE, _INDEX_O_RemoteOperations);
		super.mandatoryVariableCodeToIndex.put(ServiceActivation._PARAMETER_CODE, _INDEX_O_ServiceActivation);
		super.mandatoryVariableCodeToIndex.put(RedirectionNumberRestriction._PARAMETER_CODE, _INDEX_O_RedirectionNumberRestriction);
		super.mandatoryVariableCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE, _INDEX_O_ConferenceTreatmentIndicators);
		super.mandatoryVariableCodeToIndex.put(UIDActionIndicators._PARAMETER_CODE, _INDEX_O_UIDActionIndicators);
		super.mandatoryVariableCodeToIndex.put(ApplicationTransportParameter._PARAMETER_CODE, _INDEX_O_ApplicationTransportParameter);
		super.mandatoryVariableCodeToIndex.put(CCNRPossibleIndicator._PARAMETER_CODE, _INDEX_O_CCNRPossibleIndicator);
		super.mandatoryVariableCodeToIndex.put(HTRInformation._PARAMETER_CODE, _INDEX_O_HTRInformation);
		super.mandatoryVariableCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE, _INDEX_O_PivotRoutingBackwardInformation);
		super.mandatoryVariableCodeToIndex.put(RedirectStatus._PARAMETER_CODE, _INDEX_O_RedirectStatus);
		
		
		
		//super.optionalCodes.add(TransitNetworkSelection._PARAMETER_CODE);
		//super.optionalCodeToIndex.put(NetworkManagementControls._PARAMETER_CODE,_INDEX_O_NetworkManagementControls);
		

	}

	AddressCompleteMessageImpl(Object source) throws ParameterRangeInvalidException {
		super(source);

		super.f_Parameters = new TreeMap<Integer, ISUPParameter>();
		super.v_Parameters = new TreeMap<Integer, ISUPParameter>();
		super.o_Parameters = new TreeMap<Integer, ISUPParameter>();

		super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());

		
		super.mandatoryCodes.add(BackwardCallIndicators._PARAMETER_CODE);
		super.mandatoryCodeToIndex.put(BackwardCallIndicators._PARAMETER_CODE,_INDEX_F_BackwardCallIndicators);

		
		
		super.mandatoryVariableCodes.add(OptionalBakwardCallIndicators._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(CallReference._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(CauseIndicators._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(UserToUserIndicators._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(UserToUserInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(AccessTransport._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(TransmissionMediumUsed._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(EchoControlInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(AccessDeliveryInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(RedirectionNumber._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(CallDiversionInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(RemoteOperations._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(ServiceActivation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(RedirectionNumberRestriction._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(ConferenceTreatmentIndicators._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(UIDActionIndicators._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(ApplicationTransportParameter ._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(CCNRPossibleIndicator._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(HTRInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(PivotRoutingBackwardInformation._PARAMETER_CODE);
		super.mandatoryVariableCodes.add(RedirectStatus._PARAMETER_CODE);
		
		
		
		super.mandatoryVariableCodeToIndex.put(OptionalBakwardCallIndicators._PARAMETER_CODE, _INDEX_O_OptionalBakwardCallIndicators);
		super.mandatoryVariableCodeToIndex.put(CallReference._PARAMETER_CODE, _INDEX_O_CallReference);
		super.mandatoryVariableCodeToIndex.put(CauseIndicators._PARAMETER_CODE, _INDEX_O_CauseIndicators);
		super.mandatoryVariableCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, _INDEX_O_UserToUserIndicators);
		super.mandatoryVariableCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, _INDEX_O_UserToUserInformation);
		super.mandatoryVariableCodeToIndex.put(AccessTransport._PARAMETER_CODE, _INDEX_O_AccessTransport);
		super.mandatoryVariableCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE, _INDEX_O_GenericNotificationIndicator);
		super.mandatoryVariableCodeToIndex.put(TransmissionMediumUsed._PARAMETER_CODE, _INDEX_O_TransmissionMediumUsed);
		super.mandatoryVariableCodeToIndex.put(EchoControlInformation._PARAMETER_CODE, _INDEX_O_EchoControlInformation);
		super.mandatoryVariableCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE, _INDEX_O_AccessDeliveryInformation);
		super.mandatoryVariableCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, _INDEX_O_RedirectionNumber);
		super.mandatoryVariableCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE, _INDEX_O_ParameterCompatibilityInformation);
		super.mandatoryVariableCodeToIndex.put(CallDiversionInformation._PARAMETER_CODE, _INDEX_O_CallDiversionInformation);
		super.mandatoryVariableCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, _INDEX_O_NetworkSpecificFacility);
		super.mandatoryVariableCodeToIndex.put(RemoteOperations._PARAMETER_CODE, _INDEX_O_RemoteOperations);
		super.mandatoryVariableCodeToIndex.put(ServiceActivation._PARAMETER_CODE, _INDEX_O_ServiceActivation);
		super.mandatoryVariableCodeToIndex.put(RedirectionNumberRestriction._PARAMETER_CODE, _INDEX_O_RedirectionNumberRestriction);
		super.mandatoryVariableCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE, _INDEX_O_ConferenceTreatmentIndicators);
		super.mandatoryVariableCodeToIndex.put(UIDActionIndicators._PARAMETER_CODE, _INDEX_O_UIDActionIndicators);
		super.mandatoryVariableCodeToIndex.put(ApplicationTransportParameter._PARAMETER_CODE, _INDEX_O_ApplicationTransportParameter);
		super.mandatoryVariableCodeToIndex.put(CCNRPossibleIndicator._PARAMETER_CODE, _INDEX_O_CCNRPossibleIndicator);
		super.mandatoryVariableCodeToIndex.put(HTRInformation._PARAMETER_CODE, _INDEX_O_HTRInformation);
		super.mandatoryVariableCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE, _INDEX_O_PivotRoutingBackwardInformation);
		super.mandatoryVariableCodeToIndex.put(RedirectStatus._PARAMETER_CODE, _INDEX_O_RedirectStatus);
	}

	@Override
	public boolean hasAllMandatoryParameters() {
		if (super.f_Parameters.get(_INDEX_F_MessageType) == null || super.f_Parameters.get(_INDEX_F_MessageType).getCode() != MessageTypeImpl._PARAMETER_CODE) {
			return false;
		}

		if (super.f_Parameters.get(_INDEX_F_BackwardCallIndicators) == null || super.f_Parameters.get(_INDEX_F_BackwardCallIndicators).getCode() != BackwardCallIndicatorsImpl._PARAMETER_CODE) {
			return false;
		}

		return true;
	}

	@Override
	public MessageType getMessageType() {
		return _MESSAGE_TYPE;
	}

	public void setBackwardCallIndicators(BackwardCallIndicators indicators) {
		super.f_Parameters.put(_INDEX_F_BackwardCallIndicators, indicators);
	}

	public BackwardCallIndicators getBackwardCallIndicators() {
		return (BackwardCallIndicators) super.f_Parameters.get(_INDEX_F_BackwardCallIndicators);
	}

	public void setOptionalBakwardCallIndicators(OptionalBakwardCallIndicators value) {
		super.o_Parameters.put(_INDEX_O_OptionalBakwardCallIndicators, value);

	}

	public OptionalBakwardCallIndicators getOptionalBakwardCallIndicators() {
		return (OptionalBakwardCallIndicators) super.o_Parameters.get(_INDEX_O_OptionalBakwardCallIndicators);
	}

	public void setCallReference(CallReference value) {
		super.o_Parameters.put(_INDEX_O_CallReference, value);
	}

	public CallReference getCallReference() {
		return (CallReference) super.o_Parameters.get(_INDEX_O_CallReference);
	}

	public void setCauseIndicators(CauseIndicators value) {
		super.o_Parameters.put(_INDEX_O_CauseIndicators, value);

	}

	public CauseIndicators getCauseIndicators() {
		return (CauseIndicators) super.o_Parameters.get(_INDEX_O_CauseIndicators);
	}

	public void setUserToUserIndicators(UserToUserIndicators value) {
		super.o_Parameters.put(_INDEX_O_UserToUserIndicators, value);
	}

	public UserToUserIndicators getUserToUserIndicators() {
		return (UserToUserIndicators) super.o_Parameters.get(_INDEX_O_UserToUserIndicators);
	}

	public void setUserToUserInformation(UserToUserInformation value) {
		super.o_Parameters.put(_INDEX_O_UserToUserInformation, value);
	}

	public UserToUserInformation getUserToUserInformation() {
		return (UserToUserInformation) super.o_Parameters.get(_INDEX_O_UserToUserInformation);
	}

	public void setAccessTransport(AccessTransport value) {
		super.o_Parameters.put(_INDEX_O_AccessTransport, value);
	}

	public AccessTransport getAccessTransport() {
		return (AccessTransport) super.o_Parameters.get(_INDEX_O_AccessTransport);
	}

	public void setGenericNotificationIndicator(GenericNotificationIndicator value) {
		super.o_Parameters.put(_INDEX_O_GenericNotificationIndicator, value);
	}

	public GenericNotificationIndicator getGenericNotificationIndicator() {
		return (GenericNotificationIndicator) super.o_Parameters.get(_INDEX_O_GenericNotificationIndicator);
	}

	public void setTransmissionMediumUsed(TransmissionMediumUsed value) {
		super.o_Parameters.put(_INDEX_O_TransmissionMediumUsed, value);
	}

	public TransmissionMediumUsed getTransmissionMediumUsed() {
		return (TransmissionMediumUsed) super.o_Parameters.get(_INDEX_O_TransmissionMediumUsed);
	}

	public void setEchoControlInformation(EchoControlInformation value) {
		super.o_Parameters.put(_INDEX_O_EchoControlInformation, value);
	}

	public EchoControlInformation getEchoControlInformation() {
		return (EchoControlInformation) super.o_Parameters.get(_INDEX_O_EchoControlInformation);
	}

	public void setAccessDeliveryInformation(AccessDeliveryInformation value) {
		super.o_Parameters.put(_INDEX_O_AccessDeliveryInformation, value);
	}

	public AccessDeliveryInformation getAccessDeliveryInformation() {
		return (AccessDeliveryInformation) super.o_Parameters.get(_INDEX_O_AccessDeliveryInformation);
	}

	public void setRedirectionNumber(RedirectionNumber value) {
		super.o_Parameters.put(_INDEX_O_RedirectionNumber, value);
	}

	public RedirectionNumber getRedirectionNumber() {
		return (RedirectionNumber) super.o_Parameters.get(_INDEX_O_RedirectionNumber);
	}

	public void setParameterCompatibilityInformation(ParameterCompatibilityInformation value) {
		super.o_Parameters.put(_INDEX_O_ParameterCompatibilityInformation, value);
	}

	public ParameterCompatibilityInformation getParameterCompatibilityInformation() {
		return (ParameterCompatibilityInformation) super.o_Parameters.get(_INDEX_O_ParameterCompatibilityInformation);
	}

	public void setCallDiversionInformation(CallDiversionInformation value) {
		super.o_Parameters.put(_INDEX_O_CallDiversionInformation, value);
	}

	public CallDiversionInformation getCallDiversionInformation() {
		return (CallDiversionInformation) super.o_Parameters.get(_INDEX_O_CallDiversionInformation);
	}

	public void setNetworkSpecificFacility(NetworkSpecificFacility value) {
		super.o_Parameters.put(_INDEX_O_NetworkSpecificFacility, value);
	}

	public NetworkSpecificFacility getNetworkSpecificFacility() {
		return (NetworkSpecificFacility) super.o_Parameters.get(_INDEX_O_NetworkSpecificFacility);
	}

	public void setRemoteOperations(RemoteOperations value) {
		super.o_Parameters.put(_INDEX_O_RemoteOperations, value);
	}

	public RemoteOperations getRemoteOperations() {
		return (RemoteOperations) super.o_Parameters.get(_INDEX_O_RemoteOperations);
	}

	public void setServiceActivation(ServiceActivation value) {
		super.o_Parameters.put(_INDEX_O_ServiceActivation, value);
	}

	public RedirectionNumberRestriction getRedirectionNumberRestriction() {
		return (RedirectionNumberRestriction) super.o_Parameters.get(_INDEX_O_ServiceActivation);
	}

	public void setRedirectionNumberRestriction(RedirectionNumberRestriction value) {
		super.o_Parameters.put(_INDEX_O_RedirectionNumberRestriction, value);
	}

	public ServiceActivation getServiceActivation() {
		return (ServiceActivation) super.o_Parameters.get(_INDEX_O_RedirectionNumberRestriction);
	}

	public void setConferenceTreatmentIndicators(ConferenceTreatmentIndicators value) {
		super.o_Parameters.put(_INDEX_O_ConferenceTreatmentIndicators, value);
	}

	public ConferenceTreatmentIndicators getConferenceTreatmentIndicators() {
		return (ConferenceTreatmentIndicators) super.o_Parameters.get(_INDEX_O_ConferenceTreatmentIndicators);
	}

	public void setUIDActionIndicators(UIDActionIndicators value) {
		super.o_Parameters.put(_INDEX_O_UIDActionIndicators, value);
	}

	public UIDActionIndicators getUIDActionIndicators() {
		return (UIDActionIndicators) super.o_Parameters.get(_INDEX_O_UIDActionIndicators);
	}

	public void setApplicationTransportParameter(ApplicationTransportParameter value) {
		super.o_Parameters.put(_INDEX_O_ApplicationTransportParameter, value);
	}

	public ApplicationTransportParameter getApplicationTransportParameter() {
		return (ApplicationTransportParameter) super.o_Parameters.get(_INDEX_O_ApplicationTransportParameter);
	}

	public void setCCNRPossibleIndicator(CCNRPossibleIndicator value) {
		super.o_Parameters.put(_INDEX_O_CCNRPossibleIndicator, value);
	}

	public CCNRPossibleIndicator getCCNRPossibleIndicator() {
		return (CCNRPossibleIndicator) super.o_Parameters.get(_INDEX_O_CCNRPossibleIndicator);
	}

	public void setHTRInformation(HTRInformation value) {
		super.o_Parameters.put(_INDEX_O_HTRInformation, value);
	}

	public HTRInformation getHTRInformation() {
		return (HTRInformation) super.o_Parameters.get(_INDEX_O_HTRInformation);
	}

	public void setPivotRoutingBackwardInformation(PivotRoutingBackwardInformation value) {
		super.o_Parameters.put(_INDEX_O_PivotRoutingBackwardInformation, value);
	}

	public PivotRoutingBackwardInformation getPivotRoutingBackwardInformation() {
		return (PivotRoutingBackwardInformation) super.o_Parameters.get(_INDEX_O_PivotRoutingBackwardInformation);
	}

	public void setRedirectStatus(RedirectStatus value) {
		super.o_Parameters.put(_INDEX_O_RedirectStatus, value);
	}

	public RedirectStatus getRedirectStatus() {
		return (RedirectStatus) super.o_Parameters.get(_INDEX_O_RedirectStatus);
	}

	@Override
	protected int decodeMandatoryParameters(byte[] b, int index) throws ParameterRangeInvalidException {
		int localIndex = index;
		if (b.length - index > 1) {

			// Message Type
			if (b[index] != this._MESSAGE_CODE_ACM) {
				throw new ParameterRangeInvalidException("Message code is not: " + this._MESSAGE_CODE_ACM);
			}
			index++;
			// this.circuitIdentificationCode = b[index++];
			try {
				byte[] backwardCallIndicator = new byte[2];
				backwardCallIndicator[0] = b[index++];
				backwardCallIndicator[1] = b[index++];
				BackwardCallIndicatorsImpl bci = new BackwardCallIndicatorsImpl(backwardCallIndicator);
				this.setBackwardCallIndicators(bci);
			} catch (Exception e) {
				// AIOOBE or IllegalArg
				throw new ParameterRangeInvalidException("Failed to parse BackwardCallIndicators due to: ", e);
			}

			// return 3;
			return index - localIndex;
		} else {
			throw new IllegalArgumentException("byte[] must have atleast two octets");
		}

	}

	@Override
	protected int getNumberOfMandatoryVariableLengthParameters() {

		return 0;
	}

	protected void decodeMandatoryVariableBody(byte[] parameterBody, int parameterIndex) throws ParameterRangeInvalidException {
		// we dont have those.
	}

	protected void decodeOptionalBody(byte[] parameterBody, byte parameterCode) throws ParameterRangeInvalidException {

		switch ((int) parameterCode) {
		case OptionalBakwardCallIndicatorsImpl._PARAMETER_CODE:
			OptionalBakwardCallIndicatorsImpl obi = new OptionalBakwardCallIndicatorsImpl(parameterBody);
			this.setOptionalBakwardCallIndicators(obi);
			break;
		case CallReferenceImpl._PARAMETER_CODE:
			CallReferenceImpl cr = new CallReferenceImpl(parameterBody);
			this.setCallReference(cr);
			break;
		case CauseIndicatorsImpl._PARAMETER_CODE:
			CauseIndicatorsImpl ci = new CauseIndicatorsImpl(parameterBody);
			this.setCauseIndicators(ci);
			break;
		case UserToUserIndicatorsImpl._PARAMETER_CODE:
			UserToUserIndicatorsImpl utsi = new UserToUserIndicatorsImpl(parameterBody);
			this.setUserToUserIndicators(utsi);
			break;
		case UserToUserInformationImpl._PARAMETER_CODE:
			UserToUserInformationImpl utsi2 = new UserToUserInformationImpl(parameterBody);
			this.setUserToUserInformation(utsi2);
			break;
		case AccessTransportImpl._PARAMETER_CODE:
			AccessTransportImpl at = new AccessTransportImpl(parameterBody);
			this.setAccessTransport(at);
			break;
		// FIXME: There can be more of those.
		case GenericNotificationIndicatorImpl._PARAMETER_CODE:
			GenericNotificationIndicatorImpl gni = new GenericNotificationIndicatorImpl(parameterBody);
			this.setGenericNotificationIndicator(gni);
			break;
		case TransmissionMediumUsedImpl._PARAMETER_CODE:
			TransmissionMediumUsedImpl tmu = new TransmissionMediumUsedImpl(parameterBody);
			this.setTransmissionMediumUsed(tmu);
			break;
		case EchoControlInformationImpl._PARAMETER_CODE:
			EchoControlInformationImpl eci = new EchoControlInformationImpl(parameterBody);
			this.setEchoControlInformation(eci);
			break;
		case AccessDeliveryInformationImpl._PARAMETER_CODE:
			AccessDeliveryInformationImpl adi = new AccessDeliveryInformationImpl(parameterBody);
			this.setAccessDeliveryInformation(adi);
			break;
		case RedirectionNumberImpl._PARAMETER_CODE:
			RedirectionNumberImpl rn = new RedirectionNumberImpl(parameterBody);
			this.setRedirectionNumber(rn);
			break;
		case ParameterCompatibilityInformationImpl._PARAMETER_CODE:
			ParameterCompatibilityInformationImpl pci = new ParameterCompatibilityInformationImpl(parameterBody);
			this.setParameterCompatibilityInformation(pci);
			break;
		case CallDiversionInformationImpl._PARAMETER_CODE:
			CallDiversionInformationImpl cdi = new CallDiversionInformationImpl(parameterBody);
			this.setCallDiversionInformation(cdi);
			break;
		case NetworkSpecificFacilityImpl._PARAMETER_CODE:
			NetworkSpecificFacilityImpl nsf = new NetworkSpecificFacilityImpl(parameterBody);
			this.setNetworkSpecificFacility(nsf);
			break;
		case RemoteOperationsImpl._PARAMETER_CODE:
			RemoteOperationsImpl ro = new RemoteOperationsImpl(parameterBody);
			this.setRemoteOperations(ro);
			break;
		case ServiceActivationImpl._PARAMETER_CODE:
			ServiceActivationImpl sa = new ServiceActivationImpl(parameterBody);
			this.setServiceActivation(sa);
			break;
		case RedirectionNumberRestrictionImpl._PARAMETER_CODE:
			RedirectionNumberRestrictionImpl rnr = new RedirectionNumberRestrictionImpl(parameterBody);
			this.setRedirectionNumberRestriction(rnr);
			break;
		case ConferenceTreatmentIndicatorsImpl._PARAMETER_CODE:
			ConferenceTreatmentIndicatorsImpl cti = new ConferenceTreatmentIndicatorsImpl(parameterBody);
			this.setConferenceTreatmentIndicators(cti);
			break;
		case UIDActionIndicatorsImpl._PARAMETER_CODE:
			UIDActionIndicatorsImpl uidAI = new UIDActionIndicatorsImpl(parameterBody);
			this.setUIDActionIndicators(uidAI);
			break;
		case ApplicationTransportParameterImpl._PARAMETER_CODE:
			ApplicationTransportParameterImpl atp = new ApplicationTransportParameterImpl(parameterBody);
			this.setApplicationTransportParameter(atp);
			break;
		case CCNRPossibleIndicator._PARAMETER_CODE:
			CCNRPossibleIndicatorImpl ccnrPI = new CCNRPossibleIndicatorImpl(parameterBody);
			this.setCCNRPossibleIndicator(ccnrPI);
			break;
		case HTRInformationImpl._PARAMETER_CODE:
			HTRInformationImpl htr = new HTRInformationImpl(parameterBody);
			this.setHTRInformation(htr);
			break;
		case PivotRoutingBackwardInformationImpl._PARAMETER_CODE:
			PivotRoutingBackwardInformationImpl pivot = new PivotRoutingBackwardInformationImpl(parameterBody);
			this.setPivotRoutingBackwardInformation(pivot);
			break;
		case RedirectStatusImpl._PARAMETER_CODE:
			RedirectStatusImpl rs = new RedirectStatusImpl(parameterBody);
			this.setRedirectStatus(rs);
			break;
		case EndOfOptionalParametersImpl._PARAMETER_CODE:
			// we add this by default
			break;

		default:
			throw new IllegalArgumentException("Unrecognized parameter code for optional part: " + parameterCode);
		}

	}



}