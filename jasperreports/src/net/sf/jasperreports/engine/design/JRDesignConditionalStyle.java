package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.base.JRBaseConditionalStyle;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRConditionalStyle;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignConditionalStyle extends JRBaseConditionalStyle implements JRConditionalStyle
{

	/**
	 *
	 */
	public JRDesignConditionalStyle()
	{
	}

	/**
	 *
	 */
	public void setConditionExpression(JRExpression conditionExpression)
	{
		this.conditionExpression = conditionExpression;
	}

	/**
	 *
	 */
	public void setParentStyle(JRStyle parentStyle)
	{
		this.parentStyle = parentStyle;
	}

}
