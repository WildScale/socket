
package exercice2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.net.*;

public class ServeurThread {
	public static void main(String[] args) throws IOException {
		try {
			ServerSocket server = new ServerSocket(8888);
			int counter = 0;
			System.out.println("Serveur en marche");
			while (true) {
				counter++;
				Socket serverClient = server.accept();
				System.out.println("Client Num: " + counter + " connecté");
				ServerClientThread sct = new ServerClientThread(serverClient, counter);
				sct.start();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

class ServerClientThread extends Thread {
	Socket serverClient;
	int clientNo;

	ServerClientThread(Socket inSocket, int counter) {
		serverClient = inSocket;
		clientNo = counter;
	}

	public void run() {
		try {
			DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
			DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
			String commande = "", serverMessage = "";
			String lignes = "";
			BufferedReader laCommande;
			while (!commande.equals("exit")) {
				commande = inStream.readUTF();
				Process p = Runtime.getRuntime().exec(commande);
				p.waitFor();
				laCommande = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((commande = laCommande.readLine()) != null) {
					lignes += commande + "\n";
				}

				serverMessage = lignes;
				outStream.writeUTF(serverMessage);
				outStream.flush();
				commande = "";
				lignes = "";
				serverMessage = "";
			}
			inStream.close();
			outStream.close();
			serverClient.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out.println("le Client " + clientNo + " se déconnecte ");
		}
	}
}