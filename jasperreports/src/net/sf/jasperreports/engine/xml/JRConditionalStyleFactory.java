package net.sf.jasperreports.engine.xml;

import java.util.Map;
import java.awt.Color;

import org.xml.sax.Attributes;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JRStyle;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id
 */
public class JRConditionalStyleFactory extends JRBaseFactory
{
	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignConditionalStyle style = new JRDesignConditionalStyle();
		JRDesignStyle parentStyle = (JRDesignStyle) digester.peek();

		style.setParentStyle(parentStyle);
		parentStyle.addConditionalStyle(style);

		return style;
	}
}
