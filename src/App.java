package src;

import javax.swing.*;

class App
{
    public static void main(String[] args) 
    {
        int boardWidth=600;
        int boardHeight=boardWidth;
        int tileSize=25;

        JFrame frame=new JFrame("Snake Game");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame=new SnakeGame(boardWidth,boardHeight);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}
