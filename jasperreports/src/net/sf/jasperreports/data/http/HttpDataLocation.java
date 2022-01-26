/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.data.http;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import net.sf.jasperreports.data.DataFile;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */

@JsonPropertyOrder({ 
	"method", 
	"url",
	"username",
	"password",
	"urlParameters",
	"body",
	"postParameters",
	"headers"})
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
@JsonSubTypes({
	@JsonSubTypes.Type(value = StandardHttpDataLocation.class)
})

public interface HttpDataLocation extends DataFile
{

	RequestMethod getMethod();
	
	String getUrl();
	
	String getUsername();
	
	String getPassword();
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("urlParameters")
	@JsonAlias("urlParameter")
	List<HttpLocationParameter> getUrlParameters();
	
	String getBody();

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("postParameters")
	@JsonAlias("postParameter")
	List<HttpLocationParameter> getPostParameters();
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("headers")
	@JsonAlias("header")
	List<HttpLocationParameter> getHeaders();

}
