package UI;

import Gomoku.ChessPiece;
import UI.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GamingBoard extends JPanel {
    public static final int EDGE = 20; //the distance between the gomuko panel and the background picture
    public static final int SPAN = 25; //the distnce of the panel on each row and column
    public static final int ROWS = 23;
    public static final int COLS = 23;
    private Image backgroudImage;
    private ArrayList<ChessPiece> chessOnPanel;
    private boolean[][] boardArray;
    private GamePanel gp;

    public GamingBoard(GamePanel gp){
        this.gp = gp;
        backgroudImage = Toolkit.getDefaultToolkit().getImage("img/gameBg.jpg");
        chessOnPanel = new ArrayList<>();
        boardArray = new boolean[ROWS][COLS];
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int row = (mouseEvent.getX() - EDGE + SPAN /2 ) / SPAN;
                int column = (mouseEvent.getY() - EDGE + SPAN / 2) / SPAN;
                if(gp.getUserInterface().getGameClientLogic().getIsGoing() && chessPieceNotContained(row , column))
                {
                    ChessPiece newPiece = new ChessPiece(row , column, gp.getUserInterface().getGameClientLogic().getMyColor());
                    boardArray[row][column] = true;
                    chessOnPanel.add(newPiece);
                    if(isWin(newPiece))
                    {
                        gp.getUserInterface().getGameClientLogic().setGamingStatus(false);
                        gp.getUserInterface().getGameClientLogic().setIsGoing(false);
                        gp.getGamingInfo().getInfoArea().append(row + " " + column);
                        gp.getUserInterface().getGameClientLogic().sendGo(row , column);
                        repaint();
                        gp.getUserInterface().getGameClientLogic().sendWin();
                        gp.getUserInterface().getGameClientLogic().updateQuit();
                        announceWinner();
                        return;
                    }

                    gp.getUserInterface().getGameClientLogic().setIsGoing(false);
                    gp.getGamingInfo().getInfoArea().append("\n" + gp.getUserInterface().getGameClientLogic().getMyname() +" ("+row + "," + column + ")");
                    gp.getUserInterface().getGameClientLogic().sendGo(row , column);
                    gp.getUserInterface().getGameClientLogic().setIsGoing(false);
                    repaint();

                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }

        });
        setBounds(0,0,600,600);
        gp.add(this);
    }

    public boolean chessPieceNotContained(int row, int column)
    {
        for(int i = 0 ; i < chessOnPanel.size() ; i++)
        {
            ChessPiece currentChessPiece = chessOnPanel.get(i);
            if(currentChessPiece.getRow() == row && currentChessPiece.getColumn() == column)
            {
                return false;
            }
        }
        return true;
    }

    public GamePanel getGamePanel()
    {
        return gp;
    }

    //too see whether the newly added chessPiece will cause the winning situation
    public boolean isWin(ChessPiece newlyAddedPiece)
    {
        int row = newlyAddedPiece.getRow();
        int column = newlyAddedPiece.getColumn();
        if(searchHorizontal(row , column) || searchVertical(row , column) || searchDiagonal(row , column) || searchSubDiagonal(row , column)){
            return true;
        }

        else
        {
            return false;
        }

    }

    public boolean searchHorizontal(int row , int column)
    {
        int leftColumn = column;
        int rightColumn = column;
        int consecutiveNum = 1;

        //search along the left horizontal
        while(true)
        {
            leftColumn -= 1;

            if(leftColumn < 0 || leftColumn > COLS - 1 || !boardArray[row][leftColumn])
            {
                break;
            }

            if(boardArray[row][leftColumn])
            {
                consecutiveNum += 1;
            }

        }

        while(true)
        {
            rightColumn += 1;

            if(rightColumn < 0 || rightColumn > COLS - 1 || !boardArray[row][rightColumn])
            {
                break;
            }

            if(boardArray[row][rightColumn])
            {
                consecutiveNum += 1;
            }

        }

        if(consecutiveNum >= 5)
        {
            return true;
        }

        else
        {
            return false;
        }

    }

    public boolean searchVertical(int row , int column)
    {
        int upRow = row;
        int downRow = row;
        int consecutiveNum = 1;

        while(true)
        {
            upRow -= 1;

            if(upRow < 0 || upRow > ROWS - 1 || !boardArray[upRow][column])
            {
                break;
            }

            if(boardArray[upRow][column])
            {
                consecutiveNum += 1;
            }
        }

        while(true)
        {
            downRow += 1;

            if(downRow < 0 || downRow > ROWS - 1 || !boardArray[downRow][column])
            {
                break;
            }

            if(boardArray[downRow][column])
            {
                consecutiveNum += 1;
            }
        }

        if(consecutiveNum >= 5)
        {
            return true;
        }

        else
        {
            return false;
        }

    }


    public boolean searchDiagonal(int row , int column)
    {
        int diagonalRowDown = row;
        int diagonalColumnDown = column;
        int diagonalRowUp = row;
        int diagonalColumnUp = column;

        int consecutiveNum = 1;

        while(true)
        {
            diagonalRowDown -= 1;
            diagonalColumnDown -= 1;

            if(diagonalRowDown < 0 || diagonalRowDown > ROWS - 1 || diagonalColumnDown < 0 || diagonalColumnDown > COLS - 1 || !boardArray[diagonalRowDown][diagonalColumnDown])
            {
                break;
            }

            if(boardArray[diagonalRowDown][diagonalColumnDown])
            {
                consecutiveNum += 1;
            }

        }

        while(true)
        {
            diagonalRowUp += 1;
            diagonalColumnUp += 1;

            if(diagonalRowUp < 0 || diagonalRowUp > ROWS - 1 || diagonalColumnUp < 0 || diagonalColumnUp > COLS - 1 || !boardArray[diagonalRowUp][diagonalColumnUp])
            {
                break;
            }

            if(boardArray[diagonalRowUp][diagonalColumnUp])
            {
                consecutiveNum += 1;
            }
        }

        if(consecutiveNum >= 5)
        {
            return true;
        }

        else
        {
            return false;
        }


    }

    public boolean searchSubDiagonal(int row , int column)
    {
        int subDiagonalRowUp = row;
        int subDiagonalRowDown = row;
        int subDiagonalColumnUp = column;
        int subDiagonalColumnDown = column;

        int consecutiveNum = 1;

        while(true)
        {
            subDiagonalRowDown += 1;
            subDiagonalColumnDown -= 1;

            if(subDiagonalRowDown < 0 || subDiagonalRowDown > ROWS - 1 || subDiagonalColumnDown < 0 || subDiagonalColumnDown > COLS - 1 || !boardArray[subDiagonalRowDown][subDiagonalColumnDown])
            {
                break;
            }

            if(boardArray[subDiagonalRowDown][subDiagonalColumnDown])
            {
                consecutiveNum += 1;
            }

        }

        while(true)
        {
            subDiagonalRowUp -= 1;
            subDiagonalColumnUp += 1;

            if(subDiagonalRowUp < 0 || subDiagonalRowUp > ROWS - 1 || subDiagonalColumnUp < 0 || subDiagonalColumnUp > COLS - 1 || !boardArray[subDiagonalRowUp][subDiagonalColumnUp])
            {
                break;
            }

            if(boardArray[subDiagonalRowUp][subDiagonalColumnUp])
            {
                consecutiveNum += 1;
            }
        }

        if(consecutiveNum >= 5)
        {
            return true;
        }

        else
        {
            return false;
        }
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(backgroudImage , 0 , 0 , this);
        for(int i = 0 ; i <= ROWS -1; i++)
        {
            g.drawLine(EDGE, EDGE + i * SPAN, EDGE + (COLS - 1) * SPAN , EDGE + i * SPAN);
        }
        for(int i = 0 ; i <= COLS -1; i++)
        {
            g.drawLine(EDGE + i * SPAN , EDGE , EDGE + i * SPAN , EDGE + (ROWS - 1) * SPAN);
        }
        g.fillRect( EDGE+ 3 * SPAN - 2, EDGE + 3 * SPAN - 2 , 5 , 5);
        g.fillRect(EDGE + (COLS/2) * SPAN - 2, EDGE + 3 * SPAN - 2, 5, 5);
        g.fillRect(EDGE + (COLS - 3) * SPAN - 2, EDGE + 3 * SPAN - 2, 5 , 5);
        g.fillRect(EDGE + 3 * SPAN - 2, EDGE + (ROWS / 2) * SPAN, 5 , 5);
        g.fillRect(EDGE + (COLS / 2) * SPAN - 2, EDGE + (ROWS / 2) * SPAN - 2, 5 , 5);
        g.fillRect(EDGE + (COLS - 3) * SPAN - 2, EDGE + (ROWS / 2) * SPAN - 2, 5 , 5);
        g.fillRect(EDGE + 3 * SPAN - 2, EDGE + (ROWS - 3) * SPAN - 2, 5 , 5);
        g.fillRect(EDGE + (COLS / 2) * SPAN - 2, EDGE + (ROWS - 3) * SPAN - 2, 5 , 5);
        g.fillRect(EDGE + (COLS - 3) * SPAN - 2, EDGE + (ROWS - 3) * SPAN - 2, 5 , 5);

        for(int i = 0 ; i < chessOnPanel.size() ; i++)
        {
            chessOnPanel.get(i).draw(g);
        }

    }

    public void drawNewChessPiece(int row, int column, Color color)
    {
        ChessPiece newPiece = new ChessPiece(row , column, color);
        if(color == gp.getUserInterface().getGameClientLogic().getMyColor())
        {
            boardArray[row][column] = true;
        }
        chessOnPanel.add(newPiece);
        gp.getUserInterface().getGameClientLogic().setIsGoing(false);
        System.out.println("drwa chess piece yet");
        repaint();
    }


    public void announceWinner()
    {
        System.out.println("anounce winner called");
        new GameEndInfo(true , this);
    }


    public void announceLoser()
    {
        System.out.println("anounce loser called");
        new GameEndInfo(false , this);
    }







}
