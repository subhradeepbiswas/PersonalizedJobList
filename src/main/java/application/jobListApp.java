package application;

import gui.UserInterface;

public class jobListApp {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UserInterface app = new UserInterface();
                app.createAndShowGUI();
            }
        });
    }
}
