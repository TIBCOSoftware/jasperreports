/*
G * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.iconlabel;

import net.sf.jasperreports.components.ComponentsExtensionsRegistryFactory;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.util.JRBoxUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: TableReport.java 6461 2013-08-30 17:03:40Z teodord $
 */
public class IconLabelComponentUtil
{
	/**
	 * 
	 */
	public static JRDesignComponentElement createIconLabelComponentElement(JRTextElement textElement)
	{
		return createIconLabelComponentElement(textElement, textElement);
	}
	
	/**
	 * 
	 */
	public static JRDesignComponentElement createIconLabelComponentElement(JRTextElement parentElement, JRTextElement textElement)
	{
		JRDesignComponentElement componentElement = new JRDesignComponentElement(textElement.getDefaultStyleProvider());
		componentElement.setComponentKey(new ComponentKey(
				ComponentsExtensionsRegistryFactory.NAMESPACE, null, ComponentsExtensionsRegistryFactory.ICONLABEL_COMPONENT_NAME));
		componentElement.setX(textElement.getX());
		componentElement.setY(textElement.getY());
		componentElement.setHeight(textElement.getHeight());
		componentElement.setWidth(textElement.getWidth());
		componentElement.setStyle(textElement.getStyle());
//		componentElement.setStyleNameReference(textElement.getStyleNameReference());
		componentElement.setMode(parentElement.getOwnModeValue());
		componentElement.setForecolor(parentElement.getOwnForecolor());
		componentElement.setBackcolor(parentElement.getOwnBackcolor());
		componentElement.setStretchType(parentElement.getStretchTypeValue());
		componentElement.setPositionType(parentElement.getPositionTypeValue());
		
		IconLabelComponent iconLabelComponent = new IconLabelComponent(textElement.getDefaultStyleProvider());
		iconLabelComponent.setIconPosition(IconPositionEnum.END);
		iconLabelComponent.setVerticalAlign(parentElement.getVerticalAlignmentValue());//FIXMESORT here might have problem with conditional style
		iconLabelComponent.setHorizontalAlign(parentElement.getHorizontalAlignmentValue());
		iconLabelComponent.setLabelFill(ContainerFillEnum.NONE);
		iconLabelComponent.setLineBox(parentElement.getLineBox().clone(iconLabelComponent));
		iconLabelComponent.getLineBox().setPadding(0);
		iconLabelComponent.getLineBox().setLeftPadding(0);
		iconLabelComponent.getLineBox().setRightPadding(0);
		iconLabelComponent.getLineBox().setTopPadding(0);
		iconLabelComponent.getLineBox().setBottomPadding(0);
		
		JRDesignTextField labelTextField = new JRDesignTextField(textElement.getDefaultStyleProvider());
		labelTextField.setStretchWithOverflow(true);
		labelTextField.setX(0);
		labelTextField.setY(0);
		labelTextField.setWidth(1);
		labelTextField.setHeight(textElement.getHeight());
//				labelTextField.setHeight(Math.max(1, headerTextElement.getHeight() 
//						- headerTextElement.getLineBox().getTopPadding() - headerTextElement.getLineBox().getBottomPadding()));
		labelTextField.setStyle(textElement.getStyle());
//		labelTextField.setStyleNameReference(textElement.getStyleNameReference());
		labelTextField.setMode(parentElement.getOwnModeValue());
		labelTextField.setFontSize(parentElement.getOwnFontSize());
		labelTextField.setFontName(parentElement.getOwnFontName());
		labelTextField.setForecolor(parentElement.getOwnForecolor());
		labelTextField.setBackcolor(parentElement.getOwnBackcolor());
		labelTextField.setBold(parentElement.isOwnBold());
		labelTextField.setItalic(parentElement.isOwnItalic());
		labelTextField.setUnderline(parentElement.isOwnUnderline());
		labelTextField.setStrikeThrough(parentElement.isOwnStrikeThrough());
		labelTextField.setHorizontalAlignment(parentElement.getOwnHorizontalAlignmentValue());
		labelTextField.setVerticalAlignment(parentElement.getOwnVerticalAlignmentValue());
		JRBoxUtil.copy(parentElement.getLineBox(), labelTextField.getLineBox());
		labelTextField.getLineBox().setRightPadding(0);
		labelTextField.getLineBox().getPen().setLineWidth(0);
		labelTextField.getLineBox().getLeftPen().setLineWidth(0);
		labelTextField.getLineBox().getRightPen().setLineWidth(0);
		labelTextField.getLineBox().getTopPen().setLineWidth(0);
		labelTextField.getLineBox().getBottomPen().setLineWidth(0);
		
		iconLabelComponent.setLabelTextField(labelTextField);
		
		JRDesignTextField iconTextField = new JRDesignTextField(textElement.getDefaultStyleProvider());
		iconTextField.setStretchWithOverflow(true);
		iconTextField.setX(0);
		iconTextField.setY(0);
		iconTextField.setWidth(1);
		iconTextField.setHeight(1);
		iconTextField.setStyle(textElement.getStyle());
//		iconTextField.setStyleNameReference(textElement.getStyleNameReference());
		iconTextField.setMode(parentElement.getOwnModeValue());
		iconTextField.setFontName("Pictonic");//FIXMESORT use constant
		iconTextField.setFontSize((int)(parentElement.getFontSize() * 0.8f));//FIXMESORT problem with conditional style?
		iconTextField.setForecolor(parentElement.getOwnForecolor());
		iconTextField.setBackcolor(parentElement.getOwnBackcolor());
		iconTextField.setBold(parentElement.isOwnBold());
		iconTextField.setItalic(parentElement.isOwnItalic());
		iconTextField.setUnderline(parentElement.isOwnUnderline());
		iconTextField.setStrikeThrough(parentElement.isOwnStrikeThrough());
		iconTextField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		iconTextField.setVerticalAlignment(parentElement.getOwnVerticalAlignmentValue());
		JRBoxUtil.copy(parentElement.getLineBox(), iconTextField.getLineBox());
		iconTextField.getLineBox().setLeftPadding(0);
		iconTextField.getLineBox().getPen().setLineWidth(0);
		iconTextField.getLineBox().getLeftPen().setLineWidth(0);
		iconTextField.getLineBox().getRightPen().setLineWidth(0);
		iconTextField.getLineBox().getTopPen().setLineWidth(0);
		iconTextField.getLineBox().getBottomPen().setLineWidth(0);
		
		iconLabelComponent.setIconTextField(iconTextField);
		
		componentElement.setComponent(iconLabelComponent);

		return componentElement;
	}
}
