// CS 401 Fall 2014
// Cabin class that uses both polygons and 2-D lines
//Used Snowflake.java as a reference and modified various methods and variables

import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Line2D.Double;
import java.util.*;

class Cabin implements MyShape
{
	//Create three polygons and three 2-D lines
	//Each polygon varying in size and color in order to represent what they are called
	//Three 2-D lines that will go across the house in order to make it look wooden
	private Polygon house;
	private Polygon chimney;
	private Polygon door;
	private Line2D line1, line2, line3, line4;

	// X, Y and size instance variables
	private int X, Y;
	private int size;

	private boolean isHighlighted;
	
	//Creates a new Cabin object
	public Cabin(int startX, int startY, int sz)
	{
		X = startX;
		Y = startY;
		size = sz;
		
		//Set up the lines in the constructor
		//Mostly because I saw that Dr. Ramirez did it here in Snowflake.java
		
		setUp();
	}
	
	//Creates the various parts of the cabin
	//The points were found using trial and error
	//It took a lot of time to make it look right
	private void setUp()
	{
		house = new Polygon();
		house.addPoint(X,Y);
		house.addPoint(X+3*size/2,Y);
		house.addPoint(X+3*size/2,Y-(5+size));
		house.addPoint(X,Y-(5+size));
		chimney = new Polygon();
		chimney.addPoint(X+size,Y-(size-20));
		chimney.addPoint(X+3*size/2,Y-(size-20));
		chimney.addPoint(X+3*size/2,Y-(20+size));
		chimney.addPoint(X+size,Y-(20+size));
		door = new Polygon();
		door.addPoint(X+3*size/6,Y);
		door.addPoint(X+5*size/6,Y);
		door.addPoint(X+5*size/6,Y-size/2);
		door.addPoint(X+3*size/6,Y-size/2);
		
		line1 = new Line2D.Double(X,Y-size/4,X+3*size/2,Y-size/4);
		line2 = new Line2D.Double(X,Y-size/2,X+3*size/2,Y-size/2);
		line3 = new Line2D.Double(X,Y-2*size/3,X+3*size/2,Y-2*size/3);
		line4 = new Line2D.Double(X,Y-(size-7),X+size,Y-(size-7));
	}

	public void highlight(boolean b)
	{
		isHighlighted = b;
	}

	// The Polygon class can also be drawn with a predefined method in
	// the Graphics2D class.  There are two versions of this method:
	// 1) draw() which only draws the outline of the shape
	// 2) fill() which draws a solid shape
	// In this class the draw() method is used when the object is
	// highlighted.
	// The colors chosen are RGB colors I looked up on the Web.  Take a
	// look and use colors you like for your shapes.
	public void draw(Graphics2D g)
	{
		g.setColor(new Color(61,43,31));
		if (isHighlighted)
			g.draw(house);
		else
			g.fill(house);
		g.setColor(new Color(100,10,10));
		if (isHighlighted)
			g.draw(chimney);
		else
			g.fill(chimney);
		g.setColor(new Color(1,1,1));
		if (isHighlighted)
			g.draw(door);
		else
			g.fill(door);
		g.draw(line1);
		g.draw(line2);
		g.draw(line3);
		g.draw(line4);
	}

	// Looking at the API, we see that Polygon has a translate() method
	// which can be useful to us.  All we have to do is calculate the
	// difference of the new (x,y) and the old (X,Y) and then call
	// translate() for both parts of the tree.
	public void move(int x, int y)
	{
		int deltaX = x - X;
		int deltaY = y - Y;
		house.translate(deltaX, deltaY);
		chimney.translate(deltaX, deltaY);
		door.translate(deltaX, deltaY);
		line1.setLine(X,Y-size/4,X+3*size/2,Y-size/4);
		line2.setLine(X,Y-size/2,X+3*size/2,Y-size/2);
		line3.setLine(X,Y-2*size/3,X+3*size/2,Y-2*size/3);
		line4.setLine(X,Y-(size-7),X+size,Y-(size-7));
		X = x;
		Y = y;
	}

	// Polygon also has a contains() method, so this method is also
	// simple
	public boolean contains(double x, double y)
	{
		if (house.contains(x,y))
			return true;
		if (chimney.contains(x,y))
			return true;
		if (door.contains(x,y))
			return true;
		return false;
	}

	// The move() method for the Polygons that are in Tree are not
	// reconfigured like in Snowflake, so we can't use the trick used
	// there.  Instead, we just change the size and call setUp() to
	// regenerate all of the objects.
	public void resize(int newsize)
	{
		size = newsize;
		setUp();
	}

	// Note again the format
	public String saveData()
	{
		return ("Cabin:" + X + ":" + Y + ":" + size);
	}
}