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

/*
 * Contributors:
 * Ryan Johnson - delscovich@users.sourceforge.net
 * Carlton Moore - cmoore79@users.sourceforge.net
 */
package dori.jasper.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JViewport;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JRHyperlink;
import dori.jasper.engine.JRPrintAnchorIndex;
import dori.jasper.engine.JRPrintElement;
import dori.jasper.engine.JRPrintHyperlink;
import dori.jasper.engine.JRPrintPage;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.JasperPrintManager;
import dori.jasper.engine.util.JRLoader;
import dori.jasper.engine.xml.JRPrintXmlLoader;


/**
 *
 */
public class JRViewer extends javax.swing.JPanel implements JRHyperlinkListener
{

	/**
	 *
	 */
	private static final int TYPE_FILE_NAME = 1;
	private static final int TYPE_INPUT_STREAM = 2;
	private static final int TYPE_JASPER_PRINT = 3;

	protected int zooms[] = {50, 75, 100, 125, 150, 175, 200, 250};

	private int type = TYPE_FILE_NAME;
	private boolean isXML = false;
	private String reportFileName = null;
	private JasperPrint jasperPrint = null;
	private int pageIndex = 0;
	private float zoom = 1f;

	private int downX = 0;
	private int downY = 0;

	//private JScrollBar hBar = null;
	//private JScrollBar vBar = null;

	private java.util.List hyperlinkListeners = new ArrayList();
	private Map linksMap = new HashMap();
	private MouseListener mouseListener = 
		new java.awt.event.MouseAdapter()
		{
			public void mouseClicked(java.awt.event.MouseEvent evt)
			{
				hyperlinkClicked(evt);
			}
		};


	/** Creates new form JRViewer */
	public JRViewer(String fileName, boolean isXML) throws JRException
	{
		this.setZooms();
		
		this.initComponents();
		this.cmbZoom.setSelectedIndex(2);//100%

		//hBar = scrollPane.getHorizontalScrollBar();
		//vBar = scrollPane.getVerticalScrollBar();

		this.loadReport(fileName, isXML);
		this.setPageIndex(0);
		this.refreshPage();
		
		this.addHyperlinkListener(this);
	}

	
	/** Creates new form JRViewer */
	public JRViewer(InputStream is, boolean isXML) throws JRException
	{
		this.setZooms();
		
		this.initComponents();
		this.cmbZoom.setSelectedIndex(2);//100%

		//hBar = scrollPane.getHorizontalScrollBar();
		//vBar = scrollPane.getVerticalScrollBar();

		this.loadReport(is, isXML);
		this.setPageIndex(0);
		this.refreshPage();

		this.addHyperlinkListener(this);
	}

	
	/** Creates new form JRViewer */
	public JRViewer(JasperPrint jrPrint) throws JRException
	{
		this.setZooms();
		
		this.initComponents();
		this.cmbZoom.setSelectedIndex(2);//100%

		//hBar = scrollPane.getHorizontalScrollBar();
		//vBar = scrollPane.getVerticalScrollBar();

		this.loadReport(jrPrint);
		this.setPageIndex(0);
		this.refreshPage();

		this.addHyperlinkListener(this);
	}

	
	/**
	 *
	 */
	public void clear()
	{
		this.emptyContainer(this);
		this.jasperPrint = null;
	}


	/**
	 *
	 */
	protected void setZooms()
	{
	}


	/**
	 *
	 */
	public void addHyperlinkListener(JRHyperlinkListener listener) throws JRException
	{
		this.hyperlinkListeners.add(listener);
	}


	/**
	 *
	 */
	public void removeHyperlinkListener(JRHyperlinkListener listener) throws JRException
	{
		this.hyperlinkListeners.remove(listener);
	}


