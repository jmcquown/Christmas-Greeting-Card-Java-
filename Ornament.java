//CS 401
//John McQuown
//November 26, 2014
//Class for the Ornament Shape

import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Line2D.Double;
import java.util.*;

class Ornament implements MyShape {
	private Ellipse2D ellipse1;
	private Rectangle2D perimeter;
	private Line2D line1, line2, line3;
	private int X, Y;
	private int size;
	private boolean isHighlighted;
	
	public Ornament (int startX, int startY, int sz)
	{
		X = startX;
		Y = startY;
		size = sz;
		perimeter = new Rectangle2D.Double(X,Y,size,size);
		ellipse1 = new Ellipse2D.Double(X,Y,size,size);
		line1 = new Line2D.Double(X,Y+size/2,X+size,Y+size/2);
		line2 = new Line2D.Double(X+5,Y+size/4,X+size-5,Y+size/4);
		line3 = new Line2D.Double(X+5,Y+3*size/4,X+size-5,Y+3*size/4);
		isHighlighted = false;
	}
	
	public void highlight(boolean b)
	{
		isHighlighted = b;
	}
	
	public void draw(Graphics2D g)
	{
		g.setColor(Color.red);
		if (isHighlighted)
			g.draw(ellipse1);
		else
			g.fill(ellipse1);
		g.setColor(new Color(10,100,10));
		g.draw(line1);
		g.setColor(Color.white);
		g.draw(line2);
		g.draw(line3);
	}
	
	public void move(int x, int y)
	{
		X = x;
		Y = y;
		ellipse1.setFrame(X,Y,size,size);
		perimeter.setFrame(X,Y,size,size);
		line1.setLine(X,Y+size/2,X+size,Y+size/2);
		line2.setLine(X+5,Y+size/4,X+size-5,Y+size/4);
		line3.setLine(X+5,Y+3*size/4,X+size-5,Y+3*size/4);
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
		return ("Ornament:" + X + ":" + Y + ":" + size);
	}
}
