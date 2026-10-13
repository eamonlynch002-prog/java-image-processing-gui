package assign11;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * This class represents a GUI component for displaying an image.
 *
 * @author Prof. Martin
 * @version Assignment 10
 */
public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener{

	private BufferedImage bufferedImg;
	
	private int startX, startY;
	private int prevX, prevY;
	private boolean dragging = false;
	
	private ImageProcessorFrame frame;

	/**
	 * Creates a new ImagePanel to display the given image.
	 *
	 * @param img - the given image
	 */
	public ImagePanel(Image img, ImageProcessorFrame frame) {
		int rowCount = img.getNumberOfRows();
		int colCount = img.getNumberOfColumns();

		this.bufferedImg = new BufferedImage(colCount, rowCount, BufferedImage.TYPE_INT_RGB);
		this.frame = frame;

		for(int i = 0; i < rowCount; i++)
			for(int j = 0; j < colCount; j++)
				this.bufferedImg.setRGB(j, i, img.getPixel(i, j).getPackedRGB());

		this.setPreferredSize(new Dimension(colCount, rowCount));
		this.addMouseListener(this);		
		this.addMouseMotionListener(this);
	}

	/**
	 * This method is called by the system when a component needs to be painted.
	 * Which can be at one of three times:
	 *    --when the component first appears
	 *    --when the size of the component changes (including resizing by the user)
	 *    --when repaint() is called
	 *
	 * Partially overrides the paintComponent method of JPanel.
	 *
	 * @param g -- graphics context onto which we can draw
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(105, 105, 105, 125));
		g.drawImage(this.bufferedImg, 0, 0, this);
		g.fillRect(startX, startY, prevX - startX, prevY - startY);
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(dragging == false) {
			return;
		}
		int x = e.getX();
		int y = e.getY();
		
		prevX = x;
		prevY = y;
		frame.enableCropOnly();
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startX = e.getX();
		startY = e.getY();
		dragging = true;
		frame.enableCropOnly();
		repaint();
	}
	

	@Override
	public void mouseReleased(MouseEvent e) {
		if(dragging == false) 
			return;
		dragging = false;
		
		if(isCropped()) {
			frame.enableCropOnly();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public int getStartX() {
		return this.startX;
	}
	
	public int getStartY() {
		return this.startY;
	}
	
	public int getPrevX() {
		return this.prevX;
	}
	
	public int getPrevY() {
		return this.prevY;
	}
	
	public boolean isCropped() {
		boolean hasWidth = startX != prevX;
		boolean hasHeight = startY != prevY;
		return hasWidth && hasHeight;
	}
	
	// Required by a serializable class (ignore for now)
	private static final long serialVersionUID = 1L;
}
