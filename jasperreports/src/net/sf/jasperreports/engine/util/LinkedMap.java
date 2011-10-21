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
package net.sf.jasperreports.engine.util;

import java.util.HashMap;

/**
 * A doubly linked list that can also map entries on keys.
 * 
 * The list can be iterated in FIFO order and implements the following operations 
 * in constant time:
 * <ul>
 * <li>adding an element at the start</li>
 * <li>adding an element at the end</li>
 * <li>finding and removing an element based on a key</li>
 * </ul>
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class LinkedMap<K, V>
{

	protected static class LinkedValue<K, V>
	{
		private LinkedValue<K, V> prev;
		private LinkedValue<K, V> next;
		private K key;
		private V value;
		
		protected LinkedValue(K key, V value)
		{
			this.key = key;
			this.value = value;
		}
	}
	
	private final LinkedValue<K, V> header;
	private final HashMap<K, LinkedValue<K, V>> map;
	
	/**
	 * Creates a list.
	 */
	public LinkedMap()
	{
		map = new HashMap<K, LinkedValue<K, V>>();
		header = new LinkedValue<K, V>(null, null);
		header.prev = header;
		header.next = header;
	}
	
	/**
	 * Adds a value at the end of the list.
	 * 
	 * @param key the not-null key to which the value is mapped
	 * @param value the value
	 */
	public void add(K key, V value)
	{
		if (key != null && map.containsKey(key))
		{
			//NOP
			return;
		}
		
		// add last
		LinkedValue<K, V> entry = new LinkedValue<K, V>(key, value);
		entry.prev = header.prev;
		entry.next = header;
		header.prev.next = entry;
		header.prev = entry;
		
		if (key != null)
		{
			map.put(key, entry);
		}
	}
	
	/**
	 * Adds a value at the start of the list
	 * 
	 * @param key the not-null key to which the value is mapped
	 * @param value the value
	 */
	public void addFirst(K key, V value)
	{
		if (key != null && map.containsKey(key))
		{
			//NOP
			return;
		}
		
		// add first
		LinkedValue<K, V> entry = new LinkedValue<K, V>(key, value);
		entry.next = header.next;
		entry.prev = header;
		header.next.prev = entry;
		header.next = entry;
		
		if (key != null)
		{
			map.put(key, entry);
		}
	}

	/**
	 * Determines whether the list is empty.
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return header.next == header;
	}
	
	/**
	 * Removes and returns the first element in the list.
	 * 
	 * @return
	 */
	public V pop()
	{
		if (header.next == header)
		{
			throw new IllegalStateException("Empty map");
		}
		
		LinkedValue<K, V> entry = header.next;
		entry.prev.next = entry.next;
		entry.next.prev = entry.prev;
		
		if (entry.key != null)
		{
			map.remove(entry.key);
		}

		return entry.value;
	}
	
	/**
	 * Removes and returns an element mapped to a key.
	 * 
	 * @param key
	 * @return the element mapped to the key, <code>null</code> if the key is not mapped
	 */
	public V remove(K key)
	{
		if (key == null)
		{
			throw new NullPointerException("Key cannot be null");
		}
		
		LinkedValue<K, V> entry = map.remove(key);
		if (entry == null)
		{
			return null;
		}
		
		entry.prev.next = entry.next;
		entry.next.prev = entry.prev;
		return entry.value;
	}
}
