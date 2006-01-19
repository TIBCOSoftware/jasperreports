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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
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
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JViewport;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRFrame;
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
import net.sf.jasperreports.engine.base.JRBaseBox;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.TextRenderer;
import net.sf.jasperreports.engine.util.JRFontUtil;
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
	
	/**
	 * the screen resolution.
	 */
	private int screenResolution = JRViewer.REPORT_RESOLUTION;
	
	/**
	 * the zoom ration adjusted to the screen resolution.
	 */
	private float realZoom = 0f;

	private int offsetY = 0;
	private int upColumns = 0;
	private int downColumns = 0;

	private int downX = 0;
	private int downY = 0;

	protected JRStyledTextParser styledTextParser = new JRStyledTextParser();
	protected TextRenderer simulationTextRenderer = 
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

		setScreenDetails();

		initComponents();

		this.loadReport(fileName, isXML);
		this.cmbZoom.setSelectedIndex(2);//100%
	}

	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(InputStream is, boolean isXML) throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();

		setScreenDetails();
		
		initComponents();

		this.loadReport(is, isXML);
		this.cmbZoom.setSelectedIndex(2);//100%
	}

	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(JRReport report) throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();

		setScreenDetails();

		initComponents();

		this.loadReport(report);
		this.cmbZoom.setSelectedIndex(2);//100%
	}

	
	private void setScreenDetails()
	{
		screenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
		setZoom(1f);
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
				btnReloadActionPerformed();
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
				btnZoomInActionPerformed();
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
				btnZoomOutActionPerformed();
			}
		});

		tlbToolBar.add(btnZoomOut);

		cmbZoom.setToolTipText("Zoom Ratio");
		cmbZoom.setMaximumSize(new java.awt.Dimension(80, 23));
		cmbZoom.setMinimumSize(new java.awt.Dimension(80, 23));
		cmbZoom.setPreferredSize(new java.awt.Dimension(80, 23));
		cmbZoom.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmbZoomActionPerformed();
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
				lblPageMouseReleased();
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

	void btnReloadActionPerformed()//GEN-FIRST:event_btnReloadActionPerformed
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

	void btnZoomInActionPerformed()//GEN-FIRST:event_btnZoomInActionPerformed
	{//GEN-HEADEREND:event_btnZoomInActionPerformed
		// Add your handling code here:
		int index = this.cmbZoom.getSelectedIndex();
		if (index < this.cmbZoom.getModel().getSize() - 1)
		{
			this.cmbZoom.setSelectedIndex(index + 1);
		}
	}//GEN-LAST:event_btnZoomInActionPerformed

	void btnZoomOutActionPerformed()//GEN-FIRST:event_btnZoomOutActionPerformed
	{//GEN-HEADEREND:event_btnZoomOutActionPerformed
		// Add your handling code here:
		int index = this.cmbZoom.getSelectedIndex();
		if (index > 0)
		{
			this.cmbZoom.setSelectedIndex(index - 1);
		}
	}//GEN-LAST:event_btnZoomOutActionPerformed

	void lblPageMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPageMousePressed
	{//GEN-HEADEREND:event_lblPageMousePressed
		// Add your handling code here:
		this.lblPage.setCursor(new Cursor(Cursor.MOVE_CURSOR));

		this.downX = evt.getX();
		this.downY = evt.getY();
	}//GEN-LAST:event_lblPageMousePressed

	void lblPageMouseReleased()//GEN-FIRST:event_lblPageMouseReleased
	{//GEN-HEADEREND:event_lblPageMouseReleased
		// Add your handling code here:
		this.lblPage.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}//GEN-LAST:event_lblPageMouseReleased

	void lblPageMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPageMouseDragged
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

	void cmbZoomActionPerformed()//GEN-FIRST:event_cmbZoomActionPerformed
	{//GEN-HEADEREND:event_cmbZoomActionPerformed
		// Add your handling code here:
		int index = this.cmbZoom.getSelectedIndex();
		setZoom(zooms[index] / 100f);
		this.btnZoomIn.setEnabled( (index < this.cmbZoom.getModel().getSize() - 1) );
		this.btnZoomOut.setEnabled( (index > 0) );
		this.refreshDesign();
	}//GEN-LAST:event_cmbZoomActionPerformed


	private void setZoom(float zoom)
	{
		this.zoom = zoom;
		this.realZoom = this.zoom * screenResolution / JRViewer.REPORT_RESOLUTION;
	}


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
	private void loadReport(String fileName, boolean isXmlReport) throws JRException
	{
		if (isXmlReport)
		{
			JasperDesign jasperDesign = JRXmlLoader.load(fileName);
			setReport(jasperDesign);
		}
		else
		{
			setReport((JRReport) JRLoader.loadObject(fileName));
		}
		this.type = TYPE_FILE_NAME;
		this.isXML = isXmlReport;
		this.reportFileName = fileName;
		this.setOffsetY();
		this.btnReload.setEnabled(true);
	}


	/**
	*/
	private void loadReport(InputStream is, boolean isXmlReport) throws JRException
	{
		if (isXmlReport)
		{
			JasperDesign jasperDesign = JRXmlLoader.load(is);
			setReport(jasperDesign);
		}
		else
		{
			setReport((JRReport) JRLoader.loadObject(is));
		}
		this.type = TYPE_INPUT_STREAM;
		this.isXML = isXmlReport;
		this.setOffsetY();
		this.btnReload.setEnabled(false);
	}


	/**
	*/
	private void loadReport(JRReport rep) throws JRException
	{
		setReport(rep);
		this.type = TYPE_JASPER_DESIGN;
		this.isXML = false;
		this.setOffsetY();
		this.btnReload.setEnabled(false);
	}
	
	private void setReport(JRReport report) throws JRException
	{
		if (report instanceof JasperDesign)
		{
			verifyDesign((JasperDesign) report);
		}
		
		this.report = report;
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
			(int)(report.getPageWidth() * realZoom) + 8, //why 8 ? 2 for the balck border, 1 extra for the image and 5 for the shadow panels
			(int)(offsetY * realZoom) + 8
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
			(int)(report.getPageWidth() * realZoom) + 1,
			(int)(offsetY * realZoom) + 1,
			BufferedImage.TYPE_INT_RGB
			);
		Graphics2D grx = (Graphics2D)designImage.getGraphics();

		grx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		grx.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		grx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		AffineTransform atrans = new AffineTransform();
		atrans.scale(realZoom, realZoom);
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
				1f / realZoom,
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
			printElements(band.getElements(), grx);
		}
	}


	protected void printElements(JRElement[] elements, Graphics2D grx)
	{
		if (elements != null && elements.length > 0)
		{
			for(int i = 0; i < elements.length; i++)
			{
				JRElement element = elements[i];

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
				else if (element instanceof JRChart)
				{
					printChart((JRChart)element, grx);
				}
				else if (element instanceof JRCrosstab)
				{
					printCrosstab((JRCrosstab)element, grx);
				}
				else if (element instanceof JRFrame)
				{
					printFrame((JRFrame) element, grx);
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
			
			grx.translate(.5, .5);
			
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
			
			grx.translate(-.5, -.5);
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

			if (rectangle.getRadius() > 0)
			{
				grx.fillRoundRect(
						rectangle.getX(), 
						rectangle.getY(), 
						rectangle.getWidth(),
						rectangle.getHeight(),
						2 * rectangle.getRadius(),
						2 * rectangle.getRadius()
						);
			}
			else
			{
				grx.fillRect(
						rectangle.getX(), 
						rectangle.getY(), 
						rectangle.getWidth(),
						rectangle.getHeight()
						);
			}
		}

		grx.setColor(rectangle.getForecolor());

		byte pen = rectangle.getPen();
		Stroke stroke = getStroke(pen);

		if (stroke != null)
		{
			double cornerOffset = getBorderCornerOffset(pen);
			int sizeAdjust = getRectangleSizeAdjust(pen);
			
			AffineTransform transform = grx.getTransform();
			
			grx.translate(rectangle.getX() + cornerOffset, rectangle.getY() + cornerOffset);
			if (pen == JRGraphicElement.PEN_THIN)
			{
				grx.scale((rectangle.getWidth() - .5) / rectangle.getWidth(), (rectangle.getHeight() - .5) / rectangle.getHeight());
			}
			
			grx.setStroke(stroke);
	
			if (rectangle.getRadius() > 0)
			{
				grx.drawRoundRect(
						0, 
						0, 
						rectangle.getWidth() - sizeAdjust,
						rectangle.getHeight() - sizeAdjust,
						2 * rectangle.getRadius(),
						2 * rectangle.getRadius()
						);
			}
			else
			{
				grx.drawRect(
						0, 
						0, 
						rectangle.getWidth() - sizeAdjust,
						rectangle.getHeight() - sizeAdjust
						);
			}
			
			grx.setTransform(transform);
		}
	}
	

	private static double getBorderCornerOffset(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_THIN :
			{
				return THIN_CORNER_OFFSET;
			}
			case JRGraphicElement.PEN_1_POINT :
			case JRGraphicElement.PEN_DOTTED :
			{
				return ONE_POINT_CORNER_OFFSET;
			}
			default :
			{
				return 0;
			}
		}
	}

	
	private static int getRectangleSizeAdjust(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_1_POINT:
			case JRGraphicElement.PEN_DOTTED:
				return 1;
			default:
				return 0;
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

		byte pen = ellipse.getPen();
		Stroke stroke = getStroke(pen);

		if (stroke != null)
		{
			double cornerOffset = getBorderCornerOffset(pen);
			int sizeAdjust = getRectangleSizeAdjust(pen);
			
			AffineTransform transform = grx.getTransform();
			
			grx.translate(ellipse.getX() + cornerOffset, ellipse.getY() + cornerOffset);
			if (pen == JRGraphicElement.PEN_THIN)
			{
				grx.scale((ellipse.getWidth() - .5) / ellipse.getWidth(), (ellipse.getHeight() - .5) / ellipse.getHeight());
			}
			
			grx.setStroke(stroke);
	
			grx.drawOval(
				0, 
				0, 
				ellipse.getWidth() - sizeAdjust,
				ellipse.getHeight() - sizeAdjust
				);
			
			grx.setTransform(transform);
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

		int topPadding = jrImage.getTopPadding();
		int leftPadding = jrImage.getLeftPadding();
		int bottomPadding = jrImage.getBottomPadding();
		int rightPadding = jrImage.getRightPadding();
		
		int availableImageWidth = jrImage.getWidth() - leftPadding - rightPadding;
		availableImageWidth = (availableImageWidth < 0)?0:availableImageWidth;

		int availableImageHeight = jrImage.getHeight() - topPadding - bottomPadding;
		availableImageHeight = (availableImageHeight < 0)?0:availableImageHeight;
		
		if (availableImageWidth > 0 && availableImageHeight > 0)
		{
			Image awtImage = null;
			
			JRExpression jrExpression = jrImage.getExpression();
			if (jrExpression != null && jrExpression.getChunks().length == 1)
			{
				JRExpressionChunk firstChunk = jrExpression.getChunks()[0];
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

						Shape clip = grx.getClip();
						grx.clipRect(
							jrImage.getX() + leftPadding, 
							jrImage.getY() + topPadding, 
							availableImageWidth, 
							availableImageHeight
							);
						grx.drawImage(
							awtImage, 
							jrImage.getX() + leftPadding + xoffset, 
							jrImage.getY() + topPadding + yoffset, 
							awtWidth, 
							awtHeight, 
							this
							);
						grx.setClip(clip);
		
						break;
					}
					case JRImage.SCALE_IMAGE_FILL_FRAME :
					{
						grx.drawImage(
							awtImage, 
							jrImage.getX() + leftPadding, 
							jrImage.getY() + topPadding, 
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
								jrImage.getX() + leftPadding + xoffset, 
								jrImage.getY() + topPadding + yoffset, 
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
	
				Shape clip = grx.getClip();
				grx.clipRect(
					jrImage.getX() + leftPadding, 
					jrImage.getY() + topPadding, 
					availableImageWidth, 
					availableImageHeight
					);
				grx.drawImage(
					awtImage, 
					jrImage.getX() + leftPadding + 2, 
					jrImage.getY() + topPadding + 2, 
					awtImage.getWidth(null), 
					awtImage.getHeight(null), 
					this
					);
				grx.setClip(clip);

				//borderOffset = 0;
				//stroke = new BasicStroke(1f / zoom);
			}
		}
		
		if (
			jrImage.getTopBorder() == JRGraphicElement.PEN_NONE &&
			jrImage.getLeftBorder() == JRGraphicElement.PEN_NONE &&
			jrImage.getBottomBorder() == JRGraphicElement.PEN_NONE &&
			jrImage.getRightBorder() == JRGraphicElement.PEN_NONE
			)
		{
			if (jrImage.getPen() != JRGraphicElement.PEN_NONE)
			{
				JRBox box = new JRBaseBox(jrImage.getPen(), jrImage.getForecolor());
				printBox(box, jrImage, grx);
			}
		}
		else
		{
			/*   */
			printBox(
				jrImage,
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
			JRExpression textExpression = ((JRTextField) textElement).getExpression();
			if (textExpression != null)
			{
				text = textExpression.getText();
			}
		}
		
		if (text == null)
		{
			text = "";
		}
		
		//text = JRStringUtil.treatNewLineChars(text);

		Map attributes = new HashMap(); 
		JRFontUtil.setAttributes(attributes, textElement);
		attributes.put(TextAttribute.FOREGROUND, textElement.getForecolor());

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
		
		int topPadding = text.getTopPadding();
		int leftPadding = text.getLeftPadding();
		int bottomPadding = text.getBottomPadding();
		int rightPadding = text.getRightPadding();
		
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
		simulationTextRenderer.render(
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
			text.getHorizontalAlignment(), 
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
			simulationTextRenderer.getTextHeight(), 
			text.getHorizontalAlignment(), 
			text.getVerticalAlignment(), 
			text.getLineSpacing(), 
			styledText, 
			allText
			);

		grx.rotate(-angle, x, y);

		/*   */
		printBox(
			text,
			text,
			grx
			);
	}

	
	/**
	 *
	 */
	private void printBox(JRBox box, Color defaultBorderColor, int x, int y, int width, int height, Graphics2D grx)
	{
		Stroke topStroke = null;
		Stroke leftStroke = null;
		Stroke bottomStroke = null;
		Stroke rightStroke = null;
		if (box != null)
		{
			topStroke = getBorderStroke(box.getTopBorder());
			leftStroke = getBorderStroke(box.getLeftBorder());
			bottomStroke = getBorderStroke(box.getBottomBorder());
			rightStroke = getBorderStroke(box.getRightBorder());
		}

		if (topStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(box.getTopBorder());
			
			grx.setStroke(topStroke);
			grx.setColor(box.getTopBorderColor() == null ? defaultBorderColor : box.getTopBorderColor());
	
			grx.translate(0, cornerOffset);
			grx.drawLine(x, y, x + width, y);
			grx.translate(0, -cornerOffset);
		}

		if (leftStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(box.getLeftBorder());
			
			grx.setStroke(leftStroke);
			grx.setColor(box.getLeftBorderColor() == null ? defaultBorderColor : box.getLeftBorderColor());
	
			grx.translate(cornerOffset, 0);
			grx.drawLine(x, y, x, y + height);
			grx.translate(-cornerOffset, 0);
		}

		if (bottomStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(box.getBottomBorder());
			
			grx.setStroke(bottomStroke);
			grx.setColor(box.getBottomBorderColor() == null ? defaultBorderColor : box.getBottomBorderColor());
	
			grx.translate(0, -cornerOffset);
			grx.drawLine(x, y + height, x + width, y + height); 
			grx.translate(0, cornerOffset);
		}

		if (rightStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(box.getRightBorder());
			
			grx.setStroke(rightStroke);
			grx.setColor(box.getRightBorderColor() == null ? defaultBorderColor : box.getRightBorderColor());
	
			grx.translate(-cornerOffset, 0);
			grx.drawLine(x + width, y, x + width, y + height);
			grx.translate(cornerOffset, 0);
		}

		if (
			topStroke == null
			&& leftStroke == null
			&& bottomStroke == null
			&& rightStroke == null
			)
		{
			grx.setColor(defaultBorderColor);
			grx.setStroke(new BasicStroke(1f / realZoom));
		
			grx.drawRect(x, y, width, height);
		}
	}

	private static Stroke getBorderStroke(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				return BORDER_STROKE_DOTTED;
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
				return BORDER_STROKE_THIN;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				return BORDER_STROKE_1_POINT;
			}
		}
	}

	private void printBox(JRBox box, JRElement element, Graphics2D grx)
	{
		printBox(box, element.getForecolor(), element.getX(), element.getY(), element.getWidth(), element.getHeight(), grx);
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

		Shape clip = grx.getClip();
		grx.clipRect(
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
		grx.setClip(clip);

		grx.setColor(subreport.getForecolor());
		grx.setStroke(new BasicStroke(1f / realZoom));
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
	private void printChart(JRChart chart, Graphics2D grx)
	{
		if (chart.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(chart.getBackcolor());

			grx.fillRect(
				chart.getX(), 
				chart.getY(), 
				chart.getWidth(),
				chart.getHeight()
				);
		}

		Image image = null;
		try
		{
			image = JRImageLoader.getImage(JRImageLoader.CHART_IMAGE);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}

		grx.setClip(
			chart.getX(), 
			chart.getY(), 
			chart.getWidth(), 
			chart.getHeight()
			);
		grx.drawImage(
			image, 
			chart.getX() + 2, 
			chart.getY() + 2, 
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

		grx.setColor(chart.getForecolor());
		grx.setStroke(new BasicStroke(1f / realZoom));
		grx.drawRect(
			chart.getX(), 
			chart.getY(), 
			chart.getWidth() - 1,
			chart.getHeight() - 1
			);
	}
	
	
	/**
	 *
	 */
	private void printCrosstab(JRCrosstab crosstab, Graphics2D grx)
	{
		grx.setClip(
				crosstab.getX(), 
				crosstab.getY(), 
				crosstab.getWidth(), 
				crosstab.getHeight()
				);
		
		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		int rowHeadersXOffset = 0;
		for (int i = 0; i < rowGroups.length; i++)
		{
			rowHeadersXOffset += rowGroups[i].getWidth();
		}
		
		JRCrosstabColumnGroup[] columnGroups = crosstab.getColumnGroups();
		int colHeadersYOffset = 0;
		for (int i = 0; i < columnGroups.length; i++)
		{
			colHeadersYOffset += columnGroups[i].getHeight();
		}
		
		JRCellContents headerCell = crosstab.getHeaderCell();
		if (headerCell != null)
		{
			grx.translate(crosstab.getX(), crosstab.getY());
			printCellContents(headerCell, grx, 0, 0, false, false);
			grx.translate(-crosstab.getX(), -crosstab.getY());
		}
		
		grx.translate(crosstab.getX() + rowHeadersXOffset, crosstab.getY());
		printCrosstabColumnHeaders(crosstab, grx);
		grx.translate(-(crosstab.getX() + rowHeadersXOffset), -crosstab.getY());
		
		grx.translate(crosstab.getX(), crosstab.getY() + colHeadersYOffset);
		printCrosstabRows(crosstab, grx, rowHeadersXOffset);
		grx.translate(-crosstab.getX(), -(crosstab.getY() + colHeadersYOffset));
		
		grx.setClip(
				- report.getLeftMargin(), 
				0, 
				report.getPageWidth(), 
				report.getPageHeight()
				);
	}


	private void printCrosstabColumnHeaders(JRCrosstab crosstab, Graphics2D grx)
	{
		JRCrosstabColumnGroup[] groups = crosstab.getColumnGroups();
		for (int i = 0, x = 0, y = 0; i < groups.length; i++)
		{
			JRCrosstabColumnGroup group = groups[i];
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_START)
			{
				JRCellContents totalHeader = group.getTotalHeader();
				printCellContents(totalHeader, grx, x, y, x == 0 && crosstab.getHeaderCell() == null, false);
				x += totalHeader.getWidth();
			}
			
			JRCellContents header = group.getHeader();
			printCellContents(header, grx, x, y, x == 0 && crosstab.getHeaderCell() == null, false);
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_END)
			{
				JRCellContents totalHeader = group.getTotalHeader();
				printCellContents(totalHeader, grx, x + header.getWidth(), y, false, false);
			}
			
			y += group.getHeight();
		}
	}
	
	
	private void printCrosstabRows(JRCrosstab crosstab, Graphics2D grx, int rowHeadersXOffset)
	{
		JRCrosstabRowGroup[] groups = crosstab.getRowGroups();
		for (int i = 0, x = 0, y = 0; i < groups.length; i++)
		{
			JRCrosstabRowGroup group = groups[i];
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_START)
			{
				JRCellContents totalHeader = group.getTotalHeader();
				printCellContents(totalHeader, grx, x, y, false, y == 0 && crosstab.getHeaderCell() == null);
				printCrosstabDataCellsRow(crosstab, grx, rowHeadersXOffset, y, i);
				y += totalHeader.getHeight();
			}
			
			JRCellContents header = group.getHeader();
			printCellContents(header, grx, x, y, false, y == 0 && crosstab.getHeaderCell() == null);
			
			if (i == groups.length - 1)
			{
				printCrosstabDataCellsRow(crosstab, grx, rowHeadersXOffset, y, groups.length);				
			}
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_END)
			{
				JRCellContents totalHeader = group.getTotalHeader();
				printCellContents(totalHeader, grx, x, y + header.getHeight(), false, false);
				printCrosstabDataCellsRow(crosstab, grx, rowHeadersXOffset, y + header.getHeight(), i);
			}
			
			x += group.getWidth();
		}
	}


	private void printCrosstabDataCellsRow(JRCrosstab crosstab, Graphics2D grx, int rowOffsetX, int rowOffsetY, int rowIndex)
	{
		grx.translate(rowOffsetX, rowOffsetY);
		
		JRCrosstabColumnGroup[] colGroups = crosstab.getColumnGroups();
		JRCrosstabCell[][] cells = crosstab.getCells();
		for (int i = 0, x = 0; i < colGroups.length; i++)
		{
			JRCrosstabColumnGroup group = colGroups[i];
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_START)
			{
				printCellContents(cells[rowIndex][i].getContents(), grx, x, 0, false, false);
				x += cells[rowIndex][i].getContents().getWidth();
			}
			
			if (i == colGroups.length - 1)
			{
				printCellContents(cells[rowIndex][colGroups.length].getContents(), grx, x, 0, false, false);
			}
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_END)
			{
				printCellContents(cells[rowIndex][i].getContents(), grx, x + group.getHeader().getWidth(), 0, false, false);
			}
		}
		
		grx.translate(-rowOffsetX, -rowOffsetY);
	}


	private void printCellContents(JRCellContents cell, Graphics2D grx, int x, int y, boolean left, boolean top)
	{
		if (cell.getWidth() == 0 || cell.getHeight() == 0)
		{
			return;
		}
		
		JRDesignFrame frame = createCrosstabCellFrame(cell, x, y, left, top);
		printFrame(frame, grx);
	}


	private JRDesignFrame createCrosstabCellFrame(JRCellContents cell, int x, int y, boolean left, boolean top)
	{
		JRDesignFrame frame = new JRDesignFrame(cell.getDefaultStyleProvider());
		frame.setX(x);
		frame.setY(y);
		frame.setWidth(cell.getWidth());
		frame.setHeight(cell.getHeight());
		
		frame.setMode(cell.getMode());
		frame.setBackcolor(cell.getBackcolor());
		frame.setStyle(cell.getStyle());
		
		JRBox box = cell.getBox();
		if (box != null)
		{
			frame.setBox(box);
			
			boolean copyLeft = left && box.getLeftBorder() == JRGraphicElement.PEN_NONE && box.getRightBorder() != JRGraphicElement.PEN_NONE;
			boolean copyTop = top && box.getTopBorder() == JRGraphicElement.PEN_NONE && box.getBottomBorder() != JRGraphicElement.PEN_NONE;
			
			if (copyLeft)
			{
				frame.setLeftBorder(box.getRightBorder());
				frame.setLeftBorderColor(box.getRightBorderColor());
			}
			
			if (copyTop)
			{
				frame.setTopBorder(box.getBottomBorder());
				frame.setTopBorderColor(box.getBottomBorderColor());
			}
		}
		
		List children = cell.getChildren();
		if (children != null)
		{
			for (Iterator it = children.iterator(); it.hasNext();)
			{
				JRChild child = (JRChild) it.next();
				if (child instanceof JRElement)
				{
					frame.addElement((JRElement) child);
				}
				else if (child instanceof JRElementGroup)
				{
					frame.addElementGroup((JRElementGroup) child);
				}
			}
		}
		
		return frame;
	}


	private void printFrame(JRFrame frame, Graphics2D grx)
	{
		if (frame.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(frame.getBackcolor());

			grx.fillRect(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
		}
		
		int topPadding = frame.getTopPadding();
		int leftPadding = frame.getLeftPadding();

		grx.translate(frame.getX() + leftPadding, frame.getY() + topPadding);
		printElements(frame.getElements(), grx);
		grx.translate(-(frame.getX() + leftPadding), -(frame.getY() + topPadding));
		
		printBox(frame, frame, grx);
	}


	/**
	 * 
	 */
	private static final double THIN_CORNER_OFFSET = 0.25d;
	private static final double ONE_POINT_CORNER_OFFSET = 0.5d;
	
	private static final Stroke STROKE_THIN = new BasicStroke(0.5f);
	private static final Stroke STROKE_1_POINT = new BasicStroke(1f);
	private static final Stroke STROKE_2_POINT = new BasicStroke(2f);
	private static final Stroke STROKE_4_POINT = new BasicStroke(4f);
	private static final Stroke STROKE_DOTTED = 
		new BasicStroke(
			1f,
			BasicStroke.CAP_SQUARE,
			BasicStroke.JOIN_MITER,
			10f,
			new float[]{5f, 3f},
			0f
			);
	
	private static final Stroke BORDER_STROKE_THIN = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	private static final Stroke BORDER_STROKE_1_POINT = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	private static final Stroke BORDER_STROKE_DOTTED = 
		new BasicStroke(
			1f,
			BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER,
			10f,
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
