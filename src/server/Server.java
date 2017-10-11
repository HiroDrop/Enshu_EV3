package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Button;

public class Server {

	public static void main(String[] args) throws IOException, InterruptedException{
		// TODO Auto-generated method stub
		ServerSocket ss = new ServerSocket(6000);
		Socket s = null;
		BufferedReader br;
		PrintStream ps;
		
		while(!Button.ENTER.isDown()) {
			try{
				s = ss.accept();
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				System.out.println("Connected!");
				String msg;
				while ((msg = br.readLine()) != null) {
					System.out.println(msg);
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				try{
					if(s != null) s.close();
				} catch (IOException e){
					System.err.println(e);
				}
				try{
					if(ss != null) ss.close();
				} catch (IOException e){
					System.err.println(e);
				}
				
			}
		}
		ss.close();
	}

}
