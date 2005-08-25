package net.sf.jasperreports.engine.export;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id
 */
public class PdfFont
{
	private String pdfFontName;
	private String pdfEncoding;
	private boolean isPdfEmbedded;

	public PdfFont()
	{
	}
	
	public PdfFont(String pdfFontName, String pdfEncoding, boolean pdfEmbedded)
	{
		this.pdfFontName = pdfFontName;
		this.pdfEncoding = pdfEncoding;
		isPdfEmbedded = pdfEmbedded;
	}

	public String getPdfFontName()
	{
		return pdfFontName;
	}

	public void setPdfFontName(String pdfFontName)
	{
		this.pdfFontName = pdfFontName;
	}

	public String getPdfEncoding()
	{
		return pdfEncoding;
	}

	public void setPdfEncoding(String pdfEncoding)
	{
		this.pdfEncoding = pdfEncoding;
	}

	public boolean isPdfEmbedded()
	{
		return isPdfEmbedded;
	}

	public void setPdfEmbedded(boolean pdfEmbedded)
	{
		isPdfEmbedded = pdfEmbedded;
	}
}
