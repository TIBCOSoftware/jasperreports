/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.list;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.fill.JRFillCloneFactory;
import net.sf.jasperreports.engine.fill.JRFillCloneable;
import net.sf.jasperreports.engine.fill.JRFillElementContainer;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * List contents fill element container.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FillListContents extends JRFillElementContainer
{

	private final int contentsHeight;
	
	protected FillListContents(ListContents listContents,
			JRFillObjectFactory factory)
	{
		super(factory.getFiller(), listContents, factory);
		
		this.contentsHeight = listContents.getHeight();
		
		initElements();
		initConditionalStyles();
	}

	public FillListContents(FillListContents fillListContents,
			JRFillCloneFactory factory)
	{
		super(fillListContents, factory);
		
		this.contentsHeight = fillListContents.contentsHeight;

		initElements();
		initConditionalStyles();
	}

	public int getHeight()
	{
		return contentsHeight;
	}
	
	protected int getContainerHeight()
	{
		return contentsHeight;
	}
	
	protected void evaluateContents() throws JRException
	{
		evaluateConditionalStyles(JRExpression.EVALUATION_DEFAULT);
		evaluate(JRExpression.EVALUATION_DEFAULT);
	}
	
	protected void prepare(int availableHeight) throws JRException
	{
		initFill();
		resetElements();
		prepareElements(availableHeight, true);
	}
	
	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		return new FillListContents(this, factory);
	}
	
	public FillListContents createClone()
	{
		JRFillCloneFactory factory = new JRFillCloneFactory();
		return new FillListContents(this, factory);
	}
	
	// overridden for access
	protected int getStretchHeight()
	{
		return super.getStretchHeight();
	}
	
	// overridden for access
	protected void rewind() throws JRException
	{
		super.rewind();
	}
	
	protected void stretchTo(int height)
	{
		setStretchHeight(height);
	}

	protected void finalizeElementPositions()
	{
		stretchElements();
		moveBandBottomElements();
		removeBlankElements();
	}
}
