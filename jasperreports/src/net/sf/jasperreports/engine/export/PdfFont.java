package net.sf.jasperreports.engine.export;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class PdfFont
{
	private String pdfFontName;
	private String pdfEncoding;
	private boolean isPdfEmbedded;

	
	public PdfFont(String pdfFontName, String pdfEncoding, boolean isPdfEmbedded)
	{
		this.pdfFontName = pdfFontName;
		this.pdfEncoding = pdfEncoding;
		this.isPdfEmbedded = isPdfEmbedded;
	}

	public String getPdfFontName()
	{
		return pdfFontName;
	}

	public String getPdfEncoding()
	{
		return pdfEncoding;
	}

	public boolean isPdfEmbedded()
	{
		return isPdfEmbedded;
	}

}
