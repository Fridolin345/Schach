package de.schach.gui;

import de.schach.board.*;
import de.schach.util.Vector;

import java.util.Set;

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

        boolean hits = false;
        if ( beforeboard.isPieceAt( endPos ) )
        {
            hits = true;
        }

        String acronym = "";
        if ( startPiece.getPieceType() != PieceType.PAWN )
        {
            acronym += startPiece.getPieceType().getNotationChar();

            //Wenn z.B. zwei Türme auf das selbe Feld fahren können, muss klar gemacht werden, welcher Turm gemeint ist
            Piece temp = getOtherColorPiece( startPiece );

            Board tempBoard = new Board();
            tempBoard.setBoard( this.beforeboard.getCopy( true ) );
            tempBoard.setPiece( endPos, temp );
            tempBoard.removePieceAt( startPos.getRow(), startPos.getColumn() );
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
        return piece.invertColor();
    }

}
