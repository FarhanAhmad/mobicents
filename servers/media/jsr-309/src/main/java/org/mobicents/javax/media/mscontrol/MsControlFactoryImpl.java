package org.mobicents.javax.media.mscontrol;

import java.io.Reader;
import java.util.Properties;

import javax.media.mscontrol.MediaConfig;
import javax.media.mscontrol.MediaConfigException;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlFactory;
import javax.media.mscontrol.resource.ConfigSymbol;
import javax.media.mscontrol.resource.Parameters;
import javax.media.mscontrol.resource.video.VideoLayout;

import org.mobicents.javax.media.mscontrol.resource.ParametersImpl;
import org.mobicents.jsr309.mgcp.MgcpStackFactory;
import org.mobicents.jsr309.mgcp.MgcpWrapper;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MsControlFactoryImpl implements MsControlFactory {


	private Properties properties = null;
	private MgcpWrapper mgcpWrapper = null;
	
	public MsControlFactoryImpl(Properties properties) {
		this.properties = properties;

		MgcpStackFactory mgcpStackFactory = MgcpStackFactory.getInstance();
		this.mgcpWrapper = mgcpStackFactory.getMgcpStackProvider(properties);

		if (mgcpWrapper == null) {
			throw new RuntimeException("Could not create instance of MediaSessionFactory. Check the exception in logs");
		}


	}

	public MediaSession createMediaSession() {
		return new MediaSessionImpl(this.mgcpWrapper);
	}

	public Parameters createParameters() {
		return new ParametersImpl();
	}

	public VideoLayout createVideoLayout(String arg0, Reader arg1) throws MediaConfigException {
		// TODO Auto-generated method stub
		return null;
	}

	public <C extends MediaConfig> C getMediaConfig(ConfigSymbol<C> arg0) throws MediaConfigException {
		// TODO Auto-generated method stub
		return null;
	}

	public <C extends MediaConfig> C getMediaConfig(Class<C> arg0, Reader arg1) throws MediaConfigException {
		// TODO Auto-generated method stub
		return null;
	}

	public VideoLayout getPresetLayout(String arg0) throws MediaConfigException {
		// TODO Auto-generated method stub
		return null;
	}

	public VideoLayout[] getPresetLayouts(int arg0) throws MediaConfigException {
		// TODO Auto-generated method stub
		return null;
	}

	public Properties getProperties() {
		return this.properties;
	}

}