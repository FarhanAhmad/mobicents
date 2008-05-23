package org.mobicents.slee.resource.diameter.base.events;

import net.java.slee.resource.diameter.base.events.CapabilitiesExchangeAnswer;
import net.java.slee.resource.diameter.base.events.avp.AddressAvp;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;

import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvpImpl;

/**
 * 
 * Implementation of Capabilities-Exchange-Answer Diameter Message.
 *
 * <br>Super project:  mobicents
 * <br>3:22:58 PM May 21, 2008 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author Erick Svenson
 */
public class CapabilitiesExchangeAnswerImpl extends
		ExtensionDiameterMessageImpl implements CapabilitiesExchangeAnswer {

	public CapabilitiesExchangeAnswerImpl(Message message) {
		super(message);
	}

	@Override
	public String getLongName() {

		return "Capabilities-Exchange-Answer";
	}

	@Override
	public String getShortName() {

		return "CEA";
	}

	public long[] getAcctApplicationIds() {

		return super.getAvpsAsUInt32(Avp.ACCT_APPLICATION_ID);
	}

	public long[] getAuthApplicationIds() {
		return super.getAvpsAsUInt32(Avp.AUTH_APPLICATION_ID);
	}

	public long getFirmwareRevision() {

		return super.getAvpAsUInt32(Avp.FIRMWARE_REVISION);
	}

	public AddressAvp[] getHostIpAddresses() {
		return super.getAvpAsAddress(Avp.HOST_IP_ADDRESS);
	}

	public long[] getInbandSecurityIds() {
		return super.getAvpsAsUInt32(Avp.INBAND_SECURITY_ID);
	}

	public String getProductName() {

		return super.getAvpAsUtf8(Avp.PRODUCT_NAME);
	}

	public long[] getSupportedVendorIds() {
		return super.getAvpsAsUInt32(Avp.SUPPORTED_VENDOR_ID);
	}

	public long getVendorId() {
		return super.getAvpAsUInt32(Avp.VENDOR_ID);
	}

	public VendorSpecificApplicationIdAvp[] getVendorSpecificApplicationIds() {

		AvpSet avps = super.message.getAvps().getAvps(
				Avp.VENDOR_SPECIFIC_APPLICATION_ID);
		if (avps == null)
			return null;

		VendorSpecificApplicationIdAvp[] r = new VendorSpecificApplicationIdAvp[avps
				.size()];
		Avp avp = null;
		for (int i = 0; i < avps.size(); i++) {
			// FIXME:baranowb ; setting prt to 0
			avp = avps.getAvpByIndex(i);
			try {
				r[i] = new VendorSpecificApplicationIdAvpImpl(
						Avp.VENDOR_SPECIFIC_APPLICATION_ID, avp.getVendorId(),
						avp.isMandatory() ? 1 : 0, 0, avp.getRaw());
			} catch (AvpDataException e) {
			  log.error( "", e );
				return null;
			}
		}
		return r;
	}

	public boolean hasFirmwareRevision() {
		return getFirmwareRevision() > 0;
	}

	public boolean hasProductName() {
		return getProductName() != null;
	}

	public boolean hasVendorId() {
		return getVendorId() > 0;
	}

	public void setAcctApplicationIds(long[] acctApplicationIds) {
		// FIXME: baranowb; setting mandatory to false
		super.setAvpsAsUInt32(Avp.ACCT_APPLICATION_ID, acctApplicationIds,
				false, true);

	}

	public void setAuthApplicationIds(long[] authApplicationIds) {
		// FIXME: baranowb; setting mandatory to false
		super.setAvpsAsUInt32(Avp.AUTH_APPLICATION_ID, authApplicationIds,
				false, true);

	}

	public void setFirmwareRevision(long firmwareRevision) {

		super.setAvpAsUInt32(Avp.FIRMWARE_REVISION, firmwareRevision, false,
				true);

	}

	public void setHostIpAddress(AddressAvp hostIpAddress) {
		super.setAvpAsAddress(Avp.HOST_IP_ADDRESS,
				new AddressAvp[] { hostIpAddress }, false, true);

	}

	public void setHostIpAddresses(AddressAvp[] hostIpAddresses) {

		super
				.setAvpAsAddress(Avp.HOST_IP_ADDRESS, hostIpAddresses, false,
						true);
	}

	public void setInbandSecurityId(long inbandSecurityId) {
		// FIXME: baranowb; setting mandatory to false
		super.setAvpAsUInt32(Avp.INBAND_SECURITY_ID, inbandSecurityId, false,
				true);

	}

	public void setInbandSecurityIds(long[] inbandSecurityIds) {

		// FIXME: baranowb; setting mandatory to false
		super.setAvpsAsUInt32(Avp.INBAND_SECURITY_ID, inbandSecurityIds, false,
				true);
	}

	public void setProductName(String productName) {
		super.setAvpAsUtf8(Avp.PRODUCT_NAME, productName, true, true);

	}

	public void setSupportedVendorId(long supportedVendorId) {
		super.setAvpAsUInt32(Avp.SUPPORTED_VENDOR_ID, supportedVendorId, false,
				true);

	}

	public void setSupportedVendorIds(long[] supportedVendorIds) {
		// FIXME: baranowb; setting mandatory to false
		super.setAvpsAsUInt32(Avp.SUPPORTED_VENDOR_ID, supportedVendorIds,
				false, true);
	}

	public void setVendorId(long vendorId) {
		// FIXME: baranowb; setting mandatory to false
		super.setAvpAsUInt32(Avp.VENDOR_ID, vendorId, false, true);

	}

	public void setVendorSpecificApplicationIds(
			VendorSpecificApplicationIdAvp[] vendorSpecificApplicationIds) {

		// FIXME: baranowb; not sure if this will work correctly.
		super.setAvpAsGroup(Avp.VENDOR_SPECIFIC_APPLICATION_ID,
				vendorSpecificApplicationIds, false, true);

	}

}
