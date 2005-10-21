/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage.ObjectIDPair;
import net.sf.jasperreports.engine.fill.JRBaseFiller.BoundElementMap;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillBand extends JRFillElementContainer implements JRBand, JRVirtualPrintPage.IdentityDataProvider
{
	

	/**
	 *
	 */
	private JRBand parent = null;

	private boolean isPrintWhenTrue = true;

	/**
	 *
	 */
	private boolean isNewPageColumn = false;
	private boolean isFirstWholeOnPageColumn = false;
	private Map isNewGroupMap = new HashMap();

	/**
	 * Map of elements to be resolved at band level.
	 */
	protected BoundElementMap boundElements;
	
	/**
	 * Per page map of elements to be resolved at band level.
	 */
	protected Map pageToBoundElements;

	/**
	 *
	 */
	protected JRFillBand(
		JRBaseFiller filler,
		JRBand band, 
		JRFillObjectFactory factory
		)
	{
		super(filler, band, factory);

		this.parent = band;
		
		if (this.elements != null && this.elements.length > 0)
		{
			for(int i = 0; i < this.elements.length; i++)
			{
				this.elements[i].setBand(this);
			}
		}
		
		initElements();
	}


	/**
	 *
	 */
	protected void setNewPageColumn(boolean isNew)
	{
		this.isNewPageColumn = isNew;
	}


	/**
	 *
	 */
	protected boolean isNewPageColumn()
	{
		return this.isNewPageColumn;
	}

	
	/**
	 * Decides whether this band is the for whole band on the page/column.
	 * 
	 * @return whether this band is the for whole band on the page/column
	 */
	protected boolean isFirstWholeOnPageColumn()
	{
		return isFirstWholeOnPageColumn;
	}

	
	/**
	 *
	 */
	protected void setNewGroup(JRGroup group, boolean isNew)
	{
		this.isNewGroupMap.put(group, isNew ? Boolean.TRUE : Boolean.FALSE);
	}


	/**
	 *
	 */
	protected boolean isNewGroup(JRGroup group)
	{
		Boolean value = (Boolean)this.isNewGroupMap.get(group);
		
		if (value == null)
		{
			value = Boolean.FALSE;
		}
		
		return value.booleanValue();
	}


	/**
	 *
	 */
	public int getHeight()
	{
		return (this.parent != null ? this.parent.getHeight() : 0);
	}
		
	/**
	 *
	 */
	public boolean isSplitAllowed()
	{
		return this.parent.isSplitAllowed();
	}
		
	/**
	 *
	 */
	public void setSplitAllowed(boolean isSplitAllowed)
	{
	}
		
	/**
	 *
	 */
	public JRExpression getPrintWhenExpression()
	{
		return (this.parent != null ? this.parent.getPrintWhenExpression() : null);
	}

	/**
	 *
	 */
	protected boolean isPrintWhenExpressionNull()
	{
		return (this.getPrintWhenExpression() == null);
	}
	
	/**
	 *
	 */
	protected boolean isPrintWhenTrue()
	{
		return this.isPrintWhenTrue;
	}
	
	/**
	 *
	 */
	protected void setPrintWhenTrue(boolean isPrintWhenTrue)
	{
		this.isPrintWhenTrue = isPrintWhenTrue;
	}
	
	/**
	 *
	 */
	protected boolean isToPrint()
	{
		return 
			(this.isPrintWhenExpressionNull() ||
			(!this.isPrintWhenExpressionNull() && 
			this.isPrintWhenTrue()));
	}


	/**
	 *
	 */
	protected void evaluatePrintWhenExpression(
		byte evaluation
		) throws JRException
	{
		boolean isPrintTrue = false;

		JRExpression expression = this.getPrintWhenExpression();
		if (expression != null)
		{
			Boolean printWhenExpressionValue = (Boolean)this.filler.evaluateExpression(expression, evaluation);
			if (printWhenExpressionValue == null)
			{
				isPrintTrue = false;
			}
			else
			{
				isPrintTrue = printWhenExpressionValue.booleanValue();
			}
		}

		this.setPrintWhenTrue(isPrintTrue);
	}



	/**
	 *
	 */
	protected JRPrintBand refill(
		int availableStretchHeight
		) throws JRException
	{
		this.rewind();

		return this.fill(availableStretchHeight);
	}
	
	
	/**
	 *
	 */
	protected JRPrintBand fill() throws JRException
	{
		return this.fill(0, false);
	}
	
	
	/**
	 *
	 */
	protected JRPrintBand fill(
		int availableStretchHeight
		) throws JRException
	{
		return this.fill(availableStretchHeight, true);
	}
	
	
	/**
	 *
	 */
	protected JRPrintBand fill(
		int availableStretchHeight,
		boolean isOverflowAllowed
		) throws JRException
	{
		if (
			Thread.currentThread().isInterrupted()
			|| this.filler.isInterrupted()
			)
		{
			// child fillers will stop if this parent filler was marked as interrupted
			this.filler.setInterrupted(true);

			throw new JRFillInterruptedException();
		}
		
		initFill();
		
		if (isNewPageColumn && !isOverflow)
		{
			isFirstWholeOnPageColumn = true;
		}
		
		this.resetElements();

		this.prepareElements(availableStretchHeight, isOverflowAllowed);

		this.stretchElements();

		this.moveBandBottomElements();

		this.removeBlankElements();
		
		isFirstWholeOnPageColumn = isNewPageColumn && isOverflow;
		this.isNewPageColumn = false;
		this.isNewGroupMap = new HashMap();

		JRPrintBand printBand = new JRPrintBand();
		this.fillElements(printBand);
		
		return printBand;
	}


	protected void initBoundElementMap(boolean perPageElements)
	{
		if (perPageElements)
		{
			pageToBoundElements = new HashMap();
			boundElements = filler.new BoundElementMap(pageToBoundElements);
		}
		else
		{
			boundElements = filler.new BoundElementMap();
		}
	}


	public ObjectIDPair[] getIdentityData(JRVirtualPrintPage page)
	{
		Set allElements = new HashSet();
		JRBaseFiller.addElements(allElements, pageToBoundElements, page);

		return JRBaseFiller.createIdentityData(allElements);
	}


	public void setIdentityData(JRVirtualPrintPage page, ObjectIDPair[] identityData)
	{
		JRBaseFiller.updateIdentityData(pageToBoundElements, page, boundElements, identityData);
	}


	protected int getContainerHeight()
	{
		return getHeight();
	}

}
