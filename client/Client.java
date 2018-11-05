import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Client {
	private Socket soc;
	private PrintWriter out;
	private ArrayList<String> localCMDs;
	private GUI gui;
	private InboundCon inbound;
	private String username;
	
	public Client(GUI gui) {
		localCMDs = new ArrayList<String>();
		localCMDs.add("connect");
		localCMDs.add("disconnect");
		soc = null;
		this.gui = gui;
		username = "";
	}
	
	public void disconnect() throws IOException {
		if (isConnected()) {
			inbound.disconnect();
			out.close();
			soc.close();
		}
	}
	
	public void textInput(String text) throws NumberFormatException, UnknownHostException, IOException {
		if (text.startsWith("/") && localCMDs.contains(text.substring(1).split(" ")[0]) || !isConnected()) {
			localCMDParse(text);
		} else {
			if (text.startsWith("/")) {
				out.println(text);
			} else {
				out.println("/say " + text);
			}
		}
	}
	
	public void serverInput(String text) {
		int mode = Integer.parseInt(text.substring(0, 1));
		//System.out.println("recieved "+ text);
		text = text.substring(1);
	
		switch (mode) {
			case 0: // Standard text message
				gui.addText(standForm(text));
				break;
			case 1: // Whisper from another user
				gui.addText(whisperForm(text));
				break;
			case 9: // Message from server
				gui.addText(text);
				break;
		}
	}
	
	public boolean isConnected() {
		return soc != null && !soc.isClosed();
	}
	
	
	private void localCMDParse(String text) throws NumberFormatException, UnknownHostException, IOException {
		if (text.length() > 0) {
			String command;
			if (text.startsWith("/")) {
				command = text.substring(1);
			} else {
				command = text;
			}
		
			String[] cmd = command.split(" ");
			
			gui.addText(text);
			
			switch (cmd[0].toLowerCase()) {
				case "connect":
					connect(cmd);
					break;
				case "disconnect":
					disconnect();
					break;
				case "username":
					setUsername(cmd[1]);
					break;
				default:
					gui.addText("Not a valid command");
					break;
			}
		}
	}
	
	private void setUsername(String un) {
		username = un;
	}
	
	private void connect(String[] address) throws UnknownHostException, IOException {
		if (username.isEmpty()) {
			gui.addText("You must set your username first with /username [username]");
		} else {
			String[] tcmd = address[1].split(":");
			if(tcmd.length == 2) {
				soc = new Socket(tcmd[0],Integer.parseInt(tcmd[1]));
			} else {
				soc = new Socket(tcmd[0],6001);
			}
			inbound = new InboundCon(this, soc);
			inbound.start();
			
			out = new PrintWriter(soc.getOutputStream(),true);
			out.println(username);
			out.flush();
		}
	}
	
	private String standForm (String text) {
		Integer i = text.indexOf(" ");
		String user = text.substring(0, i);
		String message = text.substring(i+1);
	
		return addDate("[" + user + "] " + message);
	}
	
	private String whisperForm(String text) {
		Integer i = text.indexOf(" ");
		String out = "Whisper from " + text.substring(0, i) + ": " + text.substring(i+1);
		return addDate(out);
	}
	
	private String addDate(String text) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		return sdf.format(now) +" " + text;
	}
}