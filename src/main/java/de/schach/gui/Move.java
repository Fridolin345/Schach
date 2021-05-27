package de.schach.gui;

import de.schach.board.*;
import de.schach.util.Vector;

import java.util.*;

import static de.schach.board.Piece.*;

public class Move
{

    private final Position startPos;
    private final Position endPos;
    public final Board beforeboard;

    public Move( Position startPos, Position endPos, Board beforeboard )
    {
        System.out.println( "new Move" );
        System.out.println( "startPos: " + startPos.toNotation() + "  endPos: " + endPos.toNotation() );
        this.startPos = startPos;
        this.endPos = endPos;
        this.beforeboard = beforeboard;
    }

    public Vector getMoveVector()
    {
        return startPos.getDiff( getEndPos() );
    }

    public Position getStartPos()
    {
        return startPos;
    }

    public Position getEndPos()
    {
        return endPos;
    }

    public String getAcronym()
    {
        Piece startPiece = this.beforeboard.getPiece( startPos );

        boolean hits = beforeboard.isPieceAt( endPos );

        String acronym = "";
        if ( startPiece.getPieceType() != PieceType.PAWN )
        {
            acronym += startPiece.getPieceType().getNotationChar();

            //Wenn z.B. zwei Türme auf das selbe Feld fahren können, muss klar gemacht werden, welcher Turm gemeint ist
            Piece temp = getOtherColorPiece( startPiece );

            Board tempBoard = Board.createCopy( beforeboard );
            tempBoard.setPiece( endPos, temp );
            tempBoard.removePiece( startPos );
            Set<Position> pMoves = tempBoard.getLogic().getAllValidMoves( endPos );

            boolean considerRow = false;
            boolean considerCol = false;

            for ( Position p : pMoves )
            {
                if ( tempBoard.getPiece( p ) == startPiece )
                {
                    if ( p.getColumn() != startPos.getColumn() )
                    {
                        considerCol = true;
                    }
                    else
                    {
                        considerRow = true;
                    }

                }
            }
            if ( considerCol )
            {
                acronym += startPos.colToNotation();
            }
            if ( considerRow )
            {
                acronym += startPos.rowToNotation();
            }
            if ( hits )
            {
                acronym += "x";
            }
        }
        else
        { //Wenn es ein Bauer ist
            if ( hits )
            {
                acronym += startPos.colToNotation();
                acronym += "x";
            }
        }
        acronym += endPos.toNotation();
        return acronym;
    }

    Piece getOtherColorPiece( Piece piece )
    {
        ArrayList<Piece> firstcolor = new ArrayList<>();
        boolean b = Collections.addAll( firstcolor, WKING, WQUEEN, WBISHOP, WKNIGHT, WROCK, WPAWN, BKING, BQUEEN, BBISHOP, BKNIGHT, BROCK, BPAWN );
        Piece[] othercolor = { BKING, BQUEEN, BBISHOP, BKNIGHT, BROCK, BPAWN, WKING, WQUEEN, WBISHOP, WKNIGHT, WROCK, WPAWN };
        return othercolor[firstcolor.indexOf( piece )];
    }


    public boolean whiteIsMoving()
    {
        return ( beforeboard.getPiece( startPos ).getColor() == PieceColor.WHITE );
    }
}
