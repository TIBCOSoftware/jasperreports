/*
 * Copyright (C) 2005 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package net.sf.jasperreports.components.pictonic.render;

import java.util.Map;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class PictonicResourceResolver implements ResourceResolver {

	private Map<String, Resource> resources;
	
	@Override
	public Resource resolve(String name) {
		return resources.get(name);
	}

	public Map<String, Resource> getResources() {
		return resources;
	}

	public void setResources(Map<String, Resource> resources) {
		this.resources = resources;
	}

}
