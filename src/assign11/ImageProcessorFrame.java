package assign11;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * program allows user to load and display an chosen image, apply filters to the image, then save the edited image to a chosen location.
 * @author John Sayasith & Eamon Lynch
 * @version 4/11/2025
 */
public class ImageProcessorFrame extends JFrame implements ActionListener,ChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel;
	
	private JMenuBar menuBar;
	private JMenu filterList;
	private JMenu fileMenu;
	
	private JMenuItem openImage;
	private JMenuItem saveImage;
	
	private JMenuItem redBlueSwapFilter;
	private JMenuItem blackWhiteFilter;
	private JMenuItem rotateClockwiseFilter;
	private JMenuItem invertHueFilter;
	private JMenuItem mirrorImageFilter;
	private JMenuItem brightness;
	private JMenuItem crop;
	
	private JMenuItem undo;
	private JMenuItem redo;
	
	private Image coppiedImage;
	private Image currentImage;
	private ImagePanel currentImagePanel;
	
	private ArrayList<Image> imageHistory = new ArrayList<>();
	private ArrayList<Image> undoHistory = new ArrayList<>();

	private Boolean knownInputImage = false;
	
	private JSlider brightnessSlider;
	
	/**
	 * sets up the main panel and its components for ImageProcessorFrame
	 */
	public ImageProcessorFrame() {
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	
		this.menuBar = new JMenuBar();
		this.menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		setJMenuBar(this.menuBar);
		
		this.fileMenu = new JMenu("File");
		this.menuBar.add(fileMenu);
		
        this.filterList = new JMenu("Filters");
		this.menuBar.add(this.filterList);
		
		this.openImage = new JMenuItem("Open Image");
		this.saveImage = new JMenuItem("Save Image");
		this.openImage.addActionListener(this);
		this.saveImage.addActionListener(this);
        this.fileMenu.add(openImage);
        this.fileMenu.add(saveImage);
		
		this.redBlueSwapFilter = new JMenuItem("Red & Blue Swap Filter");
		this.blackWhiteFilter = new JMenuItem("Black & White Filter");
		this.rotateClockwiseFilter = new JMenuItem("Rotate Clockwise Filter");
		this.invertHueFilter = new JMenuItem("Invert Hue Filter");
		this.mirrorImageFilter = new JMenuItem("Mirror Image Filter");
		this.redBlueSwapFilter.addActionListener(this);
		this.blackWhiteFilter.addActionListener(this);
		this.rotateClockwiseFilter.addActionListener(this);
		this.invertHueFilter.addActionListener(this);
		this.mirrorImageFilter.addActionListener(this);
		
		this.filterList.add(this.redBlueSwapFilter);
		this.filterList.add(this.blackWhiteFilter);
		this.filterList.add(this.rotateClockwiseFilter);
		this.filterList.add(this.invertHueFilter);
		this.filterList.add(this.mirrorImageFilter);
		
		redBlueSwapFilter.setToolTipText("Swaps the red and blue values in the image");
		blackWhiteFilter.setToolTipText("Turns the image into black and white");
		rotateClockwiseFilter.setToolTipText("Rotates the image 90 degrees clockwise");
		mirrorImageFilter.setToolTipText("Flips to image horizontally");
		invertHueFilter.setToolTipText("Inverts the hue of all colors in the image");
		
		this.brightness = new JMenuItem("Brightness");
		this.brightness.addActionListener(this);
		this.filterList.add(brightness);
		this.brightness.setToolTipText("Control the image brightness using the slider below");
		this.brightnessSlider = new JSlider(JSlider.HORIZONTAL, -200, 200, 0);
		brightnessSlider.addChangeListener(this);
		brightnessSlider.setMajorTickSpacing(50);
		brightnessSlider.setMinorTickSpacing(5);
		brightnessSlider.setPaintTicks(true);
		brightnessSlider.setPaintLabels(true);
		
		this.crop = new JMenuItem("Crop");
		this.crop.addActionListener(this);
		this.crop.setToolTipText("Use the mouse to select a region of the image to crop");
		this.crop.setEnabled(false);
		this.filterList.add(crop);
		
		this.undo = new JMenuItem("Undo");
		this.undo.addActionListener(this);
		this.undo.setToolTipText("Reverts image back to the previous image");
		this.menuBar.add(undo);
		
		this.redo = new JMenuItem("Redo");
		this.redo.addActionListener(this);
		this.redo.setToolTipText("Redoes the previous undo action");
		this.menuBar.add(redo);
		
		this.mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(this.mainPanel);

		this.setPreferredSize(new Dimension(1200, 800));
		this.pack();
		
		this.loadImage();
	}

	/**
	 * monitors and performs menu items that are selected by the user
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source instanceof JMenuItem) {
			JMenuItem chosenMenuItem = (JMenuItem) source;
			
			if(chosenMenuItem == openImage) {
				this.loadImage();
				
			   }
			if(this.knownInputImage) {	
			
				this.crop.setEnabled(false);
				this.redBlueSwapFilter.setEnabled(true);
				this.blackWhiteFilter.setEnabled(true);
				this.rotateClockwiseFilter.setEnabled(true);
				this.mirrorImageFilter.setEnabled(true);
				this.invertHueFilter.setEnabled(true);
				this.brightness.setEnabled(true);
				this.undo.setEnabled(true);
				this.redo.setEnabled(true);
				this.filterList.setEnabled(true);
				this.saveImage.setEnabled(true);
				
				if(chosenMenuItem == saveImage) {
					this.saveImage();
				}
				if(chosenMenuItem == redBlueSwapFilter) {
					this.coppiedImage = getCurrentImage().copy();
					this.coppiedImage.redBlueSwapFilter();
					imageHistory.add(this.coppiedImage);
					redraw();
				}
				if(chosenMenuItem == blackWhiteFilter) {
					this.coppiedImage = getCurrentImage().copy();
					this.coppiedImage.blackAndWhiteFilter();
					imageHistory.add(this.coppiedImage);
					redraw();
				}
				if(chosenMenuItem == rotateClockwiseFilter) {
					this.coppiedImage = getCurrentImage().copy();
					this.coppiedImage.rotateClockwiseFilter();
					imageHistory.add(this.coppiedImage);
					redraw();
			
				}
				if(chosenMenuItem == mirrorImageFilter) {
					this.coppiedImage = getCurrentImage().copy();
					this.coppiedImage.mirrorImageFilter();
					imageHistory.add(this.coppiedImage);
					redraw();
				}
				if(chosenMenuItem == invertHueFilter) {
					this.coppiedImage = getCurrentImage().copy();
					this.coppiedImage.invertHueFilter();
					imageHistory.add(this.coppiedImage);
					redraw();
				}
				if(chosenMenuItem == brightness) {
					
					currentImagePanel.add(this.brightnessSlider, BorderLayout.SOUTH);
					setContentPane(currentImagePanel);

					revalidate();
					repaint();
				}
				if(chosenMenuItem == crop) {
					enableCropOnly();
					int x1 = currentImagePanel.getStartX();
					int y1 = currentImagePanel.getStartY();
					int x2 = currentImagePanel.getPrevX();
					int y2 = currentImagePanel.getPrevY();

					this.coppiedImage = getCurrentImage().copy();
					this.coppiedImage.cropImage(x1, y1, x2, y2);

					imageHistory.add(this.coppiedImage);
					redraw();
					disableCropOnly();
					}
				}
				
		}
	}
	
	/**
	 * Action method for using the slider. Manipulates the brightness 
	 * method to change the brightness of the image.
	 */
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	        int brightnessValue = (int)source.getValue();
	        
	        this.coppiedImage = getCurrentImage().copy();
	        coppiedImage.brightness(brightnessValue);
	        imageHistory.add(coppiedImage);
	        redraw();
	    }
	}
	
	
	/**
	 * redraws the image panel by creating a new imagePanel from the userInputImage
	 * USED AS A CLASS HELPER METHOD
	 */
	private void redraw() {
		this.currentImage = this.coppiedImage;
		this.currentImagePanel = new ImagePanel(this.currentImage, this);
		this.currentImagePanel.setLayout(new BorderLayout());
    	setContentPane(this.currentImagePanel);
    	revalidate();
    	repaint();
	}	
	
	/**
	 * Helper method for code organization and readability.
	 * @return the last image in the image array list.
	 */
	private Image getCurrentImage() {
		return this.imageHistory.get(this.imageHistory.size() - 1);
	}
	
	/**
	 * opens JFileChooser for the user to select an image and also draws it to the image panel.
	 * USED AS A CLASS HELPER METHOD
	 */
	private void loadImage() {
		JFileChooser chooser = new JFileChooser();
	    chooser.setFileFilter(new FileNameExtensionFilter("JPG, JPEG, GIF, PNG, BMP", "jpg", "jpeg", "gif", "png", "bmp"));
	    chooser.setDialogTitle("Select an image");
	    
	    if(chooser.showOpenDialog(this.mainPanel) == JFileChooser.APPROVE_OPTION) {
	    	this.knownInputImage = true;
	    	File selectedFile = chooser.getSelectedFile(); 
	    	this.currentImage = new Image(selectedFile.getAbsolutePath());
	    	
	    	this.imageHistory.clear();
	    	this.imageHistory.add(this.currentImage);
	    	
			this.currentImagePanel = new ImagePanel(this.currentImage, this);
			this.currentImagePanel.setLayout(new BorderLayout());
	    	setContentPane(this.currentImagePanel);
	    	revalidate();
	    	repaint();
	    }
	    else {
	    	JOptionPane.showMessageDialog(null, "File Not Selected");
	    	this.crop.setEnabled(false);
			this.redBlueSwapFilter.setEnabled(false);
			this.blackWhiteFilter.setEnabled(false);
			this.rotateClockwiseFilter.setEnabled(false);
			this.mirrorImageFilter.setEnabled(false);
			this.invertHueFilter.setEnabled(false);
			this.brightness.setEnabled(false);
			this.undo.setEnabled(false);
			this.redo.setEnabled(false);
			this.filterList.setEnabled(false);
			this.saveImage.setEnabled(false);
	    }
	}
	
	/**
	 * saves the filtered image panel as a png to a specified location.
	 * USED AS A CLASS HELPER METHOD
	 */
	private void saveImage() {
		//runs file chooser, filters file types, and see if "save" is clicked in the end
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("JPG", "jpg"));
		chooser.setSelectedFile(new File("new_filtered_image.jpg"));
		chooser.setDialogTitle("Select the location for the new filtered image");
		
		if(chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "Save file cancelled");
			return;
		}
		
		//creates copy template of the size of UserInputImagePanel
		Image imageToSave = imageHistory.get(imageHistory.size() - 1);
		
		int width = imageToSave.getNumberOfColumns();
		int height = imageToSave.getNumberOfRows();
		
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				Pixel p = imageToSave.getPixel(row, col);
				int rgb = (p.getRedAmount() << 16) | (p.getGreenAmount() << 8) | p.getBlueAmount();
				bufferedImage.setRGB(col, row, rgb);
			}
		}
		
		try {
			ImageIO.write(bufferedImage, "jpg", chooser.getSelectedFile());
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "The filtered image cannot be written to file.");
		}	
	}
	
	/**
	 * Reverts the image back to the previously altered image.
	 */
	public void undoImage() {
		if(imageHistory.size() > 1) {
			Image lastImage = imageHistory.remove(imageHistory.size() - 1);
			undoHistory.add(lastImage);
			coppiedImage = imageHistory.get(imageHistory.size() - 1);
			redraw();
		}
	}
	
	/**
	 * Reverts the actions of the undo button.
	 */
	public void redoImage() {
		if(undoHistory.size() >= 1) {
			Image redoImage = undoHistory.remove(undoHistory.size() - 1);
			imageHistory.add(redoImage);
			coppiedImage = redoImage;
			redraw();
		}
	}
	
	/**
	 * Disables all of the filters except for crop.
	 */
	public void enableCropOnly() {
		this.crop.setEnabled(true);
		this.redBlueSwapFilter.setEnabled(false);
		this.blackWhiteFilter.setEnabled(false);
		this.rotateClockwiseFilter.setEnabled(false);
		this.mirrorImageFilter.setEnabled(false);
		this.invertHueFilter.setEnabled(false);
		this.brightness.setEnabled(false);
		this.undo.setEnabled(false);
		this.redo.setEnabled(false);
		this.fileMenu.setEnabled(false);
	}
	
	/**
	 * Enables all of the filters except for crop.
	 */
	public void disableCropOnly() {
		this.crop.setEnabled(false);
		this.redBlueSwapFilter.setEnabled(true);
		this.blackWhiteFilter.setEnabled(true);
		this.rotateClockwiseFilter.setEnabled(true);
		this.mirrorImageFilter.setEnabled(true);
		this.invertHueFilter.setEnabled(true);
		this.brightness.setEnabled(true);
		this.undo.setEnabled(true);
		this.redo.setEnabled(true);
		this.fileMenu.setEnabled(true);
	}
	
}