import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class InboundCon extends Thread{
	private ConnectionHandler ch;
	private Integer uid;
	private BufferedReader in;
	private boolean closing;
	
	public InboundCon(ConnectionHandler ch,Socket soc, Integer uid) throws IOException {
		this.ch = ch;
		this.uid = uid;
		in = new BufferedReader(new InputStreamReader(soc.getInputStream()));

		ch.addUser(in.readLine(),uid);
		
		closing = false;
		this.start();
	}
	
	@Override
	public void run() {
		System.out.println("InboundCon " + uid + " Running");
		try {
			while (!closing) {
				if (in.ready()) {
					System.out.println("InboundCon " + uid + " Reading");
					ch.clientInput(in.readLine(),uid);
				} else {
					Thread.sleep(5);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("InboundCon " + uid + " Interupted");
		}	
	}
	
	public void disconnect() throws IOException {
		in.close();
		closing = true;
	}
}
