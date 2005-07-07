/*
 * FIXME NOW
 */
package net.sf.jasperreports.engine;


/**
 * @author John Bindel
 * @version $Id$
 */
public interface JRVirtualizer
{
	/**
	 * Lets this virtualizer know that it must track the object.<p>
	 *
	 * All virtualizable object must register with their virtualizer
	 * upon construction.
	 */
 	void registerObject(JRVirtualizable o);

	/**
	 * Lets this virtualizer know that it no longer must track the
	 * object.
	 */
 	void deregisterObject(JRVirtualizable o);

	/**
	 * Lets the virtualizer know that this object is still being used.
	 * This should be called to help the virtualizer determine which
	 * objects to keep in its cache, and which objects to page-out
	 * when it must do some paging-out.<p>
	 *
	 * The virtualizer gets to decide what type of caching strategy
	 * it will use.
	 */
	void touch(JRVirtualizable o);

	/**
	 * Called when the virtual object must be paged-in.
	 */
	void requestData(JRVirtualizable o);

	/**
	 * Called when the virtual object paged-out data should be freed.
	 */
	void clearData(JRVirtualizable o);

	/**
	 * Called when the virtual object should be paged-out.
	 */
	void virtualizeData(JRVirtualizable o);

}
