package edu.du.cs.fletchg.program2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class HubThread implements Runnable {

	private Socket sock;
	private ArrayList<Object> recorded;
	private Hub hub;
	private ObjectOutputStream oos;

	public HubThread(Socket sock, ArrayList<Object> recorded, Hub hub, ObjectOutputStream oos) {

		this.sock = sock;
		this.recorded = recorded;
		this.hub = hub;
		this.oos = oos;
	}

	public void run() {

		try {

			System.out.println("New Painter Thread started");
			ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());

			while (hub.getGo()) {

				System.out.println("Listening...");

				// Read new prim
				Object obj = ois.readObject();
				System.out.println("Object Read");

				// checks for closing condition
				if ((obj.getClass() == String.class) && (((String) obj).contains("!clsh"))) {
					System.out.println("Hub Thread Closing");
					hub.setGo(false);
					hub.close();
				} else {

					// Add to list
					recorded.add(obj);

					// Push to other painters
					hub.update(oos, obj);
				}
			}

		} catch (IOException e) {
			System.out.println("Hub-painter Thread Closed");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
