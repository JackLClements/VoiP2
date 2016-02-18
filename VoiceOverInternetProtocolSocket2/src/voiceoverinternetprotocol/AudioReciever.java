/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voiceoverinternetprotocol;

import CMPC3M06.AudioPlayer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Iterator;
import javax.sound.sampled.LineUnavailableException;
import uk.ac.uea.cmp.voip.DatagramSocket2;

/**
 *
 * @author dha13jyu
 */
public class AudioReciever implements Runnable {

	static DatagramSocket2 receiving_socket;
	AudioPlayer player;

	public AudioReciever() throws LineUnavailableException {
		player = new AudioPlayer();
	}

	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	public void run() {

		//***************************************************
		//Port to open socket on
		int PORT = 8000;
        //***************************************************

		//***************************************************
		//Open a socket to receive from on port PORT
		//DatagramSocket receiving_socket;
		try {
			receiving_socket = new DatagramSocket2(PORT);
		} catch (SocketException e) {
			System.out.println("ERROR: Audio Receiver: Could not open UDP socket to receive from.");
			e.printStackTrace();
			System.exit(0);
		}
        //***************************************************

		//***************************************************
		//Main loop.
		boolean running = true;

		while (running) {

			try {
				//Receive a DatagramPacket (note that the string cant be more than 80 chars)
				byte[] buffer = new byte[512];
				DatagramPacket packet = new DatagramPacket(buffer, 0, 512);

				receiving_socket.receive(packet);
				
				//Get a string from the byte buffer
				player.playBlock(buffer);
				//String str = new String(buffer);
				
				//Display it
				//Iterator<byte[]> voiceItr = voiceVector.iterator();
				//while (voiceItr.hasNext()) {
				//	player.playBlock(voiceItr.next());
				//}
			} catch (IOException e) {
				System.out.println("ERROR: Audio Receiver: Some random IO error occured!");
				e.printStackTrace();
			}
		}
		//Close the socket
		player.close();
		receiving_socket.close();
		//***************************************************
	}
}
