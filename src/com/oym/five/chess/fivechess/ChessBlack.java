
package com.oym.five.chess.fivechess;

import java.awt.*;

// ºÚÆå
public class ChessBlack extends Canvas {

    // ºÚÆåËùÊôµÄÆåÅÌ
    private ChessBoard chessBoard;

    public ChessBlack(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    @Override
    public void paint(Graphics g) {
        //»­Æå×Ó
        g.setColor(Color.BLACK);
        g.fillOval(0,0,chessBoard.getgap(),chessBoard.getgap());
    }
}
