package edu.du.cs.fletchg.program2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class PainterThread implements Runnable {

	private Socket sock;
	private PaintingPanel paintPan;
	private Painter painter;

	public PainterThread(Socket sock, PaintingPanel paintPan, Painter painter) {

		this.sock = sock;
		this.paintPan = paintPan;
		this.painter = painter;
	}

	public void run() {

		System.out.println("Painter Thread started");

		try {
			ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
			// painter.writeToHub("System: " + painter.getName() + " has connected");

			// Listens for message
			while (painter.isConnected()) {
				Object input = ois.readObject();

				// sorts by object class of object read and stores it
				if (input.getClass() == String.class) {
					System.out.println("Adding message");
					painter.addMessage((String) input);
				} else {
					System.out.println("Adding primitive");
					paintPan.addPrimitive((PaintingPrimitive) input);
				}
			}
		} catch (IOException e) {
			System.out.println("No hub to write to");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("No inputStream to read from in thread");
		}
	}
}
