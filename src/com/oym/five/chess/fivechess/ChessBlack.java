
package com.oym.five.chess.fivechess;

import java.awt.*;

// ����
public class ChessBlack extends Canvas {

    // ��������������
    private ChessBoard chessBoard;

    public ChessBlack(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    @Override
    public void paint(Graphics g) {
        //������
        g.setColor(Color.BLACK);
        g.fillOval(0,0,chessBoard.getgap(),chessBoard.getgap());
    }
}
