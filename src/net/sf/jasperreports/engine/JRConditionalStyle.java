package net.sf.jasperreports.engine;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public interface JRConditionalStyle extends JRStyle
{
	public JRExpression getConditionExpression();
}
