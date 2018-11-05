import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class InboundCon extends Thread{
	private Socket soc;
	private BufferedReader in;
	private Client client;
	
	private boolean closing;
	
	public InboundCon(Client c, Socket s) throws IOException {
		this.client = c;
		this.soc = s;
		in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		closing = false;
	}
	
	@Override
	public void run() {
		System.out.println("InboundCon Running");
		try {
			while (!closing) {
				//System.out.println("Waiting");
				if (in.ready()) {
					System.out.println("reading");
					client.serverInput(in.readLine());
				} else {
					Thread.sleep(5);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("InboundCon Interupted");
		}
	}
	
	
	public void disconnect() throws IOException {
		in.close();
		closing = true;
	}
}
