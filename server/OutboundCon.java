import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;


public class OutboundCon extends Thread {
	private Integer uid;
	private PrintWriter out;
	private ArrayList<String> pendingMessage;
	private boolean closing;
	
	public OutboundCon(Socket soc, Integer uid) throws IOException {
		this.uid = uid;
		out = new PrintWriter(soc.getOutputStream());
		pendingMessage = new ArrayList<String>();
		closing = false;
	}
	
	@Override
	public void run() {
		while (!closing) {
			if (!pendingMessage.isEmpty()) {
				System.out.println("OutboundCon " + uid + " Sending");
				//System.out.println(pendingMessage.get(0));
				out.println(pendingMessage.get(0));
				out.flush();
				pendingMessage.remove(0);
			} else {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendMessage(String text) {
		pendingMessage.add(text);
	}
	
	
	public void disconnect() throws IOException {
		closing = true;
		out.close();
	}
}