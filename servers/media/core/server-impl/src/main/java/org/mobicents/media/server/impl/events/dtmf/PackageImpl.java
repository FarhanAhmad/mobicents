/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */

package org.mobicents.media.server.impl.events.dtmf;

import org.mobicents.media.server.impl.AbstractSignal;
import org.mobicents.media.server.impl.events.EventPackage;
import org.mobicents.media.server.spi.events.RequestedSignal;
import org.mobicents.media.server.spi.events.dtmf.DtmfRequestedSignal;
import org.mobicents.media.server.spi.events.pkg.DTMF;

/**
 * 
 * @author Oleg Kulikov
 */
public class PackageImpl implements EventPackage {

	public AbstractSignal getSignal(RequestedSignal requestedSignal) {
		// if (requestedSignal.getID().equals(Announcement.PLAY)) {
		// return new AnnSignal(((PlayRequestedSignal)
		// requestedSignal).getURL());
		// } else

		if (requestedSignal.getID().equals(DTMF.DTMF)) {
			return new DTMFSignal(((DtmfRequestedSignal) requestedSignal).getTone());
		}
		return null;
	}

}
