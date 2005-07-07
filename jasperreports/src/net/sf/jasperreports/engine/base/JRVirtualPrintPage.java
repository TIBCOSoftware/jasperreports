/*
 * FIXME NOW
 */
package net.sf.jasperreports.engine.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRVirtualizable;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * A print page that can be virtualized to free heap memory.
 * 
 * @author John Bindel
 * @version $Id$
 */
public class JRVirtualPrintPage extends JRBasePrintPage implements
		JRVirtualizable {
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
		private static final long serialVersionUID = 608;

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

	private static final long serialVersionUID = 608;

	private static final Random random = new Random(System.currentTimeMillis());

	private static short counter = 1;

	/**
	 * A unique identifier that is useful for serialization and deserialization
	 * to some persistence mechanism.
	 */
	private final String uid;

	/**
	 * The object that does the virtualization work.
	 */
	private transient JRVirtualizer virtualizer;

	/**
	 * The filler object which has our identity data.
	 */
	private transient IdentityDataProvider[] identityProviders;

	/**
	 * Constructs a virtualizable page.
	 */
	public JRVirtualPrintPage(JasperPrint printObject, JRVirtualizer virtualizer) {
		super();
		this.uid = makeUID(printObject);
		this.virtualizer = virtualizer;
		this.identityProviders = null;
		if (virtualizer != null) {
			virtualizer.registerObject(this);
		}
	}

	/**
	 * Make some unique identifier for this object.
	 */
	private static String makeUID(JasperPrint printObject) {
		synchronized (random) {
			return Integer.toString(System.identityHashCode(printObject)) + "_"
					+ (printObject.getPages().size()) + "_"
					+ Integer.toString(counter++) + "_"
					+ Integer.toString((short) random.nextInt());
		}
	}

	public final String getUID() {
		return this.uid;
	}

	public void setVirtualData(Object o) {
		super.setElements((List) o);
	}

	public Object getVirtualData() {
		return super.getElements();
	}

	public void removeVirtualData() {
		super.setElements(null);
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
		return super.getElements() == null;
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
					int remaining = identityProviders.length - idx;
					if (remaining > 0) {
						System.arraycopy(identityProviders, idx + 1, newList,
								idx, remaining);
					}
					break;
				}
			}
		}
	}

	public List getElements() {
		if (this.virtualizer != null) {
			if (isVirtualized()) {
				// If virtualized, deserialize the List object and then set it
				// on this page.
				this.virtualizer.requestData(this);
			} else {
				this.virtualizer.touch(this);
			}
		}
		return super.getElements();
	}

	public void setElements(List elements) {
		if (this.virtualizer != null) {
			if (isVirtualized()) {
				// If virtualized, remove the persisted version of the List
				// object.
				this.virtualizer.clearData(this);
			} else {
				this.virtualizer.touch(this);
			}
		}
		super.setElements(elements);
	}

	public void addElement(JRPrintElement element) {
		if (this.virtualizer != null) {
			if (isVirtualized()) {
				// If virtualized, deserialize the List object and then set it
				// on this page.
				this.virtualizer.requestData(this);
			} else {
				this.virtualizer.touch(this);
			}
		}
		super.addElement(element);
	}
}
