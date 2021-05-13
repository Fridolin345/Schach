package de.schach.gui;

import de.schach.board.*;

public class PrintMyField
{
    public PrintMyField( Board board ){
        System.out.println("  a b c d e f g h");
        for(int i = 0; i<8; i++){
            System.out.print((8-i) + " ");
            for(int j = 0; j<8; j++){
                if(j != 0){System.out.print("|");}
                if(board.isPieceAt( new Position( i, j ) )){
                    if(board.getPiece( new Position(i, j) ).getColor() == PieceColor.WHITE){
                        System.out.print(board.getPiece( new Position(i, j) ).getPieceType().getNotationChar());
                    }else {
                        System.out.print(board.getPiece( new Position(i, j) ).getPieceType().getFenChar());
                    }
                } else
                {
                    System.out.print( " " );
                }
            }
            System.out.println("");
        }
    }
}
