package org.mobicents.mgcp.stack;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.message.Notify;
import jain.protocol.ip.mgcp.message.NotifyResponse;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.EventName;
import jain.protocol.ip.mgcp.message.parms.RequestIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.mobicents.mgcp.stack.parser.MgcpContentHandler;
import org.mobicents.mgcp.stack.parser.MgcpMessageParser;
import org.mobicents.mgcp.stack.parser.Utils;

public class NotifyHandler extends TransactionHandler {

	private Notify command;
	private NotifyResponse response;

	private static final Logger logger = Logger.getLogger(NotifyHandler.class);

	public NotifyHandler(JainMgcpStackImpl stack) {
		super(stack);
	}

	public NotifyHandler(JainMgcpStackImpl stack, InetAddress address, int port) {
		super(stack, address, port);
	}

	@Override
	public JainMgcpCommandEvent decodeCommand(String message) throws ParseException {
		Utils utils = utilsFactory.allocate();
		MgcpMessageParser parser = new MgcpMessageParser(new CommandContentHandle(utils));
		try {
			parser.parse(message);
		} catch (Exception e) {
			throw new ParseException(e.getMessage(), -1);
		} finally {
			utilsFactory.deallocate(utils);
		}

		return command;
	}

	@Override
	public JainMgcpResponseEvent decodeResponse(String message) throws ParseException {
		Utils utils = utilsFactory.allocate();
		MgcpMessageParser parser = new MgcpMessageParser(new ResponseContentHandle(utils));
		try {
			parser.parse(message);
		} catch (IOException e) {
			logger.error("Something wrong while parsing the NOTIFY Response received", e);
		} finally {
			utilsFactory.deallocate(utils);
		}

		return response;
	}

	@Override
	public String encode(JainMgcpCommandEvent event) {
		Notify notify = (Notify) event;
		StringBuffer message = new StringBuffer();
		message.append("NTFY ").append(event.getTransactionHandle()).append(SINGLE_CHAR_SPACE).append(
				notify.getEndpointIdentifier()).append(MGCP_VERSION).append(NEW_LINE);

		if (notify.getNotifiedEntity() != null) {
			message.append("N: ").append(notify.getNotifiedEntity()).append(NEW_LINE);
		}

		message.append("X: ").append(notify.getRequestIdentifier()).append(NEW_LINE);

		Utils utils = utilsFactory.allocate();
		message.append("O: ").append(utils.encodeEventNames(notify.getObservedEvents())).append(NEW_LINE);
		utilsFactory.deallocate(utils);

		return message.toString();
	}

	@Override
	public String encode(JainMgcpResponseEvent event) {
		StringBuffer s = new StringBuffer();
		s.append(event.getReturnCode().getValue()).append(SINGLE_CHAR_SPACE).append(event.getTransactionHandle())
				.append(SINGLE_CHAR_SPACE).append(event.getReturnCode().getComment()).append(NEW_LINE);
		return s.toString();

		// return event.getReturnCode().getValue() + " " +
		// event.getTransactionHandle() + " "
		// + event.getReturnCode().getComment() + "\n";
	}

	private class CommandContentHandle implements MgcpContentHandler {

		private Utils utils = null;

		public CommandContentHandle(Utils utils) {
			this.utils = utils;
		}

		public void header(String header) throws ParseException {

			command = new Notify(source != null ? source : stack, endpoint, new RequestIdentifier("0"), new EventName[] {});
			command.setTransactionHandle(remoteTID);
		}

		public void param(String name, String value) throws ParseException {
			if (name.equalsIgnoreCase("N")) {
				command.setNotifiedEntity(utils.decodeNotifiedEntity(value, false));
			} else if (name.equalsIgnoreCase("X")) {
				command.setRequestIdentifier(new RequestIdentifier(value));
			} else if (name.equalsIgnoreCase("O")) {
				command.setObservedEvents(utils.decodeEventNames(value));
			}
		}

		public void sessionDescription(String sd) throws ParseException {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	private class ResponseContentHandle implements MgcpContentHandler {
		private Utils utils;

		public ResponseContentHandle(Utils utils) {
			this.utils = utils;
		}

		public void header(String header) throws ParseException {
			String[] tokens = utils.splitStringBySpace(header);

			int tid = Integer.parseInt(tokens[1]);
			response = new NotifyResponse(source != null ? source : stack, utils.decodeReturnCode(Integer
					.parseInt(tokens[0])));
			response.setTransactionHandle(tid);
		}

		public void param(String name, String value) throws ParseException {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void sessionDescription(String sd) throws ParseException {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	@Override
	public JainMgcpResponseEvent getProvisionalResponse() {
		NotifyResponse provisionalresponse = null;
		if (!sent) {
			provisionalresponse = new NotifyResponse(source != null ? source : stack,
					ReturnCode.Transaction_Being_Executed);
		}
		return provisionalresponse;
	}
}
