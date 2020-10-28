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
package net.sf.jasperreports.customvisualization.fill;

import net.sf.jasperreports.customvisualization.CVComponent;
import net.sf.jasperreports.customvisualization.export.CVElementImageProvider;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement;
import net.sf.jasperreports.engine.fill.JRTemplateImage;
import net.sf.jasperreports.engine.fill.JRTemplatePrintImage;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.export.HtmlReportConfiguration;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.repo.RepositoryContext;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class CVFillImage extends CVFillComponent {

	private RepositoryContext repositoryContext;

    public CVFillImage(CVComponent component, JRFillObjectFactory factory) {
        super(component, factory);
        this.repositoryContext = factory.getFiller().getRepositoryContext();
    }

    @Override
    public JRPrintElement fill() {
        JRPrintImage printImage = createPrintImage();

        if (isEvaluateNow()) {
            try {
                setImageRenderer(printImage);
            } catch (JRException e) {
                throw new JRRuntimeException(e);
            }
        } else {
            fillContext.registerDelayedEvaluation(printImage,
                    getComponent().getEvaluationTime(),
                    getComponent().getEvaluationGroup());
        }

        return printImage;
    }

    @Override
    public void evaluateDelayedElement(JRPrintElement element, byte evaluation) throws JRException {
        evaluateComponent(evaluation);
        setImageRenderer((JRPrintImage) element);
    }

    protected JRPrintImage createPrintImage() {
        JRComponentElement componentElement = fillContext.getComponentElement();

        //FIXME cache per style
        JRTemplateImage template = new JRTemplateImage(
                fillContext.getElementOrigin(), fillContext.getDefaultStyleProvider());
        template.setElement(componentElement);

        template.setLazy(false);
        template.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
        template.setHorizontalImageAlign(HorizontalImageAlignEnum.LEFT);
        template.setVerticalImageAlign(VerticalImageAlignEnum.TOP);

        // resolve duplicates
        template = deduplicate(template);

        JRTemplatePrintImage printImage = new JRTemplatePrintImage(template, printElementOriginator);
        printImage.setX(componentElement.getX());
        printImage.setY(fillContext.getElementPrintY());
        printImage.setWidth(componentElement.getWidth());
        printImage.setHeight(componentElement.getHeight());
        printImage.setUUID(componentElement.getUUID());

        printImage.getPropertiesMap().setProperty(HtmlReportConfiguration.PROPERTY_EMBED_IMAGE, Boolean.TRUE.toString());
        printImage.getPropertiesMap().setProperty(HtmlReportConfiguration.PROPERTY_EMBEDDED_SVG_USE_FONTS, Boolean.TRUE.toString());

        return printImage;
    }

    protected void setImageRenderer(JRPrintImage image) throws JRException {
        JRTemplateGenericPrintElement genericPrintElement = createGenericPrintElement();
        evaluationPerformed(genericPrintElement);

        Renderable renderer = CVElementImageProvider.getInstance().createRenderable(genericPrintElement, this.repositoryContext);
        image.setRenderer(renderer);
    }
}
