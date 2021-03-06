/*
 * File Name     : CreateConnectionHandle.java
 *
 * The JAIN MGCP API implementaion.
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.mgcp.stack;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConflictingParameterException;
import jain.protocol.ip.mgcp.message.parms.ConnectionDescriptor;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionMode;
import jain.protocol.ip.mgcp.message.parms.DigitMap;
import jain.protocol.ip.mgcp.message.parms.NotificationRequestParms;
import jain.protocol.ip.mgcp.message.parms.NotifiedEntity;
import jain.protocol.ip.mgcp.message.parms.RequestIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.mobicents.mgcp.stack.parser.MgcpContentHandler;
import org.mobicents.mgcp.stack.parser.MgcpMessageParser;
import org.mobicents.mgcp.stack.parser.Utils;

/**
 * 
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 * @author amit bhayani
 */
public class CreateConnectionHandler extends TransactionHandler {

	private static final Logger logger = Logger.getLogger(CreateConnectionHandler.class);

	private CreateConnection command;
	private CreateConnectionResponse response;

	/** Creates a new instance of CreateConnectionHandle */
	public CreateConnectionHandler(JainMgcpStackImpl stack) {
		super(stack);
	}

	public CreateConnectionHandler(JainMgcpStackImpl stack, InetAddress address, int port) {
		super(stack, address, port);
	}

	public JainMgcpCommandEvent decodeCommand(final String message) throws ParseException {
		Utils utils = utilsFactory.allocate();
		MgcpMessageParser parser = new MgcpMessageParser(new CommandContentHandle(utils));
		try {
			parser.parse(message);
		} catch (Exception e) {
			throw new ParseException(e.getMessage(), -1);
		} finally {
			utilsFactory.deallocate(utils);
		}

		NotifiedEntity notifiedEntity = command.getNotifiedEntity();
		if (command.getNotifiedEntity() != null) {
			this.stack.provider.setNotifiedEntity(notifiedEntity);
		}

		return command;
	}

	public JainMgcpResponseEvent decodeResponse(String message) throws ParseException {
		Utils utils = utilsFactory.allocate();
		MgcpMessageParser parser = new MgcpMessageParser(new ResponseContentHandle(utils));
		try {
			parser.parse(message);
		} catch (IOException e) {
			logger.error("Decode of CRCX Response failed ", e);
		} finally {
			utilsFactory.deallocate(utils);
		}

		return response;
	}

	public String encode(JainMgcpCommandEvent event) {
		// encode message header
		Utils utils = utilsFactory.allocate();
		CreateConnection evt = (CreateConnection) event;

		StringBuffer s = new StringBuffer();
		s.append("CRCX ").append(evt.getTransactionHandle()).append(SINGLE_CHAR_SPACE).append(
				evt.getEndpointIdentifier()).append(MGCP_VERSION).append(NEW_LINE);

		// encode mandatory parameters

		s.append("C: ").append(evt.getCallIdentifier()).append(NEW_LINE);

		s.append("M: ").append(evt.getMode()).append(NEW_LINE);

		// encode optional parameters

		if (evt.getBearerInformation() != null) {
			s.append("B:e:").append(evt.getBearerInformation()).append(NEW_LINE);

		}

		if (evt.getNotifiedEntity() != null) {
			s.append("N: ").append(evt.getNotifiedEntity()).append(NEW_LINE);

		}

		if (evt.getLocalConnectionOptions() != null) {
			s.append("L: ").append(utils.encodeLocalOptionValueList(evt.getLocalConnectionOptions()));
		}

		if (evt.getSecondEndpointIdentifier() != null) {
			s.append("Z2: ").append(evt.getSecondEndpointIdentifier()).append(NEW_LINE);
		}

		if (evt.getNotificationRequestParms() != null) {
			s.append(utils.encodeNotificationRequestParms(evt.getNotificationRequestParms()));
		}

		if (evt.getRemoteConnectionDescriptor() != null) {
			s.append(NEW_LINE).append(evt.getRemoteConnectionDescriptor());
		}
		utilsFactory.deallocate(utils);
		// return msg;
		return s.toString();
	}

	public String encode(JainMgcpResponseEvent event) {
		CreateConnectionResponse response = (CreateConnectionResponse) event;
		ReturnCode returnCode = response.getReturnCode();

		StringBuffer s = new StringBuffer();
		s.append(returnCode.getValue()).append(SINGLE_CHAR_SPACE).append(response.getTransactionHandle()).append(
				SINGLE_CHAR_SPACE).append(returnCode.getComment()).append(NEW_LINE);

		if (response.getConnectionIdentifier() != null) {
			s.append("I: ").append(response.getConnectionIdentifier()).append(NEW_LINE);
		}
		if (response.getSpecificEndpointIdentifier() != null) {
			s.append("Z: ").append(response.getSpecificEndpointIdentifier()).append(NEW_LINE);
		}
		if (response.getSecondConnectionIdentifier() != null) {
			s.append("I2: ").append(response.getSecondConnectionIdentifier()).append(NEW_LINE);
		}
		if (response.getSecondEndpointIdentifier() != null) {
			s.append("Z2: ").append(response.getSecondEndpointIdentifier()).append(NEW_LINE);
		}
		if (response.getLocalConnectionDescriptor() != null) {
			s.append(NEW_LINE).append(response.getLocalConnectionDescriptor());
		}
		return s.toString();

	}

