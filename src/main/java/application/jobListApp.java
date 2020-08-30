package application;

import gui.UserInterface;

public class jobListApp {
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UserInterface app = new UserInterface();
                app.createAndShowGUI();
            }
        });
    }
}
