package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

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



class SnakeGame extends JPanel implements ActionListener,KeyListener
{
    private class Tile
    {
        int x;
        int y;

        public Tile(int x,int y)
        {
            this.x=x;
            this.y=y;
        }    
    }
    int boardWidth;
    int boardHeight;
    int tileSize=25;

    // snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    // food
    Tile food;
    Random random;

    // game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver=false;

    public SnakeGame(int boardWidth,int boardHeight)
    {
        this.boardWidth=boardWidth;
        this.boardHeight=boardHeight; 
        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead=new Tile(5,5);
        snakeBody=new ArrayList<Tile>();

        food=new Tile(10,10);
        random=new Random();
        placeFood();

        velocityX=0;
        velocityY=0;

        gameLoop=new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g)
    {
        // grid

        for(int i=0;i<boardWidth/tileSize;i++)
        {
            g.drawLine(i*tileSize,0,i*tileSize,boardWidth);
            g.drawLine(0,i*tileSize,boardHeight,i*tileSize);
        }

        // food
        g.setColor(Color.red);
        g.fillRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize);

        // snake head
        g.setColor(Color.green);
        g.fillRect(snakeHead.x*tileSize,snakeHead.y*tileSize,tileSize,tileSize);

        // snake body
        for(int i=0;i<snakeBody.size();i++)
        {
            Tile snakePart=snakeBody.get(i);
            g.fillRect(snakePart.x*tileSize,snakePart.y*tileSize,tileSize,tileSize);
        }

        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameOver)
        {
            g.setColor(Color.red);
            g.drawString("Game Over: "+String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
        else
        {
            g.setColor(Color.yellow);
            g.drawString("Score: "+String.valueOf(snakeBody.size()),tileSize,tileSize);
        }
    }

    public void placeFood()
    {
        food.x=random.nextInt(boardWidth/tileSize);
        food.y=random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1,Tile tile2)
    {
        return tile1.x==tile2.x && tile1.y==tile2.y;
    }
    public void move()
    {
        // snake body
        for(int i=snakeBody.size()-1;i>=0;i--)
        {
            Tile snakePart=snakeBody.get(i);
            if(i==0)
            {
                snakePart.x=snakeHead.x;
                snakePart.y=snakeHead.y;
            }
            else
            {
                Tile prevSnakePart=snakeBody.get(i-1);
                snakePart.x=prevSnakePart.x;
                snakePart.y=prevSnakePart.y;
            }
        }
        // eat food
        if(collision(snakeHead,food))
        {
            snakeBody.add(new Tile(food.x,food.y));
            placeFood();
        }
        // Snake head
        snakeHead.x+=velocityX;
        snakeHead.y+=velocityY;

        // game over condition
        for(int i=0;i<snakeBody.size();i++)
        {
            Tile snakePart=snakeBody.get(i);
            // collide with the snake
            if(collision(snakeHead,snakePart))
            {
                gameOver=true;
            }
        }

        if(snakeHead.x*tileSize<0 || snakeHead.x*tileSize>boardWidth || snakeHead.y*tileSize<0 || snakeHead.y*tileSize>boardHeight)
        {
            gameOver=true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        move();
        repaint();
        if(gameOver)
        {
            gameLoop.stop();
        }
    }
        
    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode()==e.VK_UP && velocityY!=1)
        {
            velocityX=0;
            velocityY=-1;
        }
        else if(e.getKeyCode()==e.VK_DOWN && velocityY!=-1)
        {
            velocityX=0;
            velocityY=1;
        }
        else if(e.getKeyCode()==e.VK_RIGHT && velocityX!=-1)
        {
            velocityX=1;
            velocityY=0;
        }
        else if(e.getKeyCode()==e.VK_LEFT && velocityX!=1)
        {
            velocityX=-1;
            velocityY=0;
        }
    }

    // do not need
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}

