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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.MessageProvider;
import net.sf.jasperreports.engine.util.MessageUtil;
import net.sf.jasperreports.functions.FunctionsBundle;
import net.sf.jasperreports.functions.FunctionsUtil;
import net.sf.jasperreports.functions.annotations.Function;
import net.sf.jasperreports.functions.annotations.FunctionCategories;
import net.sf.jasperreports.functions.annotations.FunctionCategory;
import net.sf.jasperreports.functions.annotations.FunctionMessagesBundle;
import net.sf.jasperreports.functions.annotations.FunctionParameter;
import net.sf.jasperreports.functions.annotations.FunctionParameters;
import net.sf.jasperreports.functions.beans.FunctionBean;
import net.sf.jasperreports.functions.beans.FunctionCategoryBean;
import net.sf.jasperreports.functions.beans.FunctionParameterBean;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: GroovyApp.java 5876 2013-01-07 19:05:05Z teodord $
 */
public class FunctionsApp extends AbstractSampleApp
{


	/**
	 *
	 */
	public static void main(String[] args)
	{
		main(new FunctionsApp(), args);
	}
	
	
	/**
	 *
	 */
	public void test() throws JRException
	{
		fill();
		pdf();
	}


	/**
	 *
	 */
	public void fill() throws JRException
	{
		long start = System.currentTimeMillis();
		JRBeanCollectionDataSource datasource = createDataSource(DefaultJasperReportsContext.getInstance(), Locale.US, false, true);

		JasperFillManager.fillReportToFile("build/reports/FunctionsReport.jasper", null, datasource);
		System.err.println("Filling time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void pdf() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToPdfFile("build/reports/FunctionsReport.jrprint");
		System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
	}
	
	public JRBeanCollectionDataSource createDataSource(
			JasperReportsContext jasperReportsContext, 
			Locale locale, 
			boolean isUseFieldDescription, 
			boolean isSorted) {
		List<FunctionCategoryBean> functionCategories = null;
		MessageUtil messageUtil = MessageUtil.getInstance(jasperReportsContext);
		List<FunctionsBundle> bundles = FunctionsUtil.getInstance(jasperReportsContext).getAllFunctionBundles();
		for (FunctionsBundle bundle : bundles) {
			List<Class<?>> classes = bundle.getFunctionClasses();
			if(classes != null && !classes.isEmpty()) {
				if(functionCategories == null) {
					functionCategories = new ArrayList<FunctionCategoryBean>();
				}
				for (Class<?> clazz : classes) {
					
					FunctionMessagesBundle functionMessagesBundle = clazz.getAnnotation(FunctionMessagesBundle.class);
					String messagesBundle = functionMessagesBundle != null 
						? functionMessagesBundle.value() 
						: FunctionsUtil.DEFAULT_MESSAGES_BUNDLE;	//FIXME: (sanda) set an appropriate value for this constant
							
					MessageProvider provider = messageUtil.getMessageProvider(messagesBundle);
					String categoryKey = null;
					FunctionCategoryBean categoryBean = null;
					FunctionCategories categories = clazz.getAnnotation(FunctionCategories.class);
					if(categories != null) {
						for(Class<?> category : categories.value()) {
							categoryKey = category.getName();
							categoryBean = getFunctionCategory(
													clazz, 
													categoryKey, 
													provider,
													messageUtil,
													messagesBundle,
													locale, 
													isSorted);
							if(categoryBean != null) {
								functionCategories.add(categoryBean);
							}
						}
					}
					FunctionCategory category = clazz.getAnnotation(FunctionCategory.class);
					if(category != null) {
						categoryKey = category.value();
						categoryBean = getFunctionCategory(
								clazz, 
								categoryKey, 
								provider,
								messageUtil,
								messagesBundle,
								locale, 
								isSorted);
						if(categoryBean != null) {
							functionCategories.add(categoryBean);
						}
					}
				}
			}
		}
		if(functionCategories != null && isSorted) {
			Collections.sort(functionCategories);
		}
		return new JRBeanCollectionDataSource(functionCategories);
	}
	
	private List <FunctionParameterBean> getFunctionParameters(
			Method method, 
			String functionKey,
			MessageProvider provider,
			Locale locale, 
			boolean isSorted) {
		if(method == null || functionKey == null) {
			return null;
		}
		List <FunctionParameterBean> parameters = null;
		FunctionParameters functionParameters = method.getAnnotation(FunctionParameters.class);
		if(functionParameters != null && functionParameters.value().length > 0) {
			parameters = new ArrayList<FunctionParameterBean>();
			FunctionParameter[] functionParams = functionParameters.value();
			for(FunctionParameter param : functionParams) {
				String parameterKey = functionKey + "." + param.value();
				FunctionParameterBean parameter = getFunctionParameter(parameterKey, provider, locale);
				if(parameter != null) {
					parameters.add(parameter);
				}
			}
		} 
		FunctionParameter functionParameter = method.getAnnotation(FunctionParameter.class);
		if(functionParameter != null) {
			if(parameters == null) {
				parameters = new ArrayList<FunctionParameterBean>();
			}
			String parameterKey = functionKey + "." +functionParameter.value();
			FunctionParameterBean parameter = getFunctionParameter(parameterKey, provider, locale);
			if(parameter != null) {
				parameters.add(parameter);
			}
		}
		if(parameters != null && isSorted) {
			Collections.sort(parameters);
		}
		return parameters;
	}
	
	private FunctionParameterBean getFunctionParameter(
										String parameterKey,
										MessageProvider provider,
										Locale locale) {
		if(parameterKey == null) {
			return null;
		}
		String name = getName(parameterKey, provider, locale);
		String description = getDescription(parameterKey, provider, locale);
		
		return new FunctionParameterBean(getId(parameterKey), name, description);
	} 

	private FunctionBean getFunction(
			Method method, 
			String prefix,
			MessageProvider provider,
			MessageUtil messageUtil,
			String messagesBundle,
			Locale locale, 
			boolean isSorted) {
		if(method == null) {
			return null;
		}
		Function function = method.getAnnotation(Function.class);
		if(function == null) {
			return null;
		}
		
		List <FunctionParameterBean> parameters = null;
		String functionKey = prefix + function.value();
		FunctionMessagesBundle bundle = method.getAnnotation(FunctionMessagesBundle.class);
		String functionMessagesBundle = bundle != null 
			? bundle.value() 
			: messagesBundle;
		MessageProvider methodProvider = messageUtil.getMessageProvider(functionMessagesBundle);
		String name = getName(functionKey, methodProvider, locale);
		String description = getDescription(functionKey, methodProvider, locale);
		
		parameters = getFunctionParameters(
				method, 
				functionKey,
				methodProvider,
				locale, 
				isSorted);
		return new FunctionBean(
				getId(functionKey),
				name,
				description,
				parameters,
				method.getReturnType());
	}
	
	private FunctionCategoryBean getFunctionCategory(
			Class<?> clazz, 
			String categoryKey, 
			MessageProvider provider,
			MessageUtil messageUtil,
			String messagesBundle,
			Locale locale, 
			boolean isSorted) {
		if(clazz == null || categoryKey == null) {
			return null;
		}
		String name = getName(categoryKey, provider, locale);
		String description = getDescription(categoryKey, provider, locale);

		String prefix = categoryKey.substring(0, categoryKey.lastIndexOf(".") + 1) + clazz.getSimpleName() + ".";
		List<FunctionBean> functions = null;
		
		Method[] methods = clazz.getMethods();
		if(methods != null && methods.length > 0) {
			functions = new ArrayList<FunctionBean>();
			for (Method method : methods) {
				FunctionBean function = getFunction(
											method, 
											prefix,
											provider,
											messageUtil,
											messagesBundle,
											locale, 
											isSorted);
				if(function != null) {
					functions.add(function);
				}
			}
		}
		if(functions != null && isSorted){
			Collections.sort(functions);
		}
		return new FunctionCategoryBean(
				getId(categoryKey),
				name,
				description,
				functions);
	}
	
	private String getId(String key) {
		return key == null 
				? null 
				: key.substring(key.lastIndexOf('.') + 1);
	}
	
	private String getName(String key, MessageProvider provider, Locale locale) {
		return provider ==  null || locale == null 
				? key 
				: provider.getMessage(key + "." + FunctionsUtil.NAME, null, locale);
	}
	
	private String getDescription(String key, MessageProvider provider, Locale locale) {
		return provider ==  null || locale == null 
			? key 
			: provider.getMessage(key + "." + FunctionsUtil.DESCRIPTION, null, locale);
	}

}
