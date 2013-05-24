/*
 * Copyright (C) 2005 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package net.sf.jasperreports.components.pictonic.render;

import java.util.Map;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public interface Resource {

	String getLocation();
	String getName();
	Map<String, String> getDependencies();
	boolean isDynamic();
}
