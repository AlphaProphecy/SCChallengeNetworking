import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
public class ConnectionHandler extends Thread{
	private Server server;
	private ServerSocket sevSoc;
	private ArrayList<Socket> socs;
	private HashMap<Integer,OutboundCon> userOut;
	private HashMap<Integer,InboundCon> userIn;
	private boolean closing;
	
	public ConnectionHandler(Server s) throws IOException {
		this.sevSoc = new ServerSocket(6001);
		this.server = s;
		
		sevSoc.setSoTimeout(5000);
		
		userOut = new HashMap<Integer,OutboundCon>();
		userIn = new HashMap<Integer,InboundCon>();
		socs = new ArrayList<Socket>();
		
		
		closing = false;
	}
	
	@Override
	public void run() {
		Socket temp = null;
		Integer i = 1;
		while(!closing) {
			System.out.println("waiting on 6001");
			try {
				temp = sevSoc.accept();
				if (!temp.equals(null)) {
					socs.add(temp);
					userIn.put(i, new InboundCon(this,temp, i));
					userOut.put(i,new OutboundCon(temp, i));
					i++;
					temp = null;
				}
				
			} catch (IOException e) {
			} 
			
		}
	}
	
	public void sendMessage(Integer uid,String text) {
		userOut.get(uid).sendMessage(text);
		if (!userOut.get(uid).isAlive()) {
			userOut.get(uid).start();
		}
	}
	
	public void broadcast(String text) {
		for (Integer uid : userOut.keySet()) {
			sendMessage(uid,text);
		}
	}
	
	public void close() throws IOException {
		closing = true;
		for (Integer uid: userOut.keySet()) {
			removeUser(uid);
		}
		sevSoc.close();
	}
	
	public void clientInput(String text,Integer uid) {
		server.textInput(text,uid);
	}
	
	public void addUser(String user, Integer uid) {
		server.addUser(user, uid);
	}
	
	public void removeUser(Integer uid) throws IOException {
		server.removeUser(uid);
		userOut.get(uid).disconnect();
		userIn.get(uid).disconnect();
		userIn.remove(uid);
		userOut.remove(uid);
		
	}
}
