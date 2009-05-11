package org.mobicents.media.server.spi.resource;

import org.mobicents.media.MediaSource;

/**
 * 
 * @author amit bhayani
 *
 */
public interface Rfc2833Generator extends MediaSource {
	
	public void setDigit(String digit);
	public String getDigit();
	
	public void setDuration(int duration);
	public int getDuration();
	
	public void setVolume(int volume);
	public int getVolume();

}