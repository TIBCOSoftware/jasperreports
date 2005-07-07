/*
 * FIXME NOW
 */
package net.sf.jasperreports.engine;

/**
 * @author John Bindel
 * @version $Id$
 */
public interface JRVirtualizable {
	/**
	 * Used by the virtualizer to identify the data for this object.
	 */
	String getUID();

	/**
	 * Used by the virtualizer to set data.
	 */
	void setVirtualData(Object o);

	/**
	 * Used by the virtualizer to get data.
	 */
	Object getVirtualData();

	/**
	 * Used by the virtualizer to remove the data from the object in memory so
	 * that it may be garbage collected.
	 */
	void removeVirtualData();

	/**
	 * Used by the virtualizer to set identity data.
	 */
	void setIdentityData(Object id);

	/**
	 * Used by the virtualizer to get identity data.
	 */
	Object getIdentityData();
}
