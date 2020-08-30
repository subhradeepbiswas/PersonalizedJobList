package gui;

import com.mashape.unirest.http.exceptions.UnirestException;
import core.JobEntity;
import core.ResumeParsing;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;

public class UserInterface implements ActionListener {
    //private static String labelPrefix = "Number of button clicks: ";
    private int numClicks = 0;
    final JLabel label = new JLabel("  ");

    // Specify the look and feel to use by defining the LOOKANDFEEL constant
    // Valid values are: null (use the default), "Metal", "System", "Motif",
    // and "GTK"
    final static String LOOKANDFEEL = "Metal";

    // If you choose the Metal L&F, you can also choose a theme.
    // Specify the theme to use by defining the THEME constant
    // Valid values are: "DefaultMetal", "Ocean",  and "Test"
    final static String THEME = "Test";
    JMenuItem open;
    JFrame frame;
    JButton button;
    String filepath = "--";
    JTextArea skillsTextArea;
    JTextArea resumeTextArea;
    JTextField keywrdText;
    JTextField locationText;

    public Component createComponents() {
        JPanel pane1 = new JPanel(new GridLayout(2, 2));
        JLabel keywrdLabel = new JLabel("Search keyword: ");
        keywrdText = new JTextField();
        JLabel locationLabel = new JLabel("Job Location: ");
        locationText = new JTextField();
        pane1.add(keywrdLabel);
        pane1.add(keywrdText);
        pane1.add(locationLabel);
        pane1.add(locationText);

        GridBagLayout layout = new GridBagLayout();
        JPanel pane = new JPanel(layout);
        pane.setBorder(BorderFactory.createEmptyBorder(
                30, //top
                30, //left
                10, //bottom
                30) //right
        );

        pane.add(pane1);

        JLabel label1 = new JLabel("Skills separated by comma (Required)");
        skillsTextArea = new JTextArea(2, 100);

        open=new JMenuItem("Open File");
        open.addActionListener(this);
        JMenu file=new JMenu("Upload your resume (word/txt)");
        file.add(open);
        JMenuBar mb=new JMenuBar();
        mb.setBounds(0,5,8,20);
        mb.add(file);

        JLabel label2 = new JLabel("Paste your resume below");
        resumeTextArea = new JTextArea(30, 100);

        resumeTextArea.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(resumeTextArea);

        button = new JButton("Search");
        button.setMnemonic(KeyEvent.VK_I);
        button.addActionListener(this);
        label.setLabelFor(button);

        /*
         * An easy way to put space between a top-level container
         * and its contents is to put the contents in a JPanel
         * that has an "empty" border.
         */
        //JPanel pane = new JPanel(new GridLayout(1, 1));
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(new JLabel(" "), c);
        pane.add(new JLabel(" "), c);
        pane.add(label1, c);
        pane.add(skillsTextArea, c);

        JLabel spacing1 = new JLabel(" ");
        pane.add(spacing1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(mb, c);
        pane.add(label, c);

        JLabel spacing2 = new JLabel("Or ");
        pane.add(spacing2, c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        pane.add(label2, c);
        pane.add(scrollPane, c);

        JLabel spacing3 = new JLabel(" ");
        pane.add(spacing3, c);
        pane.add(button, c);

        return pane;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            PriorityQueue<JobEntity> pq = new PriorityQueue<JobEntity>();
            if (((skillsTextArea.getText() == null) || (skillsTextArea.getText().trim().length()==0)) ||
                    (filepath.equals("--") &&
                    ((resumeTextArea.getText() == null) || (resumeTextArea.getText().trim().length()==0)))){
                JOptionPane.showMessageDialog(null, "Required fields cannot be empty");
                return;
            }
            ResumeParsing rp = new ResumeParsing();
            String resume = "--";
            if (!filepath.equals("--")) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(filepath));
                    String s1 = "", s2 = "";
                    while ((s1 = br.readLine()) != null) {
                        s2 += s1 + " ";
                    }
                    resume = s2;
                    br.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (resume.equals("--")){
                resume = resumeTextArea.getText();
            }
            try {
                try {
                    pq = rp.fetchMyResult(skillsTextArea.getText(), resume, keywrdText.getText(), locationText.getText());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                while(!pq.isEmpty()){
                    JobEntity je = pq.poll();
                    resumeTextArea.append(je.getUrl()+"  -  "+je.getWeight()+"\n");
                }
            } catch (UnirestException unirestException) {
                unirestException.printStackTrace();
            }
        }

        if (e.getSource() == open) {
            JFileChooser fc = new JFileChooser();
            int i = fc.showOpenDialog(frame);
            if (i == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                filepath = f.getPath();
                /*try {
                    BufferedReader br = new BufferedReader(new FileReader(filepath));
                    String s1 = "", s2 = "";
                    while ((s1 = br.readLine()) != null) {
                        s2 += s1 + "\n";
                        break;
                    }
                    br.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }*/

                label.setText(filepath);
            }
        }
    }
    private static void initLookAndFeel() {
        String lookAndFeel = null;

        if (LOOKANDFEEL != null) {
            if (LOOKANDFEEL.equals("Metal")) {
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                //  an alternative way to set the Metal L&F is to replace the
                // previous line with:
                // lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";

            } else if (LOOKANDFEEL.equals("System")) {
                lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            } else if (LOOKANDFEEL.equals("Motif")) {
                lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            } else if (LOOKANDFEEL.equals("GTK")) {
                lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            } else {
                System.err.println("Unexpected value of LOOKANDFEEL specified: "
                        + LOOKANDFEEL);
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            }

            try {


                UIManager.setLookAndFeel(lookAndFeel);

                // If L&F = "Metal", set the theme

                if (LOOKANDFEEL.equals("Metal")) {
                    if (THEME.equals("DefaultMetal"))
                        MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                    else
                        MetalLookAndFeel.setCurrentTheme(new OceanTheme());

                    UIManager.setLookAndFeel(new MetalLookAndFeel());
                }


            } catch (ClassNotFoundException e) {
                System.err.println("Couldn't find class for specified look and feel:"
                        + lookAndFeel);
                System.err.println("Did you include the L&F library in the class path?");
                System.err.println("Using the default look and feel.");
            } catch (UnsupportedLookAndFeelException e) {
                System.err.println("Can't use the specified look and feel ("
                        + lookAndFeel
                        + ") on this platform.");
                System.err.println("Using the default look and feel.");
            } catch (Exception e) {
                System.err.println("Couldn't get specified look and feel ("
                        + lookAndFeel
                        + "), for some reason.");
                System.err.println("Using the default look and feel.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public void createAndShowGUI() {
        //Set the look and feel.
        initLookAndFeel();

        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        frame = new JFrame("Personalized job listing app");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //UserInterface app = new UserInterface();
        Component contents = this.createComponents();
        frame.getContentPane().add(contents, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}