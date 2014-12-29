// CS 401 Fall 2014
// Assignment 5 Help Program
// Read this program carefully and run it as well.  This shows you how to
// do a number of things that are required for Assignment 5.  You may even
// use this as a starting point to build your completed assignment.  Note
// however that the credit will be given for what you ADD to this program,
// not for what has already been implemented by me!  Also note that you are
// not required to use this program at all, as long as you implement the
// assignment correctly! 

// For additional help, see Sections 13.8, 14.5-14.6 in the text

import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.awt.print.*;
import java.awt.image.*;

import javax.imageio.*;

// Create enum types that will be useful in the program
enum Figures {TREE,SNOWFLAKE,GREETING,CABIN,CLOUD,ORNAMENT};
enum Mode {NONE,DRAW,SELECTED,MOVING};

// Code extracted from Oracle Java Example programs.  See link below for full code:
// http://docs.oracle.com/javase/tutorial/2d/printing/examples/PrintUIWindow.java
class thePrintPanel implements Printable
{
	JPanel panelToPrint;
	
	public int print(Graphics g, PageFormat pf, int page) throws
                                                        PrinterException
    {
        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform t = new AffineTransform();
        t.scale(0.9, 0.9);
        g2d.transform(t);
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Now print the window and its visible contents */
        panelToPrint.printAll(g);

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }
    
    public thePrintPanel(JPanel p)
    {
    	panelToPrint = p;
    }
}

public class Assig5
{	
	private ShapePanel drawPanel;
	private JPanel buttonPanel;
	private JButton makeShape;
	private JRadioButton makeTree, makeFlake, makeGreet, makeCabin, makeCloud, makeOrnament;
	private ButtonGroup shapeGroup;
	private Figures currShape, currCutShape;
	private JLabel msg;
	private JMenuBar theBar;
	private JMenu fileMenu, editMenu;
	private JMenuItem endProgram, printScene, newMenu, openCard, saveCard, saveAsCard, saveAsJPG;
	private JMenuItem cutShape, copyShape, pasteShape;
	private JPopupMenu popper;
	private JMenuItem delete, resize;
	private JFrame theWindow;
	private int saveCounter = 0, cutCounter = 0, copyCounter = 0;
	private String saveFileName;
	private MyShape shapeCopy;
	
	// This ArrayList is used to store the shapes in the program.
	// It is specified to be of type MyShape, so objects of any class
	// that implements the MyShape interface can be stored in here.
	// See Section 7.13 in your text for more info on ArrayList.
	private ArrayList<MyShape> shapeList;	
	private MyShape newShape;

