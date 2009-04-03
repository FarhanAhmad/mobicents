/**
 * Start time:14:44:20 2009-04-01<br>
 * Project: mobicents-jain-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.isup.parameters;

import java.io.IOException;

/**
 * Start time:14:44:20 2009-04-01<br>
 * Project: mobicents-jain-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MCIDRequestIndicators extends AbstractParameter {
	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;
	/**
	 * Flag that indicates that information is requested
	 */
	private static final boolean _INDICATOR_REQUESTED = true;

	/**
	 * Flag that indicates that information is not requested
	 */
	private static final boolean _INDICATOR_NOT_REQUESTED = false;
	private boolean mcidRequestIndicator = false;
	private boolean holdingIndicator = false;

	public MCIDRequestIndicators(byte[] b) {
		super();
		decodeElement(b);
	}

	public MCIDRequestIndicators(boolean mcidRequest, boolean holdingRequested) {
		super();
		this.mcidRequestIndicator = mcidRequest;
		this.holdingIndicator = holdingRequested;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws IllegalArgumentException {
		if (b == null || b.length != 1) {
			throw new IllegalArgumentException("byte[] must  not be null and length must  be 1");
		}

		this.mcidRequestIndicator = (b[0] & 0x01) == _TURN_ON;
		this.holdingIndicator = ((b[0] >> 1) & 0x01) == _TURN_ON;
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		int b0 = 0;

		b0 |= (this.mcidRequestIndicator ? _TURN_ON : _TURN_OFF);
		b0 |= ((this.holdingIndicator ? _TURN_ON : _TURN_OFF)) << 1;

		return new byte[] { (byte) b0 };
	}

	public boolean isMcidRequestIndicator() {
		return mcidRequestIndicator;
	}

	public void setMcidRequestIndicator(boolean mcidRequestIndicator) {
		this.mcidRequestIndicator = mcidRequestIndicator;
	}

	public boolean isHoldingIndicator() {
		return holdingIndicator;
	}

	public void setHoldingIndicator(boolean holdingIndicator) {
		this.holdingIndicator = holdingIndicator;
	}

}
