package com.jaspersoft.jasperreports.bridge.xml;

import com.jaspersoft.jasperreports.bridge.BridgeItem;
import com.jaspersoft.jasperreports.bridge.BridgeItemProperty;
import com.jaspersoft.jasperreports.bridge.design.BridgeDesignItem;
import com.jaspersoft.jasperreports.bridge.design.BridgeDesignItemData;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.type.*;
import net.sf.jasperreports.engine.xml.*;
import org.apache.commons.digester.Digester;



public class BridgeDigester  implements XmlDigesterConfigurer {

    public void configureDigester(Digester digester)
    {
            addRules(digester);
    }


    public static void addRules(Digester digester)
    {
        String jrNamespace = JRXmlConstants.JASPERREPORTS_NAMESPACE;
        String namespace = digester.getRuleNamespaceURI();

        String mainComponentPattern = "*/componentElement/bridge";
        digester.addFactoryCreate(mainComponentPattern, BridgeXmlFactory.class.getName());
        
        addEvaluationPropertiesRules(digester, mainComponentPattern);
        
        
        addItemPropertieyRules(digester, mainComponentPattern + "/" + BridgeXmlFactory.ELEMENT_itemProperty, namespace);
            
        addBridgeItemDataRules(digester, mainComponentPattern + "/" + BridgeXmlFactory.ELEMENT_bridgeData, namespace);
          

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
                            new String[]{JRXmlConstants.ATTRIBUTE_evaluationTime, BridgeXmlFactory.ATTRIBUTE_onErrorType}, 
                            new String[0]);
            
            digester.addRule(pattern, 
                            new XmlConstantPropertyRule(
                                            JRXmlConstants.ATTRIBUTE_evaluationTime, "evaluationTimeValue",
                                            EvaluationTimeEnum.values()));
            
            digester.addRule(pattern, 
                            new XmlConstantPropertyRule(
                                            BridgeXmlFactory.ATTRIBUTE_onErrorType,
                                            OnErrorTypeEnum.values()));
    }
    
    
    protected static void addItemPropertieyRules(Digester digester, String itemPropertyPattern, String namespace)
    {
        digester.addFactoryCreate(itemPropertyPattern, BridgeItemPropertyXmlFactory.class);
        digester.addSetNext(itemPropertyPattern, "addItemProperty", BridgeItemProperty.class.getName());

        addExpressionRules(digester, itemPropertyPattern + "/" + JRXmlConstants.ELEMENT_valueExpression,"setValueExpression", true);
    }
    
    protected static void addBridgeItemDataRules(Digester digester, String pattern, String namespace)
    {
        digester.addObjectCreate(pattern, BridgeDesignItemData.class);
        digester.addSetNext(pattern, "addItemData", BridgeDesignItemData.class.getName());

        String itemPattern = pattern + "/item";
        
        digester.addObjectCreate( itemPattern, BridgeDesignItem.class);
	digester.addSetNext(itemPattern, "addItem", BridgeItem.class.getName());

        addItemPropertieyRules(digester, itemPattern + "/itemProperty", namespace);
        
        digester.setRuleNamespaceURI(JRXmlWriter.JASPERREPORTS_NAMESPACE.getNamespaceURI());
        
        digester.addFactoryCreate(pattern + "/dataset", BridgeItemDatasetFactory.class.getName());
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
