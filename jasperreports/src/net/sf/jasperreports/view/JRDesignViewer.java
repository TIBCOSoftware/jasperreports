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
package net.sf.jasperreports.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JViewport;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JRDesignFont;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.TextRenderer;
import net.sf.jasperreports.engine.util.JRGraphEnvInitializer;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.xml.sax.SAXException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignViewer extends javax.swing.JPanel
{


	/**
	 *
	 */
	private static final int TYPE_FILE_NAME = 1;
	private static final int TYPE_INPUT_STREAM = 2;
	private static final int TYPE_JASPER_DESIGN = 3;

	private static final int zooms[] = {50, 75, 100, 125, 150, 175, 200, 250};

	private int type = TYPE_FILE_NAME;
	private boolean isXML = false;
	private String reportFileName = null;
	private JRReport report = null;
	private float zoom = 1f;

	private int offsetY = 0;
	private int upColumns = 0;
	private int downColumns = 0;

	private int downX = 0;
	private int downY = 0;

	private JScrollBar hBar = null;
	private JScrollBar vBar = null;

	private JRFont defaultFont = null;

	protected JRStyledTextParser styledTextParser = new JRStyledTextParser();
	protected TextRenderer simluationTextRenderer = 
		new TextRenderer()
		{
			public void draw(TextLayout layout) 
			{
			}
		};
	protected TextRenderer textRenderer = new TextRenderer();

	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(String fileName, boolean isXML) throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();

		initComponents();

		hBar = scrollPane.getHorizontalScrollBar();
		vBar = scrollPane.getVerticalScrollBar();

		this.loadReport(fileName, isXML);
		this.cmbZoom.setSelectedIndex(2);//100%
	}

	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(InputStream is, boolean isXML) throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();

		initComponents();

		hBar = scrollPane.getHorizontalScrollBar();
		vBar = scrollPane.getVerticalScrollBar();

		this.loadReport(is, isXML);
		this.cmbZoom.setSelectedIndex(2);//100%
	}

	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(JRReport report) throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();

		initComponents();

		hBar = scrollPane.getHorizontalScrollBar();
		vBar = scrollPane.getVerticalScrollBar();

		this.loadReport(report);
		this.cmbZoom.setSelectedIndex(2);//100%
	}

	
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        tlbToolBar = new javax.swing.JPanel();
        btnReload = new javax.swing.JButton();
        pnlSep01 = new javax.swing.JPanel();
        pnlSep02 = new javax.swing.JPanel();
        btnZoomIn = new javax.swing.JButton();
        btnZoomOut = new javax.swing.JButton();
        cmbZoom = new javax.swing.JComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for(int i = 0; i < zooms.length; i++)
        {
            model.addElement("" + zooms[i] + "%");
        }
        cmbZoom.setModel(model);

        pnlMain = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        scrollPane.getHorizontalScrollBar().setUnitIncrement(5);
        scrollPane.getVerticalScrollBar().setUnitIncrement(5);

        pnlInScroll = new javax.swing.JPanel();
        pnlPage = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        lblPage = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        tlbToolBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 2));

        btnReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jasperreports/view/images/reload.GIF")));
        btnReload.setText("Reload");
        btnReload.setToolTipText("Reload Document");
        btnReload.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnReload.setMaximumSize(new java.awt.Dimension(80, 23));
        btnReload.setMinimumSize(new java.awt.Dimension(80, 23));
        btnReload.setPreferredSize(new java.awt.Dimension(80, 23));
        btnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadActionPerformed(evt);
            }
        });

        tlbToolBar.add(btnReload);

        pnlSep01.setMaximumSize(new java.awt.Dimension(10, 10));
        tlbToolBar.add(pnlSep01);

        pnlSep02.setMaximumSize(new java.awt.Dimension(10, 10));
        tlbToolBar.add(pnlSep02);

        btnZoomIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jasperreports/view/images/zoomin.GIF")));
        btnZoomIn.setToolTipText("Zoom In");
        btnZoomIn.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnZoomIn.setMaximumSize(new java.awt.Dimension(23, 23));
        btnZoomIn.setMinimumSize(new java.awt.Dimension(23, 23));
        btnZoomIn.setPreferredSize(new java.awt.Dimension(23, 23));
        btnZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomInActionPerformed(evt);
            }
        });

        tlbToolBar.add(btnZoomIn);

        btnZoomOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jasperreports/view/images/zoomout.GIF")));
        btnZoomOut.setToolTipText("Zoom Out");
        btnZoomOut.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnZoomOut.setMaximumSize(new java.awt.Dimension(23, 23));
        btnZoomOut.setMinimumSize(new java.awt.Dimension(23, 23));
        btnZoomOut.setPreferredSize(new java.awt.Dimension(23, 23));
        btnZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomOutActionPerformed(evt);
            }
        });

        tlbToolBar.add(btnZoomOut);

        cmbZoom.setToolTipText("Zoom Ratio");
        cmbZoom.setMaximumSize(new java.awt.Dimension(80, 23));
        cmbZoom.setMinimumSize(new java.awt.Dimension(80, 23));
        cmbZoom.setPreferredSize(new java.awt.Dimension(80, 23));
        cmbZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbZoomActionPerformed(evt);
            }
        });

        tlbToolBar.add(cmbZoom);

        add(tlbToolBar, java.awt.BorderLayout.NORTH);

        pnlMain.setLayout(new java.awt.BorderLayout());

        pnlInScroll.setLayout(new java.awt.GridBagLayout());

        pnlPage.setLayout(new java.awt.BorderLayout());

        pnlPage.setMinimumSize(new java.awt.Dimension(100, 100));
        pnlPage.setPreferredSize(new java.awt.Dimension(100, 100));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4.setMinimumSize(new java.awt.Dimension(100, 120));
        jPanel4.setPreferredSize(new java.awt.Dimension(100, 120));
        jPanel5.setBackground(java.awt.Color.gray);
        jPanel5.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel5.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        jPanel4.add(jPanel5, gridBagConstraints);

        jPanel6.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel6.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel4.add(jPanel6, gridBagConstraints);

        jPanel7.setBackground(java.awt.Color.gray);
        jPanel7.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel7.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(jPanel7, gridBagConstraints);

        jPanel8.setBackground(java.awt.Color.gray);
        jPanel8.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel8.setPreferredSize(new java.awt.Dimension(5, 5));
        jLabel1.setText("jLabel1");
        jPanel8.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel4.add(jPanel8, gridBagConstraints);

        jPanel9.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel9.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel4.add(jPanel9, gridBagConstraints);

        lblPage.setBackground(java.awt.Color.white);
        lblPage.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        lblPage.setOpaque(true);
        lblPage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblPageMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblPageMouseReleased(evt);
            }
        });

        lblPage.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                lblPageMouseDragged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(lblPage, gridBagConstraints);

        pnlPage.add(jPanel4, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlInScroll.add(pnlPage, gridBagConstraints);

        scrollPane.setViewportView(pnlInScroll);

        pnlMain.add(scrollPane, java.awt.BorderLayout.CENTER);

        add(pnlMain, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void btnReloadActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReloadActionPerformed
    {//GEN-HEADEREND:event_btnReloadActionPerformed
		// Add your handling code here:
		if (this.type == TYPE_FILE_NAME)
		{
			try
			{
				this.loadReport(this.reportFileName, this.isXML);
				//this.cmbZoom.setSelectedIndex(2);//100%
				this.refreshDesign();
			}
			catch (JRException e)
			{
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error loading report design. See console for details.");
			}
		}
    }//GEN-LAST:event_btnReloadActionPerformed

    private void btnZoomInActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnZoomInActionPerformed
    {//GEN-HEADEREND:event_btnZoomInActionPerformed
		// Add your handling code here:
		int index = this.cmbZoom.getSelectedIndex();
		if (index < this.cmbZoom.getModel().getSize() - 1)
		{
			this.cmbZoom.setSelectedIndex(index + 1);
		}
    }//GEN-LAST:event_btnZoomInActionPerformed

    private void btnZoomOutActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnZoomOutActionPerformed
    {//GEN-HEADEREND:event_btnZoomOutActionPerformed
		// Add your handling code here:
		int index = this.cmbZoom.getSelectedIndex();
		if (index > 0)
		{
			this.cmbZoom.setSelectedIndex(index - 1);
		}
    }//GEN-LAST:event_btnZoomOutActionPerformed

    private void lblPageMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPageMousePressed
    {//GEN-HEADEREND:event_lblPageMousePressed
		// Add your handling code here:
		this.lblPage.setCursor(new Cursor(Cursor.MOVE_CURSOR));

		this.downX = evt.getX();
		this.downY = evt.getY();
    }//GEN-LAST:event_lblPageMousePressed

    private void lblPageMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPageMouseReleased
    {//GEN-HEADEREND:event_lblPageMouseReleased
		// Add your handling code here:
		this.lblPage.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_lblPageMouseReleased

    private void lblPageMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPageMouseDragged
    {//GEN-HEADEREND:event_lblPageMouseDragged
		// Add your handling code here:
		Container container = pnlInScroll.getParent();
		if (container instanceof JViewport)
		{
			JViewport viewport = (JViewport) container;
			Point point = viewport.getViewPosition();
			int newX = point.x - (evt.getX() - downX);
			int newY = point.y - (evt.getY() - downY);
			
			int maxX = pnlInScroll.getWidth() - viewport.getWidth();
			int maxY = pnlInScroll.getHeight() - viewport.getHeight();

			if (newX < 0)
			{
				newX = 0;
			}
			if (newX > maxX)
			{
				newX = maxX;
			}
			if (newY < 0)
			{
				newY = 0;
			}
			if (newY > maxY)
			{
				newY = maxY;
			}
			
			viewport.setViewPosition(new Point(newX, newY));
		}
    }//GEN-LAST:event_lblPageMouseDragged

    private void cmbZoomActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbZoomActionPerformed
    {//GEN-HEADEREND:event_cmbZoomActionPerformed
		// Add your handling code here:
		int index = this.cmbZoom.getSelectedIndex();
		this.zoom = zooms[index] / 100f;
		this.btnZoomIn.setEnabled( (index < this.cmbZoom.getModel().getSize() - 1) );
		this.btnZoomOut.setEnabled( (index > 0) );
		this.refreshDesign();
    }//GEN-LAST:event_cmbZoomActionPerformed


	/**
	*/
	private void verifyDesign(JasperDesign jasperDesign) throws JRException
	{
		/*   */
		Collection brokenRules = JasperCompileManager.verifyDesign(jasperDesign);
		if (brokenRules != null && brokenRules.size() > 0)
		{
			StringBuffer sbuffer = new StringBuffer();
			sbuffer.append("Report definition not valid : ");
			int i = 1;
			for(Iterator it = brokenRules.iterator(); it.hasNext(); i++)
			{
				sbuffer.append("\n\t " + i + ". " + (String)it.next());
			}
			
			throw new JRException(sbuffer.toString());
		}
	}


	/**
	*/
	private void loadReport(String fileName, boolean isXML) throws JRException
	{
		if (isXML)
		{
			JasperDesign jasperDesign = JRXmlLoader.load(fileName);
			this.verifyDesign(jasperDesign);
			this.report = jasperDesign;
		}
		else
		{
			this.report = (JRReport)JRLoader.loadObject(fileName);
		}
		this.type = TYPE_FILE_NAME;
		this.isXML = isXML;
		this.reportFileName = fileName;
		this.setOffsetY();
		this.btnReload.setEnabled(true);
	}


	/**
	*/
	private void loadReport(InputStream is, boolean isXML) throws JRException
	{
		if (isXML)
		{
			JasperDesign jasperDesign = JRXmlLoader.load(is);
			this.verifyDesign(jasperDesign);
			this.report = jasperDesign;
		}
		else
		{
			this.report = (JRReport)JRLoader.loadObject(is);
		}
		this.type = TYPE_INPUT_STREAM;
		this.isXML = isXML;
		this.setOffsetY();
		this.btnReload.setEnabled(false);
	}


	/**
	*/
	private void loadReport(JRReport report) throws JRException
	{
		this.report = report;
		this.type = TYPE_JASPER_DESIGN;
		this.isXML = false;
		this.setOffsetY();
		this.btnReload.setEnabled(false);
	}


	/**
	*
	*/
	public void setOffsetY()
	{
		offsetY = report.getTopMargin();
		offsetY += (report.getTitle() != null ? report.getTitle().getHeight() : 0);
		offsetY += (report.getPageHeader() != null ? report.getPageHeader().getHeight() : 0);
		upColumns = offsetY;
		offsetY += (report.getColumnHeader() != null ? report.getColumnHeader().getHeight() : 0);

		JRGroup group = null;
		JRGroup[] groups = report.getGroups();
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				group = groups[i];

				offsetY += (group.getGroupHeader() != null ? group.getGroupHeader().getHeight() : 0);
			}
		}

		offsetY += (report.getDetail() != null ? report.getDetail().getHeight() : 0);

		if (groups != null && groups.length > 0)
		{
			for(int i = groups.length - 1; i >= 0; i--)
			{
				group = groups[i];

				offsetY += (group.getGroupFooter() != null ? group.getGroupFooter().getHeight() : 0);
			}
		}

		offsetY += (report.getColumnFooter() != null ? report.getColumnFooter().getHeight() : 0);
		downColumns = offsetY;
		offsetY += (report.getPageFooter() != null ? report.getPageFooter().getHeight() : 0);
		offsetY += (report.getLastPageFooter() != null ? report.getLastPageFooter().getHeight() : 0);
		offsetY += (report.getSummary() != null ? report.getSummary().getHeight() : 0);
		offsetY += report.getBottomMargin();
	}

	
	/**
	*/
	private void refreshDesign()
	{
		Image image = null;
		ImageIcon imageIcon = null;

		Dimension dim = new Dimension(
			(int)(report.getPageWidth() * zoom) + 8, //why 8 ? 2 for the balck border, 1 extra for the image and 5 for the shadow panels
			(int)(offsetY * zoom) + 8
			);
		this.pnlPage.setMaximumSize(dim);
		this.pnlPage.setMinimumSize(dim);
		this.pnlPage.setPreferredSize(dim);

		try
		{
			image = this.printDesignToImage();
			imageIcon = new ImageIcon(image);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		this.lblPage.setIcon(imageIcon);
	}


    /**
     */
    private Image printDesignToImage()
    {
		Image designImage = new BufferedImage(
			(int)(report.getPageWidth() * zoom) + 1,
			(int)(offsetY * zoom) + 1,
			BufferedImage.TYPE_INT_RGB
			);
		Graphics2D grx = (Graphics2D)designImage.getGraphics();

		grx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		grx.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		AffineTransform atrans = new AffineTransform();
		atrans.scale(zoom, zoom);
		grx.transform(atrans);

		printDesign(grx);

		grx.dispose();
		
		return designImage;
	}

	
	/**
     */
    private void printDesign(Graphics2D grx)
    {
		Stroke dashedStroke =
			new BasicStroke(
				1f / zoom,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL,
				0f,
				new float[]{5f, 3f},
				0f
				);
		Color dashedColor = new Color(170, 170, 255);

		grx.setColor(Color.white);
		grx.fillRect(
			0, 
			0, 
			report.getPageWidth() + 1,
			offsetY + 1
			);

		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			report.getLeftMargin(), 
			0,
			report.getLeftMargin(), 
			offsetY + 1
			);
		grx.drawLine(
			(report.getPageWidth() - report.getRightMargin()), 
			0,
			(report.getPageWidth() - report.getRightMargin()), 
			offsetY + 1
			);
		grx.drawLine(
			(report.getLeftMargin() + report.getColumnWidth()), 
			upColumns,
			(report.getLeftMargin() + report.getColumnWidth()), 
			downColumns
			);
		grx.drawLine(
			(report.getLeftMargin() + report.getColumnWidth() + report.getColumnSpacing()), 
			upColumns,
			(report.getLeftMargin() + report.getColumnWidth() + report.getColumnSpacing()), 
			downColumns
			);


		grx.translate(
			report.getLeftMargin(), 
			report.getTopMargin()
			);

		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getTitle(), grx);
		grx.translate(
			0, 
			(report.getTitle() != null ? report.getTitle().getHeight() : 0)
			);


		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getPageHeader(), grx);
		grx.translate(
			0, 
			(report.getPageHeader() != null ? report.getPageHeader().getHeight() : 0)
			);


		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getColumnHeader(), grx);
		grx.translate(
			0, 
			(report.getColumnHeader() != null ? report.getColumnHeader().getHeight() : 0)
			);


		JRGroup group = null;
		JRGroup[] groups = report.getGroups();
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				group = groups[i];

				grx.setStroke(dashedStroke);
				grx.setColor(dashedColor);
				grx.drawLine(
					- report.getLeftMargin(), 
					0,
					report.getPageWidth() + 1, 
					0
					);
				grx.setColor(Color.black);
				grx.setStroke(new BasicStroke(1f));
				printBand(group.getGroupHeader(), grx);
				grx.translate(
					0, 
					(group.getGroupHeader() != null ? group.getGroupHeader().getHeight() : 0)
					);
			}
		}

		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getDetail(), grx);
		grx.translate(
			0, 
			(report.getDetail() != null ? report.getDetail().getHeight() : 0)
			);

		if (groups != null && groups.length > 0)
		{
			for(int i = groups.length - 1; i >= 0; i--)
			{
				group = groups[i];

				grx.setStroke(dashedStroke);
				grx.setColor(dashedColor);
				grx.drawLine(
					- report.getLeftMargin(), 
					0,
					report.getPageWidth() + 1, 
					0
					);
				grx.setColor(Color.black);
				grx.setStroke(new BasicStroke(1f));
				printBand(group.getGroupFooter(), grx);
				grx.translate(
					0, 
					(group.getGroupFooter() != null ? group.getGroupFooter().getHeight() : 0)
					);
			}
		}

		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getColumnFooter(), grx);
		grx.translate(
			0, 
			(report.getColumnFooter() != null ? report.getColumnFooter().getHeight() : 0)
			);


		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getPageFooter(), grx);
		grx.translate(
			0, 
			(report.getPageFooter() != null ? report.getPageFooter().getHeight() : 0)
			);


		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getLastPageFooter(), grx);
		grx.translate(
			0, 
			(report.getLastPageFooter() != null ? report.getLastPageFooter().getHeight() : 0)
			);


		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getSummary(), grx);
		grx.translate(
			0, 
			(report.getSummary() != null ? report.getSummary().getHeight() : 0)
			);


		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
	}


	/**
	 *
	 */
    private void printBand(JRBand band, Graphics2D grx)
    {
	    if (band != null)
	    {
		    JRElement element = null;
		    JRElement[] elements = band.getElements();
		    if (elements != null && elements.length > 0)
		    {
				for(int i = 0; i < elements.length; i++)
			    {
				    element = elements[i];
	
				    if (element instanceof JRLine)
				    {
					    printLine((JRLine)element, grx);
				    }
				    else if (element instanceof JRRectangle)
				    {
					    printRectangle((JRRectangle)element, grx);
				    }
				    else if (element instanceof JREllipse)
				    {
					    printEllipse((JREllipse)element, grx);
				    }
				    else if (element instanceof JRImage)
				    {
					    printImage((JRImage)element, grx);
				    }
				    else if (element instanceof JRStaticText)
				    {
					    printText((JRTextElement)element, grx);
				    }
				    else if (element instanceof JRTextField)
				    {
					    printText((JRTextElement)element, grx);
				    }
				    else if (element instanceof JRSubreport)
				    {
					    printSubreport((JRSubreport)element, grx);
				    }
			    }
		    }
		}
    }
	
	
	/**
	 *
	 */
	private void printLine(JRLine line, Graphics2D grx)
	{
		grx.setColor(line.getForecolor());

		Stroke stroke = getStroke(line.getPen());

		if (stroke != null)
		{
			grx.setStroke(stroke);
			
			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				grx.drawLine(
					line.getX(), 
					line.getY(),
					line.getX() + line.getWidth() - 1,  
					line.getY() + line.getHeight() - 1
					);
			}
			else
			{
				grx.drawLine(
					line.getX(), 
					line.getY() + line.getHeight() - 1,
					line.getX() + line.getWidth() - 1,  
					line.getY()
					);
			}
		}
	}


	/**
	 *
	 */
	private void printRectangle(JRRectangle rectangle, Graphics2D grx)
	{
		if (rectangle.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(rectangle.getBackcolor());

			grx.fillRoundRect(
				rectangle.getX(), 
				rectangle.getY(), 
				rectangle.getWidth(),
				rectangle.getHeight(),
				2 * rectangle.getRadius(),
				2 * rectangle.getRadius()
				);
		}

		grx.setColor(rectangle.getForecolor());

		Stroke stroke = getStroke(rectangle.getPen());

		if (stroke != null)
		{
			grx.setStroke(stroke);
	
			grx.drawRoundRect(
				rectangle.getX(), 
				rectangle.getY(), 
				rectangle.getWidth() - 1,
				rectangle.getHeight() - 1,
				2 * rectangle.getRadius(),
				2 * rectangle.getRadius()
				);
		}
	}


	/**
	 *
	 */
	private void printEllipse(JREllipse ellipse, Graphics2D grx)
	{
		if (ellipse.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(ellipse.getBackcolor());

			grx.fillOval(
				ellipse.getX(), 
				ellipse.getY(), 
				ellipse.getWidth(),
				ellipse.getHeight()
				);
		}

		grx.setColor(ellipse.getForecolor());

		Stroke stroke = getStroke(ellipse.getPen());

		if (stroke != null)
		{
			grx.setStroke(stroke);
	
			grx.drawOval(
				ellipse.getX(), 
				ellipse.getY(), 
				ellipse.getWidth() - 1,
				ellipse.getHeight() - 1
				);
		}
	}


	/**
	 *
	 */
	private void printImage(JRImage jrImage, Graphics2D grx)
	{
		if (jrImage.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(jrImage.getBackcolor());

			grx.fillRect(
				jrImage.getX(), 
				jrImage.getY(), 
				jrImage.getWidth(),
				jrImage.getHeight()
				);
		}

		
		int availableImageWidth = jrImage.getWidth();
		availableImageWidth = (availableImageWidth < 0)?0:availableImageWidth;

		int availableImageHeight = jrImage.getHeight();
		availableImageHeight = (availableImageHeight < 0)?0:availableImageHeight;
		
		if (availableImageWidth > 0 && availableImageHeight > 0)
		{
			Image awtImage = null;
			
			JRExpression jrExpression = jrImage.getExpression();
			if (jrExpression != null && jrExpression.getChunks().length == 1)
			{
				JRExpressionChunk firstChunk = (JRExpressionChunk)jrExpression.getChunks()[0];
				if (firstChunk.getType() == JRExpressionChunk.TYPE_TEXT)
				{
					String location = firstChunk.getText().trim();
					if (location.startsWith("\"") && location.endsWith("\""))
					{
						location = location.substring(1, location.length() - 1);
						try
						{
							awtImage = JRImageLoader.loadImage(
								JRImageLoader.loadImageDataFromLocation(location)
								);
						}
						catch (JRException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
	
			if (awtImage != null)
			{
				int awtWidth = awtImage.getWidth(null);
				int awtHeight = awtImage.getHeight(null);

				float xalignFactor = 0f;
				switch (jrImage.getHorizontalAlignment())
				{
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
					{
						xalignFactor = 1f;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
					{
						xalignFactor = 0.5f;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					default :
					{
						xalignFactor = 0f;
						break;
					}
				}

				float yalignFactor = 0f;
				switch (jrImage.getVerticalAlignment())
				{
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
					{
						yalignFactor = 1f;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
					{
						yalignFactor = 0.5f;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_TOP :
					default :
					{
						yalignFactor = 0f;
						break;
					}
				}

				switch (jrImage.getScaleImage())
				{
					case JRImage.SCALE_IMAGE_CLIP :
					{
						int xoffset = (int)(xalignFactor * (availableImageWidth - awtWidth));
						int yoffset = (int)(yalignFactor * (availableImageHeight - awtHeight));

						grx.setClip(
							jrImage.getX(), 
							jrImage.getY(), 
							availableImageWidth, 
							availableImageHeight
							);
						grx.drawImage(
							awtImage, 
							jrImage.getX() + xoffset, 
							jrImage.getY() + yoffset, 
							awtWidth, 
							awtHeight, 
							this
							);
						grx.setClip(
							- report.getLeftMargin(), 
							0, 
							report.getPageWidth(), 
							report.getPageHeight()
							);
		
						break;
					}
					case JRImage.SCALE_IMAGE_FILL_FRAME :
					{
						grx.drawImage(
							awtImage, 
							jrImage.getX(), 
							jrImage.getY(), 
							availableImageWidth, 
							availableImageHeight,
							this
							);
		
						break;
					}
					case JRImage.SCALE_IMAGE_RETAIN_SHAPE :
					default :
					{
						if (jrImage.getHeight() > 0)
						{
							double ratio = (double)awtWidth / (double)awtHeight;
							
							if( ratio > (double)availableImageWidth / (double)availableImageHeight )
							{
								awtWidth = availableImageWidth; 
								awtHeight = (int)(availableImageWidth / ratio); 
							}
							else
							{
								awtWidth = (int)(availableImageHeight * ratio); 
								awtHeight = availableImageHeight; 
							}

							int xoffset = (int)(xalignFactor * (availableImageWidth - awtWidth));
							int yoffset = (int)(yalignFactor * (availableImageHeight - awtHeight));

							grx.drawImage(
								awtImage, 
								jrImage.getX() + xoffset, 
								jrImage.getY() + yoffset, 
								awtWidth, 
								awtHeight, 
								this
								);
						}
						
						break;
					}
				}
			}
			else
			{
				try
				{
					awtImage = JRImageLoader.getImage(JRImageLoader.NO_IMAGE);
				}
				catch (JRException e)
				{
					e.printStackTrace();
				}
	
				grx.setClip(
					jrImage.getX(), 
					jrImage.getY(), 
					availableImageWidth, 
					availableImageHeight
					);
				grx.drawImage(
					awtImage, 
					jrImage.getX() + 2, 
					jrImage.getY() + 2, 
					awtImage.getWidth(null), 
					awtImage.getHeight(null), 
					this
					);
				grx.setClip(
					- report.getLeftMargin(), 
					0, 
					report.getPageWidth(), 
					report.getPageHeight()
					);

				//borderOffset = 0;
				//stroke = new BasicStroke(1f / zoom);
			}
		}
		
		if (jrImage.getBox() == null)
		{
			Stroke stroke = getStroke(jrImage.getPen());
			if (stroke != null)
			{
				grx.setColor(jrImage.getForecolor());
				
				grx.setStroke(stroke);
		
				grx.drawRect(
					jrImage.getX(), 
					jrImage.getY(), 
					jrImage.getWidth() - 1,
					jrImage.getHeight() - 1
					);
			}
		}
		else
		{
			/*   */
			printBox(
				jrImage.getBox(),
				jrImage,
				grx
				);
		}
	}


	/**
	 *
	 */
	private JRStyledText getStyledText(JRTextElement textElement)
	{
		JRStyledText styledText = null;

		String text = null;
		if (textElement instanceof JRStaticText)
		{
			text = ((JRStaticText)textElement).getText();
		}
		else if (textElement instanceof JRTextField)
		{
			if (((JRTextField)textElement).getExpression() != null)
			{
				text = ((JRTextField)textElement).getExpression().getText();
			}
		}
		
		if (text != null && text.length() > 0)
		{
			//text = JRStringUtil.treatNewLineChars(text);

			JRFont font = textElement.getFont();
			if (font == null)
			{
				font = getDefaultFont();
			}

			Map attributes = new HashMap(); 
			attributes.putAll(font.getAttributes());
			attributes.put(TextAttribute.FOREGROUND, textElement.getForecolor());
			if (textElement.getMode() == JRElement.MODE_OPAQUE)
			{
				attributes.put(TextAttribute.BACKGROUND, textElement.getBackcolor());
			}

			if (
				textElement instanceof JRStaticText
				&& textElement.isStyledText()
				)
			{
				try
				{
					styledText = styledTextParser.parse(attributes, text);
				}
				catch (SAXException e)
				{
					//ignore if invalid styled text and treat like normal text
				}
			}
		
			if (styledText == null)
			{
				styledText = new JRStyledText();
				styledText.append(text);
				styledText.addRun(new JRStyledText.Run(attributes, 0, text.length()));
			}
		}
		
		return styledText;
	}


	/**
	 *
	 */
	private void printText(JRTextElement text, Graphics2D grx)
	{
		JRStyledText styledText = getStyledText(text);
		
		if (styledText == null)
		{
			return;
		}

		String allText = styledText.getText();
		
		int x = text.getX();
		int y = text.getY();
		int width = text.getWidth();
		int height = text.getHeight();
		int topPadding = 0;
		int leftPadding = 0;
		int bottomPadding = 0;
		int rightPadding = 0;
		
		if (text.getBox() != null)
		{
			topPadding = text.getBox().getTopPadding();
			leftPadding = text.getBox().getLeftPadding();
			bottomPadding = text.getBox().getBottomPadding();
			rightPadding = text.getBox().getRightPadding();
		}
		
		double angle = 0;
		
		switch (text.getRotation())
		{
			case JRTextElement.ROTATION_LEFT :
			{
				y = text.getY() + text.getHeight();
				width = text.getHeight();
				height = text.getWidth();
				int tmpPadding = topPadding;
				topPadding = leftPadding;
				leftPadding = bottomPadding;
				bottomPadding = rightPadding;
				rightPadding = tmpPadding;
				angle = - Math.PI / 2;
				break;
			}
			case JRTextElement.ROTATION_RIGHT :
			{
				x = text.getX() + text.getWidth();
				width = text.getHeight();
				height = text.getWidth();
				int tmpPadding = topPadding;
				topPadding = rightPadding;
				rightPadding = bottomPadding;
				bottomPadding = leftPadding;
				leftPadding = tmpPadding;
				angle = Math.PI / 2;
				break;
			}
			case JRTextElement.ROTATION_NONE :
			default :
			{
			}
		}
		
		grx.rotate(angle, x, y);

		if (text.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(text.getBackcolor());
			grx.fillRect(x, y, width, height); 
		}

		grx.setColor(text.getForecolor());

		/*   */
		simluationTextRenderer.render(
			grx, 
			x, 
			y, 
			width, 
			height,
			topPadding,
			leftPadding,
			bottomPadding,
			rightPadding,
			0f, 
			text.getTextAlignment(), 
			text.getVerticalAlignment(), 
			text.getLineSpacing(), 
			styledText, 
			allText
			);
		
		/*   */
		textRenderer.render(
			grx, 
			x, 
			y, 
			width, 
			height, 
			topPadding,
			leftPadding,
			bottomPadding,
			rightPadding,
			simluationTextRenderer.getTextHeight(), 
			text.getTextAlignment(), 
			text.getVerticalAlignment(), 
			text.getLineSpacing(), 
			styledText, 
			allText
			);

		grx.rotate(-angle, x, y);

		/*   */
		printBox(
			text.getBox(),
			text,
			grx
			);
	}

    
	/**
	 *
	 */
	private void printBox(JRBox box, JRElement element, Graphics2D grx)
	{
		Stroke topStroke = null;
		Stroke leftStroke = null;
		Stroke bottomStroke = null;
		Stroke rightStroke = null;
		if (box != null)
		{
			topStroke = getStroke(box.getTopBorder());
			leftStroke = getStroke(box.getLeftBorder());
			bottomStroke = getStroke(box.getBottomBorder());
			rightStroke = getStroke(box.getRightBorder());
		}

		if (topStroke != null)
		{
			grx.setStroke(topStroke);
			grx.setColor(box.getTopBorderColor() == null ? element.getForecolor() : box.getTopBorderColor());
	
			if (topStroke == STROKE_THIN)
			{
				grx.translate(-THIN_CORNER_OFFSET, -THIN_CORNER_OFFSET);
				grx.drawLine(
					element.getX(), 
					element.getY(), 
					element.getX() + element.getWidth(),
					element.getY()
					);
				grx.translate(THIN_CORNER_OFFSET, THIN_CORNER_OFFSET);
			}
			else
			{
				grx.drawLine(
					element.getX(), 
					element.getY(), 
					element.getX() + element.getWidth() - 1,
					element.getY()
					);
			}
		}

		if (leftStroke != null)
		{
			grx.setStroke(leftStroke);
			grx.setColor(box.getLeftBorderColor() == null ? element.getForecolor() : box.getLeftBorderColor());
	
			if (leftStroke == STROKE_THIN)
			{
				grx.translate(-THIN_CORNER_OFFSET, -THIN_CORNER_OFFSET);
				grx.drawLine(
					element.getX(), 
					element.getY(), 
					element.getX(),
					element.getY() + element.getHeight()
					);
				grx.translate(THIN_CORNER_OFFSET, THIN_CORNER_OFFSET);
			}
			else
			{
				grx.drawLine(
					element.getX(), 
					element.getY(), 
					element.getX(),
					element.getY() + element.getHeight() - 1
					);
			}
		}

		if (bottomStroke != null)
		{
			grx.setStroke(bottomStroke);
			grx.setColor(box.getBottomBorderColor() == null ? element.getForecolor() : box.getBottomBorderColor());
	
			if (bottomStroke == STROKE_THIN)
			{
				grx.translate(-THIN_CORNER_OFFSET, THIN_CORNER_OFFSET);
				grx.drawLine(
					element.getX(), 
					element.getY() + element.getHeight() - 1,
					element.getX() + element.getWidth(),
					element.getY() + element.getHeight() - 1
					);
				grx.translate(THIN_CORNER_OFFSET, -THIN_CORNER_OFFSET);
			}
			else
			{
				grx.drawLine(
					element.getX(), 
					element.getY() + element.getHeight() - 1,
					element.getX() + element.getWidth() - 1,
					element.getY() + element.getHeight() - 1
					);
			}
		}

		if (rightStroke != null)
		{
			grx.setStroke(rightStroke);
			grx.setColor(box.getRightBorderColor() == null ? element.getForecolor() : box.getRightBorderColor());
	
			if (rightStroke == STROKE_THIN)
			{
				grx.translate(THIN_CORNER_OFFSET, -THIN_CORNER_OFFSET);
				grx.drawLine(
					element.getX() + element.getWidth() - 1,
					element.getY(),
					element.getX() + element.getWidth() - 1,
					element.getY() + element.getHeight()
					);
				grx.translate(-THIN_CORNER_OFFSET, THIN_CORNER_OFFSET);
			}
			else
			{
				grx.drawLine(
					element.getX() + element.getWidth() - 1,
					element.getY(),
					element.getX() + element.getWidth() - 1,
					element.getY() + element.getHeight() - 1
					);
			}
		}

		if (
			topStroke == null
			&& leftStroke == null
			&& bottomStroke == null
			&& rightStroke == null
			)
		{
			grx.setColor(element.getForecolor());
			grx.setStroke(new BasicStroke(1f / zoom));
		
			grx.drawRect(element.getX(), element.getY(), element.getWidth() - 1, element.getHeight() - 1);
		}
	}

    
	/**
	 *
	 */
	private void printSubreport(JRSubreport subreport, Graphics2D grx)
	{
		if (subreport.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(subreport.getBackcolor());

			grx.fillRect(
				subreport.getX(), 
				subreport.getY(), 
				subreport.getWidth(),
				subreport.getHeight()
				);
		}

		Image image = null;
		try
		{
			image = JRImageLoader.getImage(JRImageLoader.SUBREPORT_IMAGE);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}

		grx.setClip(
			subreport.getX(), 
			subreport.getY(), 
			subreport.getWidth(), 
			subreport.getHeight()
			);
		grx.drawImage(
			image, 
			subreport.getX() + 2, 
			subreport.getY() + 2, 
			image.getWidth(null), 
			image.getHeight(null), 
			this
			);
		grx.setClip(
			- report.getLeftMargin(), 
			0, 
			report.getPageWidth(), 
			report.getPageHeight()
			);

		grx.setColor(subreport.getForecolor());
		grx.setStroke(new BasicStroke(1f / zoom));
		grx.drawRect(
			subreport.getX(), 
			subreport.getY(), 
			subreport.getWidth() - 1,
			subreport.getHeight() - 1
			);
	}


	/**
	 *
	 */
	private JRFont getDefaultFont()
	{
		if (defaultFont == null)
		{
			defaultFont = report.getDefaultFont();
			if (defaultFont == null)
			{
				defaultFont = new JRDesignFont();
			}
		}
		
		return defaultFont;
	}


	/**
	 * 
	 */
	private static final double THIN_CORNER_OFFSET = 0.25d;
	private static final Stroke STROKE_THIN = new BasicStroke(0.5f);
	private static final Stroke STROKE_1_POINT = new BasicStroke(1f);
	private static final Stroke STROKE_2_POINT = new BasicStroke(2f);
	private static final Stroke STROKE_4_POINT = new BasicStroke(4f);
	private static final Stroke STROKE_DOTTED = 
		new BasicStroke(
			1f,
			BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_BEVEL,
			0f,
			new float[]{5f, 3f},
			0f
			);

	/**
	 * 
	 */
	private static Stroke getStroke(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				return STROKE_DOTTED;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				return STROKE_4_POINT;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				return STROKE_2_POINT;
			}
			case JRGraphicElement.PEN_NONE :
			{
				return null;
			}
			case JRGraphicElement.PEN_THIN :
			{
				return STROKE_THIN;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				return STROKE_1_POINT;
			}
		}
	}

	
	// Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JPanel tlbToolBar;
    private javax.swing.JPanel pnlInScroll;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel pnlPage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlSep02;
    private javax.swing.JButton btnReload;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.JLabel lblPage;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel pnlSep01;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JComboBox cmbZoom;
    private javax.swing.JPanel jPanel9;
    // End of variables declaration//GEN-END:variables

}
