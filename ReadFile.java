package readfilePractice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ReadFile {

	public static void main(String[] args) throws IOException {
			boolean shutdown = false;
		
		
			ServerSocket ss = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1")); 
			Socket socket = ss.accept();
			System.out.println("Connection from: " + socket);
			
			OutputStream outputStream = socket.getOutputStream();
	        	ObjectOutputStream dataOutputStream = new ObjectOutputStream(outputStream);
			
			InputStream inputStream = socket.getInputStream();
			DataInputStream dataInputStream = new DataInputStream(inputStream);			
			String message = dataInputStream.readUTF();
	//	        System.out.println("The message from client was: " + message);
	        	        
	        	writeToClient(dataOutputStream, message);
	        
			System.out.println("Closing sockets.");	
			outputStream.close();
			inputStream.close();
			ss.close();
			socket.close();
	         
	
}
	
		static public void writeToClient(ObjectOutputStream outputStream, String message) throws IOException {
			read r = new read("chr1.fa");
			outputStream.writeObject((r.Search(message)));
			
			System.out.println("Writing to client...");
	        	outputStream.flush();	         
		}
		
}


class read {
	String file;
	read(String file){
		this.file = file;
	}
	
	public List<Integer> Search(String gene) {
		List <Integer> list = Collections.synchronizedList(new ArrayList<Integer>());
		
		try {
			
			BufferedReader reader =new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			sb.append(gene);
			String str;
			
			while((str = reader.readLine()) != null) {
				sb.append(str.toUpperCase());				
			}
			
			int index = 0;
			while(index >= 0) {
				index = sb.indexOf(gene, index + 1);
				if(index >= 0) {
					list.add(index);
				}
			}
					
//			System.out.println("Index list: " + list);
//			System.out.println("Size: " + list.size());
			
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}

