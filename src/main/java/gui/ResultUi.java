package gui;

import javax.swing.*;
import java.awt.*;

public class ResultUi {
    JFrame frame;
    JEditorPane resultTextArea;

    public Component createComponents() {

        GridBagLayout layout = new GridBagLayout();
        JPanel pane = new JPanel(layout);
        pane.setBorder(BorderFactory.createEmptyBorder(
                30, //top
                30, //left
                10, //bottom
                30) //right
        );

        JLabel resLabel = new JLabel("Job posts that match your skills: ");
        resultTextArea = new JEditorPane();
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(1000, 600));
        scrollPane.setMinimumSize(new Dimension(1000, 600));

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(resLabel);
        pane.add(new JLabel(" "));

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        pane.add(scrollPane, c);

        return pane;
    }

    public void showResultGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        frame = new JFrame("Personalized job listing app");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Component contents = this.createComponents();
        frame.getContentPane().add(contents, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
