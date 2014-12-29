//CS 401
//John McQuown
//November 26, 2014
//Class for the Cloud Shape

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Cloud implements MyShape {
	private Ellipse2D ellipse1, ellipse2, ellipse3, ellipse4;
	private Rectangle2D perimeter;
	private int X, Y;
	private int size;
	private boolean isHighlighted;
	
	public Cloud (int startX, int startY, int sz)
	{
		X = startX;
		Y = startY;
		size = sz;
		perimeter = new Rectangle2D.Double(X-size,Y+size/20,size*2,(size*5)/3);
		ellipse1 = new Ellipse2D.Double(X,Y,size,size);
		ellipse2 = new Ellipse2D.Double(X-size/2,Y+size/6,size,size);
		ellipse3 = new Ellipse2D.Double(X-size/5,Y+size/2,size,size);
		ellipse4 = new Ellipse2D.Double(X-size,Y+size/2,size,size);
		isHighlighted = false;
	}
	
	public void highlight(boolean b)
	{
		isHighlighted = b;
	}
	
	public void draw(Graphics2D g)
	{
		g.setColor(Color.white);
		if (isHighlighted)
			g.draw(ellipse1);
		else
			g.fill(ellipse1);
		g.setColor(Color.white);
		if (isHighlighted)
			g.draw(ellipse2);
		else
			g.fill(ellipse2);
		g.setColor(Color.white);
		if (isHighlighted)
			g.draw(ellipse3);
		else
			g.fill(ellipse3);
		g.setColor(Color.white);
		if (isHighlighted)
			g.draw(ellipse4);
		else
			g.fill(ellipse4);
	}
	
	public void move(int x, int y)
	{
		X = x;
		Y = y;
		ellipse1.setFrame(X,Y,size,size);
		ellipse2.setFrame(X-size/2,Y+size/6,size,size);
		ellipse3.setFrame(X-size/5,Y+size/2,size,size);
		ellipse4.setFrame(X-size,Y+size/2,size,size);
		perimeter.setFrame(X-size,Y+size/20,size*2,(size*5)/3);
	}
	
	public void resize(int newsize)
	{
		size = newsize;
		move(X,Y);
	}
	
	public boolean contains(double x, double y)
	{
		return perimeter.contains(x,y);
	}
	
	public String saveData()
	{
		return ("Cloud:" + X + ":" + Y + ":" + size);
	}
}
