package com.jaspersoft.jasperreports.customvisualization.fill;

import java.io.Serializable;

import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentFillFactory;
import net.sf.jasperreports.engine.component.FillComponent;
import net.sf.jasperreports.engine.fill.JRFillCloneFactory;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import com.jaspersoft.jasperreports.customvisualization.CVComponent;
import com.jaspersoft.jasperreports.customvisualization.CVConstants;

public class CVFillFactory implements ComponentFillFactory, Serializable
{
        private static final long serialVersionUID = CVConstants.SERIAL_VERSION_UID;

	public FillComponent toFillComponent(Component component,
			JRFillObjectFactory factory)
	{
                return new CVFillComponent((CVComponent) component, factory);
        }

	public FillComponent cloneFillComponent(FillComponent component,
			JRFillCloneFactory factory)
	{
                throw new UnsupportedOperationException();
	}

}
