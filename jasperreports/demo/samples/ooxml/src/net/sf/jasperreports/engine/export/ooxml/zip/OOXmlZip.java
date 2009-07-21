/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * JasperSoft Corporation
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.export.ooxml.zip;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.jasperreports.engine.export.ooxml.JROpenOfficeXmlExporterNature;


/**
 * Creates the internal open office xml structure of a document.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public abstract class OOXmlZip
{
	protected static final String UTF8_ENCODING = "UTF-8";
	/**
	 * 
	 */
	private List ooxmlZipEntries;
	private String xmlProlog;
	/**
	 * 
	 */
	private OOXmlZipEntry contentTypesEntry = createEntry("[Content_Types].xml");
	private OOXmlZipEntry relsEntry = createEntry("_rels/.rels");
//	private OOXmlZipEntry appEntry = createEntry("docProps/app.xml");
//	private OOXmlZipEntry coreEntry = createEntry("docProps/core.xml");

//	private OOXmlZipEntry mimetypeRelsEntry;
//	private OOXmlZipEntry themeEntry;
	private OOXmlZipEntry documentEntry;
//	private OOXmlZipEntry fontTableEntry;
//	private OOXmlZipEntry settingsEntry;
	private OOXmlZipEntry stylesEntry;
//	private OOXmlZipEntry webSettingsEntry;
//	private OOXmlZipEntry worksheetsRelsEntry;
//	private OOXmlZipEntry calcChainEntry;
//	private OOXmlZipEntry sharedStringsEntry;
//	private OOXmlZipEntry workbookEntry;
	
	
	/**
	 * Default constructor
	 * @throws IOException
	 */
	public OOXmlZip() throws IOException
	{
		this(JROpenOfficeXmlExporterNature.DOCX_NATURE, null, null);
	}

	/**
	 * @param ooxmlNature
	 * @throws IOException
	 */
	public OOXmlZip(byte ooxmlNature) throws IOException
	{
		this(ooxmlNature, null, null);
	}
	
	/**
	 * @param ooxmlNature
	 * @param sheetNames
	 * @throws IOException
	 */
	public OOXmlZip(byte ooxmlNature, String[] sheetNames) throws IOException
	{
		this(ooxmlNature, null, sheetNames);
	}
	
	/**
	 * @param ooxmlNature
	 * @param xmlProlog
	 * @throws IOException
	 */
	public OOXmlZip(byte ooxmlNature, String encoding) throws IOException
	{
		this(ooxmlNature, encoding, null);
	}
	
	/**
	 * @param ooxmlNature
	 * @param sheetNames
	 * @throws IOException
	 */
	public OOXmlZip(byte ooxmlNature, String encoding, String[] sheetNames) throws IOException
	{
		
		ooxmlZipEntries = new ArrayList();
		this.xmlProlog = encoding == null ? 
			"<?xml version=\"1.0\" encoding=\"" + UTF8_ENCODING + "\" standalone=\"yes\"?> \r\n" :
			"<?xml version=\"1.0\" encoding=\"" + encoding + "\" standalone=\"yes\"?> \r\n";
		String mimetype = null;
		
		switch(ooxmlNature)
		{
//			case JROpenOfficeXmlExporterNature.XLSX_NATURE:
//				mimetype = "xl";
//				mimetypeRelsEntry = createEntry(mimetype + "/_rels/workbook.xml.rels");
//				ooxmlZipEntries.add(mimetypeRelsEntry);
//				calcChainEntry = createEntry(mimetype + "/calcChain.xml");
//				ooxmlZipEntries.add(calcChainEntry);
//				sharedStringsEntry = createEntry(mimetype + "/sharedStrings.xml");
//				ooxmlZipEntries.add(sharedStringsEntry);
//				workbookEntry = createEntry(mimetype + "/workbook.xml");
//				ooxmlZipEntries.add(workbookEntry);
//				break;
			case JROpenOfficeXmlExporterNature.DOCX_NATURE:
			default:
				mimetype = "word";
//				createMimetypeRelsEntry(mimetype);
				documentEntry = createEntry(mimetype + "/document.xml");
				ooxmlZipEntries.add(documentEntry);
//				fontTableEntry = createEntry(mimetype + "/fontTable.xml");
//				createFontTableEntry();
//				ooxmlZipEntries.add(fontTableEntry);
//				createSettingsEntry(mimetype);
//				createWebSettingsEntry(mimetype);
		}
		
		createContentTypesEntry(mimetype);
		createRelsEntry(mimetype);
		createWordRelsEntry(mimetype);
//		createAppEntry(mimetype);
//		createCoreEntry(mimetype);
//		createThemeEntry(mimetype);
//		
		stylesEntry = createEntry(mimetype + "/styles.xml");
		ooxmlZipEntries.add(stylesEntry);
	}
	
	/**
	 *
	 */
	public abstract OOXmlZipEntry createEntry(String name);
	
	/**
	 *
	 */
	public void addEntry(OOXmlZipEntry entry)
	{
		ooxmlZipEntries.add(entry);
	}
	
	/**
	 *
	 */
	public void zipEntries(OutputStream os) throws IOException
	{
		ZipOutputStream zipos = new ZipOutputStream(os);
		zipos.setMethod(ZipOutputStream.DEFLATED);
		
		for (int i = 0; i < ooxmlZipEntries.size(); i++) 
		{
			OOXmlZipEntry ooxmlZipEntry = (OOXmlZipEntry)ooxmlZipEntries.get(i);
			ZipEntry zipEntry = new ZipEntry(ooxmlZipEntry.getName());
			zipos.putNextEntry(zipEntry);
			ooxmlZipEntry.writeData(zipos);
		}
		
		zipos.flush();
		zipos.finish();
	}
	
	/**
	 *
	 */
	public void dispose()
	{
		for (int i = 0; i < ooxmlZipEntries.size(); i++) 
		{
			OOXmlZipEntry oOXmlZipEntry = (OOXmlZipEntry)ooxmlZipEntries.get(i);
			oOXmlZipEntry.dispose();
		}
	}

	/**
	 * @return the stylesEntry
	 */
	public OOXmlZipEntry getStylesEntry()
	{
		return stylesEntry;
	}
	
	/**
     * @return the contentTypesEntry
     */
    public OOXmlZipEntry getContentTypesEntry()
    {
    	return contentTypesEntry;
    }
	/**
     * @return the relsEntry
     */
    public OOXmlZipEntry getRelsEntry()
    {
    	return relsEntry;
    }
