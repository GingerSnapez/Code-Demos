package edu.du.cs.fletchg.program2;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Painter extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

	private Color color; // Stores current color selected
	private String prim; // Stores current primitive type
	private Point start; // start point for drawing
	private Point end; // end point for drawing
	private PaintingPanel paintPan; // the painting panel
	private JTextArea textArea; // displays text history
	private Socket sock;
	private ObjectOutputStream oos;
	private String name; // name entered from user
	private String msg; // current message
	private ArrayList<String> messages; // stores message history
	private boolean connected; // is connected to hub
	private PaintingPrimitive object; // current illustrations

	public Painter() {
		messages = new ArrayList<String>();
		name = JOptionPane.showInputDialog("Enter your name");
		// messages.add("----Start of Chat Session---- (\"!ls\" for a list of
		// commands)");

		// Open the socket
		try {
			connected = true;
			sock = new Socket("localhost", 6001);
			oos = new ObjectOutputStream(sock.getOutputStream());
			// confirms it is connected
		} catch (UnknownHostException e) {
			// System.out.println("Attempted to connect to host but none was available");
		} catch (IOException e) {
			System.out.println("Attempted to connect to host but none was available");
			connected = false;
		}

		// Start the Panel
		color = Color.RED;
		prim = "line";
		start = new Point();
		end = new Point();
		object = new Line(color, start, end);

		setSize(500, 600);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel holder = new JPanel();
		holder.setBorder(new TitledBorder(name + "'s Painter"));
		holder.setLayout(new BorderLayout());

		// Create the paints
		JPanel colorPanel = new JPanel();
		colorPanel.setLayout(new GridLayout(3, 1)); // 3 by 1

		// red paint button
		JButton redPaint = new JButton();
		redPaint.setBackground(Color.RED);
		redPaint.setOpaque(true);
		redPaint.setBorderPainted(false);
		redPaint.addActionListener(this);
		redPaint.setActionCommand("red");
		colorPanel.add(redPaint); // Added in next open cell in the grid

		// green paint button
		JButton greenPaint = new JButton();
		greenPaint.setBackground(Color.GREEN);
		greenPaint.setOpaque(true);
		greenPaint.setBorderPainted(false);
		greenPaint.addActionListener(this);
		greenPaint.setActionCommand("green");
		colorPanel.add(greenPaint);

		// blue paint button
		JButton bluePaint = new JButton();
		bluePaint.setBackground(Color.BLUE);
		bluePaint.setOpaque(true);
		bluePaint.setBorderPainted(false);
		bluePaint.addActionListener(this);
		bluePaint.setActionCommand("blue");
		colorPanel.add(bluePaint);

		// add the panels to the overall panel, holder
		// note that holder's layout should be set to BorderLayout
		holder.add(colorPanel, BorderLayout.WEST);

		// Shape selector buttons
		JPanel shapePanel = new JPanel();
		shapePanel.setLayout(new GridLayout(1, 2)); // 3 by 1

		// Circle Button
		JButton circle = new JButton("Circle");
		circle.addActionListener(this);
		circle.setActionCommand("circle");
		shapePanel.add(circle);

		// Line Button
		JButton line = new JButton("Line");
		line.addActionListener(this);
		line.setActionCommand("line");
		shapePanel.add(line);

		holder.add(shapePanel, BorderLayout.NORTH);

		// after finishing the PaintingPanel class (described later) add a
		// new object of this class as the CENTER panel
		paintPan = new PaintingPanel();
		paintPan.setBackground(Color.WHITE); // HOW DO I DO THIS IN THE CONSTRUCTOR
		paintPan.addMouseListener(this);
		paintPan.addMouseMotionListener(this);
		holder.add(paintPan, BorderLayout.CENTER);

		// chat panel
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout());

		// chat components
		textArea = new JTextArea(5, 40);
		JTextField textField = new JTextField();
		JButton enter = new JButton("Enter");

		// text area settings
		textArea.setEditable(false);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setFont(new Font("Serif", Font.PLAIN, 16));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		// put text area in a scroll pane
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// text panel layout
		textPanel.add(scrollPane, BorderLayout.NORTH);
		textPanel.add(textField, BorderLayout.CENTER);
		textPanel.add(enter, BorderLayout.EAST);

		// sending event listener
		enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				msg = textField.getText();
				messages.add(name + ": " + msg);

				// commands in chat
				if (msg.contains("!ls")) { // list commands
					messages.add("Commands:\n!clsp : disconnects the painter \n!clsh : closes the hub");
				} else if (msg.contains("!clsp") && connected) { // disconnects painter from hub if connected
					messages.add("System: You have been disconnected, have a nice day!");
					writeToHub("System: " + name + " has disconnected");
					connected = false;
					try {
						sock.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (msg.contains("!clsh") && connected) { // closes hub if connected
					messages.add("System: Closing hub, have a nice day!");
					writeToHub("System: Closing hub, have a nice day! (" + name
							+ ")\n System: You have been disconnected, have a nice day!");
					writeToHub(msg);
					addMessage("System: You have been disconnected, have a nice day!");
				} else if (connected) { // writes to hub if connected
					writeToHub(name + ": " + msg);
				} else if ((msg.contains("!clsp") || msg.contains("!clsh")) && !connected) { //catches unconnected painters commands
					addMessage("System: You are not connected to the hub, commands are not available");
				}

				//wipe field and update chat
				textField.setText("");
				updateChat();
			}
		});

		// add chat
		holder.add(textPanel, BorderLayout.SOUTH);

		// Lastly, connect the holder to the JFrame
		setContentPane(holder);

		// And make it visible to layout all the components on the screen
		setVisible(true);

		// prime chat
		updateChat();

		// Assign socket to a thread
		Thread th = new Thread(new PainterThread(sock, paintPan, this));
		th.start();

		writeToHub("System: " + name + " has connected");
	}

	// Make the object in mousePressed() and update it when mosueDragged() and use
	// the repaint()

	// write new object to hub
	public void writeToHub(Object o) {
		if (connected) {
			try {
				oos.writeObject(o);
				System.out.println("Writing Object");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	// clears and rewrites local chat stream
	public void updateChat() {
		textArea.setText("----Start of Chat Session----  (\"!ls\" for a list of commands)\n");
		for (String message : messages) {
			textArea.append(message + "\n");
		}
	}

	// adds a message and calls update
	public void addMessage(String s) {
		messages.add(s);
		updateChat();
	}

	// get name
	public String getName() {
		return this.name;
	}

	// returns connected boolean
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void mouseDragged(MouseEvent m) {

		// update current position and repaint
		end.x = paintPan.getMousePosition().x;
		end.y = paintPan.getMousePosition().y;
		object.setEnd(end);
		paintPan.repaint();
	}

	@Override
	public void mousePressed(MouseEvent m) {

		// start
		start.x = paintPan.getMousePosition().x;
		start.y = paintPan.getMousePosition().y;
		object.setStart(start);

		// end
		end.x = paintPan.getMousePosition().x;
		end.y = paintPan.getMousePosition().y;
		object.setEnd(end);

		// draws based on prim
		if (prim.equals("circle")) {
			object = new Circle(color, new Point(start), new Point(end));
			object.draw(paintPan.getGraphics());
		} else if (prim.equals("line")) {
			object = new Line(color, new Point(start), new Point(end));
			object.draw(paintPan.getGraphics());
		}

		paintPan.addPrimitive(object);
	}

	@Override
	public void mouseReleased(MouseEvent m) {

		// set end
		end.x = paintPan.getMousePosition().x;
		end.y = paintPan.getMousePosition().y;

		object.setEnd(new Point(end));

		// make deep copy of current object for storage
		switch (prim) {

		case "circle":
			object = new Circle(color, new Point(start), new Point(end));
			break;
		case "line":
			object = new Line(color, new Point(start), new Point(end));
			break;
		}

		paintPan.repaint();
		writeToHub(object);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String temp = e.getActionCommand();

		// colors and prims
		switch (temp) {

		case "red":
			color = Color.RED;
			break;
		case "blue":
			color = Color.BLUE;
			break;
		case "green":
			color = Color.GREEN;
			break;
		case "circle":
			prim = "circle";
			break;
		case "line":
			prim = "line";
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String args[]) {
		// runs painter
		new Painter();
	}
}