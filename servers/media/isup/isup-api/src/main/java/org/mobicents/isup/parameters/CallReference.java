/**
 * Start time:14:48:06 2009-03-30<br>
 * Project: mobicents-jain-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.isup.parameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Start time:14:48:06 2009-03-30<br>
 * Project: mobicents-jain-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public class CallReference extends AbstractParameter {

	public static final int _PARAMETER_CODE = 0x01;
	private int callIdentity = 0;
	private int signalingPointCode = 0;

	public CallReference(byte[] b) {
		super();
		decodeElement(b);
	}

	public CallReference(int callIdentity, int signalingPointCode) {
		super();
		this.callIdentity = callIdentity;
		this.signalingPointCode = signalingPointCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws IllegalArgumentException {
		if (b == null || b.length != 5) {
			throw new IllegalArgumentException("byte[] must not be null or have different size than 1");
		}
		for (int i = 0; i < 3; i++) {
			this.callIdentity |= (b[i] << (i * 8));
		}

		this.signalingPointCode |= b[3];
		this.signalingPointCode |= (b[4] << 8);
		// This kills first two bits.
		this.signalingPointCode &= 0x3FFF;
		return 5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		byte[] b = new byte[5];

		for (int i = 0; i < 3; i++) {
			b[i] = (byte) (this.callIdentity >> (i * 8));

		}
		// This kills first two bits.
		this.signalingPointCode &= 0x3FFF;
		b[3] = (byte) this.signalingPointCode;
		b[4] = (byte) (this.signalingPointCode >> 8);

		return b;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.ISUPComponent#encodeElement(java.io.ByteArrayOutputStream
	 * )
	 */
	public int encodeElement(ByteArrayOutputStream bos) throws IOException {
		byte[] b = this.encodeElement();
		bos.write(b);
		return b.length;
	}

	public int getCallIdentity() {
		return callIdentity;
	}

	public void setCallIdentity(int callIdentity) {
		this.callIdentity = callIdentity;
	}

	public int getSignalingPointCode() {
		return signalingPointCode;
	}

	public void setSignalingPointCode(int signalingPointCode) {
		this.signalingPointCode = signalingPointCode;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
