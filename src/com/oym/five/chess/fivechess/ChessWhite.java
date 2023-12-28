package com.oym.five.chess.fivechess;

import java.awt.*;

//����
public class ChessWhite extends Canvas {

    // ������������
    ChessBoard chessBoard;

    public ChessWhite(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    @Override
    public void paint(Graphics g) {
        //������
        g.setColor(Color.WHITE);
        g.fillOval(0,0,chessBoard.getgap(),chessBoard.getgap());
    }
}
