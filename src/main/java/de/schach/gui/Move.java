package de.schach.gui;

import de.schach.board.*;
import de.schach.util.Vector;

import java.util.Set;

public class Move
{

    private final Position startPos;
    private final Position endPos;
    private final Board beforeboard;

    public Move( Position startPos, Position endPos, Board beforeboard )
    {
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
        String acronym = "";
        Piece startPiece = beforeboard.getPiece( startPos );

        if ( startPiece.getPieceType() != PieceType.PAWN )
        {
            acronym += startPiece.getPieceType().getNotationChar();
        }

        boolean hits = false;
        if ( beforeboard.isPieceAt( endPos ) )
        {
            hits = true;
        }

        //Wenn z.B. zwei Türme auf das selbe Feld fahren können, muss klar gemacht werden, welcher Turm gemeint ist
        Piece temp = startPiece;
        if ( startPiece.getColor() == PieceColor.WHITE ) //Farbe umdrehen, damit possibleMoves funktioniert
        {
            temp.setColor( PieceColor.BLACK );
        }
        else
        {
            temp.setColor( PieceColor.WHITE );
        }

        beforeboard.setPiece( endPos, temp );
        Set<Position> pMoves = beforeboard.getLogic().getAllValidMoves( endPos ); //Wo wird da das Brett berücksichtigt? Wird da nur das Haupt-Brett angeschaut?

        boolean considerRow = false;
        boolean considerCol = false;

        for ( Position p : pMoves )
        {
            if ( beforeboard.getPiece( p ) == startPiece && startPos != p )
            {
                if ( p.getRow() == startPos.getRow() ) //Wenn Figuren in selber Reihe
                {
                    considerCol = true; //dann muss die Spalte bei der Notation angegeben werden
                }
                if ( p.getColumn() == startPos.getColumn() )
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
        acronym += endPos.toNotation();

        System.out.println( acronym );
        return acronym;
    }
}
