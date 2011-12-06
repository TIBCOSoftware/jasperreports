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
import javax.swing.JButton;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRViewerPlus extends JRViewer
{


	/**
	 *
	 */
	protected JButton btnPlus = new javax.swing.JButton();


	/**
	 *
	 */
	public JRViewerPlus(JasperPrint jrPrint) throws JRException
	{
		super(jrPrint);
		
		tlbToolBar.remove(btnSave);
		tlbToolBar.remove(btnReload);

		btnPlus = new javax.swing.JButton();
		btnPlus.setToolTipText("Plus...");
		btnPlus.setText("Plus...");
		btnPlus.setPreferredSize(new java.awt.Dimension(80, 23));
		btnPlus.setMaximumSize(new java.awt.Dimension(80, 23));
		btnPlus.setMinimumSize(new java.awt.Dimension(80, 23));
		btnPlus.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnPlusActionPerformed(evt);
			}
		});
		tlbToolBar.add(btnPlus, 0);
	}


	/**
	 *
	 */
	protected void setZooms()
	{
		this.zooms = new int[]{33, 66, 100, 133, 166, 200, 233};
		this.defaultZoomIndex = 2;
	}


	/**
	 *
	 */
	protected void btnPlusActionPerformed(java.awt.event.ActionEvent evt)
	{
		JOptionPane.showMessageDialog(this, "I just wanted to let you know that you can extend the JRViewer to customize it.\n The button you have pushed was added this way.");
	}


}
