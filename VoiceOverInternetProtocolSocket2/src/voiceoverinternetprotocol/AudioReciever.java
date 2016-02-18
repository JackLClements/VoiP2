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
import java.util.Vector;
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
		int burst = 0;
		while (running) {
			try {
				byte[] recieve = new byte[513];
				DatagramPacket packet = new DatagramPacket(recieve, 0, 513);

				//Add data to packet
				receiving_socket.receive(packet);

				//Unshuffle packet
				byte header = recieve[0];
				byte[] payload = new byte[512];
				for (int i = 1; i < recieve.length; i++) {
					payload[i - 1] = recieve[i];
				}

				if (burst < 16) {
					burst++;
				} else {
					//play audio
				}

				player.playBlock(recieve);

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
