package net.sf.jasperreports.engine.export;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class ImageSettings {

	private int index;
	private int anchorType;
	
	public ImageSettings() {
	}
	
	public ImageSettings(int index, int anchorType) {
		this.index = index;
		this.anchorType = anchorType;
	}

	public int getIndex() {
		return index;
	}
	
	public int getAnchorType() {
		return anchorType;
	}
}
