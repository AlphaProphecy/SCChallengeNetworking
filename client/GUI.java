import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
public class GUI extends Frame implements ActionListener, WindowListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static TextArea outputTA;
	private TextField inputTF;
	private Button sendBut;
	private Client client;
	
	public static void main(String args[]) throws IOException, InterruptedException {
		GUI g = new GUI();
	}
	
	public GUI() {
		client = new Client(this);
		
		setLayout(new FlowLayout());
		
		outputTA = new TextArea(20,50);
		add(outputTA);
		outputTA.setEditable(false);
		
		inputTF = new TextField(40);
		add(inputTF);
		
		sendBut = new Button("Send");
		add(sendBut);
		sendBut.addActionListener(this);
	    
	    addWindowListener(this);
	    
	    setTitle("Client");
	    setSize(450, 420);
	   	  
	    setVisible(true);
	}
	
	public void addText(String s) {
		outputTA.append(s + "\n");
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0){
		try {
			client.textInput(inputTF.getText());
			inputTF.setText("");
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void windowClosing(WindowEvent evt) {
		try {
			
			client.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}
