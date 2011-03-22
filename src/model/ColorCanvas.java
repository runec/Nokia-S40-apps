package model;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

class ColorCanvas extends Canvas {
	
	// The currently selected component for change (0,1 or 2).
	private int m_selectedComponent = 2;
	
	// Additive or Subtractive color mix.
	private int m_colorMixType = 0;
	
	// Color components
	private int m_colorComponents[] = {255,255,255};
	
	public ColorCanvas() {
		super();
	}
	
	protected void showNotify(){
		m_colorMixType = Settings.getInstance().getColorMixType();
		setFullScreenMode(true);
	}

	public void paint(Graphics g) {
		
		// draw the arcs with colors
		
		int c1,c2,c3;
		
		int cx = getWidth()>>1;
		int cy = getHeight()>>1;
		
		int r1 = (cx<cy?(cx-25):(cy-25));
		int r1small = r1-(r1>>2);
		
		int r2 = r1>>1;
		int r3 = r2-6;
		
		// Clear background
		if(0 == m_colorMixType) {
			// Additive (RGB)
			g.setColor(0,0,0);
		} else {
			// Subtractive (CMY)
			g.setColor(255,255,255);
		}
		g.fillRect(0,0,getWidth(),getHeight());

		for(int i=0;i<6;i++){
			// component 1 (Red or Cyan) - [-60 to 120 degrees in circle]
			if( 0==i || 1==i || 5==i ) c1 = m_colorComponents[0];
			else c1 = 0;
			// component 2 (Green or Magenta) - [60 to 240 degrees in circle]
			if( 1==i || 2==i || 3==i ) c2 = m_colorComponents[1];
			else c2 = 0;
			// component 3 (Green or Magenta) - [60 to 240 degrees in circle]
			if( 3==i || 4==i || 5==i ) c3 = m_colorComponents[2];
			else c3 = 0;
			
			// Set color
			if(0 == m_colorMixType) {
				// Additive (RGB)
				g.setColor(c1, c2, c3);
			} else {
				// Subtractive (CMY)
				g.setColor(255-c1, 255-c2, 255-c3);
			}
			
			// Fill arc
			// Note:  Every other should be a bit smaller to indicate
			//        that it's a mixed color (= not primary).
			if( (i&1) == 0 ) {
				g.fillArc(cx-r1,cy-r1,r1<<1,r1<<1,(i*60)+8, 44);
			} else {
				g.fillArc(cx-r1small,cy-r1small,r1small<<1,r1small<<1,(i*60)+2, 56);

			}
		}
		
		// Draw the center color circle
		if(0 == m_colorMixType) {
			// Make a border
			g.setColor(0,0,0);
			g.fillArc(cx-r2,cy-r2,r2<<1,r2<<1, 0, 360);				
			// Additive (RGB)
			g.setColor(m_colorComponents[0], m_colorComponents[1], m_colorComponents[2]);
			g.fillArc(cx-r3,cy-r3,r3<<1,r3<<1, 0, 360);
		} else {
			// Make a border
			g.setColor(255,255,255);
			g.fillArc(cx-r2,cy-r2,r2<<1,r2<<1, 0, 360);				
			// Subtractive (CMY)
			g.setColor(255-m_colorComponents[0], 255-m_colorComponents[1], 255-m_colorComponents[2]);
			g.fillArc(cx-r3,cy-r3,r3<<1,r3<<1, 0, 360);
		}
		
		// Draw a dot in the color arc currently selected.
		int dx = cx + (int)(r1small * Math.cos( Math.toRadians(30 + 120 * m_selectedComponent) ));
		int dy = cy - (int)(r1small * Math.sin( Math.toRadians(30 + 120 * m_selectedComponent) ));
		g.setColor(0,0,0);
		g.fillArc(dx-5,dy-5,11,11,0,360);
		g.setColor(255,255,255);
		g.fillArc(dx-3,dy-3,7,7,0,360);
		
		// Draw control input indicators.
		
		// Make sure the color is inverse of background
		if(0 == m_colorMixType) {
			// Additive (RGB)
			g.setColor(255,255,255);
		} else {
			// Subtractive (CMY)
			g.setColor(0,0,0);
		}
		
		int maxx = getWidth()-1;
		int maxy = getHeight()-1;
		
		// Draw arrows in the sides
		g.fillTriangle(0,cy,15,cy-5,15,cy+5);
		g.fillTriangle(maxx,cy,maxx-15,cy-5,maxx-15,cy+5);
		
		// Draw +/- in top/bottom
		g.fillRect(cx-8,7,17,3);
		g.fillRect(cx-1,0,3,17);

		g.fillRect(cx-8,maxy-9,17,3);
		
		// Top right percentage
		s1 = m_colorComponents[0] * 100 / 255 + "%";
		g.drawString(s1, getWidth()-5, 60, Graphics.TOP|Graphics.RIGHT);
		
		// Top left percentage
		s2 = m_colorComponents[1] * 100 / 255 + "%";
		g.drawString(s2, 5, 60, Graphics.TOP|Graphics.LEFT);
		
		// Bottom percentage
		s3 = m_colorComponents[2] * 100 / 255 + "%";
		g.drawString(s3, getWidth()/2, getHeight()-40 ,Graphics.BOTTOM|Graphics.HCENTER);
	}

