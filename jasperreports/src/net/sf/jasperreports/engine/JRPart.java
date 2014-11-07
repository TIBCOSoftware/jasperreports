/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.part.PartComponent;
import net.sf.jasperreports.engine.part.PartEvaluationTime;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBand.java 7045 2014-04-24 14:20:46Z shertage $
 */
public interface JRPart extends JRPropertiesHolder, JRCloneable, JRIdentifiable
{
	

	/**
	 * Returns the boolean expression that specifies if the part will be displayed.
	 */
	JRExpression getPrintWhenExpression();

	JRExpression getPartNameExpression();
		
	/**
	 * Returns the component type key for this part.
	 * 
	 * <p>
	 * The component type key needs to be set in order to locate the
	 * component manager. 
	 * 
	 * @return the component type key
	 */
	ComponentKey getComponentKey();
	

	/**
	 * Returns the component instance wrapped by this part.
	 * 
	 * @return the component instance
	 */
	PartComponent getComponent();


	/**
	 * Determines the moment at which this part is to be evaluated.
	 * 
	 * @return the evaluation time of this part
	 */
	PartEvaluationTime getEvaluationTime();
	
}
