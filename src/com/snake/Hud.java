package com.snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Hud extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 10;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int currentDelay = DELAY;
    int snakeBody = 6;
    int foodsEaten;
    int foodX;
    int foodY;
    char direction = 'R';
    boolean run = false;
    Timer timer;
    Random random;

    Hud() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        start();
    }

    public void start() {
        newFood();
        run = true;
        currentDelay = DELAY;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);
        draw(graphic);
    }

    public void draw(Graphics graphics) {
        if (run) {
            graphics.setColor(Color.red);
            graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < snakeBody; i++) {
                if (i == 0) {
                    graphics.setColor(Color.green);
                } else {
                    Color bodyColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                    graphics.setColor(bodyColor);
                }
                graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            graphics.setColor(Color.red);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Pontuação: " + foodsEaten, (SCREEN_WIDTH - metrics.stringWidth("Pontuação: " + foodsEaten)) / 2, graphics.getFont().getSize());

            graphics.setColor(Color.blue);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics footerMetrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Amazing Snake", (SCREEN_WIDTH - footerMetrics.stringWidth("Amazing Snake")) / 2, SCREEN_HEIGHT - graphics.getFont().getSize());

            graphics.setColor(Color.yellow);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 20));
            FontMetrics footerMetrics2 = getFontMetrics(graphics.getFont());
            graphics.drawString("Made By: Gustavo Gaiotti", (SCREEN_WIDTH - footerMetrics2.stringWidth("Made by: Gustavo Gaiotti")) / 2, SCREEN_HEIGHT - graphics.getFont().getSize());
        } else {
            gameOver(graphics);
        }
    }

    public void newFood() {
        foodX = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for (int i = snakeBody; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkFood() {
        if ((x[0] == foodX && y[0] == foodY)) {
            snakeBody++;
            foodsEaten++;
            newFood();
            currentDelay -= 2;
            if (currentDelay < 20) {
                currentDelay = 20;
            }
            timer.setDelay(currentDelay);
        }
    }

    public void checkCollisions() {
        for (int i = snakeBody; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                run = false;
            }
        }

        if (x[0] < 0) {
            x[0] = SCREEN_WIDTH - UNIT_SIZE;
        } else if (x[0] >= SCREEN_WIDTH) {
            x[0] = 0;
        }

        if (y[0] < 0) {
            y[0] = SCREEN_HEIGHT - UNIT_SIZE;
        } else if (y[0] >= SCREEN_HEIGHT) {
            y[0] = 0;
        }
    }

    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Pontuação: " + foodsEaten, (SCREEN_WIDTH - metrics.stringWidth("Pontuação: " + foodsEaten)) / 2, graphics.getFont().getSize());

        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics _metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Fim de Jogo", (SCREEN_WIDTH - _metrics.stringWidth("Fim de Jogo")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (run) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ke) {
            switch (ke.getKeyCode()) {
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
}
