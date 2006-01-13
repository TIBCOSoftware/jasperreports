/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2005 Works, Inc.  http://www.works.com/
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
 * Works, Inc.
 * 6034 West Courtyard Drive
 * Suite 210
 * Austin, TX 78730-5032
 * USA
 * http://www.works.com/
 */

/*
 * Licensed to JasperSoft Corporation under a Contributer Agreement
 */
package net.sf.jasperreports.engine.base;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVirtualizable;
import net.sf.jasperreports.engine.JRVirtualizationHelper;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.JRVirtualizationContext;

/**
 * A print page that can be virtualized to free heap memory.
 * 
 * @author John Bindel
 * @version $Id$
 */
public class JRVirtualPrintPage implements JRPrintPage, JRVirtualizable, Serializable
{
	/**
	 * Identity objects are those that we want to replace when we devirtualize
	 * data. If object A was virtualized, and it is referenced outside the
	 * virtualized data, then we want to replace those references with object
	 * A', which is the version of the object that has been devirtualized. For
	 * example the Serialization mechanism creates a new version of the
	 * TextElement we want to be filled, but the bound object map references the
	 * original object A until we replace it with the new version A'.
	 */
	public static class ObjectIDPair implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		private final Object o;

		private final int id;

		public ObjectIDPair(Object o) {
			this.o = o;
			this.id = System.identityHashCode(o);
		}

		/**
		 * Gets the object.
		 */
		public Object getObject() {
			return o;
		}