	private class CommandContentHandle implements MgcpContentHandler {
		private Utils utils = null;

		public CommandContentHandle(Utils utils) {
			this.utils = utils;
		}

		/**
		 * Receive notification of the header of a message. Parser will call
		 * this method to report about header reading.
		 * 
		 * @param header
		 *            the header from the message.
		 */
		public void header(String header) throws ParseException {
			
			command = new CreateConnection(source != null ? source : stack, new CallIdentifier("0"), endpoint,
					ConnectionMode.Inactive);
			command.setTransactionHandle(remoteTID);
		}

		/**
		 * Receive notification of the parameter of a message. Parser will call
		 * this method to report about parameter reading.
		 * 
		 * @param name
		 *            the name of the paremeter
		 * @param value
		 *            the value of the parameter.
		 */
		public void param(String name, String value) throws ParseException {
			if (name.equalsIgnoreCase("B")) {
				command.setBearerInformation(utils.decodeBearerInformation(value));
			} else if (name.equalsIgnoreCase("c")) {
				command.setCallIdentifier(new CallIdentifier(value));
			}
			if (name.equalsIgnoreCase("L")) {
				command.setLocalConnectionOptions(utils.decodeLocalOptionValueList(value));
			} else if (name.equalsIgnoreCase("m")) {
				command.setMode(utils.decodeConnectionMode(value));
			} else if (name.equalsIgnoreCase("N")) {
				command.setNotifiedEntity(utils.decodeNotifiedEntity(value, true));
			} else if (name.equalsIgnoreCase("X")) {
				command.setNotificationRequestParms(new NotificationRequestParms(new RequestIdentifier(value)));
			} else if (name.equalsIgnoreCase("D")) {
				command.getNotificationRequestParms().setDigitMap(new DigitMap(value));
			} else if (name.equalsIgnoreCase("R")) {
				command.getNotificationRequestParms().setRequestedEvents(utils.decodeRequestedEventList(value));
			} else if (name.equalsIgnoreCase("S")) {
				command.getNotificationRequestParms().setSignalRequests(utils.decodeEventNames(value));
			} else if (name.equalsIgnoreCase("T")) {
				command.getNotificationRequestParms().setDetectEvents(utils.decodeEventNames(value));
			} else if (name.equalsIgnoreCase("Z2")) {
				try {
					command.setSecondEndpointIdentifier(utils.decodeEndpointIdentifier(value));
				} catch (ConflictingParameterException e) {
				}
			}
		}

		/**
		 * Receive notification of the session description. Parser will call
		 * this method to report about session descriptor reading.
		 * 
		 * @param sd
		 *            the session description from message.
		 */
		public void sessionDescription(String sd) throws ParseException {
			try {
				command.setRemoteConnectionDescriptor(new ConnectionDescriptor(sd));
			} catch (Exception e) {
				logger.error("Unexpected error in session descriptor", e);
			}
		}
	}

	private class ResponseContentHandle implements MgcpContentHandler {
		private Utils utils = null;

		public ResponseContentHandle(Utils utils) {
			this.utils = utils;

		}

		/**
		 * Receive notification of the header of a message. Parser will call
		 * this method to report about header reading.
		 * 
		 * @param header
		 *            the header from the message.
		 */
		public void header(String header) throws ParseException {
			String[] tokens = utils.splitStringBySpace(header);

			int tid = Integer.parseInt(tokens[1]);
			response = new CreateConnectionResponse(source != null ? source : stack, utils.decodeReturnCode(Integer
					.parseInt(tokens[0])), new ConnectionIdentifier("00"));
			response.setTransactionHandle(tid);
		}

		/**
		 * Receive notification of the parameter of a message. Parser will call
		 * this method to report about parameter reading.
		 * 
		 * @param name
		 *            the name of the paremeter
		 * @param value
		 *            the value of the parameter.
		 */
		public void param(String name, String value) throws ParseException {
			if (name.equalsIgnoreCase("I")) {
				response.setConnectionIdentifier(new ConnectionIdentifier(value));
			} else if (name.equalsIgnoreCase("I2")) {
				response.setSecondConnectionIdentifier(new ConnectionIdentifier(value));
			} else if (name.equalsIgnoreCase("Z")) {
				response.setSpecificEndpointIdentifier(utils.decodeEndpointIdentifier(value));
			} else if (name.equalsIgnoreCase("Z2")) {
				response.setSecondEndpointIdentifier(utils.decodeEndpointIdentifier(value));
			}
		}

		/**
		 * Receive notification of the session description. Parser will call
		 * this method to report about session descriptor reading.
		 * 
		 * @param sd
		 *            the session description from message.
		 */
		public void sessionDescription(String sd) throws ParseException {
			response.setLocalConnectionDescriptor(new ConnectionDescriptor(sd));
		}
	}

	@Override
	public JainMgcpResponseEvent getProvisionalResponse() {

		CreateConnectionResponse provisionalResponse = null;

		if (!sent) {

			// TODO We are hardcoding connectionIdentifier here. This would
			// differ from final response. Problem?
			ConnectionIdentifier connectionIdentifier = new ConnectionIdentifier("1");

			provisionalResponse = new CreateConnectionResponse(source != null ? source : stack,
					ReturnCode.Transaction_Being_Executed, connectionIdentifier);
			provisionalResponse.setTransactionHandle(remoteTID);
		}

		return provisionalResponse;
	}

}
