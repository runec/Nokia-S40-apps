package model;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class Main extends MIDlet implements CommandListener {

    public Main() {
    }

    // Display
    private Display m_display;

    // Main form to hold settings
    private Form m_form;
        
    // The command objects
    private Command m_exitCommand;
    private Command m_startCommand;
    
    // The main exercise screen
    private Displayable m_exerciseScreen;

    
    public void commandAction(Command command, Displayable displayable) {
        if (displayable == m_form) {
            if (command == m_exitCommand) {
                doQuit();
            } else if (command == m_startCommand) {
            	// Start the exercise
            	m_display.setCurrent(m_exerciseScreen);
            }
        }
    }
    
    public void startApp(){
        m_display = Display.getDisplay(this);
        
        // Init the exercise screen
        m_exerciseScreen = new ColorCanvas();
        
        createSettingsForm();
                
        m_display.setCurrent (m_form); 
    }
    
    // Create the settings form
    private void createSettingsForm(){
    	m_form = new Form("Settings");
    	
    	// Settings specific to this exercise
    	
    	// Additive or Subtractive colors
    	Item[] items = Settings.getInstance().getItems();
    	
    	for(int i=0;i<items.length;i++){
    		m_form.append(items[i]);
    	}
    	
    	// Standard commands
    	m_exitCommand = new Command("Exit", Command.EXIT, 1);
    	m_startCommand = new Command("Start", Command.OK, 2);
        
    	m_form.addCommand(m_exitCommand);
    	m_form.addCommand(m_startCommand);
    	m_form.setCommandListener(this);
    }
     
    // Called by the system 
    public void pauseApp() {
    }
 
    // Called by the system 
    public void destroyApp(boolean unconditional) {
    }
    
    // Exits the application
    public void doQuit() {
    	m_display.setCurrent(null);
        notifyDestroyed();
    }
 
}