	public Assig5()
	{
		drawPanel = new ShapePanel(640, 480);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 2));

		makeShape = new JButton("Click to Draw");

		ButtonHandler bhandler = new ButtonHandler();
		makeShape.addActionListener(bhandler);

		buttonPanel.add(makeShape);
		msg = new JLabel("");
		buttonPanel.add(msg);

		makeTree = new JRadioButton("Tree", false);
		makeFlake = new JRadioButton("Snowflake", true);
		makeGreet = new JRadioButton("Greeting", false);
		makeCabin = new JRadioButton("Cabin", false);
		makeCloud = new JRadioButton("Cloud", false);
		makeOrnament = new JRadioButton("Ornament", false);

		RadioHandler rhandler = new RadioHandler();
		makeTree.addItemListener(rhandler);
		makeFlake.addItemListener(rhandler);
		makeGreet.addItemListener(rhandler);
		makeCabin.addItemListener(rhandler);
		makeCloud.addItemListener(rhandler);
		makeOrnament.addItemListener(rhandler);

		buttonPanel.add(makeFlake);
		buttonPanel.add(makeTree);
		buttonPanel.add(makeGreet);
		buttonPanel.add(makeCabin);
		buttonPanel.add(makeCloud);
		buttonPanel.add(makeOrnament);

		// A ButtonGroup allows a set of JRadioButtons to be associated
		// together such that only one can be selected at a time
		shapeGroup = new ButtonGroup();
		shapeGroup.add(makeFlake);
		shapeGroup.add(makeTree);
		shapeGroup.add(makeGreet);
		shapeGroup.add(makeCabin);
		shapeGroup.add(makeCloud);
		shapeGroup.add(makeOrnament);

		currShape = Figures.SNOWFLAKE;
		drawPanel.setMode(Mode.NONE);

		theWindow = new JFrame("CS 401 Assig5 Help Program");
		Container c = theWindow.getContentPane();
		drawPanel.setBackground(Color.lightGray);
		c.add(drawPanel, BorderLayout.NORTH);
		c.add(buttonPanel, BorderLayout.SOUTH);

		// Note how the menu is created.  First we make a JMenuBar, then
		// we put a JMenu in it, then we put JMenuItems in the JMenu.  We
		// can have multiple JMenus if we like.  JMenuItems generate
		// ActionEvents, just like JButtons, so we just have to link an
		// ActionListener to them.
		theBar = new JMenuBar();
		theWindow.setJMenuBar(theBar);
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		theBar.add(fileMenu);
		theBar.add(editMenu);

		newMenu = new JMenuItem("New");
		openCard = new JMenuItem("Open");
		saveCard = new JMenuItem("Save");
		saveAsCard = new JMenuItem("Save As");
		saveAsJPG = new JMenuItem("Save As JPG");
		printScene = new JMenuItem("Print");
		endProgram = new JMenuItem("Exit");
		fileMenu.add(newMenu);
		fileMenu.add(openCard);
		fileMenu.add(saveCard);
		fileMenu.add(saveAsCard);
		fileMenu.add(saveAsJPG);
		fileMenu.add(printScene);
		fileMenu.add(endProgram);
		newMenu.addActionListener(bhandler);
		openCard.addActionListener(bhandler);
		saveCard.addActionListener(bhandler);
		saveAsCard.addActionListener(bhandler);
		saveAsJPG.addActionListener(bhandler);
		printScene.addActionListener(bhandler);
		endProgram.addActionListener(bhandler);
		
		cutShape = new JMenuItem("Cut");
		copyShape = new JMenuItem("Copy");
		pasteShape = new JMenuItem("Paste");
		editMenu.add(cutShape);
		editMenu.add(copyShape);
		editMenu.add(pasteShape);
		cutShape.addActionListener(bhandler);
		copyShape.addActionListener(bhandler);
		pasteShape.addActionListener(bhandler);
		pasteShape.setEnabled(false);

		// JPopupMenu() also holds JMenuItems.  To see how it is actually
		// brought out, see the mouseReleased() method in the ShapePanel class
		// below.
		popper = new JPopupMenu();
		delete = new JMenuItem("Delete");
		resize = new JMenuItem("Resize");
		delete.addActionListener(bhandler);
		resize.addActionListener(bhandler);
		popper.add(resize);
		popper.add(delete);
		
		theWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		theWindow.pack();
		theWindow.setVisible(true);
	}

	public static void main(String [] args)
	{
		new Assig5();
	}

	// See Section 12.4 for information on JRadioButtons.  Note that the
	// text uses ActionListeners to handle JRadioButtons.  Clicking on
	// a JRadioButton actually generates both an ActionEvent and an
	// ItemEvent.  I am using the ItemEvent here.  To handle the event,
	// all I am doing is changing a state variable that will affect the
	// MouseListener in the ShapePanel.
	private class RadioHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getSource() == makeTree)
				currShape = Figures.TREE;
			else if (e.getSource() == makeFlake)
				currShape = Figures.SNOWFLAKE;
			else if (e.getSource() == makeGreet)
				currShape = Figures.GREETING;
			else if(e.getSource() == makeCabin)
				currShape = Figures.CABIN;
			else if(e.getSource() == makeCloud)
				currShape = Figures.CLOUD;
			else if(e.getSource() == makeOrnament)
				currShape = Figures.ORNAMENT;
		}
	}

	// Note how the makeShape button and moveIt menu item are handled 
	// -- we again simply set the state in the panel so that the mouse will 
	// actually do the work.  The state needs to be set back in the mouse
	// listener.
	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == makeShape)
			{
				if (makeShape.getText().equals("Click to Draw"))
				{
					drawPanel.setMode(Mode.DRAW);
					msg.setText("Position new shapes with mouse");
					makeShape.setText("Click to Edit");
				}
				else
				{
					drawPanel.setMode(Mode.NONE);
					msg.setText("Edit shapes with mouse");
					makeShape.setText("Click to Draw");
				}
			}
			
			//Deletes the selected figure
			else if (e.getSource() == delete)
			{
				boolean ans = drawPanel.deleteSelected();
				if (ans)
				{
					msg.setText("Shape deleted");
					drawPanel.repaint();
				}
			}
			//Resizes the selected figure
			else if (e.getSource() == resize)
			{
				msg.setText("Shape resized");
				drawPanel.resizeSelected();
			}
			//Cuts the selected shape
			else if (e.getSource() == cutShape)
			{
				drawPanel.cutSelected();
				msg.setText("Shape has been cut");
				cutShape.setEnabled(false);
				copyShape.setEnabled(false);
				pasteShape.setEnabled(true);
			}
			//Copy the selected shape
			else if (e.getSource() == copyShape)
			{
				drawPanel.copySelected();
				msg.setText("Shape has been copied");
				cutShape.setEnabled(false);
				copyShape.setEnabled(false);
				pasteShape.setEnabled(true);
			}
			//Paste the selected shape
			else if (e.getSource() == pasteShape)
			{
				msg.setText("Shape has been pasted");
				drawPanel.pasteSelected();
				pasteShape.setEnabled(false);
				cutShape.setEnabled(true);
				copyShape.setEnabled(true);
			}
			//If "New" is clicked under the file menu
			else if (e.getSource() == newMenu) 
			{
				//Ask the user if to confirm if they 
				int reply = JOptionPane.showConfirmDialog(null, "Open New Greeting Card?", "New Card Message", 
						+ JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION)
				{
					//Ask the user if they would like to save first
					int reply2 = JOptionPane.showConfirmDialog(null, "Would you like to save first?", "Save Option", 
							+ JOptionPane.YES_NO_OPTION);
					saveCounter = 0;
					//If the user says yes ask for the file name and call the fileSave method
					if (reply2 == JOptionPane.YES_OPTION)
					{
						String saveFileName = JOptionPane.showInputDialog(theWindow, "Please name the file");
						if (saveFileName == null)
						{
							//Do nothing if the user clicks null
						}
						
						else if (saveFileName != null)
						{
							drawPanel.fileSave(saveFileName);
							msg.setText(saveFileName + ".txt has been saved");
							drawPanel.clearShapes();
						}
					}
					
					if (reply2 == JOptionPane.NO_OPTION)
					//Calls the cleapShapes method to make the drawPanel blank
					drawPanel.clearShapes();
				}
					
			}
			//Method that will open an existing text file 
			else if (e.getSource() == openCard) 
			{
				String reply = JOptionPane.showInputDialog(theWindow, "Please enter the name of the file");
				if (reply == null)
				{
					//Do nothing if the use clicks cancel
				}
				else if(reply != null)
					//Here call the open file method
					drawPanel.openFile(reply);
			}
			
			else if (e.getSource() == saveCard)
			{
				if (saveCounter <= 0)
				{
					saveFileName = JOptionPane.showInputDialog(theWindow, "Please name the file");
					if (saveFileName == null)
					{
						
					}
					else if (saveFileName != null)
					{
						//Here call the save file method
						msg.setText(saveFileName + ".txt has been saved");
						drawPanel.fileSave(saveFileName);
						saveCounter++;
					}
				}
				
				else 
				{
					//Here figure out how to save the file
					msg.setText(saveFileName + ".txt has been saved");
					drawPanel.fileSave(saveFileName);
					saveCounter++;
				}
				
			}
			
			else if (e.getSource() == saveAsCard)
			{
				saveFileName = JOptionPane.showInputDialog(theWindow, "Please name the file");
				if (saveFileName == null)
				{
					
				}
				else if (saveFileName != null)
				{
				//Here call the save file method
				msg.setText(saveFileName + ".txt has been saved");
				drawPanel.fileSave(saveFileName);
				}
			}
			
			else if (e.getSource() == saveAsJPG)
			{
				String picture = JOptionPane.showInputDialog(theWindow, "Please name the picture");
				if (picture == null)
				{
					//Do nothing if the user clicks cancel
				}
				else
				drawPanel.pictureSave(picture);
			}
			
			
			else if (e.getSource() == printScene)
			{
				Printable thePPanel = new thePrintPanel(drawPanel); 
			    PrinterJob job = PrinterJob.getPrinterJob();
         		job.setPrintable(thePPanel);
         		boolean ok = job.printDialog();
         		if (ok) 
         		{
             	 	try {
                  		job.print();
             		} 
             		catch (PrinterException ex) {
              		/* The job did not successfully complete */
             		}
             	}
            }
			
			//If exit is selected
			else if (e.getSource() == endProgram)
			{
				
				//First prompted whether or not to quit the program
				int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quit confirmation",  
						+ JOptionPane.YES_NO_OPTION);
				
				//Is yes if selected the user is asked if they want to save first
				if (reply == JOptionPane.YES_OPTION)
				{
					int reply2 = JOptionPane.showConfirmDialog(null, "Do you want to save first?", "Save confirmation",
							+ JOptionPane.YES_NO_OPTION);
					if (reply2 == JOptionPane.YES_OPTION)
					{
						if (saveCounter <= 0)
						{
							saveFileName = JOptionPane.showInputDialog(theWindow, "Please enter the name of the file");
							//Here call the save file method
							drawPanel.fileSave(saveFileName);
							msg.setText(saveFileName + " has been saved");
							
							if (saveFileName == null)
							{
								//Nothing happens if the user clicks cancel
							}
						}
						
						//If the save option has already been used then the same file will be saved again
						else
						{
							JOptionPane.showMessageDialog(theWindow, "File has been saved");
						}
					}
					
					System.exit(0);
				}
			}
		}
	}

	// Here we are extending JPanel.  This way we can use all of the
	// properties of JPanel (including generating MouseEvents) and also
	// add new instance data and methods, as shown below.  Since this is
	// an inner class, it can access instance variables from the A5Help
	// class if necessary.
	private class ShapePanel extends JPanel
	{
		
		// These instance variables are used to store the desired size
		// of the panel.  See method getPreferredSize() below.
		private int prefwid, prefht;

		// Store index of the selected MyShape.  This allows the Shape
		// to be moved and updated.
		private int selindex;

		// Keep track of positions where mouse is moved on the display.
		// This is used by mouse event handlers when moving the shapes.
		private int x1, y1, x2, y2; 
		
		private boolean popped; // has popup menu been activated?

		private Mode mode;   // Keep track of the current Mode

		public ShapePanel (int pwid, int pht)
		{
			shapeList = new ArrayList<MyShape>(); // create empty ArrayList
			selindex = -1;
		
			prefwid = pwid;	// values used by getPreferredSize method below
			prefht = pht;   // (which is called implicitly).  This enables
			// the JPanel to request the room that it needs.
			// However, the JFrame is not required to honor
			// that request.

			setOpaque(true);// Paint all pixels here (See API)

			setBackground(Color.lightGray);

			addMouseListener(new MyMouseListener());
			addMouseMotionListener(new MyMover());
			popped = false;
		}  // end of constructor


		// This class is extending MouseAdapter.  MouseAdapter is a predefined
		// class that implements MouseListener in a trivial way (i.e. none of
		// the methods actually do anything).  Extending MouseAdapter allows
		// a programmer to implement only the MouseListener methods that
		// he/she needs but still satisfy the interface (recall that to
		// implement an interface one must implement ALL of the methods in the
		// interface -- in this case I do not need 3 of the 5 MouseListener
		// methods)
		
		// Note that there is a lot of logic in this class to test for the
		// different state conditions of the program.  The idea is that clicking
		// on and releasing the mouse will do different things at different 
		// times in the program execution.  As an alternative, you could in fact
		// have MouseListeners for different circumstances (ex: for being
		// in DRAW mode vs. being in NONE mode).  In this case, you could
		// actually swap the listeners in and out as appropriate using the 
		// removeMouseListener method in addition to the addMouseListener method
		// for the JPanel.
		
		private class MyMouseListener extends MouseAdapter
		{
			public void mousePressed(MouseEvent e)
			{
				x1 = e.getX();  // store where mouse is when clicked
				y1 = e.getY();

				if (!e.isPopupTrigger() && (mode == Mode.NONE ||
										    mode == Mode.SELECTED)) // left click and
				{												    // either NONE or
					if (selindex >= 0)								// SELECTED mode
					{
						unSelect();			// unselect previous shape
						mode = Mode.NONE;
					}
					selindex = getSelected(x1, y1);  // find shape mouse is
													 // clicked on
					if (selindex >= 0)
					{
						mode = Mode.SELECTED;  	// Now in SELECTED mode for shape
						
					// Check for double-click.  If so, show dialog to update text of
					// the current text shape (will do nothing if shape is not a MyText)
						MyShape curr = shapeList.get(selindex);
						if (curr instanceof MyText && e.getClickCount() == 2)
						{
							String newText = JOptionPane.showInputDialog(theWindow, 
							                  "Enter new text [Cancel for no change]");
							if (newText != null)
								((MyText) curr).setText(newText);
						}
					}
					repaint();	//  Make sure updates are redrawn
				}
				else if (e.isPopupTrigger() && selindex >= 0)  // if button is
				{								               // the popup menu
					popper.show(ShapePanel.this, x1, y1);      // trigger, show
					popped = true;							   // popup menu
				}											  
			}
			
			public void mouseReleased(MouseEvent e)
			{
				if (mode == Mode.DRAW) // in DRAW mode, create the new Shape
				{					   // and add it to the list of Shapes.  In this
									   // case we need to distinguish between the
									   // shapes since we are calling constructors
					if (currShape == Figures.TREE)
					{
						newShape = new Tree(x1,y1,50);
					}
					else if (currShape == Figures.SNOWFLAKE)
					{
						newShape = new Snowflake(x1,y1,10);
					}
					else if (currShape == Figures.GREETING)
					{
						newShape = new Greeting(x1,y1,30);
					}
					else if(currShape == Figures.CABIN)
					{
						newShape = new Cabin(x1,y1,60);
					}
					else if(currShape == Figures.CLOUD)
					{
						newShape = new Cloud(x1,y1,30);
					}
					else if(currShape == Figures.ORNAMENT)
					{
						newShape = new Ornament(x1,y1,25);
					}
					addShape(newShape);
				}
				// In MOVING mode, set mode back to NONE and unselect shape (since 
				// the move is finished when we release the mouse).
				else if (mode == Mode.MOVING) 
				{
					mode = Mode.NONE;
					unSelect();  
					makeShape.setEnabled(true);
					repaint();
				}
				else if (e.isPopupTrigger() && selindex >= 0) // if button is
				{							// the popup menu trigger, show the
					popper.show(ShapePanel.this, x1, y1); // popup menu
				}
				popped = false;  // unset popped since mouse is being released
			}
		}

		// the MouseMotionAdapter has the same idea as the MouseAdapter
		// above, but with only 2 methods.  The method not implemented
		// here is mouseMoved.  The difference between the two is whether or
		// not the mouse is pressed at the time.  Since we require a "click and
		// hold" to move our objects, we are using mouseDragged and not
		// mouseMoved.
		private class MyMover extends MouseMotionAdapter
		{
			public void mouseDragged(MouseEvent e)
			{
				x2 = e.getX();   // store where mouse is now
				y2 = e.getY();

				// Note how easy moving the shapes is, since the "work"
				// is done within the various shape classes.  All we do
				// here is call the appropriate method.  However, we don't 
				// want to accidentally move the selected shape with a right click
				// so we make sure the popup menu has not been activated.
				if ((mode == Mode.SELECTED || mode == Mode.MOVING) && !popped)
				{
					MyShape s = shapeList.get(selindex);
					mode = Mode.MOVING;
					s.move(x2, y2);
				}
				repaint();	// Repaint screen to show updates
			}
		}

		// Check to see if point (x,y) is within any of the shapes.  If
		// so, select that shape and highlight it so user can see.  Again,
		// note that we do not care which shape we are selecting here --
		// it only matters that it has the MyShape interface methods.
		// This version of getSelected() always considers the shapes from
		// beginning to end of the ArrayList.  Thus, if a shape is "under"
		// or "within" a shape that was previously created, it will not
		// be possible to select the "inner" shape.  In your assignment you
		// must redo this method so that it allows all shapes to be selected.
		// Think about how you would do this.
		private int getSelected(double x, double y)
		{                                             
			for (int i = 0; i < shapeList.size(); i++)
			{
				if (shapeList.get(i).contains(x, y))
				{
					shapeList.get(i).highlight(true); 
					return i;
				}
			}
			return -1;
		}

		public void unSelect()
		{
			if (selindex >= 0)
			{
				shapeList.get(selindex).highlight(false);
				selindex = -1;
			}
		}
		//Method that is similar to the delete and resize
		//It removes the shape from the array list but also saves the current shape
		public boolean cutSelected()
		{
			if (selindex >= 0)
			{
				//Set a Figures variable equal to the current shape
				//Which will be used when the paste method is called
				currCutShape = currShape;
				shapeList.remove(selindex);
				drawPanel.repaint();
				selindex = -1;
				return true;
			}
			else
				return false;
		}
		//Method that copies the selected shape
		public boolean copySelected()
		{
			if (selindex >= 0)
			{
				currCutShape = currShape;
				selindex = -1;
				return true;
			}
			else
				return false;
		}
		//Method that pastes the selected shape
		//Basically changed the code from the MouseReleased method and used currCutShape instead of currShape
		public void pasteSelected()
		{
			if (currCutShape == Figures.TREE)
			{
				newShape = new Tree(x1,y1,50);
			}
			else if (currCutShape == Figures.SNOWFLAKE)
			{
				newShape = new Snowflake(x1,y1,10);
			}
			else if (currCutShape == Figures.GREETING)
			{
				newShape = new Greeting(x1,y1,30);
			}
			else if(currCutShape == Figures.CABIN)
			{
				newShape = new Cabin(x1,y1,60);
			}
			else if(currCutShape == Figures.CLOUD)
			{
				newShape = new Cloud(x1,y1,30);
			}
			else if(currCutShape == Figures.ORNAMENT)
			{
				newShape = new Ornament(x1,y1,25);
			}
			addShape(newShape);
			drawPanel.repaint();
		}
		public boolean deleteSelected()
		{
			if (selindex >= 0)
			{
				shapeList.remove(selindex);
				selindex = -1;
				return true;
			}
			else return false;
		}
		//Method to resize the selected shape
		//Took inspiration from the deleteSelected method
		public boolean resizeSelected()
		{
			if (selindex >= 0)
			{
				int newSize = Integer.parseInt(JOptionPane.showInputDialog(theWindow, "Enter the new size"));
				shapeList.get(selindex).resize(newSize);
				drawPanel.repaint();
				selindex = -1;
				return true;
			}
			else 
				return false;
				
		}
		
		//Method that saves the GUI as a JPG
		public void pictureSave(String pic)
		{
			BufferedImage bi = new BufferedImage(drawPanel.getSize().width, drawPanel.getSize().height, BufferedImage.TYPE_INT_RGB); 
			Graphics2D g = bi.createGraphics();
			drawPanel.paint(g);
			g.dispose();
			try
			{
				ImageIO.write(bi,"jpg",new File(pic + ".jpg"));
			}
			catch (Exception e) {}
		}
		
		public void fileSave(String name)
		{
			try
			{
			PrintWriter writer = new PrintWriter(name + ".txt", "UTF-8");
			writer.println(shapeList.size());
			for (int i = 0; i < shapeList.size(); i++)
			{
				writer.println(shapeList.get(i).saveData());
			}
			writer.close();
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(theWindow, "Exception");
			}
		}
		//Method that will delete all items from the arraylist of shapes and repaint the drawPanel
		//Used when the user wants to make a new greeting card
		public void clearShapes()
		{
			shapeList.clear();
			drawPanel.repaint();
		}
		//Method to open a file
		public void openFile(String name)
		{
			try
			{
				Scanner fScan = new Scanner(new FileInputStream(name));
				int number = Integer.parseInt(fScan.nextLine());
				while (fScan.hasNextLine())
				{
					
					String theShape = fScan.nextLine();
					drawPanel.clearShapes();
					
				}
				fScan.close();
			}
			catch (FileNotFoundException e)
			{
				JOptionPane.showMessageDialog(theWindow, "FileNotFoundException");
			}
		}
		public void setMode(Mode newMode)            // set Mode
		{
			mode = newMode;
		}

		private void addShape(MyShape newshape)      // Add shape
		{
			shapeList.add(newshape);
			repaint();	// repaint so we can see new shape
		}

		// Method called implicitly by the JFrame to determine how much
		// space this JPanel wants.  Be sure to include this method in
		// your program so that your panel will be sized correctly.
		public Dimension getPreferredSize()
		{
			return new Dimension(prefwid, prefht);
		}

		// This method enables the shapes to be seen.  Note the parameter,
		// which is implicitly passed.  To draw the shapes, we in turn
		// call the draw() method for each shape.  The real work is in the draw()
		// method for each MyShape
		public void paintComponent (Graphics g)    
		{
			super.paintComponent(g);         // don't forget this line!
			Graphics2D g2d = (Graphics2D) g;
			for (int i = 0; i < shapeList.size(); i++)
			{
				shapeList.get(i).draw(g2d);
			}
		}
	} // end of ShapePanel
}