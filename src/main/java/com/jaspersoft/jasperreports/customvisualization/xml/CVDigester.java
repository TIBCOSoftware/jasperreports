package com.jaspersoft.jasperreports.customvisualization.xml;

import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.xml.JRExpressionFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.jasperreports.engine.xml.XmlConstantPropertyRule;

import org.apache.commons.digester.Digester;

import com.jaspersoft.jasperreports.customvisualization.CVItem;
import com.jaspersoft.jasperreports.customvisualization.CVItemProperty;
import com.jaspersoft.jasperreports.customvisualization.design.CVDesignItem;
import com.jaspersoft.jasperreports.customvisualization.design.CVDesignItemData;



public class CVDigester  implements XmlDigesterConfigurer {

    public void configureDigester(Digester digester)
    {
            addRules(digester);
    }


    public static void addRules(Digester digester)
    {
        String jrNamespace = JRXmlConstants.JASPERREPORTS_NAMESPACE;
        String namespace = digester.getRuleNamespaceURI();

        String mainComponentPattern = "*/componentElement/customvisualization";
        digester.addFactoryCreate(mainComponentPattern, CVXmlFactory.class.getName());
        
        addEvaluationPropertiesRules(digester, mainComponentPattern);
        
        
        addItemPropertieyRules(digester, mainComponentPattern + "/" + CVXmlFactory.ELEMENT_itemProperty, namespace);
            
        addCVItemDataRules(digester, mainComponentPattern + "/" + CVXmlFactory.ELEMENT_cvData, namespace);
          

    }

    protected static void addExpressionRules(Digester digester, String expressionPattern, String setterMethod, boolean jrNamespace)
    {
            String originalNamespace = digester.getRuleNamespaceURI();
            if (jrNamespace)
            {
                    digester.setRuleNamespaceURI(JRXmlWriter.JASPERREPORTS_NAMESPACE.getNamespaceURI());
            }

            digester.addFactoryCreate(expressionPattern, JRExpressionFactory.class);
            digester.addCallMethod(expressionPattern, "setText", 0);
            digester.addSetNext(expressionPattern, setterMethod, JRExpression.class.getName());

            if (jrNamespace)
            {
                    digester.setRuleNamespaceURI(originalNamespace);
            }
    }
    
    protected static void addEvaluationPropertiesRules(Digester digester, String pattern)
    {
            digester.addSetProperties(pattern,
                            //properties to be ignored by this rule
                            new String[]{JRXmlConstants.ATTRIBUTE_evaluationTime, CVXmlFactory.ATTRIBUTE_onErrorType}, 
                            new String[0]);
            
            digester.addRule(pattern, 
                            new XmlConstantPropertyRule(
                                            JRXmlConstants.ATTRIBUTE_evaluationTime, "evaluationTimeValue",
                                            EvaluationTimeEnum.values()));
            
            digester.addRule(pattern, 
                            new XmlConstantPropertyRule(
                                            CVXmlFactory.ATTRIBUTE_onErrorType,
                                            OnErrorTypeEnum.values()));
    }
    
    
    protected static void addItemPropertieyRules(Digester digester, String itemPropertyPattern, String namespace)
    {
        digester.addFactoryCreate(itemPropertyPattern, CVItemPropertyXmlFactory.class);
        digester.addSetNext(itemPropertyPattern, "addItemProperty", CVItemProperty.class.getName());

        addExpressionRules(digester, itemPropertyPattern + "/" + JRXmlConstants.ELEMENT_valueExpression,"setValueExpression", true);
    }
    
    protected static void addCVItemDataRules(Digester digester, String pattern, String namespace)
    {
        digester.addObjectCreate(pattern, CVDesignItemData.class);
        digester.addSetNext(pattern, "addItemData", CVDesignItemData.class.getName());

        String itemPattern = pattern + "/item";
        
        digester.addObjectCreate( itemPattern, CVDesignItem.class);
	digester.addSetNext(itemPattern, "addItem", CVItem.class.getName());

        addItemPropertieyRules(digester, itemPattern + "/itemProperty", namespace);
        
        digester.setRuleNamespaceURI(JRXmlWriter.JASPERREPORTS_NAMESPACE.getNamespaceURI());
        
        digester.addFactoryCreate(pattern + "/dataset", CVItemDatasetFactory.class.getName());
        digester.addSetNext(pattern + "/dataset", "setDataset", JRElementDataset.class.getName());
        
        digester.setRuleNamespaceURI(namespace);
    }
    
    
    
/*

    protected static void addHyperlinkRules(Digester digester, String pattern)
    {
            addHyperlinkRules(digester, pattern, "setHyperlink");
    }

    protected static void addHyperlinkRules(Digester digester, String pattern, String methodName)
    {
            digester.addFactoryCreate(pattern, JRHyperlinkFactory.class);
            digester.addSetNext(pattern, methodName, JRHyperlink.class.getName());
    }

    
    protected static void addColorRangeRules(Digester digester, String pattern)
    {
            String colorRangePattern = pattern + "/colorRange";
            digester.addObjectCreate(colorRangePattern, DesignSVGMapColorRange.class);
            digester.addRule(colorRangePattern, new ColorPropertyRule("color"));
            digester.addSetNext(colorRangePattern, "addColorRange",
                            DesignSVGMapColorRange.class.getName());

            addExpressionRules(digester, colorRangePattern + "/minValueExpression", "setMinValueExpression", false);
            addExpressionRules(digester, colorRangePattern + "/maxValueExpression", "setMaxValueExpression", false);
            addExpressionRules(digester, colorRangePattern + "/labelExpression","setLabelExpression", false);
    }
*/
}