	/**
	 *
	 */
	public void gotoHyperlink(JRPrintHyperlink hyperlink) throws JRException
	{
		switch(hyperlink.getHyperlinkType())
		{
			case JRHyperlink.HYPERLINK_TYPE_REFERENCE :
			{
				if (this.hyperlinkListeners != null && this.hyperlinkListeners.size() > 1)
				{
					System.out.println("Hyperlink reference : " + hyperlink.getHyperlinkReference());
					System.out.println("Implement your own JRHyperlinkListener to manage this type of event.");
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR :
			{
				if (hyperlink.getHyperlinkAnchor() != null)
				{
					Map anchorIndexes = jasperPrint.getAnchorIndexes();
					JRPrintAnchorIndex anchorIndex = (JRPrintAnchorIndex)anchorIndexes.get(hyperlink.getHyperlinkAnchor());
					if (anchorIndex.getPageIndex() != pageIndex)
					{
						this.setPageIndex(anchorIndex.getPageIndex());
						this.refreshPage();
					}
					Container container = pnlInScroll.getParent();
					if (container instanceof JViewport)
					{
						JViewport viewport = (JViewport) container;

						int newX = (int)(anchorIndex.getElement().getX() * zoom);
						int newY = (int)(anchorIndex.getElement().getY() * zoom);

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
				}
				
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE :
			{
				int page = pageIndex + 1;
				if (hyperlink.getHyperlinkPage() != null)
				{
					page = hyperlink.getHyperlinkPage().intValue();
				}
				
				if (page >= 1 && page <= jasperPrint.getPages().size() && page != pageIndex + 1)
				{
					this.setPageIndex(page - 1);
					this.refreshPage();
					Container container = pnlInScroll.getParent();
					if (container instanceof JViewport)
					{
						JViewport viewport = (JViewport) container;
						viewport.setViewPosition(new Point(0, 0));
					}
				}
				
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR :
			{
				if (this.hyperlinkListeners != null && this.hyperlinkListeners.size() > 1)
				{
					System.out.println("Hyperlink reference : " + hyperlink.getHyperlinkReference());
					System.out.println("Hyperlink anchor    : " + hyperlink.getHyperlinkAnchor());
					System.out.println("Implement your own JRHyperlinkListener to manage this type of event.");
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE :
			{
				if (this.hyperlinkListeners != null && this.hyperlinkListeners.size() > 1)
				{
					System.out.println("Hyperlink reference : " + hyperlink.getHyperlinkReference());
					System.out.println("Hyperlink page      : " + hyperlink.getHyperlinkPage());
					System.out.println("Implement your own JRHyperlinkListener to manage this type of event.");
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_NONE :
			default :
			{
				break;
			}
		}
	}

	
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        tlbToolBar = new javax.swing.JToolBar();
        btnPrint = new javax.swing.JButton();
        btnReload = new javax.swing.JButton();
        pnlSep01 = new javax.swing.JPanel();
        btnFirst = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
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
        pnlLinks = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        lblPage = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        tlbToolBar.setFloatable(false);
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dori/jasper/view/images/print.GIF")));
        btnPrint.setText("Print");
        btnPrint.setToolTipText("Print");
        btnPrint.setMaximumSize(new java.awt.Dimension(80, 23));
        btnPrint.setMinimumSize(new java.awt.Dimension(80, 23));
        btnPrint.setPreferredSize(new java.awt.Dimension(80, 23));
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        tlbToolBar.add(btnPrint);

        btnReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dori/jasper/view/images/reload.GIF")));
        btnReload.setText("Reload");
        btnReload.setToolTipText("Reload Document");
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

        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dori/jasper/view/images/first.GIF")));
        btnFirst.setToolTipText("First Page");
        btnFirst.setMaximumSize(new java.awt.Dimension(23, 23));
        btnFirst.setMinimumSize(new java.awt.Dimension(23, 23));
        btnFirst.setPreferredSize(new java.awt.Dimension(23, 23));
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        tlbToolBar.add(btnFirst);

        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dori/jasper/view/images/previous.GIF")));
        btnPrevious.setToolTipText("Previous Page");
        btnPrevious.setMaximumSize(new java.awt.Dimension(23, 23));
        btnPrevious.setMinimumSize(new java.awt.Dimension(23, 23));
        btnPrevious.setPreferredSize(new java.awt.Dimension(23, 23));
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        tlbToolBar.add(btnPrevious);

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dori/jasper/view/images/next.GIF")));
        btnNext.setToolTipText("Next Page");
        btnNext.setMaximumSize(new java.awt.Dimension(23, 23));
        btnNext.setMinimumSize(new java.awt.Dimension(23, 23));
        btnNext.setPreferredSize(new java.awt.Dimension(23, 23));
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        tlbToolBar.add(btnNext);

        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dori/jasper/view/images/last.GIF")));
        btnLast.setToolTipText("Last Page");
        btnLast.setMaximumSize(new java.awt.Dimension(23, 23));
        btnLast.setMinimumSize(new java.awt.Dimension(23, 23));
        btnLast.setPreferredSize(new java.awt.Dimension(23, 23));
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        tlbToolBar.add(btnLast);

        pnlSep02.setMaximumSize(new java.awt.Dimension(10, 10));
        tlbToolBar.add(pnlSep02);

        btnZoomIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dori/jasper/view/images/zoomin.GIF")));
        btnZoomIn.setToolTipText("Zoom In");
        btnZoomIn.setMaximumSize(new java.awt.Dimension(23, 23));
        btnZoomIn.setMinimumSize(new java.awt.Dimension(23, 23));
        btnZoomIn.setPreferredSize(new java.awt.Dimension(23, 23));
        btnZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomInActionPerformed(evt);
            }
        });

        tlbToolBar.add(btnZoomIn);

        btnZoomOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dori/jasper/view/images/zoomout.GIF")));
        btnZoomOut.setToolTipText("Zoom Out");
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
        pnlLinks.setLayout(null);

        pnlLinks.setMinimumSize(new java.awt.Dimension(5, 5));
        pnlLinks.setPreferredSize(new java.awt.Dimension(5, 5));
        pnlLinks.setOpaque(false);
        pnlLinks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlLinksMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlLinksMouseReleased(evt);
            }
        });

        pnlLinks.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlLinksMouseDragged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel4.add(pnlLinks, gridBagConstraints);

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

    private void pnlLinksMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLinksMouseDragged
		// Add your handling code here:
		/*
		hBar.setValue(
			(int)
			( hBar.getValue() + 
			( ( downX - evt.getX() ) * ( hBar.getMaximum() - hBar.getVisibleAmount() ) ) / 
			jasperPrint.getPageWidth() )
			);

		vBar.setValue(
			(int)
			( vBar.getValue() + 
			( ( downY - evt.getY() ) * ( vBar.getMaximum() - vBar.getVisibleAmount() ) ) / 
			jasperPrint.getPageHeight() )
			);
		*/

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
    }//GEN-LAST:event_pnlLinksMouseDragged

    private void pnlLinksMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLinksMouseReleased
		// Add your handling code here:
		this.pnlLinks.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_pnlLinksMouseReleased

    private void pnlLinksMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLinksMousePressed
		// Add your handling code here:
		this.pnlLinks.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		
		this.downX = evt.getX();
		this.downY = evt.getY();
    }//GEN-LAST:event_pnlLinksMousePressed

	private void btnPrintActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrintActionPerformed
	{//GEN-HEADEREND:event_btnPrintActionPerformed
		// Add your handling code here:
		
		Thread thread = 
			new Thread(
				new Runnable()
				{
					public void run()
					{
						try 
						{
							JasperPrintManager.printReport(jasperPrint, true);
						}
						catch (Exception ex) 
						{
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Error printing report. See the console for details.");
						}
					}
				}
			);
		
		thread.start();

	}//GEN-LAST:event_btnPrintActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnLastActionPerformed
    {//GEN-HEADEREND:event_btnLastActionPerformed
		// Add your handling code here:
		this.setPageIndex(jasperPrint.getPages().size() - 1);
		this.refreshPage();
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNextActionPerformed
    {//GEN-HEADEREND:event_btnNextActionPerformed
		// Add your handling code here:
		this.setPageIndex(pageIndex + 1);
		this.refreshPage();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPreviousActionPerformed
    {//GEN-HEADEREND:event_btnPreviousActionPerformed
		// Add your handling code here:
		this.setPageIndex(pageIndex - 1);
		this.refreshPage();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFirstActionPerformed
    {//GEN-HEADEREND:event_btnFirstActionPerformed
		// Add your handling code here:
		this.setPageIndex(0);
		this.refreshPage();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnReloadActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReloadActionPerformed
    {//GEN-HEADEREND:event_btnReloadActionPerformed
		// Add your handling code here:
		if (this.type == TYPE_FILE_NAME)
		{
			try
			{
				this.loadReport(this.reportFileName, this.isXML);
			}
			catch (JRException e)
			{
				e.printStackTrace();

				this.jasperPrint = null;
				this.setPageIndex(0);
				this.refreshPage();

				JOptionPane.showMessageDialog(this, "Error loading report. See the console for details.");
			}
			this.setPageIndex(0);
			this.cmbZoom.setSelectedIndex(2);//100%
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

    private void cmbZoomActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbZoomActionPerformed
    {//GEN-HEADEREND:event_cmbZoomActionPerformed
		// Add your handling code here:
		int index = this.cmbZoom.getSelectedIndex();
		this.zoom = zooms[index] / 100f;
		this.btnZoomIn.setEnabled( (index < this.cmbZoom.getModel().getSize() - 1) );
		this.btnZoomOut.setEnabled( (index > 0) );
		this.refreshPage();
    }//GEN-LAST:event_cmbZoomActionPerformed


	/**
	*/
	private void hyperlinkClicked(MouseEvent evt)
	{
		JPanel link = (JPanel)evt.getSource();
		JRPrintHyperlink element = (JRPrintHyperlink)this.linksMap.get(link);
		
		try
		{
			JRHyperlinkListener listener = null;
			for(int i = 0; i < this.hyperlinkListeners.size(); i++)
			{
				listener = (JRHyperlinkListener)this.hyperlinkListeners.get(i);
				listener.gotoHyperlink(element);
			}
		}
		catch(JRException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error encoutered when following hyperlink. See the console for details.");
		}
	}


	/**
	*/
	private void setPageIndex(int index)
	{
		this.pageIndex = index;
		if (
			this.jasperPrint != null && 
			this.jasperPrint.getPages() != null &&
			this.jasperPrint.getPages().size() > 0
			)
		{
			this.btnFirst.setEnabled( (pageIndex > 0) );
			this.btnPrevious.setEnabled( (pageIndex > 0) );
			this.btnNext.setEnabled( (pageIndex < jasperPrint.getPages().size() - 1) );
			this.btnLast.setEnabled( (pageIndex < jasperPrint.getPages().size() - 1) );
		}
		else
		{
			this.btnFirst.setEnabled(false);
			this.btnPrevious.setEnabled(false);
			this.btnNext.setEnabled(false);
			this.btnLast.setEnabled(false);
		}
	}


	/**
	*/
	protected void loadReport(String fileName, boolean isXML) throws JRException
	{
		if (isXML)
		{
			this.jasperPrint = JRPrintXmlLoader.load(fileName);
		}
		else
		{
			this.jasperPrint = (JasperPrint)JRLoader.loadObject(fileName);
		}

		this.type = TYPE_FILE_NAME;
		this.isXML = isXML;
		this.reportFileName = fileName;
		this.btnReload.setEnabled(true);
	}


	/**
	*/
	protected void loadReport(InputStream is, boolean isXML) throws JRException
	{
		if (isXML)
		{
			this.jasperPrint = JRPrintXmlLoader.load(is);
		}
		else
		{
			this.jasperPrint = (JasperPrint)JRLoader.loadObject(is);
		}

		this.type = TYPE_INPUT_STREAM;
		this.isXML = isXML;
		this.btnReload.setEnabled(false);
	}


	/**
	*/
	protected void loadReport(JasperPrint jrPrint) throws JRException
	{
		this.jasperPrint = jrPrint;
		this.type = TYPE_JASPER_PRINT;
		this.isXML = false;
		this.btnReload.setEnabled(false);
	}


	/**
	*/
	protected void refreshPage()
	{
		if (
			jasperPrint == null ||
			jasperPrint.getPages() == null ||
			jasperPrint.getPages().size() == 0
			)
		{
			this.pnlPage.setVisible(false);
			this.btnPrint.setEnabled(false);
			this.btnZoomIn.setEnabled(false);
			this.btnZoomOut.setEnabled(false);
			this.cmbZoom.setEnabled(false);
			
			if (jasperPrint != null)
			{
				JOptionPane.showMessageDialog(this, "The document has no pages.");
			}

			return;
		}

		this.pnlPage.setVisible(true);
		this.btnPrint.setEnabled(true);
		this.btnZoomIn.setEnabled(true);
		this.btnZoomOut.setEnabled(true);
		this.cmbZoom.setEnabled(true);
		
		Image image = null;
		ImageIcon imageIcon = null;

		Dimension dim = new Dimension(
			(int)(jasperPrint.getPageWidth() * zoom) + 8, // 2 from border, 5 from shadow and 1 extra pixel for image
			(int)(jasperPrint.getPageHeight() * zoom) + 8
			);
		this.pnlPage.setMaximumSize(dim);
		this.pnlPage.setMinimumSize(dim);
		this.pnlPage.setPreferredSize(dim);

		try
		{
			image = JasperPrintManager.printPageToImage(jasperPrint, pageIndex, zoom);
			imageIcon = new ImageIcon(image);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error displaying report page. See the console for details.");
		}

		pnlLinks.removeAll();
		this.linksMap = new HashMap();

		java.util.List pages = jasperPrint.getPages();
		JRPrintPage page = (JRPrintPage)pages.get(pageIndex);
		Collection elements = page.getElements();
		if(elements != null && elements.size() > 0)
		{
			String toolTip = null;
			JPanel link = null;
			JRPrintElement element = null;
			JRPrintHyperlink hyperlink = null;
			for(Iterator it = elements.iterator(); it.hasNext();)
			{
				element = (JRPrintElement)it.next();
				if (
					element instanceof JRPrintHyperlink && 
					((JRPrintHyperlink)element).getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE
					)
				{
					hyperlink = (JRPrintHyperlink)element;
					
					link = new JPanel();
					link.setCursor(new Cursor(Cursor.HAND_CURSOR));
					link.setLocation(
						(int)(element.getX() * zoom), 
						(int)(element.getY() * zoom)
						);
					link.setSize(
						(int)(element.getWidth() * zoom),
						(int)(element.getHeight() * zoom)
						);
					link.setOpaque(false);
					//link.setOpaque(true);
					//link.setBackground(Color.yellow);
					
					toolTip = null;
					switch(hyperlink.getHyperlinkType())
					{
						case JRHyperlink.HYPERLINK_TYPE_REFERENCE :
						{
							toolTip = hyperlink.getHyperlinkReference();
							break;
						}
						case JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR :
						{
							if (hyperlink.getHyperlinkAnchor() != null)
							{
								toolTip = "#" + hyperlink.getHyperlinkAnchor();
							}
							break;
						}
						case JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE :
						{
							if (hyperlink.getHyperlinkPage() != null)
							{
								toolTip = "#page " + hyperlink.getHyperlinkPage();
							}
							break;
						}
						case JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR :
						{
							toolTip = "";
							if (hyperlink.getHyperlinkReference() != null)
							{
								toolTip = toolTip + hyperlink.getHyperlinkReference();
							}
							if (hyperlink.getHyperlinkAnchor() != null)
							{
								toolTip = toolTip + "#" + hyperlink.getHyperlinkAnchor();
							}
							break;
						}
						case JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE :
						{
							toolTip = "";
							if (hyperlink.getHyperlinkReference() != null)
							{
								toolTip = toolTip + hyperlink.getHyperlinkReference();
							}
							if (hyperlink.getHyperlinkPage() != null)
							{
								toolTip = toolTip + "#page " + hyperlink.getHyperlinkPage();
							}
							break;
						}
						default :
						{
							break;
						}
					}
					
					link.setToolTipText(toolTip);
					link.addMouseListener(this.mouseListener);
					pnlLinks.add(link);
					this.linksMap.put(link, element);
				}
			}
		}
		
		this.lblPage.setIcon(imageIcon);
	}


	/**
	*/
	private void emptyContainer(Container container)
	{
		Component[] components = container.getComponents();

		if (components != null)
		{
			for(int i = 0; i < components.length; i++)
			{
				if (components[i] instanceof Container)
				{
					emptyContainer((Container)components[i]);
				}
			}
		}

		components = null; 
		container.removeAll(); 
		container = null; 
	} 


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JToolBar tlbToolBar;
    private javax.swing.JPanel pnlInScroll;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel pnlPage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel pnlLinks;
    private javax.swing.JPanel pnlMain;
    protected javax.swing.JPanel pnlSep02;
    protected javax.swing.JButton btnLast;
    protected javax.swing.JButton btnReload;
    private javax.swing.JPanel jPanel5;
    protected javax.swing.JButton btnPrevious;
    protected javax.swing.JButton btnZoomOut;
    private javax.swing.JLabel lblPage;
    private javax.swing.JPanel jPanel8;
    protected javax.swing.JButton btnZoomIn;
    private javax.swing.JPanel jPanel7;
    protected javax.swing.JButton btnNext;
    protected javax.swing.JPanel pnlSep01;
    protected javax.swing.JButton btnFirst;
    private javax.swing.JPanel jPanel6;
    protected javax.swing.JComboBox cmbZoom;
    private javax.swing.JPanel jPanel9;
    protected javax.swing.JButton btnPrint;
    // End of variables declaration//GEN-END:variables

}
