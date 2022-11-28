package edu.du.cs.fletchg.program2;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class Hub {

	private ArrayList<ObjectOutputStream> outStreams; // stores outStream
	private boolean go;	// continues the hub running
	private ServerSocket ss;	

	public static void main(String[] args) {

		new Hub();
	}

	public Hub() {

		try {

			System.out.println("Hub Started");
			go = true;

			ss = new ServerSocket(6001);

			// hold all sockets accepted
			outStreams = new ArrayList<ObjectOutputStream>();
			// This will manage all painter threads
			ArrayList<Thread> painters = new ArrayList<Thread>();
			// This will hold all illustrations
			ArrayList<Object> recorded = new ArrayList<Object>();
			
			Thread th = null;

			// Change this while loop to close with console input
			while (go) {

				// check for new connections
				Socket sock = ss.accept();
				System.out.println("New Painter Added");

				// make output stream and add to array of outputs
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				outStreams.add(oos);

				// pass every new socket all existing primitives
				for (Object obj : recorded)
					oos.writeObject(obj);

				// fire off painter thread for new socket
				th = new Thread(new HubThread(sock, recorded, this, oos));
				th.start();
				painters.add(th);
			}

			System.out.println("Hub Closing");
			ss.close();
		} catch (IOException e) {
			System.out.println("Hub Closed");
		}
	}
	
	// These get and sets are for HubThread
	public boolean getGo() {
		return go;
	}
	
	public void setGo(boolean b) {
		go = b;
	}


	// Update to be passed the current object output stream so you don't send
	// redundant information
	public synchronized void update(ObjectOutputStream oos, Object obj) {
		
		ObjectOutputStream remove = null;
		boolean found = false;

		for (ObjectOutputStream stream : outStreams) {
			if (stream != oos) {
				System.out.println(obj);
				try {
					stream.writeObject(obj);
				} catch (IOException e) {
					remove = stream;
					found = true;
				}
			}
		}
		
		if (found) {
			outStreams.remove(remove);
			System.out.println("Removed Obsolete Stream");
		}
		
		/*
		Iterator<ObjectOutputStream> iter = outStreams.iterator();

		while (iter.hasNext()) {
			if (iter.next() != oos) {
				System.out.println(obj);
				try {
					iter.next().writeObject(obj);
				} catch (IOException e) {
					
					outStreams.remove(iter.next());
				}
			}
		}
		*/
	}
	
	public synchronized void close() {
		try {
			ss.close();
			for (ObjectOutputStream stream : outStreams) {
				stream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
