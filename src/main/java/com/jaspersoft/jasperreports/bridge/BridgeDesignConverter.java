package com.jaspersoft.jasperreports.bridge;



import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.component.*;
import net.sf.jasperreports.engine.convert.*;
import net.sf.jasperreports.engine.type.*;
import net.sf.jasperreports.engine.util.JRImageLoader;


public class BridgeDesignConverter extends ElementIconConverter implements ComponentDesignConverter
{

        public static final String COMPONENT_ICON = "com/jaspersoft/jasperreports/bridge/icon-32.png";
	/**
	 *
	 */
	private final static BridgeDesignConverter INSTANCE = new BridgeDesignConverter();
	
	/**
	 *
	 */
	private BridgeDesignConverter()
	{
                super(COMPONENT_ICON);
        }

	/**
	 *
	 */
	public static BridgeDesignConverter getInstance()
	{
        	return INSTANCE;
	}

	/**
	 *
	 */
        @Override
	public JRPrintElement convert(ReportConverter reportConverter, JRComponentElement element)
	{
            
                JRPrintElement pe = super.convert(reportConverter,element);
                return pe;
            
	}

}
