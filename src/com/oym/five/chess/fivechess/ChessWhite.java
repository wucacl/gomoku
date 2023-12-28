package com.oym.five.chess.fivechess;

import java.awt.*;

//°×Æå
public class ChessWhite extends Canvas {

    // °×ÆåËùÔÚÆåÅÌ
    ChessBoard chessBoard;

    public ChessWhite(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    @Override
    public void paint(Graphics g) {
        //»­Æå×Ó
        g.setColor(Color.WHITE);
        g.fillOval(0,0,chessBoard.getgap(),chessBoard.getgap());
    }
}
