/**
 * Start time:14:36:25 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.InformationIndicators;

/**
 * Start time:14:36:25 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class InformationIndicatorsImpl extends AbstractParameter implements InformationIndicators {

	private final static int _TURN_ON = 1;
	private final static int _TURN_OFF = 0;

	private int callingPartyAddressResponseIndicator;
	private boolean holdProvidedIndicator;
	private boolean callingPartysCategoryResponseIndicator;
	private boolean chargeInformationResponseIndicator;
	private boolean solicitedInformationIndicator;
	// FIXME: should we care about it.
	private int reserved;

	public InformationIndicatorsImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public InformationIndicatorsImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InformationIndicatorsImpl(int callingPartyAddressResponseIndicator, boolean holdProvidedIndicator, boolean callingPartysCategoryResponseIndicator,
			boolean chargeInformationResponseIndicator, boolean solicitedInformationIndicator, int reserved) {
		super();
		this.callingPartyAddressResponseIndicator = callingPartyAddressResponseIndicator;
		this.holdProvidedIndicator = holdProvidedIndicator;
		this.callingPartysCategoryResponseIndicator = callingPartysCategoryResponseIndicator;
		this.chargeInformationResponseIndicator = chargeInformationResponseIndicator;
		this.solicitedInformationIndicator = solicitedInformationIndicator;
		this.reserved = reserved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 2) {
			throw new ParameterRangeInvalidException("byte[] must  not be null and length must  be 2");
		}

		this.reserved = (b[1] >> 4) & 0x0F;
		this.callingPartyAddressResponseIndicator = b[0] & 0x03;
		this.holdProvidedIndicator = ((b[0] >> 2) & 0x01) == _TURN_ON;
		this.callingPartysCategoryResponseIndicator = ((b[0] >> 5) & 0x01) == _TURN_ON;
		this.chargeInformationResponseIndicator = ((b[0] >> 6) & 0x01) == _TURN_ON;
		this.solicitedInformationIndicator = ((b[0] >> 7) & 0x01) == _TURN_ON;
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {

		int b1 = this.callingPartyAddressResponseIndicator & 0x03;
		b1 |= (this.holdProvidedIndicator ? _TURN_ON : _TURN_OFF) << 2;
		b1 |= (this.callingPartysCategoryResponseIndicator ? _TURN_ON : _TURN_OFF) << 5;
		b1 |= (this.chargeInformationResponseIndicator ? _TURN_ON : _TURN_OFF) << 6;
		b1 |= (this.solicitedInformationIndicator ? _TURN_ON : _TURN_OFF) << 7;

		int b2 = this.reserved & 0x0F;
		byte[] b = new byte[] { (byte) b1, (byte) b2 };
		return b;
	}

	public int getCallingPartyAddressResponseIndicator() {
		return callingPartyAddressResponseIndicator;
	}

	public void setCallingPartyAddressResponseIndicator(int callingPartyAddressResponseIndicator) {
		this.callingPartyAddressResponseIndicator = callingPartyAddressResponseIndicator;
	}

	public boolean isHoldProvidedIndicator() {
		return holdProvidedIndicator;
	}

	public void setHoldProvidedIndicator(boolean holdProvidedIndicator) {
		this.holdProvidedIndicator = holdProvidedIndicator;
	}

	public boolean isCallingPartysCategoryResponseIndicator() {
		return callingPartysCategoryResponseIndicator;
	}

	public void setCallingPartysCategoryResponseIndicator(boolean callingPartysCategoryResponseIndicator) {
		this.callingPartysCategoryResponseIndicator = callingPartysCategoryResponseIndicator;
	}

	public boolean isChargeInformationResponseIndicator() {
		return chargeInformationResponseIndicator;
	}

	public void setChargeInformationResponseIndicator(boolean chargeInformationResponseIndicator) {
		this.chargeInformationResponseIndicator = chargeInformationResponseIndicator;
	}

	public boolean isSolicitedInformationIndicator() {
		return solicitedInformationIndicator;
	}

	public void setSolicitedInformationIndicator(boolean solicitedInformationIndicator) {
		this.solicitedInformationIndicator = solicitedInformationIndicator;
	}

	public int getReserved() {
		return reserved;
	}

	public void setReserved(int reserved) {
		this.reserved = reserved;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
