import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.io.IOException;

public class Server {
	private GUI gui;
	private ConnectionHandler ch;
	private HashMap<Integer,String> uidUser;
	private HashMap<String,Integer> userUid;
	private ArrayList<String> cmds;
	
	public Server(GUI gui) throws IOException {
		this.gui = gui;
		ch = new ConnectionHandler(this);
		ch.start();
		cmds = new ArrayList<String>();
		uidUser = new HashMap<Integer,String>();
		userUid = new HashMap<String,Integer>();
		uidUser.put(0,"SERVER");
		userUid.put("SERVER",0);
		cmds.add("say");
		cmds.add("whisper");
	}
	
	public void textInput(String text, Integer uid) {
		System.out.println(text);
		text = text.substring(1);
		String test;
		if (text.contains(" ")) {
			test = text.substring(0, text.indexOf(" "));
		} else {
			test = text;
		}
		
		test = test.toLowerCase();
		
		switch (test) {
			case "say": case "s": // Standard Broadcast message
				say(text,uid);
				break;
			
			case "whisper": case "w": // Whispers
				whisper(text,uid);
				break;
			default: // Unknown command
				if (uid.equals(0)) {
					gui.addText("That command does not exist");
				} else {
					serverMessage("That command does not exist",uid);
				}
				
		}
	}
	
	public void say(String text, Integer uid) {
		String out = "0"+uidUser.get(uid)+ text.substring(3);
		ch.broadcast(out);
		gui.addText(standForm(out.substring(1)));
	}
	
	public void whisper(String text, Integer uid) {
		String[] splitText = text.split(" ");
		if (userUid.containsKey(splitText[1])) {
			ch.sendMessage(userUid.get(splitText[1]),"1"+uidUser.get(uid)+ " " + text);
		} else {
			serverMessage("That user does not exist",uid);
		}
	}
	
	public void serverMessage(String text,Integer uid) {
		ch.sendMessage(uid, "9" + text);
	}
	
	private String standForm (String text) {
		Integer i = text.indexOf(" ");
		String user = text.substring(0, i);
		String message = text.substring(i+1);
		
		return addDate("[" + user + "] " + message);
	}
	
	public String addDate(String text) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		return sdf.format(now) +" " + text;
	}
	
	public void addUser(String user,Integer uid) {
		System.out.println("Adding User: " + user + " UserID: "+ uid);
		uidUser.put(uid, user);
		userUid.put(user, uid);
	}
	
	public void removeUser(Integer uid) {
		userUid.remove(uidUser.get(uid));
		uidUser.remove(uid);
	}
	
	public void shutDown() throws IOException {
		ch.close();
	}
}