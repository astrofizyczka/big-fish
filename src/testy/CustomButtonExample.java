package testy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomButtonExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Button Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(426, 116);

        // Ładowanie obrazka z folderu resources
        ImageIcon buttonIcon = new ImageIcon(CustomButtonExample.class.getResource("przycisk1.png"));

        // Tworzymy przycisk z obrazkiem
        JButton customButton = new JButton(buttonIcon);
        customButton.setHorizontalTextPosition(SwingConstants.CENTER);
        customButton.setVerticalTextPosition(SwingConstants.BOTTOM);

        // Akcja po kliknięciu
        customButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Przycisk został kliknięty!");
            }
        });

        frame.add(customButton);
        frame.setVisible(true);
    }
}
