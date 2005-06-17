package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;

import org.xml.sax.Attributes;

/**
 * @author Ionut Nedelcu
 */
public class JRDatasetFactory extends JRBaseFactory
{

    private static final String ATTRIBUTE_resetType = "resetType";
    private static final String ATTRIBUTE_resetGroup = "resetGroup";
    private static final String ATTRIBUTE_incrementType = "incrementType";
    private static final String ATTRIBUTE_incrementGroup = "incrementGroup";


    /**
     *
     */
    public Object createObject(Attributes atts)
    {
        JRDesignDataset dataset = (JRDesignDataset) digester.peek();

        Byte resetType = (Byte)JRXmlConstants.getResetTypeMap().get(atts.getValue(ATTRIBUTE_resetType));
        if (resetType != null)
        {
            dataset.setResetType(resetType.byteValue());
        }

        String groupName = atts.getValue(ATTRIBUTE_resetGroup);
        if (groupName != null)
        {
            JRDesignGroup group = new JRDesignGroup();
            group.setName(groupName);
            dataset.setResetGroup(group);
        }

        Byte incrementType = (Byte)JRXmlConstants.getResetTypeMap().get(atts.getValue(ATTRIBUTE_incrementType));
        if (incrementType != null)
        {
            dataset.setIncrementType(incrementType.byteValue());
        }

        groupName = atts.getValue(ATTRIBUTE_incrementGroup);
        if (groupName != null)
        {
            JRDesignGroup group = new JRDesignGroup();
            group.setName(groupName);
            dataset.setIncrementGroup(group);
        }

        return dataset;
    }

}
