package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;

public class ElementalPropertiesHolder implements JRPropertiesHolder
{

	private JRPropertiesMap propertiesMap;

	public ElementalPropertiesHolder()
	{
		super();
	}

	@Override
	public boolean hasProperties()
	{
		return propertiesMap != null && propertiesMap.hasProperties();
	}

	@Override
	public synchronized JRPropertiesMap getPropertiesMap()
	{
		if (propertiesMap == null)
		{
			propertiesMap = new JRPropertiesMap();
		}
		return propertiesMap;
	}

	@Override
	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}

}
