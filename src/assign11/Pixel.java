package assign11;

/**
 * this class represents a single pixel's RGB values from 0 to 255 stored as ints.
 * contains getter functions and packed RGB int value
 * 
 * @author John Sayasith and Eamon Lynch
 * @version 3/7/2025
 */
public class Pixel {
	
	private int redAmount;
	private int greenAmount;
	private int blueAmount;
	
	
	/**
	 * constructor of an RGB pixel object
	 * @param redAmount - int of red intensity between 0 and 255
	 * @param greenAmount - int of green intensity between 0 and 255
	 * @param blueAmount - int of blue intensity between 0 and 255
	 */
	public Pixel(int redAmount, int greenAmount, int blueAmount) {
		if (redAmount < 0 || redAmount > 255) {
			throw new IllegalArgumentException("redAmount value is out of bounds: got " + redAmount);
		}
		this.redAmount = redAmount;
		
		if (greenAmount < 0 || greenAmount > 255) {
			throw new IllegalArgumentException("greenAmount value is out of bounds: got " + greenAmount);
		}
		this.greenAmount = greenAmount;
		
		if (blueAmount < 0 || blueAmount > 255) {
			throw new IllegalArgumentException("blueAmount value is out of bounds: got " + blueAmount);
		}
		this.blueAmount = blueAmount;
	}
	
	/**
	 * retrieves red intensity value
	 * @return int - red intensity value
	 */
	public int getRedAmount() {
		return this.redAmount;
	}
	
	/**
	 * retrieves green intensity value
	 * @return int - green intensity value
	 */
	public int getGreenAmount() {
		return this.greenAmount;
	}
	
	/**
	 * retrieves blue intensity value
	 * @return int - blue intensity value
	 */
	public int getBlueAmount() {
		return this.blueAmount;
	}
	
	/**
	 * retrieves the four byte int value of a RGB pixel object
	 * @return int - packed RGB value
	 */
	public int getPackedRGB() {
		int packedRGBValue = (this.redAmount << 16 | this.greenAmount << 8 | this.blueAmount);
		return packedRGBValue;
	}
}
