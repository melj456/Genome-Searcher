package guiClientTest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class guiClientTest {
	private static void constructGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		myFrame frame = new myFrame();
		frame.setVisible(true);
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				constructGUI();
			}
		});
 }

static class myFrame extends JFrame implements ActionListener {
	static public JLabel label1;
	static public JList jList;
	public JButton button1;
	public JTextField textField;
	
	myFrame() {
		super();
		init();
	}
	
	private void init() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Gene Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JComponent panel = new JPanel();
		panel.setLayout(new FlowLayout());
		
		jList = new JList();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(jList, BorderLayout.CENTER);
		
		button1 = new JButton("Send");
		textField = new JTextField("", 20);
		label1 = new JLabel("Total Items: ");
		panel.add(new JLabel("Enter Gene Sequence Here: "));
		panel.add(textField);
		panel.add(button1);
		button1.addActionListener(this);
		
		this.add(panel, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(label1, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		String userInput;
		JButton source = (JButton) e.getSource();
		String s = source.getText();
		if(s == "Send") {
			userInput = textField.getText();
			try {
				connect(userInput);
			} catch (ClassNotFoundException e1) {			
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	static private void connect(String userInput) throws IOException, ClassNotFoundException {
	    	Socket serverConnection = new Socket(InetAddress.getByName("127.0.0.1"), 8080);
	    	System.out.println("Connected");
		 
	    	OutputStream outputStream = serverConnection.getOutputStream();
	    	DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		 
	    	InputStream inputStream = serverConnection.getInputStream();
	    	ObjectInputStream dataInputStream = new ObjectInputStream(inputStream);	     
	     
	    	writeToServer(dataOutputStream, userInput);
	    	readFromServer(dataInputStream);
	     
	    	System.out.println("Closing sockets.");
	    	outputStream.close();
	    	inputStream.close();
	    	serverConnection.close();
	}
	
	static public void writeToServer(DataOutputStream outputStream, String userInput) throws IOException {
	    	outputStream.writeUTF(userInput.toUpperCase());
	    	outputStream.flush();	
		
		
	}
	
	static public void readFromServer(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
		List <Integer> intList = Collections.synchronizedList(new ArrayList<Integer>());
		intList = (List<Integer>) inputStream.readObject();
		
		DefaultListModel listModel = new DefaultListModel();
		for(int i = 0; i < intList.size(); i++) {
			listModel.addElement(intList.get(i));
		}
		
		
		jList.setModel(listModel);
		label1.setText("Total Results: " + String.valueOf(intList.size()));
//		System.out.print("Message from server: ");
//		System.out.println(intList);
//		System.out.println("Size: " + intList.size());
			
	}	
  }
}