		/**
		 * Gets the identity of the object. The identity is the current object's
		 * identity hash code before we deserialize, but when we have
		 * deserialized it, the identity is that of the object that was
		 * serialized, not that of the newly deserialized object.
		 */
		public int getIdentity() {
			return id;
		}
	}

	/**
	 * Classes that want to deal with the identity data should implement this.
	 * The JRBaseFiller needs to do this.
	 */
	public static interface IdentityDataProvider {
		/**
		 * Get identity data that the provider later want to handle when the
		 * virtual object is paged in.
		 */
		ObjectIDPair[] getIdentityData(JRVirtualPrintPage page);

		/**
		 * Handle the identity data as necessary.
		 */
		void setIdentityData(JRVirtualPrintPage page,
				ObjectIDPair[] identityData);
	}

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private static final Random random = new Random(System.currentTimeMillis());

	private static short counter = 1;
	
	
	protected List elements = new ArrayList();

	/**
	 * A unique identifier that is useful for serialization and deserialization
	 * to some persistence mechanism.
	 */
	private String uid;

	/**
	 * The object that does the virtualization work.
	 */
	private transient JRVirtualizer virtualizer;

	/**
	 * The filler object which has our identity data.
	 */
	private transient IdentityDataProvider[] identityProviders;

	private JRVirtualizationContext virtualizationContext;
	
	/**
	 * Constructs a virtualizable page.
	 */
	public JRVirtualPrintPage(JasperPrint printObject, JRVirtualizer virtualizer, JRVirtualizationContext virtualizationContext) {
		super();
		this.uid = makeUID(printObject);
		this.virtualizer = virtualizer;
		this.identityProviders = null;
		if (virtualizer != null) {
			virtualizer.registerObject(this);
		}
		
		this.virtualizationContext = virtualizationContext;
	}
	
	
	/**
	 * Make some unique identifier for this object.
	 */
	private static String makeUID(JasperPrint printObject) {
		synchronized (random) {
			return Integer.toString(System.identityHashCode(printObject)) + "_"
					+ (printObject.getPages().size()) + "_"
					+ Integer.toString(counter++) + "_"
					+ Integer.toString(random.nextInt());
		}
	}

	public final String getUID() {
		return this.uid;
	}

	public void setVirtualData(Object o)
	{
		elements = (List) o;
		
		afterElementVirtualization();
	}

	protected void afterElementVirtualization()
	{
		// restore the cached image renderes from the virtualization context
		List elems = getDeepElements();
		for (Iterator iter = elems.iterator(); iter.hasNext();)
		{
			JRPrintElement element = (JRPrintElement) iter.next();
			if (element instanceof JRPrintImage)
			{
				JRPrintImage image = (JRPrintImage) element;
				JRRenderable renderer = image.getRenderer();
				if (renderer != null && renderer instanceof JRIDHolderRenderer)
				{
					JRRenderable cachedRenderer = virtualizationContext.getCachedRenderer(renderer.getId());
					if (cachedRenderer == null)
					{
						throw new JRRuntimeException("Renderer " + renderer.getId() + " not found in virtualization context.");
					}
					image.setRenderer(cachedRenderer);
				}
			}
		}
	}

	
	public Object getVirtualData()
	{
		beforeElementVirtualization();
		
		return elements;
	}

	
	protected void beforeElementVirtualization()
	{
		// replacing image renderers cached in the virtualization context 
		// with dummy renderers that only store the renderer ID
		List elems = getDeepElements();
		for (Iterator it = elems.iterator(); it.hasNext();)
		{
			JRPrintElement element = (JRPrintElement) it.next();
			if (element instanceof JRPrintImage)
			{
				JRPrintImage image = (JRPrintImage) element;
				JRRenderable renderer = image.getRenderer();
				if (renderer != null && virtualizationContext.hasCachedRenderer(renderer.getId()))
				{
					image.setRenderer(new JRIDHolderRenderer(renderer));
				}
			}
		}
	}

	public void removeVirtualData() {
		elements = null;
	}

	public void setIdentityData(Object o) {
		if (identityProviders != null) {
			for (int i = 0; i < identityProviders.length; ++i) {
				identityProviders[i].setIdentityData(this, (ObjectIDPair[]) o);
			}
		}
	}

	public Object getIdentityData() {
		ObjectIDPair[] data;
		if (identityProviders != null) {
			if (identityProviders.length == 1) {
				data = identityProviders[0].getIdentityData(this);
			} else if (identityProviders.length > 1) {
				Set list = new HashSet();
				for (int i = 0; i < identityProviders.length; ++i) {
					ObjectIDPair[] pairs = identityProviders[i]
							.getIdentityData(this);
					if (pairs != null) {
						for (int j = 0; j < pairs.length; ++j) {
							list.add(pairs[j]);
						}
					}
				}
				data = (ObjectIDPair[]) list.toArray(new ObjectIDPair[list
						.size()]);
			} else {
				data = null;
			}
		} else {
			data = null;
		}

		return data;
	}

	public boolean isVirtualized() {
		return elements == null;
	}

	/**
	 * Sets the virtualizer.
	 */
	public void setVirtualizer(JRVirtualizer virtualizer) {
		this.virtualizer = virtualizer;
	}

	/**
	 * Gets the virtualizer.
	 */
	public JRVirtualizer getVirtualizer() {
		return this.virtualizer;
	}

	public void addIdentityDataProvider(IdentityDataProvider p) {
		if (identityProviders == null) {
			identityProviders = new IdentityDataProvider[] { p };
		} else {
			IdentityDataProvider[] newList = new IdentityDataProvider[identityProviders.length + 1];
			System.arraycopy(identityProviders, 0, newList, 0,
					identityProviders.length);
			newList[identityProviders.length] = p;
			identityProviders = newList;
		}
	}

	public void removeIdentityDataProvider(IdentityDataProvider p) {
		if (identityProviders != null) {
			int idx;
			for (idx = 0; idx < identityProviders.length; ++idx) {
				if (identityProviders[idx] == p) {
					IdentityDataProvider[] newList = new IdentityDataProvider[identityProviders.length - 1];
					System.arraycopy(identityProviders, 0, newList, 0, idx);
					int remaining = identityProviders.length - idx - 1;
					if (remaining > 0) {
						System.arraycopy(identityProviders, idx + 1, newList,
								idx, remaining);
					}
					identityProviders = newList;
					break;
				}
			}
		}
	}

	public List getElements()
	{
		ensureVirtualData();
		return elements;
	}

	protected void ensureVirtualData()
	{
		if (this.virtualizer != null)
		{
			if (isVirtualized())
			{
				// If virtualized, deserialize the List object and then set it on this page.
				this.virtualizer.requestData(this);
			}
			else
			{
				this.virtualizer.touch(this);
			}
		}
	}

	public void setElements(List elements) {
		cleanVirtualData();
		this.elements = elements;
	}

	protected void cleanVirtualData()
	{
		if (this.virtualizer != null)
		{
			if (isVirtualized())
			{
				// If virtualized, remove the persisted version of the List object.
				this.virtualizer.clearData(this);
			}
			else
			{
				this.virtualizer.touch(this);
			}
		}
	}

	
	public void addElement(JRPrintElement element)
	{
		ensureVirtualData();
		elements.add(element);
	}
	
	
	/**
	 * Dummy image renderer that only stores the ID of a cached renderer.
	 * When a page gets serialized, all image renderers that are cached in the
	 * virtualization context are replaced with dummy renderers that only store the ID.
	 * When a page gets deserialized, the original renderers are restored from the 
	 * virtualization context based on the ID.
	 */
	protected static class JRIDHolderRenderer implements JRRenderable, Serializable
	{
		private static final long serialVersionUID = 1L;
		
		protected final String id;
		
		protected JRIDHolderRenderer(JRRenderable renderer)
		{
			this.id = renderer.getId();
		}

		public String getId()
		{
			return id;
		}

		public byte getType()
		{
			return TYPE_IMAGE;
		}

		public byte getImageType()
		{
			return IMAGE_TYPE_UNKNOWN;
		}

		public Dimension2D getDimension() throws JRException
		{
			return null;
		}

		public byte[] getImageData() throws JRException
		{
			return null;
		}

		public void render(Graphics2D grx, Rectangle2D rectanle) throws JRException
		{
		}
	}
	
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		uid = (String) in.readObject();
		virtualizationContext = (JRVirtualizationContext) in.readObject();
		
		byte[] buffer = (byte[]) in.readUnshared();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer, 0, buffer.length);
		ObjectInputStream elementsStream = new ObjectInputStream(inputStream);
		elements = (List) elementsStream.readObject();
		afterElementVirtualization();
		
		setThreadVirtualizer();
	}

	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		ensureVirtualData();
		out.writeObject(uid);
		out.writeObject(virtualizationContext);
		
		beforeElementVirtualization();
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bout);
		stream.writeObject(elements);
		stream.flush();
		
		byte[] bytes = bout.toByteArray();
		out.writeUnshared(bytes);
	}

	
	private void setThreadVirtualizer()
	{
		JRVirtualizer threadVirtualizer = JRVirtualizationHelper.getThreadVirtualizer();
		if (threadVirtualizer != null)
		{
			virtualizer = threadVirtualizer;
			virtualizer.registerObject(this);
		}
	}


	protected void finalize()
	{
		if (virtualizer != null)
		{
			virtualizer.deregisterObject(this);
		}
	}
	
	
	/**
	 * Returns all the elements on tha page, including the ones placed inside
	 * {@link JRPrintFrame frames}.
	 * 
	 * @return all the elements on tha page
	 */
	protected List getDeepElements()
	{
		List deepElements = new ArrayList(elements.size());
		collectDeepElements(elements, deepElements);
		return deepElements;
	}

	protected void collectDeepElements(List elementsList, List deepElements)
	{
		for (Iterator it = elementsList.iterator(); it.hasNext();)
		{
			JRPrintElement element = (JRPrintElement) it.next();
			deepElements.add(element);
			
			if (element instanceof JRPrintFrame)
			{
				JRPrintFrame frame = (JRPrintFrame) element;
				collectDeepElements(frame.getElements(), deepElements);
			}
		}
	}
}
