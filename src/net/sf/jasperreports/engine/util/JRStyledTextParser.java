/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package dori.jasper.engine.util;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import dori.jasper.engine.JRRuntimeException;
import dori.jasper.engine.xml.JRXmlConstants;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRStyledTextParser
{

	/**
	 *
	 */
	private DocumentBuilder documentBuilder = null;


	/**
	 *
	 */
	public JRStyledTextParser()
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			documentBuilder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			throw new JRRuntimeException(e);
		}
	}


	/**
	 *
	 */
	public JRStyledText parse(Map attributes, String text) throws SAXException
	{
		JRStyledText styledText = new JRStyledText();
		
		Document document = null;

		try
		{
			document = documentBuilder.parse(new InputSource(new StringReader(text)));
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		
		parseStyle(styledText, document);
		
		styledText.addRun(new JRStyledText.Run(attributes, 0, styledText.length()));
		
		return styledText;
	}

	/**
	 *
	 */
	public void parseStyle(JRStyledText styledText, Node parentNode)
	{
		NodeList nodeList = parentNode.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.TEXT_NODE)
			{
				styledText.append(node.getNodeValue());
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				NamedNodeMap nodeAttrs = node.getAttributes();

				Map styleAttrs = new HashMap();

				if (nodeAttrs.getNamedItem("fontName") != null)
				{
					styleAttrs.put(
						TextAttribute.FAMILY, 
						nodeAttrs.getNamedItem("fontName").getNodeValue()
						);
				}

				if (nodeAttrs.getNamedItem("isBold") != null)
				{
					styleAttrs.put(
						TextAttribute.WEIGHT, 
						Boolean.valueOf(nodeAttrs.getNamedItem("isBold").getNodeValue()).booleanValue() 
							? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR
						);
				}

				if (nodeAttrs.getNamedItem("isItalic") != null)
				{
					styleAttrs.put(
						TextAttribute.POSTURE, 
						Boolean.valueOf(nodeAttrs.getNamedItem("isItalic").getNodeValue()).booleanValue() 
							? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR
						);
				}

				if (nodeAttrs.getNamedItem("isUnderline") != null)
				{
					styleAttrs.put(
						TextAttribute.UNDERLINE, 
						Boolean.valueOf(nodeAttrs.getNamedItem("isUnderline").getNodeValue()).booleanValue() 
							? TextAttribute.UNDERLINE_ON : null
						);
				}

				if (nodeAttrs.getNamedItem("isStrikeThrough") != null)
				{
					styleAttrs.put(
						TextAttribute.STRIKETHROUGH, 
						Boolean.valueOf(nodeAttrs.getNamedItem("isStrikeThrough").getNodeValue()).booleanValue() 
							? TextAttribute.STRIKETHROUGH_ON : null
						);
				}

				if (nodeAttrs.getNamedItem("size") != null)
				{
					styleAttrs.put(
						TextAttribute.SIZE, 
						new Float(nodeAttrs.getNamedItem("size").getNodeValue()) 
						);
				}

				if (nodeAttrs.getNamedItem("pdfFontName") != null)
				{
					styleAttrs.put(
						JRTextAttribute.PDF_FONT_NAME, 
						nodeAttrs.getNamedItem("pdfFontName").getNodeValue()
						);
				}

				if (nodeAttrs.getNamedItem("pdfEncoding") != null)
				{
					styleAttrs.put(
						JRTextAttribute.PDF_ENCODING, 
						nodeAttrs.getNamedItem("pdfEncoding").getNodeValue()
						);
				}

				if (nodeAttrs.getNamedItem("isPdfEmbedded") != null)
				{
					styleAttrs.put(
						JRTextAttribute.IS_PDF_EMBEDDED, 
						Boolean.valueOf(nodeAttrs.getNamedItem("isPdfEmbedded").getNodeValue()) 
						);
				}
				
				if (nodeAttrs.getNamedItem("color") != null)
				{
					Color color = null;
					String colorStr = nodeAttrs.getNamedItem("color").getNodeValue();
					if (colorStr != null && colorStr.length() > 0)
					{
						char firstChar = colorStr.charAt(0);
						if (firstChar == '#')
						{
							color = new Color(Integer.parseInt(colorStr.substring(1), 16));
						}
						else if ('0' <= firstChar && firstChar <= '9')
						{
							color = new Color(Integer.parseInt(colorStr));
						}
						else
						{
							if (JRXmlConstants.getColorMap().containsKey(colorStr))
							{
								color = (Color)JRXmlConstants.getColorMap().get(colorStr);
							}
							else
							{
								color = Color.black;
							}
						}
					}
					styleAttrs.put(
						TextAttribute.FOREGROUND, 
						color 
						);
				}
				
				int startIndex = styledText.length();

				parseStyle(styledText, node);
				
				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
		}
	}

}
