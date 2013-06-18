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
package net.sf.jasperreports.components.headertoolbar.actions;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import net.sf.jasperreports.components.sort.FilterTypeDateOperatorsEnum;
import net.sf.jasperreports.components.sort.FilterTypesEnum;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.web.actions.ActionException;
import net.sf.jasperreports.web.commands.CommandException;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class ConditionalFormattingAction extends AbstractVerifiableTableAction {
	
	public ConditionalFormattingAction() {
	}

	public ConditionalFormattingData getConditionalFormattingData() {
		return (ConditionalFormattingData) columnData;
	}

	public void setConditionalFormattingData(ConditionalFormattingData conditionalFormattingData) {
		columnData = conditionalFormattingData;
	}

	public void performAction() throws ActionException {
		// execute command
		try {
			getCommandStack().execute(
				new ResetInCacheCommand(
					new ConditionalFormattingCommand(getJasperReportsContext(), table, getConditionalFormattingData()),
					getJasperReportsContext(),
					getReportContext(), 
					targetUri
					)
				);
		} catch (CommandException e) {
			throw new ActionException(e.getMessage());
		}
	}

	@Override
	public void verify() throws ActionException {
		ConditionalFormattingData cfd = getConditionalFormattingData();
		List<FormatCondition> conditions = cfd.getConditions();
		if (conditions.size() > 0) {
			FilterTypesEnum conditionType = FilterTypesEnum.getByName(cfd.getConditionType());
			FormatCondition condition;
			
			Locale locale = (Locale)getReportContext().getParameterValue(JRParameter.REPORT_LOCALE);
			if (locale == null) {
				locale = Locale.getDefault();
			}
			
			for (int i = 0, ln = conditions.size(); i < ln; i ++) {
				condition = conditions.get(i);
				if (FilterTypesEnum.DATE.equals(conditionType) || FilterTypesEnum.TIME.equals(conditionType)) {
					FilterTypeDateOperatorsEnum dateEnum = FilterTypeDateOperatorsEnum.getByEnumConstantName(condition.getConditionTypeOperator());
					boolean containsBetween = FilterTypeDateOperatorsEnum.IS_BETWEEN.equals(dateEnum) || FilterTypeDateOperatorsEnum.IS_NOT_BETWEEN.equals(dateEnum);

					try {
						DateFormat df = formatFactory.createDateFormat(cfd.getConditionPattern(), locale, null);
						df.setLenient(false);
					
						if (containsBetween) {
							if (condition.getConditionStart() == null || condition.getConditionStart().length() == 0) {
								errors.add("net.sf.jasperreports.components.headertoolbar.actions.conditionalformatting.empty.start.date", i+1);
							} else {
								try {
									df.parse(condition.getConditionStart());
								} catch (ParseException e) {
									errors.add("net.sf.jasperreports.components.headertoolbar.actions.conditionalformatting.invalid.start.date", i+1, condition.getConditionStart());
								}
							}

							if (condition.getConditionEnd() != null && condition.getConditionEnd().length() > 0) {
								try {
									df.parse(condition.getConditionEnd());
								} catch (ParseException e) {
									errors.add("net.sf.jasperreports.components.headertoolbar.actions.conditionalformatting.invalid.end.date", i+1, condition.getConditionEnd());
								}
							} else {
								errors.add("net.sf.jasperreports.components.headertoolbar.actions.conditionalformatting.empty.end.date", i+1);
							}
							
						} else {
							if (condition.getConditionStart() == null || condition.getConditionStart().length() == 0) {
								errors.add("net.sf.jasperreports.components.headertoolbar.actions.conditionalformatting.empty.date", i+1);
							} else {
								try {
									df.parse(condition.getConditionStart());
								} catch (ParseException e) {
									errors.add("net.sf.jasperreports.components.headertoolbar.actions.conditionalformatting.invalid.date", i+1, condition.getConditionStart());
								}
							}
						}
					} catch (IllegalArgumentException e) {
						errors.add("net.sf.jasperreports.components.headertoolbar.actions.conditionalformatting.invalid.pattern", i+1);
					}
					
				} else if (conditionType == FilterTypesEnum.NUMERIC) {
					if (condition.getConditionStart() == null || condition.getConditionStart().trim().length() == 0) {
						errors.add("net.sf.jasperreports.components.headertoolbar.actions.conditionalformatting.empty.number", i+1);
						continue;
					}
					try {
						NumberFormat nf = createNumberFormat(cfd.getConditionPattern(), locale);
						nf.parse(condition.getConditionStart());
						if (condition.getConditionEnd() != null && condition.getConditionEnd().length() > 0) {
							try {
								nf.parse(condition.getConditionEnd());
							} catch (ParseException e) {
								errors.add("net.sf.jasperreports.components.headertoolbar.actions.conditionalformatting.invalid.number", i+1, condition.getConditionEnd());
							}
						}
					} catch (ParseException e) {
						errors.add("net.sf.jasperreports.components.headertoolbar.actions.conditionalformatting.invalid.number", i+1, condition.getConditionStart());
					} catch (IllegalArgumentException e) {
						errors.add("net.sf.jasperreports.components.headertoolbar.actions.conditionalformatting.invalid.pattern", i+1);
					}
				}
			}
		}
	}

}