	private void selectPreviousColor(){
		m_selectedComponent--;
		
		// Remember to stay within [0;2]
		if(m_selectedComponent<0)m_selectedComponent=2;		
	}
	
	private void selectNextColor(){
		m_selectedComponent++;
		
		// Remember to stay within [0;2]
		if(m_selectedComponent>2)m_selectedComponent=0;
	}
	
	/*
	 * Add a value to the current color.
	 * If the color value exceeds the boundaries, the color value will
	 */
	private void addToCurrentColor(int value) {
		m_colorComponents[m_selectedComponent] += value;
		
		// Remember to stay within [0;255]
		// Use the bitwise AND operator to limit as 255 (dec) = 0xFF (hex) = 11111111 (binary)
		m_colorComponents[m_selectedComponent] &= 255;
/*		while(m_colorComponents[m_selectedComponent]>255)
			m_colorComponents[m_selectedComponent] -= 256;		
		while(m_colorComponents[m_selectedComponent]<)
			m_colorComponents[m_selectedComponent] -= 256; */		
	}
	
	// Input events are handled here

	// Handle key events
	private void handleKeyEvent(int ga) {
		if (Canvas.LEFT == ga) {
			selectPreviousColor();
		} else if (Canvas.RIGHT == ga) {
			selectNextColor();
		} else if (Canvas.UP == ga) {
			addToCurrentColor(8);
		} else if (Canvas.DOWN == ga) {
			addToCurrentColor(-8);
		}
		
		repaint();
	}
	
	/*
	 * Standard keyPressed handler (called by system)
	 * When pressed, handle event
	 */
	protected void keyPressed(int keyCode){
		handleKeyEvent(getGameAction(keyCode));
	}
	
	/*
	 * Standard keyRepeated handler (called by system)
	 * When holding key (repeat), handle event
	 */
	protected void keyRepeated(int keyCode){
		handleKeyEvent(getGameAction(keyCode));
	}
	
	// Handle touch events for devices with a touch screen
	protected void pointerPressed(int x, int y) {
		super.pointerPressed(x, y);
		
		// NOTE:  This is a very quick method to detect touch events in 
		//        the border of the screen and translate to key game action events.
		if( x>getWidth()-50 )
			handleKeyEvent(Canvas.RIGHT);
		else if( x<50 )
			handleKeyEvent(Canvas.LEFT);
		else if( y>getHeight()-50 )
			handleKeyEvent(Canvas.DOWN);
		else if( y<50 )
			handleKeyEvent(Canvas.UP);
	}
/*
	protected void pointerReleased(int x, int y) {
		// TODO Auto-generated method stub
		super.pointerReleased(x, y);
		m_status = "Pointer Released";
		m_x = x;
		m_y = y;
		repaint();
	}

	protected void pointerDragged(int x, int y) {
		// TODO Auto-generated method stub
		super.pointerDragged(x, y);
		m_status = "Pointer Dragged";
		m_x = x;
		m_y = y;
		repaint();
	}
*/
}