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
package net.sf.jasperreports.engine.fill;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVirtualizable;
import net.sf.jasperreports.engine.JRVirtualizationHelper;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.PrintElementVisitor;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.base.VirtualElementsData;
import net.sf.jasperreports.engine.util.CompositePrintElementVisitor;
import net.sf.jasperreports.engine.util.DeepPrintElementVisitor;
import net.sf.jasperreports.engine.util.DefaultPrintElementVisitor;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.UniformPrintElementVisitor;

import org.apache.commons.collections.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Context used to store data shared by virtualized objects resulted from a report fill process.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRVirtualizationContext implements Serializable, VirtualizationListener<VirtualElementsData>
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final Log log = LogFactory.getLog(JRVirtualizationContext.class);
	
	private static final ReferenceMap contexts = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.WEAK);

	private transient JRVirtualizer virtualizer;
	
	private Map<String,JRRenderable> cachedRenderers;
	private Map<String,JRTemplateElement> cachedTemplates;
	
	private boolean readOnly;
	
	private int pageElementSize;
	
	private transient List<VirtualizationListener<VirtualElementsData>> listeners;
	
	private transient volatile PrintElementVisitor<Void> cacheTemplateVisitor;
	
	/**
	 * Constructs a context.
	 */
	public JRVirtualizationContext()
	{
		cachedRenderers = new HashMap<String,JRRenderable>();
		cachedTemplates = new HashMap<String,JRTemplateElement>();
		
		pageElementSize = JRProperties.getIntegerProperty(JRVirtualPrintPage.PROPERTY_VIRTUAL_PAGE_ELEMENT_SIZE, 0);
	}

	protected JRVirtualizationContext(JRVirtualizationContext parentContext)
	{
		this.virtualizer = parentContext.virtualizer;

		// using the same caches as the parent
		this.cachedRenderers = parentContext.cachedRenderers;
		this.cachedTemplates = parentContext.cachedTemplates;

		this.pageElementSize = parentContext.pageElementSize;
	}
	
	/**
	 * Adds a virtualization listener.
	 * 
	 * @param listener
	 */
	public void addListener(VirtualizationListener<VirtualElementsData> listener)
	{
		if (listeners == null)
		{
			listeners = new ArrayList<VirtualizationListener<VirtualElementsData>>(1);
		}
		
		listeners.add(listener);
	}
	
	/**
	 * Remove a virtualization listener.
	 * 
	 * @param listener
	 */
	public void removeListener(VirtualizationListener<VirtualElementsData> listener)
	{
		if (listeners != null)
		{
			listeners.remove(listener);
		}
	}

	public void beforeExternalization(JRVirtualizable<VirtualElementsData> object)
	{
		if (listeners != null)
		{
			for (VirtualizationListener<VirtualElementsData> listener : listeners)
			{
				listener.beforeExternalization(object);	
			}
		}

		setElementsExternalData(object.getVirtualData().getElements());
	}
	
	public void afterExternalization(JRVirtualizable<VirtualElementsData> object)
	{
		restoreElementsData(object.getVirtualData().getElements());
	}
	
	public void afterInternalization(JRVirtualizable<VirtualElementsData> object)
	{
		restoreElementsData(object.getVirtualData().getElements());
		
		if (listeners != null)
		{
			for (VirtualizationListener<VirtualElementsData> listener : listeners)
			{
				listener.afterInternalization(object);
			}
		}
	}
	
	/**
	 * Caches an image renderer.
	 * 
	 * @param image the image whose renderer should be cached
	 */
	public void cacheRenderer(JRPrintImage image)
	{
		JRRenderable renderer = image.getRenderer();
		if (renderer != null)
		{
			cachedRenderers.put(renderer.getId(), renderer);
		}
	}

	
	/**
	 * Retrieves a cached image renderer based on an ID.
	 * 
	 * @param id the ID
	 * @return the cached image renderer for the ID
	 */
	public JRRenderable getCachedRenderer(String id)
	{
		return cachedRenderers.get(id);
	}

	
	/**
	 * Determines whether a cached image renderer for a specified ID exists.
	 * 
	 * @param id the ID
	 * @return <code>true</code> if and only if the context contains a cached renderer with the specified ID
	 */
	public boolean hasCachedRenderer(String id)
	{
		return cachedRenderers.containsKey(id);
	}

	
	/**
	 * Determines whether a cached {@link JRTemplateElement template} with a specified ID exists.
	 * 
	 * @param id the template ID
	 * @return <code>true</code> if and only if the context contains a cached template with the specified ID
	 */
	public boolean hasCachedTemplate(String id)
	{
		return cachedTemplates.containsKey(id);
	}
	
	
	/**
	 * Caches an element template.
	 * 
	 * @param template the template to cache
	 */
	public void cacheTemplate(JRTemplateElement template)
	{
		Object old = cachedTemplates.put(template.getId(), template);
		if (old == null && log.isDebugEnabled())
		{
			log.debug("Cached template " + template + " having id " + template.getId());
		}
	}
	
	
	/**
	 * Retrieves a cached template.
	 * 
	 * @param templateId the template ID
	 * @return the cached template having the given ID
	 */
	public JRTemplateElement getCachedTemplate(String templateId)
	{
		return cachedTemplates.get(templateId);
	}

	/**
	 * Caches the template of an element.
	 * 
	 * @param template 
	 */
	public void cacheTemplate(JRPrintElement element)
	{
		if (cacheTemplateVisitor == null)
		{
			cacheTemplateVisitor = new CacheTemplateVisitor();
		}
		
		element.accept(cacheTemplateVisitor, null);
	}

	/**
	 * Determines whether this context has been marked as read-only.
	 * 
	 * @return whether this context has been marked as read-only
	 * @see #setReadOnly(boolean)
	 */
	public boolean isReadOnly()
	{
		return readOnly;
	}


	/**
	 * Sets the read-only flag for this context.
	 * <p>
	 * When in read-only mode, all the virtualizable objects belonging to this context
	 * are assumed final by the virtualizer and any change in a virtualizable object's data
	 * would be discarded on virtualization.
	 * 
	 * @param readOnly the read-only flag
	 */
	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}
	
	
	/**
	 * Registers a virtualization context for {@link JasperPrint JasperPrint} object.
	 * 
	 * @param context the virtualization context
	 * @param print the print object
	 */
	public static void register(JRVirtualizationContext context, JasperPrint print)
	{
		synchronized (contexts)
		{
			contexts.put(print, context);
		}
	}

	
	/**
	 * Returns the virtualization context registered for a print object.
	 * <p>
	 * When the engine fills a report using a virtualizer, it {@link #register(JRVirtualizationContext, JasperPrint) registers}
	 * the virtualization context with the generated {@link JasperPrint JasperPrint} object so that the caller
	 * would be able to retrieve the context based on the returned print object.
	 * 
	 * @param print a print object
	 * @return the virtualization context registered for the print object, or <code>null</code> if no context
	 * has been registered
	 */
	public static JRVirtualizationContext getRegistered(JasperPrint print)
	{
		synchronized (contexts)
		{
			return (JRVirtualizationContext) contexts.get(print);
		}
	}
	
	/**
	 * Returns the virtual page size used by the report.
	 * 
	 * @return
	 * @see JRVirtualPrintPage#PROPERTY_VIRTUAL_PAGE_ELEMENT_SIZE
	 */
	public int getPageElementSize()
	{
		return pageElementSize;
	}

	/**
	 * Set the virtual page size used by the report.
	 * 
	 * @param elementPageSize
	 * @see JRVirtualPrintPage#PROPERTY_VIRTUAL_PAGE_ELEMENT_SIZE
	 */
	public void setPageElementSize(int pageElementSize)
	{
		this.pageElementSize = pageElementSize;
	}

	/**
	 * Returns the virtualizer used by this context.
	 */
	public JRVirtualizer getVirtualizer()
	{
		return virtualizer;
	}

	protected void setVirtualizer(JRVirtualizer virtualizer)
	{
		this.virtualizer = virtualizer;
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		GetField fields = in.readFields();
		cachedRenderers = (Map<String, JRRenderable>) fields.get("cachedRenderers", null);
		cachedTemplates = (Map<String, JRTemplateElement>) fields.get("cachedTemplates", null);
		readOnly = fields.get("readOnly", false);
		// use configured default if serialized by old version
		pageElementSize = fields.get("pageElementSize", JRProperties.getIntegerProperty(
				JRVirtualPrintPage.PROPERTY_VIRTUAL_PAGE_ELEMENT_SIZE, 0));
		
		setThreadVirtualizer();
	}

	private void setThreadVirtualizer()
	{
		JRVirtualizer threadVirtualizer = JRVirtualizationHelper.getThreadVirtualizer();
		if (threadVirtualizer != null)
		{
			virtualizer = threadVirtualizer;
		}
	}

	protected void setElementsExternalData(Collection<? extends JRPrintElement> elements)
	{
		@SuppressWarnings("unchecked")
		PrintElementVisitor<Void> visitor = new CompositePrintElementVisitor<Void>(
				new ExternalizationTemplateVisitor(),
				new ExternalizationImageVisitor());
		traverseDeepElements(visitor, elements);
	}

	public void restoreElementsData(Collection<? extends JRPrintElement> elements)
	{
		@SuppressWarnings("unchecked")
		PrintElementVisitor<Void> visitor = new CompositePrintElementVisitor<Void>(
				new InternalizationTemplateVisitor(),
				new InternalizationImageVisitor());
		traverseDeepElements(visitor, elements);
	}
	
	/**
	 * Traverses all the elements on the page, including the ones placed inside
	 * {@link JRPrintFrame frames}.
	 * 
	 * @param visitor element visitor
	 */
	protected void traverseDeepElements(PrintElementVisitor<Void> visitor, 
			Collection<? extends JRPrintElement> elements)
	{
		DeepPrintElementVisitor<Void> deepVisitor = new DeepPrintElementVisitor<Void>(visitor);
		for (JRPrintElement element : elements)
		{
			element.accept(deepVisitor, null);
		}
	}

	/**
	 * Print element visitor that caches print element templates.
	 * 
	 * @see JRVirtualizationContext#cacheTemplate(JRPrintElement)
	 */
	class CacheTemplateVisitor extends UniformPrintElementVisitor<Void>
	{
		public CacheTemplateVisitor()
		{
			super(true);
		}
		
		@Override
		protected void visitElement(JRPrintElement element, Void arg)
		{
			if (element instanceof JRTemplatePrintElement)
			{
				JRTemplatePrintElement templateElement = (JRTemplatePrintElement) element;
				JRTemplateElement template = templateElement.getTemplate();
				if (template != null)
				{
					cacheTemplate(template);
				}
			}
		}	
	}
	
	protected class ExternalizationTemplateVisitor extends UniformPrintElementVisitor<Void>
	{
		private final Map<String, JRVirtualPrintPage.JRIdHolderTemplateElement> idTemplates = 
				new HashMap<String, JRVirtualPrintPage.JRIdHolderTemplateElement>();

		@Override
		protected void visitElement(JRPrintElement element, Void arg)
		{
			// replacing element template with dummy template that only stores the template ID
			if (element instanceof JRTemplatePrintElement)
			{
				setExternalizationTemplate((JRTemplatePrintElement) element);
			}
		}

		protected void setExternalizationTemplate(JRTemplatePrintElement templateElement)
		{
			JRTemplateElement template = templateElement.getTemplate();
			if (template != null)
			{
				if (hasCachedTemplate(template.getId()))
				{
					String templateId = template.getId();
					JRVirtualPrintPage.JRIdHolderTemplateElement idTemplate = idTemplates.get(templateId);
					if (idTemplate == null)
					{
						idTemplate = new JRVirtualPrintPage.JRIdHolderTemplateElement(templateId);
						idTemplates.put(templateId, idTemplate);
					}
					templateElement.setTemplate(idTemplate);
				}
				else
				{
					if (log.isDebugEnabled())
					{
						log.debug("Template " + template + " having id " + template.getId() + " not found in virtualization context cache");
					}
				}
			}
		}
	}
	
	protected class ExternalizationImageVisitor extends DefaultPrintElementVisitor<Void>
	{
		@Override
		public void visit(JRPrintImage image, Void arg)
		{
			// replacing image renderer cached in the virtualization context 
			// with dummy renderer that only stores the renderer ID
			setExternalizationRenderer(image);
		}

		protected void setExternalizationRenderer(JRPrintImage image)
		{
			JRRenderable renderer = image.getRenderer();
			if (renderer != null && hasCachedRenderer(renderer.getId()))
			{
				image.setRenderer(new JRVirtualPrintPage.JRIdHolderRenderer(renderer));
			}
		}
	}
	
	protected class InternalizationTemplateVisitor extends UniformPrintElementVisitor<Void>
	{

		@Override
		protected void visitElement(JRPrintElement element, Void arg)
		{			
			if (element instanceof JRTemplatePrintElement)
			{
				// restore the cached element template from the virtualization context
				restoreTemplate((JRTemplatePrintElement) element);
			}
		}

		protected void restoreTemplate(JRTemplatePrintElement element)
		{
			JRTemplateElement template = element.getTemplate();
			if (template != null && template instanceof JRVirtualPrintPage.JRIdHolderTemplateElement)
			{
				JRTemplateElement cachedTemplate = getCachedTemplate(template.getId());
				if (cachedTemplate == null)
				{
					throw new JRRuntimeException("Template " + template.getId() + " not found in virtualization context.");
				}

				element.setTemplate(cachedTemplate);
			}
		}
	}
	
	protected class InternalizationImageVisitor extends DefaultPrintElementVisitor<Void>
	{
		@Override
		public void visit(JRPrintImage image, Void arg)
		{
			// restore the cached image rendere from the virtualization context
			restoreRenderer(image);
		}

		protected void restoreRenderer(JRPrintImage image)
		{
			JRRenderable renderer = image.getRenderer();
			if (renderer != null && renderer instanceof JRVirtualPrintPage.JRIdHolderRenderer)
			{
				JRRenderable cachedRenderer = getCachedRenderer(renderer.getId());
				if (cachedRenderer == null)
				{
					throw new JRRuntimeException("Renderer " + renderer.getId() + " not found in virtualization context.");
				}
				image.setRenderer(cachedRenderer);
			}
		}		
	}
}