//	/**
//     * @return the appEntry
//     */
//    public OOXmlZipEntry getAppEntry()
//    {
//    	return appEntry;
//    }
//	/**
//     * @return the coreEntry
//     */
//    public OOXmlZipEntry getCoreEntry()
//    {
//    	return coreEntry;
//    }
//	/**
//     * @return the mimetypeRelsEntry
//     */
//    public OOXmlZipEntry getMimetypeRelsEntry()
//    {
//    	return mimetypeRelsEntry;
//    }
//	/**
//     * @return the themeEntry
//     */
//    public OOXmlZipEntry getThemeEntry()
//    {
//    	return themeEntry;
//    }
	/**
     * @return the documentEntry
     */
    public OOXmlZipEntry getDocumentEntry()
    {
    	return documentEntry;
    }
//	/**
//     * @return the fontTableEntry
//     */
//    public OOXmlZipEntry getFontTableEntry()
//    {
//    	return fontTableEntry;
//    }
//	/**
//     * @return the settingsEntry
//     */
//    public OOXmlZipEntry getSettingsEntry()
//    {
//    	return settingsEntry;
//    }
//	/**
//     * @return the webSettingsEntry
//     */
//    public OOXmlZipEntry getWebSettingsEntry()
//    {
//    	return webSettingsEntry;
//    }
//	/**
//     * @return the worksheetsRelsEntry
//     */
//    public OOXmlZipEntry getWorksheetsRelsEntry()
//    {
//    	return worksheetsRelsEntry;
//    }
//	/**
//     * @return the calcChainEntry
//     */
//    public OOXmlZipEntry getCalcChainEntry()
//    {
//    	return calcChainEntry;
//    }
//	/**
//     * @return the sharedStringsEntry
//     */
//    public OOXmlZipEntry getSharedStringsEntry()
//    {
//    	return sharedStringsEntry;
//    }
//	/**
//     * @return the workbookEntry
//     */
//    public OOXmlZipEntry getWorkbookEntry()
//    {
//    	return workbookEntry;
//    }

