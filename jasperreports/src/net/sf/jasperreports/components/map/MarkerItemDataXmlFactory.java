package net.sf.jasperreports.components.map;

import net.sf.jasperreports.engine.xml.JRBaseFactory;
import org.xml.sax.Attributes;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class MarkerItemDataXmlFactory extends JRBaseFactory  {
    @Override
    public Object createObject(Attributes attributes) throws Exception {
        return new MarkerStandardItemData();
    }
}
