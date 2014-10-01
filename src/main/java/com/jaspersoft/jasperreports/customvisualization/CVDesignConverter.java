package com.jaspersoft.jasperreports.customvisualization;



import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.component.ComponentDesignConverter;
import net.sf.jasperreports.engine.convert.ElementIconConverter;
import net.sf.jasperreports.engine.convert.ReportConverter;


public class CVDesignConverter extends ElementIconConverter implements ComponentDesignConverter
{

        public static final String COMPONENT_ICON = "com/jaspersoft/jasperreports/customvisualization/icon-32.png";
	/**
	 *
	 */
	private final static CVDesignConverter INSTANCE = new CVDesignConverter();
	
	/**
	 *
	 */
	private CVDesignConverter()
	{
                super(COMPONENT_ICON);
        }

	/**
	 *
	 */
	public static CVDesignConverter getInstance()
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