//    private void createThemeEntry(String mimetype) throws IOException
//    {
//		Writer writer = null;
//		themeEntry = createEntry(mimetype + "/theme/theme1.xml");
//		try
//		{
//			writer = themeEntry.getWriter();
//			writer.write(xmlProlog);
//			writer.write("<a:theme xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" name=\"Office Theme\"> \r\n");
//			writer.write(" <a:themeElements> \r\n");
//			writer.write("  <a:clrScheme name=\"Office\"> \r\n");
//			writer.write("   <a:dk1> \r\n");
//			writer.write("    <a:sysClr val=\"windowText\" lastClr=\"000000\"/> \r\n");
//			writer.write("   </a:dk1> \r\n");
//			writer.write("   <a:lt1> \r\n");
//			writer.write("    <a:sysClr val=\"window\" lastClr=\"FFFFFF\"/> \r\n");
//			writer.write("   </a:lt1> \r\n");
//			writer.write("   <a:dk2> \r\n");
//			writer.write("    <a:srgbClr val=\"1F497D\"/> \r\n");
//			writer.write("   </a:dk2> \r\n");
//			writer.write("   <a:lt2> \r\n");
//			writer.write("    <a:srgbClr val=\"EEECE1\"/> \r\n");
//			writer.write("   </a:lt2> \r\n");
//			writer.write("   <a:accent1> \r\n");
//			writer.write("    <a:srgbClr val=\"4F81BD\"/> \r\n");
//			writer.write("   </a:accent1> \r\n");
//			writer.write("   <a:accent2> \r\n");
//			writer.write("    <a:srgbClr val=\"C0504D\"/> \r\n");
//			writer.write("   </a:accent2> \r\n");
//			writer.write("   <a:accent3> \r\n");
//			writer.write("    <a:srgbClr val=\"9BBB59\"/> \r\n");
//			writer.write("   </a:accent3> \r\n");
//			writer.write("   <a:accent4> \r\n");
//			writer.write("    <a:srgbClr val=\"8064A2\"/> \r\n");
//			writer.write("   </a:accent4> \r\n");
//			writer.write("   <a:accent5> \r\n");
//			writer.write("    <a:srgbClr val=\"4BACC6\"/> \r\n");
//			writer.write("   </a:accent5> \r\n");
//			writer.write("   <a:accent6> \r\n");
//			writer.write("    <a:srgbClr val=\"F79646\"/> \r\n");
//			writer.write("   </a:accent6> \r\n");
//			writer.write("   <a:hlink> \r\n");
//			writer.write("    <a:srgbClr val=\"0000FF\"/> \r\n");
//			writer.write("   </a:hlink> \r\n");
//			writer.write("   <a:folHlink> \r\n");
//			writer.write("    <a:srgbClr val=\"800080\"/> \r\n");
//			writer.write("   </a:folHlink> \r\n");
//			writer.write("  </a:clrScheme> \r\n");
//			writer.write("  <a:fontScheme name=\"Office\"> \r\n");
//			writer.write("   <a:majorFont> \r\n");
//			writer.write("    <a:latin typeface=\"Cambria\"/> \r\n");
//			writer.write("    <a:ea typeface=\"\"/> \r\n");
//			writer.write("    <a:cs typeface=\"\"/> \r\n");
//			//TODO: see that chinese character codes used below are the proper ones
//			writer.write("    <a:font script=\"Jpan\" typeface=\"\u00ef\u00bc\u00ad\u00ef\u00bc\u00b3 \u00e3\u201a\u00b4\u00e3\u201a\u00b7\u00e3\u0192\u0192\u00e3\u201a\u00af\"/> \r\n");
//			writer.write("    <a:font script=\"Hang\" typeface=\"\u00eb\u00a7\u2018\u00ec\ufffd\u20ac \u00ea\u00b3\u00a0\u00eb\u201d\u2022\"/> \r\n");
//			writer.write("    <a:font script=\"Hans\" typeface=\"\u00e5\u00ae\u2039\u00e4\u00bd\u201c\"/> \r\n");
//			writer.write("    <a:font script=\"Hant\" typeface=\"\u00e6\u2013\u00b0\u00e7\u00b4\u00b0\u00e6\u02dc\u017d\u00e9\u00ab\u201d\"/> \r\n");
//			writer.write("    <a:font script=\"Arab\" typeface=\"Times New Roman\"/> \r\n");
//			writer.write("    <a:font script=\"Hebr\" typeface=\"Times New Roman\"/> \r\n");
//			writer.write("    <a:font script=\"Thai\" typeface=\"Angsana New\"/> \r\n");
//			writer.write("    <a:font script=\"Ethi\" typeface=\"Nyala\"/> \r\n");
//			writer.write("    <a:font script=\"Beng\" typeface=\"Vrinda\"/> \r\n");
//			writer.write("    <a:font script=\"Gujr\" typeface=\"Shruti\"/> \r\n");
//			writer.write("    <a:font script=\"Khmr\" typeface=\"MoolBoran\"/> \r\n");
//			writer.write("    <a:font script=\"Knda\" typeface=\"Tunga\"/> \r\n");
//			writer.write("    <a:font script=\"Guru\" typeface=\"Raavi\"/> \r\n");
//			writer.write("    <a:font script=\"Cans\" typeface=\"Euphemia\"/> \r\n");
//			writer.write("    <a:font script=\"Cher\" typeface=\"Plantagenet Cherokee\"/> \r\n");
//			writer.write("    <a:font script=\"Yiii\" typeface=\"Microsoft Yi Baiti\"/> \r\n");
//			writer.write("    <a:font script=\"Tibt\" typeface=\"Microsoft Himalaya\"/> \r\n");
//			writer.write("    <a:font script=\"Thaa\" typeface=\"MV Boli\"/> \r\n");
//			writer.write("    <a:font script=\"Deva\" typeface=\"Mangal\"/> \r\n");
//			writer.write("    <a:font script=\"Telu\" typeface=\"Gautami\"/> \r\n");
//			writer.write("    <a:font script=\"Taml\" typeface=\"Latha\"/> \r\n");
//			writer.write("    <a:font script=\"Syrc\" typeface=\"Estrangelo Edessa\"/> \r\n");
//			writer.write("    <a:font script=\"Orya\" typeface=\"Kalinga\"/> \r\n");
//			writer.write("    <a:font script=\"Mlym\" typeface=\"Kartika\"/> \r\n");
//			writer.write("    <a:font script=\"Laoo\" typeface=\"DokChampa\"/> \r\n");
//			writer.write("    <a:font script=\"Sinh\" typeface=\"Iskoola Pota\"/> \r\n");
//			writer.write("    <a:font script=\"Mong\" typeface=\"Mongolian Baiti\"/> \r\n");
//			writer.write("    <a:font script=\"Viet\" typeface=\"Times New Roman\"/> \r\n");
//			writer.write("    <a:font script=\"Uigh\" typeface=\"Microsoft Uighur\"/> \r\n");
//			writer.write("   </a:majorFont> \r\n");
//			writer.write("   <a:minorFont> \r\n");
//			writer.write("    <a:latin typeface=\"Calibri\"/> \r\n");
//			writer.write("    <a:ea typeface=\"\"/> \r\n");
//			writer.write("    <a:cs typeface=\"\"/> \r\n");
//			//TODO: see that chinese character codes used below are the proper ones
//			writer.write("    <a:font script=\"Jpan\" typeface=\"\u00ef\u00bc\u00ad\u00ef\u00bc\u00b3 \u00e6\u02dc\u017d\u00e6\u0153\ufffd\"/> \r\n");
//			writer.write("    <a:font script=\"Hang\" typeface=\"\u00eb\u00a7\u2018\u00ec\ufffd\u20ac \u00ea\u00b3\u00a0\u00eb\u201d\u2022\"/> \r\n");
//			writer.write("    <a:font script=\"Hans\" typeface=\"\u00e5\u00ae\u2039\u00e4\u00bd\u201c\"/> \r\n");
//			writer.write("    <a:font script=\"Hant\" typeface=\"\u00e6\u2013\u00b0\u00e7\u00b4\u00b0\u00e6\u02dc\u017d\u00e9\u00ab\u201d\"/> \r\n");
//			writer.write("    <a:font script=\"Arab\" typeface=\"Arial\"/> \r\n");
//			writer.write("    <a:font script=\"Hebr\" typeface=\"Arial\"/> \r\n");
//			writer.write("    <a:font script=\"Thai\" typeface=\"Cordia New\"/> \r\n");
//			writer.write("    <a:font script=\"Ethi\" typeface=\"Nyala\"/> \r\n");
//			writer.write("    <a:font script=\"Beng\" typeface=\"Vrinda\"/> \r\n");
//			writer.write("    <a:font script=\"Gujr\" typeface=\"Shruti\"/> \r\n");
//			writer.write("    <a:font script=\"Khmr\" typeface=\"DaunPenh\"/> \r\n");
//			writer.write("    <a:font script=\"Knda\" typeface=\"Tunga\"/> \r\n");
//			writer.write("    <a:font script=\"Guru\" typeface=\"Raavi\"/> \r\n");
//			writer.write("    <a:font script=\"Cans\" typeface=\"Euphemia\"/> \r\n");
//			writer.write("    <a:font script=\"Cher\" typeface=\"Plantagenet Cherokee\"/> \r\n");
//			writer.write("    <a:font script=\"Yiii\" typeface=\"Microsoft Yi Baiti\"/> \r\n");
//			writer.write("    <a:font script=\"Tibt\" typeface=\"Microsoft Himalaya\"/> \r\n");
//			writer.write("    <a:font script=\"Thaa\" typeface=\"MV Boli\"/> \r\n");
//			writer.write("    <a:font script=\"Deva\" typeface=\"Mangal\"/> \r\n");
//			writer.write("    <a:font script=\"Telu\" typeface=\"Gautami\"/> \r\n");
//			writer.write("    <a:font script=\"Taml\" typeface=\"Latha\"/> \r\n");
//			writer.write("    <a:font script=\"Syrc\" typeface=\"Estrangelo Edessa\"/> \r\n");
//			writer.write("    <a:font script=\"Orya\" typeface=\"Kalinga\"/> \r\n");
//			writer.write("    <a:font script=\"Mlym\" typeface=\"Kartika\"/> \r\n");
//			writer.write("    <a:font script=\"Laoo\" typeface=\"DokChampa\"/> \r\n");
//			writer.write("    <a:font script=\"Sinh\" typeface=\"Iskoola Pota\"/> \r\n");
//			writer.write("    <a:font script=\"Mong\" typeface=\"Mongolian Baiti\"/> \r\n");
//			writer.write("    <a:font script=\"Viet\" typeface=\"Arial\"/> \r\n");
//			writer.write("    <a:font script=\"Uigh\" typeface=\"Microsoft Uighur\"/> \r\n");
//			writer.write("   </a:minorFont> \r\n");
//			writer.write("  </a:fontScheme> \r\n");
//			writer.write("  <a:fmtScheme name=\"Office\"> \r\n");
//			writer.write("   <a:fillStyleLst> \r\n");
//			writer.write("    <a:solidFill> \r\n");
//			writer.write("     <a:schemeClr val=\"phClr\"/> \r\n");
//			writer.write("    </a:solidFill> \r\n");
//			writer.write("    <a:gradFill rotWithShape=\"1\"> \r\n");
//			writer.write("     <a:gsLst> \r\n");
//			writer.write("      <a:gs pos=\"0\"> \r\n");
//			writer.write("       <a:schemeClr val=\"phClr\"> \r\n");
//			writer.write("        <a:tint val=\"50000\"/> \r\n");
//			writer.write("        <a:satMod val=\"300000\"/> \r\n");
//			writer.write("       </a:schemeClr> \r\n");
//			writer.write("      </a:gs> \r\n");
//			writer.write("      <a:gs pos=\"35000\"> \r\n");
//			writer.write("       <a:schemeClr val=\"phClr\"> \r\n");
//			writer.write("        <a:tint val=\"37000\"/> \r\n");
//			writer.write("        <a:satMod val=\"300000\"/> \r\n");
//			writer.write("       </a:schemeClr> \r\n");
//			writer.write("      </a:gs> \r\n");
//			writer.write("      <a:gs pos=\"100000\"> \r\n");
//			writer.write("       <a:schemeClr val=\"phClr\"> \r\n");
//			writer.write("        <a:tint val=\"15000\"/> \r\n");
//			writer.write("        <a:satMod val=\"350000\"/> \r\n");
//			writer.write("       </a:schemeClr> \r\n");
//			writer.write("      </a:gs> \r\n");
//			writer.write("     </a:gsLst> \r\n");
//			writer.write("     <a:lin ang=\"16200000\" scaled=\"1\"/> \r\n");
//			writer.write("    </a:gradFill> \r\n");
//			writer.write("    <a:gradFill rotWithShape=\"1\"> \r\n");
//			writer.write("     <a:gsLst> \r\n");
//			writer.write("      <a:gs pos=\"0\"> \r\n");
//			writer.write("       <a:schemeClr val=\"phClr\"> \r\n");
//			writer.write("        <a:shade val=\"51000\"/> \r\n");
//			writer.write("        <a:satMod val=\"130000\"/> \r\n");
//			writer.write("       </a:schemeClr> \r\n");
//			writer.write("      </a:gs> \r\n");
//			writer.write("      <a:gs pos=\"80000\"> \r\n");
//			writer.write("       <a:schemeClr val=\"phClr\"> \r\n");
//			writer.write("        <a:shade val=\"93000\"/> \r\n");
//			writer.write("        <a:satMod val=\"130000\"/> \r\n");
//			writer.write("       </a:schemeClr> \r\n");
//			writer.write("      </a:gs> \r\n");
//			writer.write("      <a:gs pos=\"100000\"> \r\n");
//			writer.write("       <a:schemeClr val=\"phClr\"> \r\n");
//			writer.write("        <a:shade val=\"94000\"/> \r\n");
//			writer.write("        <a:satMod val=\"135000\"/> \r\n");
//			writer.write("       </a:schemeClr> \r\n");
//			writer.write("      </a:gs> \r\n");
//			writer.write("     </a:gsLst> \r\n");
//			writer.write("     <a:lin ang=\"16200000\" scaled=\"0\"/> \r\n");
//			writer.write("    </a:gradFill> \r\n");
//			writer.write("   </a:fillStyleLst> \r\n");
//			writer.write("   <a:lnStyleLst> \r\n");
//			writer.write("    <a:ln w=\"9525\" cap=\"flat\" cmpd=\"sng\" algn=\"ctr\"> \r\n");
//			writer.write("     <a:solidFill> \r\n");
//			writer.write("      <a:schemeClr val=\"phClr\"> \r\n");
//			writer.write("       <a:shade val=\"95000\"/> \r\n");
//			writer.write("       <a:satMod val=\"105000\"/> \r\n");
//			writer.write("      </a:schemeClr> \r\n");
//			writer.write("     </a:solidFill> \r\n");
//			writer.write("     <a:prstDash val=\"solid\"/> \r\n");
//			writer.write("    </a:ln> \r\n");
//			writer.write("    <a:ln w=\"25400\" cap=\"flat\" cmpd=\"sng\" algn=\"ctr\"> \r\n");
//			writer.write("     <a:solidFill> \r\n");
//			writer.write("      <a:schemeClr val=\"phClr\"/> \r\n");
//			writer.write("     </a:solidFill> \r\n");
//			writer.write("     <a:prstDash val=\"solid\"/> \r\n");
//			writer.write("    </a:ln> \r\n");
//			writer.write("    <a:ln w=\"38100\" cap=\"flat\" cmpd=\"sng\" algn=\"ctr\"> \r\n");
//			writer.write("     <a:solidFill> \r\n");
//			writer.write("      <a:schemeClr val=\"phClr\"/> \r\n");
//			writer.write("     </a:solidFill> \r\n");
//			writer.write("     <a:prstDash val=\"solid\"/> \r\n");
//			writer.write("    </a:ln> \r\n");
//			writer.write("   </a:lnStyleLst> \r\n");
//			writer.write("   <a:effectStyleLst> \r\n");
//			writer.write("    <a:effectStyle> \r\n");
//			writer.write("     <a:effectLst> \r\n");
//			writer.write("      <a:outerShdw blurRad=\"40000\" dist=\"20000\" dir=\"5400000\" rotWithShape=\"0\"> \r\n");
//			writer.write("       <a:srgbClr val=\"000000\"> \r\n");
//			writer.write("        <a:alpha val=\"38000\"/> \r\n");
//			writer.write("       </a:srgbClr> \r\n");
//			writer.write("      </a:outerShdw> \r\n");
//			writer.write("     </a:effectLst> \r\n");
//			writer.write("    </a:effectStyle> \r\n");
//			writer.write("    <a:effectStyle> \r\n");
//			writer.write("     <a:effectLst> \r\n");
//			writer.write("      <a:outerShdw blurRad=\"40000\" dist=\"23000\" dir=\"5400000\" rotWithShape=\"0\"> \r\n");
//			writer.write("       <a:srgbClr val=\"000000\"> \r\n");
//			writer.write("        <a:alpha val=\"35000\"/> \r\n");
//			writer.write("       </a:srgbClr> \r\n");
//			writer.write("      </a:outerShdw> \r\n");
//			writer.write("     </a:effectLst> \r\n");
//			writer.write("    </a:effectStyle> \r\n");
//			writer.write("    <a:effectStyle> \r\n");
//			writer.write("     <a:effectLst> \r\n");
//			writer.write("      <a:outerShdw blurRad=\"40000\" dist=\"23000\" dir=\"5400000\" rotWithShape=\"0\"> \r\n");
//			writer.write("       <a:srgbClr val=\"000000\"> \r\n");
//			writer.write("        <a:alpha val=\"35000\"/> \r\n");
//			writer.write("       </a:srgbClr> \r\n");
//			writer.write("      </a:outerShdw> \r\n");
//			writer.write("     </a:effectLst> \r\n");
//			writer.write("     <a:scene3d> \r\n");
//			writer.write("      <a:camera prst=\"orthographicFront\"> \r\n");
//			writer.write("       <a:rot lat=\"0\" lon=\"0\" rev=\"0\"/> \r\n");
//			writer.write("      </a:camera> \r\n");
//			writer.write("      <a:lightRig rig=\"threePt\" dir=\"t\"> \r\n");
//			writer.write("       <a:rot lat=\"0\" lon=\"0\" rev=\"1200000\"/> \r\n");
//			writer.write("      </a:lightRig> \r\n");
//			writer.write("     </a:scene3d> \r\n");
//			writer.write("     <a:sp3d> \r\n");
//			writer.write("      <a:bevelT w=\"63500\" h=\"25400\"/> \r\n");
//			writer.write("     </a:sp3d> \r\n");
//			writer.write("    </a:effectStyle> \r\n");
//			writer.write("   </a:effectStyleLst> \r\n");
//			writer.write("   <a:bgFillStyleLst> \r\n");
//			writer.write("    <a:solidFill> \r\n");
//			writer.write("     <a:schemeClr val=\"phClr\"/> \r\n");
//			writer.write("    </a:solidFill> \r\n");
//			writer.write("    <a:gradFill rotWithShape=\"1\"> \r\n");
//			writer.write("     <a:gsLst> \r\n");
//			writer.write("      <a:gs pos=\"0\"> \r\n");
//			writer.write("       <a:schemeClr val=\"phClr\"> \r\n");
//			writer.write("        <a:tint val=\"40000\"/> \r\n");
//			writer.write("        <a:satMod val=\"350000\"/> \r\n");
//			writer.write("       </a:schemeClr> \r\n");
//			writer.write("      </a:gs> \r\n");
//			writer.write("      <a:gs pos=\"40000\"> \r\n");
//			writer.write("       <a:schemeClr val=\"phClr\"> \r\n");
//			writer.write("        <a:tint val=\"45000\"/> \r\n");
//			writer.write("        <a:shade val=\"99000\"/> \r\n");
//			writer.write("        <a:satMod val=\"350000\"/> \r\n");
//			writer.write("       </a:schemeClr> \r\n");
//			writer.write("      </a:gs> \r\n");
//			writer.write("      <a:gs pos=\"100000\"> \r\n");
//			writer.write("       <a:schemeClr val=\"phClr\"> \r\n");
//			writer.write("        <a:shade val=\"20000\"/> \r\n");
//			writer.write("        <a:satMod val=\"255000\"/> \r\n");
//			writer.write("       </a:schemeClr> \r\n");
//			writer.write("      </a:gs> \r\n");
//			writer.write("     </a:gsLst> \r\n");
//			writer.write("     <a:path path=\"circle\"> \r\n");
//			writer.write("      <a:fillToRect l=\"50000\" t=\"-80000\" r=\"50000\" b=\"180000\"/> \r\n");
//			writer.write("     </a:path> \r\n");
//			writer.write("    </a:gradFill> \r\n");
//			writer.write("    <a:gradFill rotWithShape=\"1\"> \r\n");
//			writer.write("     <a:gsLst> \r\n");
//			writer.write("      <a:gs pos=\"0\"> \r\n");
//			writer.write("       <a:schemeClr val=\"phClr\"> \r\n");
//			writer.write("        <a:tint val=\"80000\"/> \r\n");
//			writer.write("        <a:satMod val=\"300000\"/> \r\n");
//			writer.write("       </a:schemeClr> \r\n");
//			writer.write("      </a:gs> \r\n");
//			writer.write("      <a:gs pos=\"100000\"> \r\n");
//			writer.write("       <a:schemeClr val=\"phClr\"> \r\n");
//			writer.write("        <a:shade val=\"30000\"/> \r\n");
//			writer.write("        <a:satMod val=\"200000\"/> \r\n");
//			writer.write("       </a:schemeClr> \r\n");
//			writer.write("      </a:gs> \r\n");
//			writer.write("     </a:gsLst> \r\n");
//			writer.write("     <a:path path=\"circle\"> \r\n");
//			writer.write("      <a:fillToRect l=\"50000\" t=\"50000\" r=\"50000\" b=\"50000\"/> \r\n");
//			writer.write("     </a:path> \r\n");
//			writer.write("    </a:gradFill> \r\n");
//			writer.write("   </a:bgFillStyleLst> \r\n");
//			writer.write("  </a:fmtScheme> \r\n");
//			writer.write(" </a:themeElements> \r\n");
//			writer.write(" <a:objectDefaults/> \r\n");
//			writer.write(" <a:extraClrSchemeLst/> \r\n");
//			writer.write("</a:theme> \r\n");
//			
//			writer.flush();
//			writer.close();
//			ooxmlZipEntries.add(themeEntry);
//		}
//		finally
//		{
//			if (writer != null)
//			{
//				try
//				{
//					writer.close();
//				}
//				catch (IOException e)
//				{
//				}
//			}
//		}
//    }

    private void createContentTypesEntry(String mimetype) throws IOException
    {
		Writer writer = null;
		contentTypesEntry = createEntry("[Content_Types].xml");
		try
		{
			writer = contentTypesEntry.getWriter();
			writer.write(xmlProlog);
			writer.write("<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\"> \r\n");
			writer.write(" <Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/> \r\n");
			writer.write(" <Default Extension=\"xml\" ContentType=\"application/xml\"/> \r\n");
//			if(mimetype.equals("xl"))
//			{
//				writer.write(" <Default Extension=\"bin\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.printerSettings\" /> \r\n");
//			}
//			writer.write(" <Override PartName=\"/docProps/app.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.extended-properties+xml\" /> \r\n");
//			writer.write(" <Override PartName=\"/docProps/core.xml\" ContentType=\"application/vnd.openxmlformats-package.core-properties+xml\"/> \r\n");
//			writer.write(" <Override PartName=\"/" + mimetype + "/theme/theme1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.theme+xml\"/> \r\n");
//			if(mimetype.equals("xl"))
//			{
//				writer.write(" <Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\" /> \r\n");
//				writer.write(" <Override PartName=\"/xl/workbook.xml\"	ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\" /> \r\n");
//				writer.write(" <Override PartName=\"/xl/worksheets/sheet1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\" /> \r\n");
//				writer.write(" <Override PartName=\"/xl/calcChain.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.calcChain+xml\" /> \r\n");
//				writer.write(" <Override PartName=\"/xl/sharedStrings.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml\" /> \r\n");
//			}
//			else
//			{
				writer.write(" <Override PartName=\"/word/document.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml\"/> \r\n");
				writer.write(" <Override PartName=\"/word/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml\"/> \r\n");
//				writer.write(" <Override PartName=\"/word/fontTable.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.fontTable+xml\"/> \r\n");
//				writer.write(" <Override PartName=\"/word/webSettings.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.webSettings+xml\"/> \r\n");
//			}
			
			writer.write("</Types> \r\n");
			writer.flush();
			writer.close();
			ooxmlZipEntries.add(contentTypesEntry);
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch (IOException e)
				{
				}
			}
		}
    }

    private void createRelsEntry(String mimetype) throws IOException
    {
		Writer writer = null;
		relsEntry = createEntry("_rels/.rels");
		String target = mimetype.equals("xl") ? "xl/workbook" : "word/document";
		
		try
		{
			writer = relsEntry.getWriter();
			writer.write(xmlProlog);
			writer.write("<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\"> \r\n");
//			writer.write(" <Relationship Id=\"rId3\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties\" Target=\"docProps/app.xml\"/> \r\n");
//			writer.write(" <Relationship Id=\"rId2\" Type=\"http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties\" Target=\"docProps/core.xml\"/> \r\n");
			writer.write(" <Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"" + target + ".xml\"/> \r\n");
			writer.write("</Relationships> \r\n");
			
			writer.flush();
			writer.close();
			ooxmlZipEntries.add(relsEntry);
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch (IOException e)
				{
				}
			}
		}
    }

    private void createWordRelsEntry(String mimetype) throws IOException
    {
		Writer writer = null;
		relsEntry = createEntry("word/_rels/document.xml.rels");
		
		try
		{
			writer = relsEntry.getWriter();
			writer.write(xmlProlog);
			writer.write("<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\"> \r\n");
			writer.write(" <Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/> \r\n");
			writer.write("</Relationships> \r\n");
			
			writer.flush();
			writer.close();
			ooxmlZipEntries.add(relsEntry);
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch (IOException e)
				{
				}
			}
		}
    }

