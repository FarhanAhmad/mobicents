/*
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.servlet.sip.testsuite.click2call;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.sip.message.Response;

import org.apache.log4j.Logger;
import org.cafesip.sipunit.SipCall;
import org.cafesip.sipunit.SipPhone;
import org.cafesip.sipunit.SipStack;
import org.mobicents.servlet.sip.SipServletTestCase;

public class Click2CallBasicTest extends SipServletTestCase {

	private static final String CLICK2DIAL_URL = "http://127.0.0.1:8080/click2call/call";
	private static final String CLICK2DIAL_PARAMS = "?from=sip:from@127.0.0.1:5056&to=sip:to@127.0.0.1:5057";
	private static transient Logger logger = Logger.getLogger(Click2CallBasicTest.class);

	private SipStack[] sipStackReceivers;

	private SipPhone[] sipPhoneReceivers;

	private static final int timeout = 5000;

	private static final int receiversCount = 2;

	// Don't restart the server for this set of tests.
	private static boolean firstTime = true;

	public Click2CallBasicTest(String name) {
		super(name);
	}

	@Override
	public void setUp() throws Exception {
		if (firstTime) {
			super.setUp();			
		}
		firstTime = true;
	}

	@Override
	public void tearDown() throws Exception {
		for (SipPhone sp : sipPhoneReceivers) {
			if(sp != null) {
				sp.dispose();
			}
		}
		for (SipStack ss : sipStackReceivers) {
			if(ss != null) {
				ss.dispose();
			}
		}
		super.tearDown();
	}

	@Override
	public void deployApplication() {
		assertTrue(tomcat
				.deployContext(
						projectHome
								+ "/sip-servlets-test-suite/applications/click-to-call-servlet/src/main/sipapp",
						"click2call-context", "/click2call"));
	}

	@Override
	protected String getDarConfigurationFile() {
		return "file:///"
				+ projectHome
				+ "/sip-servlets-test-suite/testsuite/src/test/resources/"
				+ "org/mobicents/servlet/sip/testsuite/click2call/click-to-call-dar.properties";
	}

	public SipStack makeStack(String transport, int port) throws Exception {
		Properties properties = new Properties();
		String peerHostPort1 = "127.0.0.1:5070";
		properties.setProperty("javax.sip.OUTBOUND_PROXY", peerHostPort1 + "/"
				+ "udp");
		properties.setProperty("javax.sip.STACK_NAME", "UAC_" + transport + "_"
				+ port);
		properties.setProperty("sipunit.BINDADDR", "127.0.0.1");
		properties.setProperty("gov.nist.javax.sip.DEBUG_LOG",
				"logs/simplesipservlettest_debug_port" + port + ".txt");
		properties.setProperty("gov.nist.javax.sip.SERVER_LOG",
				"logs/simplesipservlettest_log_port" + port + ".xml");
		properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL",
				"32");
		return new SipStack(transport, port, properties);
	}

	public void setupPhones() throws Exception {
		sipStackReceivers = new SipStack[receiversCount];
		sipPhoneReceivers = new SipPhone[receiversCount];

		sipStackReceivers[0] = makeStack(SipStack.PROTOCOL_UDP, 5057);
		sipPhoneReceivers[0] = sipStackReceivers[0].createSipPhone("127.0.0.1",
				SipStack.PROTOCOL_UDP, 5070, "sip:to@127.0.0.1");

		sipStackReceivers[1] = makeStack(SipStack.PROTOCOL_UDP, 5056);
		sipPhoneReceivers[1] = sipStackReceivers[1].createSipPhone("127.0.0.1",
				SipStack.PROTOCOL_UDP, 5070, "sip:from@127.0.0.1");
	}

	public void init() throws Exception {
		setupPhones();
	}

	public void testClickToCallNoConvergedSession()
			throws Exception {
		init();
		SipCall[] receiverCalls = new SipCall[receiversCount];

		receiverCalls[0] = sipPhoneReceivers[0].createSipCall();
		receiverCalls[1] = sipPhoneReceivers[1].createSipCall();

		receiverCalls[0].listenForIncomingCall();
		receiverCalls[1].listenForIncomingCall();

		logger.info("Trying to reach url : " + CLICK2DIAL_URL
				+ CLICK2DIAL_PARAMS);

		URL url = new URL(CLICK2DIAL_URL + CLICK2DIAL_PARAMS);
		InputStream in = url.openConnection().getInputStream();

		byte[] buffer = new byte[10000];
		int len = in.read(buffer);
		String httpResponse = "";
		for (int q = 0; q < len; q++)
			httpResponse += (char) buffer[q];
		logger.info("Received the follwing HTTP response: " + httpResponse);

		receiverCalls[0].waitForIncomingCall(timeout);

		assertTrue(receiverCalls[0].sendIncomingCallResponse(Response.RINGING,
				"Ringing", 0));
		assertTrue(receiverCalls[0].sendIncomingCallResponse(Response.OK, "OK",
				0));

		receiverCalls[1].waitForIncomingCall(timeout);

		assertTrue(receiverCalls[1].sendIncomingCallResponse(Response.RINGING,
				"Ringing", 0));
		assertTrue(receiverCalls[1].sendIncomingCallResponse(Response.OK, "OK",
				0));

		assertTrue(receiverCalls[1].waitForAck(timeout));
		assertTrue(receiverCalls[0].waitForAck(timeout));

		assertTrue(receiverCalls[0].disconnect());
		assertTrue(receiverCalls[1].waitForDisconnect(timeout));
		assertTrue(receiverCalls[1].respondToDisconnect());
	}
}
