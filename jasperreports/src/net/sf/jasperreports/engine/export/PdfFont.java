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
	private boolean isPdfSimulatedBold;
	private boolean isPdfSimulatedItalic;

	
	public PdfFont(String pdfFontName, String pdfEncoding, boolean isPdfEmbedded)
	{
		this(pdfFontName, pdfEncoding, isPdfEmbedded, false, false);
	}

	public PdfFont(
		String pdfFontName, 
		String pdfEncoding, 
		boolean isPdfEmbedded,
		boolean isPdfSimulatedBold,
		boolean isPdfSimulatedItalic
		)
	{
		this.pdfFontName = pdfFontName;
		this.pdfEncoding = pdfEncoding;
		this.isPdfEmbedded = isPdfEmbedded;
		this.isPdfSimulatedBold = isPdfSimulatedBold;
		this.isPdfSimulatedItalic = isPdfSimulatedItalic;
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

	public boolean isPdfSimulatedBold()
	{
		return isPdfSimulatedBold;
	}

	public boolean isPdfSimulatedItalic()
	{
		return isPdfSimulatedItalic;
	}

}