//    private void createCoreEntry(String mimetype) throws IOException
//    {
//		Writer writer = null;
//		coreEntry = createEntry("docProps/core.xml");
//		
//		try
//		{
//			writer = coreEntry.getWriter();
//			writer.write(xmlProlog);
//			writer.write("<cp:coreProperties \r\n");
//			writer.write(" xmlns:cp=\"http://schemas.openxmlformats.org/package/2006/metadata/core-properties\" \r\n");
//			writer.write(" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" \r\n");
//			writer.write(" xmlns:dcterms=\"http://purl.org/dc/terms/\" \r\n");
//			writer.write(" xmlns:dcmitype=\"http://purl.org/dc/dcmitype/\" \r\n");
//			writer.write(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> \r\n");
//			writer.write(" <dc:title></dc:title> \r\n");
//			writer.write(" <dc:subject></dc:subject> \r\n");
//			writer.write(" <dc:creator></dc:creator> \r\n");
//			writer.write(" <cp:keywords></cp:keywords> \r\n");
//			writer.write(" <dc:description></dc:description> \r\n");
//			writer.write(" <cp:lastModifiedBy></cp:lastModifiedBy> \r\n");
//			writer.write(" <cp:revision>1</cp:revision> \r\n");
//			writer.write(" <dcterms:created xsi:type=\"dcterms:W3CDTF\"></dcterms:created> \r\n");
//			writer.write(" <dcterms:modified xsi:type=\"dcterms:W3CDTF\"></dcterms:modified> \r\n");
//			writer.write("</cp:coreProperties> \r\n");
//
//			writer.flush();
//			writer.close();
//			ooxmlZipEntries.add(coreEntry);
//		}
//		finally
//		{
//			if (writer != null)
//			{
//				try
//				{
//					writer.close();
//				}
//				catch (IOException e)
//				{
//				}
//			}
//		}
//    }
//
//    private void createWebSettingsEntry(String mimetype) throws IOException
//    {
//		Writer writer = null;
//		webSettingsEntry = createEntry(mimetype + "/webSettings.xml");
//		
//		try
//		{
//			writer = webSettingsEntry.getWriter();
//			writer.write(xmlProlog);
//			writer.write("<w:webSettings \r\n");
//			writer.write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" \r\n");
//			writer.write(" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"> \r\n");
//			writer.write(" <w:optimizeForBrowser/> \r\n");
//			writer.write("</w:webSettings> \r\n");
//			
//			writer.flush();
//			writer.close();
//			ooxmlZipEntries.add(webSettingsEntry);
//		}
//		finally
//		{
//			if (writer != null)
//			{
//				try
//				{
//					writer.close();
//				}
//				catch (IOException e)
//				{
//				}
//			}
//		}
//    }
//
//    private void createFontTableEntry() throws IOException
//    {
//		Writer writer = null;
////		fontTableEntry = createEntry(mimetype + "/webSettings.xml");
//		
//		try
//		{
//			writer = fontTableEntry.getWriter();
//			writer.write(xmlProlog);
//			writer.write("<w:fonts \r\n");
//			writer.write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" \r\n");
//			writer.write(" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"> \r\n");
//			writer.write(" <w:font w:name=\"DejaVu Sans\"> \r\n");
//			writer.write("  <w:pitch w:val=\"variable\"/> \r\n");
//			writer.write(" </w:font> \r\n");
//			writer.write("</w:fonts> \r\n");
//			
//			writer.flush();
//			writer.close();
//		}
//		finally
//		{
//			if (writer != null)
//			{
//				try
//				{
//					writer.close();
//				}
//				catch (IOException e)
//				{
//				}
//			}
//		}
//    }
//
//    private void createSettingsEntry(String mimetype) throws IOException
//    {
//		Writer writer = null;
//		settingsEntry = createEntry(mimetype + "/settings.xml");
//		
//		try
//		{
//			writer = settingsEntry.getWriter();
//			writer.write(xmlProlog);
//			writer.write("<w:settings \r\n");
//			writer.write(" xmlns:o=\"urn:schemas-microsoft-com:office:office\" \r\n");
//			writer.write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" \r\n");
//			writer.write(" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" \r\n");
//			writer.write(" xmlns:v=\"urn:schemas-microsoft-com:vml\" \r\n");
//			writer.write(" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" \r\n");
//			writer.write(" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" \r\n");
//			writer.write(" xmlns:sl=\"http://schemas.openxmlformats.org/schemaLibrary/2006/main\"> \r\n");
//			writer.write(" <w:zoom w:percent=\"100\"/> \r\n");
//			writer.write(" <w:embedSystemFonts /> \r\n");
//			writer.write(" <w:proofState w:grammar=\"clean\"/> \r\n");
//			writer.write(" <w:defaultTabStop w:val=\"720\"/> \r\n");
//			writer.write(" <w:characterSpacingControl w:val=\"doNotCompress\"/> \r\n");
//			writer.write(" <w:stylePaneFormatFilter w:val=\"3F01\"></w:stylePaneFormatFilter> \r\n");
//			writer.write(" <w:doNotTrackMoves /> \r\n");
//			writer.write(" <w:noPunctuationKerning /> \r\n");
//			writer.write(" <w:compat> \r\n");
//			writer.write("  <w:useWord2002TableStyleRules /> \r\n");
//			writer.write("  <w:growAutofit /> \r\n");
//			writer.write("  <w:useNormalStyleForList /> \r\n");
//			writer.write("  <w:doNotUseIndentAsNumberingTabStop /> \r\n");
//			writer.write("  <w:useAltKinsokuLineBreakRules /> \r\n");
//			writer.write("  <w:allowSpaceOfSameStyleInTable /> \r\n");
//			writer.write("  <w:doNotSuppressIndentation /> \r\n");
//			writer.write("  <w:doNotAutofitConstrainedTables /> \r\n");
//			writer.write("  <w:autofitToFirstFixedWidthCell /> \r\n");
//			writer.write("  <w:displayHangulFixedWidth /> \r\n");
//			writer.write("  <w:splitPgBreakAndParaMark /> \r\n");
//			writer.write("  <w:doNotVertAlignCellWithSp /> \r\n");
//			writer.write("  <w:doNotBreakConstrainedForcedTable /> \r\n");
//			writer.write("  <w:doNotVertAlignInTxbx /> \r\n");
//			writer.write("  <w:useAnsiKerningPairs /> \r\n");
//			writer.write("  <w:cachedColBalance /> \r\n");
//			writer.write(" </w:compat> \r\n");
//			writer.write("</w:settings> \r\n");
//			
//			writer.flush();
//			writer.close();
//			ooxmlZipEntries.add(settingsEntry);
//		}
//		finally
//		{
//			if (writer != null)
//			{
//				try
//				{
//					writer.close();
//				}
//				catch (IOException e)
//				{
//				}
//			}
//		}
//    }
//
//    private void createMimetypeRelsEntry(String mimetype) throws IOException
//    {
//		Writer writer = null;
//		mimetypeRelsEntry = createEntry(mimetype + "/_rels/document.xml.rels");
//		try
//		{
//			writer = mimetypeRelsEntry.getWriter();
//			writer.write(xmlProlog);
//			writer.write("<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\"> \r\n");
//			writer.write(" <Relationship Id=\"rId3\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/webSettings\" Target=\"webSettings.xml\"/> \r\n");
//			writer.write(" <Relationship Id=\"rId2\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings\" Target=\"settings.xml\"/> \r\n");
//			writer.write(" <Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/> \r\n");
//			writer.write(" <Relationship Id=\"rId5\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme\" Target=\"theme/theme1.xml\"/> \r\n");
//			writer.write(" <Relationship Id=\"rId4\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable\" Target=\"fontTable.xml\"/> \r\n");
//			writer.write("</Relationships> \r\n");
//			
//			writer.flush();
//			writer.close();
//			ooxmlZipEntries.add(mimetypeRelsEntry);
//		}
//		finally
//		{
//			if (writer != null)
//			{
//				try
//				{
//					writer.close();
//				}
//				catch (IOException e)
//				{
//				}
//			}
//		}
//    }
//    
//    private void createAppEntry(String mimetype) throws IOException
//    {
//		Writer writer = null;
//		appEntry = createEntry("docProps/app.xml");
//		
//		try
//		{
//			writer = appEntry.getWriter();
//			writer.write(xmlProlog);
//			writer.write("<Properties \r\n");
//			writer.write(" xmlns=\"http://schemas.openxmlformats.org/officeDocument/2006/extended-properties\" \r\n");
//			writer.write(" xmlns:vt=\"http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes\"> \r\n");
//			writer.write(" <Template>Normal.dotm</Template> \r\n");
//			writer.write(" <TotalTime></TotalTime> \r\n");
//			writer.write(" <Pages></Pages> \r\n");
//			writer.write(" <Words></Words> \r\n");
//			writer.write(" <Characters></Characters> \r\n");
//			writer.write(" <Application>Microsoft Office Word</Application> \r\n");
//			writer.write(" <DocSecurity>0</DocSecurity> \r\n");
//			writer.write(" <Lines></Lines> \r\n");
//			writer.write(" <Paragraphs></Paragraphs> \r\n");
//			writer.write(" <ScaleCrop>false</ScaleCrop> \r\n");
//			writer.write(" <Company></Company> \r\n");
//			writer.write(" <LinksUpToDate>false</LinksUpToDate> \r\n");
//			writer.write(" <CharactersWithSpaces></CharactersWithSpaces> \r\n");
//			writer.write(" <SharedDoc>false</SharedDoc> \r\n");
//			writer.write(" <HyperlinksChanged>false</HyperlinksChanged> \r\n");
//			writer.write(" <AppVersion>12.0000</AppVersion> \r\n");
//			writer.write("</Properties> \r\n");
//			
//			writer.flush();
//			writer.close();
//			ooxmlZipEntries.add(appEntry);
//		}
//		finally
//		{
//			if (writer != null)
//			{
//				try
//				{
//					writer.close();
//				}
//				catch (IOException e)
//				{
//				}
//			}
//		}
//    }
}
