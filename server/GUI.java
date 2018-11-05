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
	private Server server;
	
	public static void main(String args[]) throws IOException {
		GUI g = new GUI();
	}
	
	public GUI() throws IOException {
		server = new Server(this);
		
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
	    
	    setTitle("Server");
	    setSize(450, 420);
	   	  
	    setVisible(true);
	}
	
	public void addText(String s) {
		outputTA.append(s + "\n");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0){
		try {
			server.textInput(inputTF.getText(),0);
			inputTF.setText("");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void windowClosing(WindowEvent evt) {
		try {
			
			server.shutDown();
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
