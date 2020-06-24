package Gomoku;

import UI.GamingBoard;

import java.awt.*;

public class ChessPiece {
    private Color chessColor;
    private int row;
    private int column;

    public ChessPiece(int row, int column, Color color)
    {
        setChessColor(color);
        setRow(row);
        setColumn(column);
    }

    public void setChessColor(Color color)
    {
        this.chessColor = color;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public void setColumn(int column)
    {
        this.column = column;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public void draw(Graphics g)
    {
        g.setColor(this.chessColor);
        g.fillOval(GamingBoard.EDGE + this.row * GamingBoard.SPAN - GamingBoard.SPAN / 3 , GamingBoard.EDGE + this.column * GamingBoard.SPAN - GamingBoard.SPAN / 3 , 18 , 18);
    }



}
