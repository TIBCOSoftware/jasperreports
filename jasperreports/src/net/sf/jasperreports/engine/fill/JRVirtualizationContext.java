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
package net.sf.jasperreports.engine.fill;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVirtualizable;
import net.sf.jasperreports.engine.JRVirtualizationHelper;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.PrintElementVisitor;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.base.VirtualElementsData;
import net.sf.jasperreports.engine.util.DeepPrintElementVisitor;
import net.sf.jasperreports.engine.util.UniformPrintElementVisitor;

import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Context used to store data shared by virtualized objects resulted from a report fill process.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRVirtualizationContext implements Serializable, VirtualizationListener<VirtualElementsData>
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String EXCEPTION_MESSAGE_KEY_LOCKING_INTERRUPTED = "fill.virtualizer.locking.interrupted";
	public static final String EXCEPTION_MESSAGE_KEY_RENDERER_NOT_FOUND_IN_CONTEXT = "fill.virtualizer.renderer.not.found.in.context";
	public static final String EXCEPTION_MESSAGE_KEY_TEMPLATE_NOT_FOUND_IN_CONTEXT = "fill.virtualizer.template.not.found.in.context";
	
	private static final Log log = LogFactory.getLog(JRVirtualizationContext.class);
	
	private static final ReferenceMap contexts = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.WEAK);

	private transient JRVirtualizationContext parentContext;
	private transient JRVirtualizer virtualizer;
	private transient JasperReportsContext jasperReportsContext;
	
	private Map<String,Renderable> cachedRenderers;
	private Map<String,JRTemplateElement> cachedTemplates;
	
	private volatile boolean readOnly;
	private volatile boolean disposed;
	
	private int pageElementSize;
	
	private transient List<VirtualizationListener<VirtualElementsData>> listeners;
	
	private transient volatile PrintElementVisitor<Void> cacheTemplateVisitor;
	
	private transient ReentrantLock lock;
	
	/**
	 * Constructs a context.
	 */
	public JRVirtualizationContext(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
		
		cachedRenderers = new ConcurrentHashMap<String,Renderable>(16, 0.75f, 1);
		cachedTemplates = new ConcurrentHashMap<String,JRTemplateElement>(16, 0.75f, 1);
		
		pageElementSize = JRPropertiesUtil.getInstance(jasperReportsContext).getIntegerProperty(JRVirtualPrintPage.PROPERTY_VIRTUAL_PAGE_ELEMENT_SIZE, 0);
		
		initLock();
	}

	protected JRVirtualizationContext(JRVirtualizationContext parentContext)
	{
		this.parentContext = parentContext;
		this.virtualizer = parentContext.virtualizer;
		this.jasperReportsContext = parentContext.jasperReportsContext;

		// using the same caches as the parent
		this.cachedRenderers = parentContext.cachedRenderers;
		this.cachedTemplates = parentContext.cachedTemplates;

		this.pageElementSize = parentContext.pageElementSize;
		
		// always locking the master context
		this.lock = parentContext.lock;
	}
	
	private void initLock()
	{
		lock = new ReentrantLock(true);
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
			listeners = new CopyOnWriteArrayList<VirtualizationListener<VirtualElementsData>>();
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
	}
	
	public void afterExternalization(JRVirtualizable<VirtualElementsData> object)
	{
	}
	
	public void afterInternalization(JRVirtualizable<VirtualElementsData> object)
	{
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
		Renderable renderer = image.getRenderable();
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
	public Renderable getCachedRenderer(String id)
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
	 * @param element the element whose template to cache 
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
	 * @return the virtual page size used by the report
	 * @see JRVirtualPrintPage#PROPERTY_VIRTUAL_PAGE_ELEMENT_SIZE
	 */
	public int getPageElementSize()
	{
		return pageElementSize;
	}

	/**
	 * Set the virtual page size used by the report.
	 * 
	 * @param pageElementSize the virtual page size
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
		setThreadJasperReportsContext();

		GetField fields = in.readFields();
		cachedRenderers = (Map<String, Renderable>) fields.get("cachedRenderers", null);
		cachedTemplates = (Map<String, JRTemplateElement>) fields.get("cachedTemplates", null);
		readOnly = fields.get("readOnly", false);
		// use configured default if serialized by old version
		pageElementSize = fields.get("pageElementSize", JRPropertiesUtil.getInstance(jasperReportsContext).getIntegerProperty(
				JRVirtualPrintPage.PROPERTY_VIRTUAL_PAGE_ELEMENT_SIZE, 0));
		
		setThreadVirtualizer();
		
		initLock();
	}

	private void setThreadVirtualizer()
	{
		JRVirtualizer threadVirtualizer = JRVirtualizationHelper.getThreadVirtualizer();
		if (threadVirtualizer != null)
		{
			virtualizer = threadVirtualizer;
		}
	}

	private void setThreadJasperReportsContext()
	{
		JasperReportsContext threadJasperReportsContext = JRVirtualizationHelper.getThreadJasperReportsContext();
		if (threadJasperReportsContext != null)
		{
			jasperReportsContext = threadJasperReportsContext;
		}
		else if (jasperReportsContext == null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("no thread JRContext, using default");
			}
			
			jasperReportsContext = DefaultJasperReportsContext.getInstance();
		}
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
	
	public Object replaceSerializedObject(Object obj)
	{
		Object replace = obj;
		if (obj instanceof JRTemplateElement)
		{
			JRTemplateElement template = (JRTemplateElement) obj;
			String templateId = template.getId();
			if (hasCachedTemplate(templateId))
			{
				replace = new JRVirtualPrintPage.JRIdHolderTemplateElement(templateId);
			}
			else
			{
				if (log.isDebugEnabled())
				{
					log.debug("Template " + template + " having id " + template.getId() + " not found in virtualization context cache");
				}
			}
		}
		else if (obj instanceof Renderable)
		{
			Renderable renderer = (Renderable) obj;
			if (hasCachedRenderer(renderer.getId()))
			{
				replace = new JRVirtualPrintPage.JRIdHolderRenderer(renderer);
			}
		}
		return replace;
	}
	
	public Object resolveSerializedObject(Object obj)
	{
		Object resolve = obj;
		if (obj instanceof JRVirtualPrintPage.JRIdHolderTemplateElement)
		{
			JRVirtualPrintPage.JRIdHolderTemplateElement template = (JRVirtualPrintPage.JRIdHolderTemplateElement) obj;
			JRTemplateElement cachedTemplate = getCachedTemplate(template.getId());
			if (cachedTemplate == null)
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_TEMPLATE_NOT_FOUND_IN_CONTEXT,
						new Object[]{template.getId()});
			}
			resolve = cachedTemplate;
		}
		else if (obj instanceof JRVirtualPrintPage.JRIdHolderRenderer)
		{
			JRVirtualPrintPage.JRIdHolderRenderer renderer = (JRVirtualPrintPage.JRIdHolderRenderer) obj;
			Renderable cachedRenderer = getCachedRenderer(renderer.getId());
			if (cachedRenderer == null)
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_RENDERER_NOT_FOUND_IN_CONTEXT,
						new Object[]{renderer.getId()});
			}
			resolve = cachedRenderer;
		}
		return resolve;
	}

	/**
	 * Acquires a lock on this context.
	 */
	public void lock()
	{
		try
		{
			lock.lockInterruptibly();
		}
		catch (InterruptedException e)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_LOCKING_INTERRUPTED,
					(Object[])null,
					e);
		}
	}

	/**
	 * Attempts to acquire a lock on this context.
	 * 
	 * @return true iff the lock was acquired on the context
	 */
	public boolean tryLock()
	{
		return lock.tryLock();
	}

	/**
	 * Releases the lock previously acquired on this context.
	 */
	public void unlock()
	{
		lock.unlock();
	}
	
	/**
	 * Marks this context as disposed in order to instruct the virtualizer
	 * that pages owned by this context are no longer used.
	 */
	public void dispose()
	{
		disposed = true;
	}
	
	/**
	 * Determines if this context is marked as disposed.
	 * 
	 * @return whether this context has been marked as disposed
	 */
	public boolean isDisposed()
	{
		return disposed;
	}
	
	public JRVirtualizationContext getMasterContext()
	{
		JRVirtualizationContext context = this;
		while (context.parentContext != null)
		{
			context = context.parentContext;
		}
		return context;
	}
}
