/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
 package net.sf.jasperreports.engine.data;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;

import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;


/**
 * The base implementation for JRBeanXXXDataSource providers. It provides a common
 * implementation for bean properties introspection.
 * 
 * @author Peter Severin (peter_s@sourceforge.net, contact@jasperassistant.com)
 * @version $Id$
 */
public abstract class JRAbstractBeanDataSourceProvider implements JRDataSourceProvider 
{

	/** The introspected bean class */
	private Class beanClass;

	/**
	 * Creates the provider. Superclasses must pass a valid class that will be
	 * used to introspect the available bean fields.
	 */
	public JRAbstractBeanDataSourceProvider(Class beanClass) {
		if (beanClass == null)
			throw new NullPointerException("beanClass must not be null");

		this.beanClass = beanClass;
	}

	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#supportsGetFieldsOperation()
	 */
	public boolean supportsGetFieldsOperation() {
		return true;
	}
	
	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#getFields(net.sf.jasperreports.engine.JasperReport)
	 */
	public JRField[] getFields(JasperReport report) throws JRException {
		BeanInfo beanInfo = null;

		try {
			beanInfo = Introspector.getBeanInfo(beanClass);
		} catch (IntrospectionException e) {
			throw new JRException(e);
		}

		PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
		if(descriptors != null) {
			ArrayList fields = new ArrayList(descriptors.length);
			
			for (int i = 0; i < descriptors.length; i++) {
				PropertyDescriptor descriptor = descriptors[i];
				
				if (!(descriptor instanceof IndexedPropertyDescriptor) && descriptor.getReadMethod() != null) {
					JRDesignField field = new JRDesignField();
					String valueClassName = normalizeClass(descriptor.getPropertyType()).getName();
					if (!isValidFieldClass(valueClassName))
						valueClassName = Object.class.getName();
					field.setValueClassName(valueClassName);
					field.setName(descriptor.getName());
					
					fields.add(field);
				}
			}
	
			return (JRField[]) fields.toArray(new JRField[fields.size()]);
		} else {
			return new JRField[0];
		}
	}

	/**
	 * Converts a primitive class to its object counterpart
	 */
	private static Class normalizeClass(Class clazz) {
		if(clazz.isPrimitive()) {
			if(clazz == boolean.class)
				return Boolean.class;
			if(clazz == byte.class)
				return Byte.class;
			if(clazz == char.class)
				return Character.class;
			if(clazz == short.class)
				return Short.class;
			if(clazz == int.class)
				return Integer.class;
			if(clazz == long.class)
				return Long.class;
			if(clazz == float.class)
				return Float.class;
			if(clazz == double.class)
				return Double.class;
		}
		
		return clazz;
	}

	private static boolean isValidFieldClass(String className) {
		return Arrays.binarySearch(fieldClassNames, className) >= 0; 
	}

	private static String[] fieldClassNames;

	static {
		if (fieldClassNames == null) {
			fieldClassNames = new String[] {
					java.lang.Object.class.getName(),
					java.lang.Boolean.class.getName(),
					java.lang.Byte.class.getName(),
					java.util.Date.class.getName(),
					java.sql.Timestamp.class.getName(),
					java.sql.Time.class.getName(),
					java.lang.Double.class.getName(),
					java.lang.Float.class.getName(),
					java.lang.Integer.class.getName(),
					java.io.InputStream.class.getName(),
					java.lang.Long.class.getName(),
					java.lang.Short.class.getName(),
					java.math.BigDecimal.class.getName(),
					java.lang.String.class.getName() };

			Arrays.sort(fieldClassNames);
		}
	}
}
