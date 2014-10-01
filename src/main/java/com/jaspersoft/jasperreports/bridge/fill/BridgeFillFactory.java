package com.jaspersoft.jasperreports.bridge.fill;

import com.jaspersoft.jasperreports.bridge.BridgeConstants;
import com.jaspersoft.jasperreports.bridge.BridgeComponent;
import java.io.Serializable;
import net.sf.jasperreports.engine.component.*;
import net.sf.jasperreports.engine.fill.*;

public class BridgeFillFactory implements ComponentFillFactory, Serializable
{
        private static final long serialVersionUID = BridgeConstants.SERIAL_VERSION_UID;

	public FillComponent toFillComponent(Component component,
			JRFillObjectFactory factory)
	{
                return new BridgeFillComponent((BridgeComponent) component, factory);
        }

	public FillComponent cloneFillComponent(FillComponent component,
			JRFillCloneFactory factory)
	{
                throw new UnsupportedOperationException();
	}

}
