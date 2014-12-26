package io.github.nelsoncrosby.gprg;

import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class SwingApp extends JFrame {
    CanvasGameContainer gc;

    public SwingApp() throws HeadlessException, SlickException {
        super(CONST.TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gc = new CanvasGameContainer(new GPRGame());
        final JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                remove(startButton);
                try {
                    add(gc);
                    pack();
                    gc.start();
                } catch (SlickException e) {
                    e.printStackTrace();
                    remove(gc);
                    add(startButton);
                    pack();
                }
            }
        });
        add(startButton);
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    SwingApp app = new SwingApp();
                    app.setVisible(true);
                } catch (SlickException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
