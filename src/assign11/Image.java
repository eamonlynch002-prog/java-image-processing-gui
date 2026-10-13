package assign11;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class represents an image as a two-dimensional array of pixels and provides a number
 * of image filters (via instance methods) for changing the appearance of the image.
 * Application of multiple filters is cumulative; e.g., obj.redBlueSwapFilter() followed by
 * obj.rotateClockwiseFilter() results in an image altered both in color and orientation.
 *
 * Note:
 *   - The pixel in the northwest corner of the image is stored in the first row, first column.
 *   - The pixel in the northeast corner of the image is stored in the first row, last column.
 *   - The pixel in the southeast corner of the image is stored in the last row, last column.
 *   - The pixel in the southwest corner of the image is stored in the last row, first column.
 *
 * @author Prof. Martin, John Sayasith, and Eamon Lynch
 * @version 3/7/2025
 */
public class Image {

	private Pixel[][] imageArray;
	

	/**
	 * Creates a new Image object by reading the image file with the given filename.
	 *
	 * DO NOT MODIFY THIS METHOD
	 *
	 * @param filename - name of the given image file to read
	 * @throws IOException if file does not exist or cannot be read
	 */
	public Image(String filename) {
		BufferedImage imageInput = null;
		try {
			imageInput = ImageIO.read(new File(filename));
		}
		catch(IOException e) {
			System.out.println("Image file " + filename + " does not exist or cannot be read.");
		}

		imageArray = new Pixel[imageInput.getHeight()][imageInput.getWidth()];
		for(int i = 0; i < imageArray.length; i++)
			for(int j = 0; j < imageArray[0].length; j++) {
				int rgb = imageInput.getRGB(j, i);
				imageArray[i][j] = new Pixel((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
			}
	}

	/**
	 * Create an Image object directly from a pre-made Pixel array.
   * This is primarily to be used in testing.
	 *
	 * DO NOT MODIFY THIS METHOD
	 */
	public Image(Pixel[][] imageArray) {
		this.imageArray = imageArray;
	}

	/**
	 * Create a new "default" Image object, whose purpose is to be used in testing.
	 *
	 * The orientation of this image:
	 * 		cyan 	 red
	 *		green	 magenta
	 *		yellow	 blue
	 *
	 * DO NOT MODIFY THIS METHOD
	 */
	public Image() {
		imageArray = new Pixel[3][2];
		imageArray[0][0] = new Pixel(0, 255, 255);  // cyan
		imageArray[0][1] = new Pixel(255, 0, 0);  // red
		imageArray[1][0] = new Pixel(0, 255, 0);  // green
		imageArray[1][1] = new Pixel(255, 0, 255);  // magenta
		imageArray[2][0] = new Pixel(255, 255, 0);  // yellow
		imageArray[2][1] = new Pixel(0, 0, 255);  // blue
	}

	/**
	 * Gets the pixel at the specified row and column indexes.
	 *
	 * DO NOT MODIFY THIS METHOD
	 *
	 * @param rowIndex - given row index
	 * @param columnIndex - given column index
	 * @return the pixel at the given row index and column index
	 * @throws IndexOutOfBoundsException if row or column index is out of bounds
	 */
	public Pixel getPixel(int rowIndex, int columnIndex) {
		if(rowIndex < 0 || rowIndex >= imageArray.length)
			throw new IndexOutOfBoundsException("rowIndex must be in range 0-" + (imageArray.length - 1));

		if(columnIndex < 0 || columnIndex >= imageArray[0].length)
			throw new IndexOutOfBoundsException("columnIndex must be in range 0-" + (imageArray[0].length - 1));

		return imageArray[rowIndex][columnIndex];
	}

	/**
	 * Writes the image represented by this object to file.
	 * Does nothing if the image length is 0.
	 *
	 * DO NOT MODIFY THIS METHOD
	 *
	 * @param filename - name of image file to write
	 * @throws IOException if file does cannot be written
	 */
	public void writeImage(String filename) {
		if(imageArray.length > 0) {
			BufferedImage imageOutput = new BufferedImage(imageArray[0].length,
					imageArray.length, BufferedImage.TYPE_INT_RGB);

			for(int i = 0; i < imageArray.length; i++)
				for(int j = 0; j < imageArray[0].length; j++)
					imageOutput.setRGB(j, i, imageArray[i][j].getPackedRGB());

			try {
				ImageIO.write(imageOutput, "png", new File(filename));
			}
			catch(IOException e) {
				System.out.println("The image cannot be written to file " + filename);
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that for each
	 * pixel the red amount and blue amount are swapped.
	 *
	 * HINT: Since the Pixel class does not include setter methods for its private
	 *       instance variables, create new Pixel objects with the altered colors.
	 */
	public void redBlueSwapFilter() {
		Pixel alteredPixel;
		Pixel currentPixel;
		for (int r = 0; r < this.imageArray.length; r++) {	//loops through array rows
			for (int c = 0; c < this.imageArray[r].length; c++) {	//loops through array columns
				currentPixel = getPixel(r, c);
				alteredPixel = new Pixel(currentPixel.getBlueAmount(), currentPixel.getGreenAmount(), currentPixel.getRedAmount());	//creates new pixel with swapped red and blue
				this.imageArray[r][c] = alteredPixel;	//assigns swapped pixel at the current position
			}
		}
	}


	/**
	 * Applies a filter to the image represented by this object such that the color
	 * of each pixel is converted to its corresponding grayscale shade, producing the
	 * effect of a black and white photo. The filter sets the amount of red, green,
	 * and blue all to the value of this average:
	 *           (originalRed + originalGreen + originalBlue) / 3
	 *
	 * HINT: Since the Pixel class does not include setter methods for its private
	 *       instance variables, create new Pixel objects with the altered colors.
	 */
	public void blackAndWhiteFilter() {
		Pixel currentPixel;
		Pixel alteredPixel;
		int rgbAverage;
		for (int r = 0; r < this.imageArray.length; r++) {	//loops through array rows
			for (int c = 0; c < this.imageArray[r].length; c++) {	//loops through array columns
				currentPixel = getPixel(r, c);
				rgbAverage = (currentPixel.getRedAmount() +currentPixel.getGreenAmount() + currentPixel.getBlueAmount()) / 3;	//finds gray scale average of pixel
				alteredPixel = new Pixel(rgbAverage, rgbAverage, rgbAverage);	//creates new pixel with gray scale value
				this.imageArray[r][c] = alteredPixel;	//assigns new altered pixel at the current position
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that it is rotated
	 * clockwise (by 90 degrees). This filter rotates directly clockwise, it should
   * not do this by rotating counterclockwise 3 times.
	 *
	 * HINT: If the image is not square, this filter requires creating a new array with
	 *       different lengths. Use the technique of creating and reassigning a new backing array
	 *       from BetterDynamicArray (assign06) as a guide for how to make a second array and
	 *       eventually reset the imageArray reference to this new array.
	 *       Note that we learned how to rotate a square 2D array *left* in Class Meeting 11.
	 */
	public void rotateClockwiseFilter() {
		Pixel[][] rotatedImageArray= new Pixel[this.imageArray[0].length][this.imageArray.length];	//creates a backing array with the rows and columns of the original image flipped
		Pixel currentPixel;
		for(int r = 0; r < this.imageArray.length; r++) {	//loops through the rows
			for (int c = 0; c < this.imageArray[r].length; c++) {	//loops through array columns
				currentPixel = getPixel(r, c);
				rotatedImageArray[c][this.imageArray.length - 1 - r] = currentPixel;	//sets the current pixel in the correct position in the rotated image array
			}
		}
		this.imageArray = rotatedImageArray;	//sets rotated array as the current array instance
	}

	/**
	 * mirrors the image from left to right / right to left
	 */
	public void mirrorImageFilter() {
		Pixel[][] mirroredImageArray = new Pixel[this.imageArray.length][this.imageArray[0].length];	//creates backing array of the same size of the original image array
		Pixel currentPixel;
		for (int r = 0; r < this.imageArray.length; r++) {	//loops through array rows
			for (int c = 0; c < this.imageArray[r].length; c++) {	//loops through the array columns
				currentPixel = getPixel(r, c);
				mirroredImageArray[r][imageArray[r].length - 1 - c] = currentPixel;	//sets first row in the original image to the last row
			}
		}
		this.imageArray = mirroredImageArray;	//sets the mirrored array as the current array instance
	}
	
	public int getNumberOfRows() {
		return this.imageArray.length;
		}

	public int getNumberOfColumns() {
	   if(this.imageArray.length == 0)
	      return 0;
	   return this.imageArray[0].length;
	}
	
	/**
	 * Applies a filter to the image represented by this object where the colors are
	 * inverted. Each pixels RGB values are subtracted from 255, giving the fox a unique hue.
	 */
	public void invertHueFilter() {
        for (int i = 0; i < imageArray.length; i++) {
            for (int j = 0; j < imageArray[0].length; j++) {
                Pixel original = imageArray[i][j];
                imageArray[i][j] = new Pixel(255 - original.getRedAmount(), 
                                             255 - original.getGreenAmount(), 
                                             255 - original.getBlueAmount());
            }
        }
    }
	
	/**
	 * The filter takes an integer as input and adds the integer 
	 * to each of the red, green, and blue amounts.  A positive 
	 * integer makes the image brighter and a negative integer 
	 * makes the image darker while ensuring the integer is within
	 * the range of 0 to 255.
	 * @param brightnessValue determines how bright or dark the image is altered by.
	 */
	public void brightness(int brightnessValue) {
		int redAmount;
		int greenAmount;
		int blueAmount;
		
		for(int i = 0; i < imageArray.length; i++) {
			for(int j = 0; j < imageArray[0].length; j++) {
				Pixel original = imageArray[i][j];
				redAmount = original.getRedAmount() + brightnessValue;
				greenAmount = original.getGreenAmount() + brightnessValue;
				blueAmount = original.getBlueAmount() + brightnessValue;
				
				if(redAmount > 255) {
					redAmount = 255;
				}else if(redAmount < 0) {
					redAmount = 0;
				}
				
				if(greenAmount > 255) {
					greenAmount = 255;
				}else if(greenAmount < 0) {
					greenAmount = 0;
				}
				
				if(blueAmount > 255) {
					blueAmount = 255;
				}else if(blueAmount < 0) {
					blueAmount = 0;
				}
				
                imageArray[i][j] = new Pixel(redAmount, greenAmount, blueAmount);
			}
		}

	}
	
	/**
	 * Takes in information as input to define a rectangle bounded by
	 * the x,y position of its northwest corner and the x,y position
	 * of its south east corner. 
	 * @param startX top left x coordinate
	 * @param startY top left y coordinate
	 * @param prevX bottom right x coordinate
	 * @param prevY bottom right y coordinate
	 */
	public void cropImage(int startX, int startY, int prevX, int prevY) {
		
		int width = prevX - startX;
		int height = prevY - startY;
		Pixel[][] croppedArray = new Pixel[height][width];

	
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				Pixel original = imageArray[startY + y][startX + x];
				croppedArray[y][x] = new Pixel(original.getRedAmount(),original.getGreenAmount(),original.getBlueAmount());
			}
		
		}
		this.imageArray = croppedArray;
	}
	
	/**
	 * Creates a copy of an image in the ArrayList of Images.
	 * @return the new copied image.
	 */
	public Image copy() {
		int row = getNumberOfRows();
		int col = getNumberOfColumns();
		Pixel[][] copiedPixels = new Pixel[row][col];
		
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				Pixel pixel = this.getPixel(i, j);
				copiedPixels[i][j] = new Pixel(pixel.getRedAmount(), pixel.getGreenAmount(), pixel.getBlueAmount());
			}
		}
		return new Image(copiedPixels);
	}
	
	
}
	
