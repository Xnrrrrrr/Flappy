package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlappyBird extends JFrame implements ActionListener, KeyListener {
    private final Timer timer;
    private final List<Rectangle> pipes;
    private int birdY, birdVelocity;
    private boolean jumping;

    public FlappyBird() {
        setTitle("Flappy Bird");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(this);

        pipes = new ArrayList<>();
        timer = new Timer(20, this);
        birdY = 300;
        birdVelocity = 0;
        jumping = false;

        generatePipes();
        timer.start();
    }

    private void generatePipes() {
        Random rand = new Random();
        int pipeWidth = 100;
        int gap = 200;
        int spaceBetweenPipes = 300;

        int maxHeight = getHeight() - gap;

        // Generate pipes with varying length and frequency
        for (int i = 0; i < 3; i++) {
            int pipeHeight = rand.nextInt(Math.max(50, maxHeight)) + 50; // Minimum pipe height of 50

            int pipeY = rand.nextInt(Math.max(1, maxHeight)) + pipeHeight;

            pipes.add(new Rectangle(getWidth() + i * spaceBetweenPipes, 0, pipeWidth, pipeY - gap));
            pipes.add(new Rectangle(getWidth() + i * spaceBetweenPipes, pipeY + gap, pipeWidth, getHeight() - pipeY - gap));
        }
    }


    private void movePipes() {
        for (Rectangle pipe : pipes) {
            pipe.x -= 5;
        }

        if (pipes.get(0).x + pipes.get(0).width <= 0) {
            pipes.remove(0);
            pipes.remove(0);
            generatePipes();
        }
    }

    private void jump() {
        if (!jumping) {
            birdVelocity = -10;
            jumping = true;
        }
    }

    private void checkCollisions() {
        Rectangle bird = new Rectangle(50, birdY, 50, 50);

        for (Rectangle pipe : pipes) {
            if (bird.intersects(pipe)) {
                gameOver();
            }
        }

        if (birdY >= getHeight() - 50) {
            gameOver();
        }
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + (pipes.size() / 2),
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movePipes();
        checkCollisions();

        birdY += birdVelocity;
        birdVelocity += 1;

        jumping = false;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.cyan);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.orange);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        g.setColor(Color.red);
        g.fillRect(50, birdY, 50, 50);

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlappyBird flappyBird = new FlappyBird();
            flappyBird.setVisible(true);
        });
    }
}