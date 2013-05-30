/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.functions.annotations;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.MessageProvider;
import net.sf.jasperreports.engine.util.MessageUtil;
import net.sf.jasperreports.functions.FunctionsBundle;
import net.sf.jasperreports.functions.FunctionsUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: FunctionBean.java 6184 2013-05-27 10:24:51Z shertage $
 */
public class FunctionsInfo 
{
	/**
	 * 
	 */
	public static final String DEFAULT_MESSAGES_BUNDLE = "jasperreports_messages";
	public static final String PROPERTY_SUFFIX_NAME = "name";
	public static final String PROPERTY_SUFFIX_DESCRIPTION = "description";

	/**
	 * 
	 */
	private final JasperReportsContext jasperReportsContext;
	private final Locale locale;
	private final MessageUtil messageUtil;
	
	private final Map<String, FunctionCategoryBean> categories = new HashMap<String, FunctionCategoryBean>();
	
	/**
	 * 
	 */
	private FunctionsInfo(JasperReportsContext jasperReportsContext, Locale locale) 
	{
		this.jasperReportsContext = jasperReportsContext;
		this.locale = locale;
		this.messageUtil = MessageUtil.getInstance(jasperReportsContext);
		
		addFunctions();
	}
	
	/**
	 * 
	 */
	public static FunctionsInfo getInstance(JasperReportsContext jasperReportsContext, Locale locale)
	{
		return new FunctionsInfo(jasperReportsContext, locale);
	}
	
	/**
	 * 
	 */
	public Collection<FunctionCategoryBean> getCategories()
	{
		return categories.values();
	}
	
	/**
	 * 
	 */
	protected void addFunctions()
	{
		List<FunctionsBundle> bundles = FunctionsUtil.getInstance(jasperReportsContext).getAllFunctionBundles();
		for (FunctionsBundle bundle : bundles) 
		{
			List<Class<?>> functionClasses = bundle.getFunctionClasses();
			if(functionClasses != null && !functionClasses.isEmpty()) 
			{
				for (Class<?> functionClass : functionClasses) 
				{
					addFunctionClass(functionClass);
				}
			}
		}
	}
	
	/**
	 * 
	 */
	protected void addFunctionClass(Class<?> functionClass)
	{
		FunctionCategories categories = functionClass.getAnnotation(FunctionCategories.class);
		Set<String> defaultCategories = addCategories(categories);
		
		MessageProvider provider = getMessageProvider(functionClass);
		
		Method[] methods = functionClass.getMethods();
		if (methods != null && methods.length > 0) 
		{
			for (Method method : methods) 
			{
				addFunction(method, provider, defaultCategories);
			}
		}
	}
	
	/**
	 * 
	 */
	protected Set<String> addCategories(FunctionCategories categories)
	{
		Set<String> categoryIds = null;
		if(categories != null) 
		{
			categoryIds = new HashSet<String>();
			for(Class<?> categoryClass : categories.value()) 
			{
				String categoryId = addCategoryClass(categoryClass);
				categoryIds.add(categoryId);
			}
		}
		return categoryIds;
	}
	
	/**
	 * 
	 */
	protected void addFunction(Method functionMethod, MessageProvider provider, Set<String> categoryIds)
	{
		Function function = functionMethod.getAnnotation(Function.class);
		if(function != null) 
		{
			String functionId = functionMethod.getDeclaringClass().getName() + "." + function.value();

			String name = getName(functionId, provider);
			String description = getDescription(functionId, provider);
			
			FunctionBean functionBean = 
				new FunctionBean(
					functionId,
					name,
					description,
					functionMethod.getReturnType()
					);

			FunctionParameters functionParameters = functionMethod.getAnnotation(FunctionParameters.class);
			if (functionParameters != null && functionParameters.value().length > 0) 
			{
				for(FunctionParameter functionParameter : functionParameters.value()) 
				{
					addFunctionParameter(functionBean, functionParameter, provider);
				}
			} 

			FunctionParameter functionParameter = functionMethod.getAnnotation(FunctionParameter.class);
			if(functionParameter != null) 
			{
				addFunctionParameter(functionBean, functionParameter, provider);
			}

			FunctionCategories functionCategories = functionMethod.getAnnotation(FunctionCategories.class);
			Set<String> functionCategoryIds = addCategories(functionCategories);
			if (functionCategoryIds != null && functionCategoryIds.size() > 0)
			{
				categoryIds = functionCategoryIds;
			}

			for(String categoryId : categoryIds) 
			{
				FunctionCategoryBean categoryBean = categories.get(categoryId);
				categoryBean.addFunction(functionBean);
			}
		}
	}

	/**
	 * 
	 */
	protected void addFunctionParameter(FunctionBean functionBean, FunctionParameter functionParameter, MessageProvider provider)
	{
		String parameterId = functionBean.getId() + "." + functionParameter.value();

		String name = getName(parameterId, provider);
		String description = getDescription(parameterId, provider);
		
		FunctionParameterBean functionParameterBean = 
			new FunctionParameterBean(
				parameterId,
				name,
				description
				);
		
		functionBean.addParameter(functionParameterBean);
	}

	/**
	 * 
	 */
	protected String addCategoryClass(Class<?> categoryClass)
	{
		MessageProvider provider = getMessageProvider(categoryClass);
		
		String categoryId = categoryClass.getName();
		FunctionCategory category = categoryClass.getAnnotation(FunctionCategory.class);
		if(category != null && category.value() != null && category.value().trim().length() > 0) 
		{
			categoryId = category.value();
		}
		
		String categoryName = getName(categoryId, provider);
		String categoryDescription = getDescription(categoryId, provider);

		FunctionCategoryBean categoryBean = new FunctionCategoryBean(categoryId, categoryName, categoryDescription);
		
		categories.put(categoryId, categoryBean);
		
		return categoryId;
	}
	
	/**
	 * 
	 */
	private MessageProvider getMessageProvider(Class<?> clazz)
	{
		FunctionMessagesBundle functionMessagesBundle = clazz.getAnnotation(FunctionMessagesBundle.class);

		String messagesBundle = 
			functionMessagesBundle == null 
				? getDefaultMessageBundle(clazz)
				: functionMessagesBundle.value();

		return messageUtil.getMessageProvider(messagesBundle);//FIXME cache them
	}
		
	/**
	 * 
	 */
	private String getDefaultMessageBundle(Class<?> clazz) 
	{
		return clazz.getPackage().getName() + "." + DEFAULT_MESSAGES_BUNDLE;
	}
	
	/**
	 * 
	 */
	private String getName(String key, MessageProvider provider) 
	{
		return provider.getMessage(key + "." + PROPERTY_SUFFIX_NAME, null, locale);
	}
	
	/**
	 * 
	 */
	private String getDescription(String key, MessageProvider provider) 
	{
		return provider.getMessage(key + "." + PROPERTY_SUFFIX_DESCRIPTION, null, locale);
	}

}
