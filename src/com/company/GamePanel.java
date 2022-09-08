package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int screen_width = 1000;
    static final int screen_height = 1000;
    static final int unitsize = 50;
    static final int game_unit = (screen_width * screen_height) / unitsize;
    static final int delay = 100;
    static int[] x = new int[game_unit];
    static int[] y = new int[game_unit];
    int bodyparts = 6;
    int appellate;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    JButton tryagainbutton = new JButton();
    ImageIcon icon = new ImageIcon("istockphoto-1347146680-612x612.jpg");
    int imageIconHeight = icon.getIconHeight();
    int imageIconWeight = icon.getIconWidth();

    JButton settings = new JButton("settings");
    int settingsH = 50;
    int settingsW = 100;


    Timer timer;
    Random random;


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(screen_width, screen_height));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();


    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }


    public void draw(Graphics g) {
        if (running) {

            for (int i = 0; i < screen_height / unitsize; i++) {
                g.drawLine(i * unitsize, 0, i * unitsize, screen_height);
                g.drawLine(0, i * unitsize, screen_width, i * unitsize);


            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, unitsize, unitsize);

            for (int i = 0; i < bodyparts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], unitsize, unitsize);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], unitsize, unitsize);
                }
                g.setColor(Color.red);
                g.setFont(new Font("ink free", Font.BOLD, 20));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("SCORE : " + appellate,
                        (screen_width - metrics.stringWidth("Wynik")) / 2,
                        g.getFont().getSize());

            }
        } else {
            GameLobby(g);


        }

    }

    public void newApple() {
        appleX = random.nextInt(screen_width / unitsize) * unitsize;
        appleY = random.nextInt(screen_height / unitsize) * unitsize;


    }

    public void move() {
        for (int i = bodyparts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];


        }

        switch (direction) {
            case 'U' -> y[0] = y[0] - unitsize;
            case 'D' -> y[0] = y[0] + unitsize;
            case 'L' -> x[0] = x[0] - unitsize;
            case 'R' -> x[0] = x[0] + unitsize;
        }


    }


    public void checkapple() {
        boolean b = y[0] == appleY;
        boolean a = x[0] == appleX;
        if ((a) && (b)) {

            bodyparts++;
            appellate++;
            newApple();

        }


    }

    public void checkcolistons() {
        //checks if head colides with body
        for (int i = bodyparts; i > 0; i--) {

            if ((x[0] == x[i]) && (y[0] == y[i])) {

                running = false;
                break;
            }
        }
        //check if head touches left
        if (x[0] < 0) {
            running = false;

        }
        if (x[0] > screen_width) {
            running = false;


        }
        if (y[0] < 0) {
            running = false;

        }
        if (y[0] > screen_height) {
            running = false;

        }

        if (!running) {
            timer.stop();
        }
    }

    public void GameLobby(Graphics g) {


        g.setColor(Color.red);
        g.setFont(new Font("ink free", Font.BOLD, 100));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("SNAKE", (screen_width - metrics.stringWidth("SNAKE")) / 2, screen_height / 2);

        if (appellate > 0) {
            g.setFont(new Font("ink free", Font.BOLD, 50));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("SCORE: " + appellate,
                    (screen_width - metrics2.stringWidth("SCORE ")) / 2,
                    screen_height / 3);
        }

        tryagainbutton.setSize(screen_width / 9, screen_height / 20);
        if(appellate==0) {
            tryagainbutton.setText("PLAY");
        }else {
            tryagainbutton.setText("PLAY AGAIN");
        }
        tryagainbutton.setVisible(true);
        tryagainbutton.setLocation(screen_width / 2 - (screen_width / 10) / 2, screen_height / 2 + 100);

        tryagainbutton.addActionListener(this);


        settings.addActionListener(this);
        settings.setSize(settingsW, settingsH);
        settings.setBackground(Color.gray);


        settings.setLocation(screen_width - settingsW * 2, settingsH - settingsH * 2);
        settings.setVisible(true);

        this.add(tryagainbutton);
    }


    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }


        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {

            move();
            checkapple();
            checkcolistons();
            repaint();
        }


        if (e.getSource() == tryagainbutton) {

            appellate = 0;
            tryagainbutton.setVisible(false);
            bodyparts = 5;
            x[0] = unitsize;
            y[0] = unitsize;
            direction = 'R';
            running = true;
            timer.start();

        }
    }


}

