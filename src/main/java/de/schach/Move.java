package de.schach;

import java.util.Set;

public class Move
{

    Position startPos, endPos;
    Board beforeboard;

    public Move( Position startPos, Position endPos, Board beforeboard )
    {
        this.startPos = startPos;
        this.endPos = endPos;
        this.beforeboard = beforeboard;
    }

    public String getAcronym()
    {
        String acronym = "";
        Piece startPiece = beforeboard.getPiece( startPos );

        switch ( startPiece.getPieceType() )
        {
            case KING:
                acronym += 'K';
                break;
            case QUEEN:
                acronym += 'Q';
                break;
            case ROCK:
                acronym += 'R';
                break;
            case BISHOP:
                acronym += 'B';
                break;
            case KNIGHT:
                acronym += 'N';
                break;
            case PAWN:
                break;
        }

        boolean hits = false;
        if(beforeboard.isPieceAt( endPos ))
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
        Set<Position> pMoves = BoardLogic.getAllValidMoves( endPos ); //Wo wird da das Brett berücksichtigt? Wird da nur das Haupt-Brett angeschaut?

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
            acronym += colForNotion( startPos.getColumn() );
        }
        if ( considerRow )
        {
            acronym += rowForNotion( startPos.getRow() );
        }
        if( hits) {
            acronym += "x";
        }
        acronym += colForNotion( endPos.getColumn() );
        acronym += rowForNotion( endPos.getRow() );

        System.out.println(acronym);
        return acronym;
    }


    public char rowForNotion( int row )
    {
        switch ( row )
        {
            case 0:
                return '1';
            case 1:
                return '2';
            case 2:
                return '3';
            case 3:
                return '4';
            case 4:
                return '5';
            case 5:
                return '6';
            case 6:
                return '7';
            case 7:
                return '8';
        }
        System.out.println("row out of bounds");
        return '%';
    }

    public char colForNotion( int column )
    {
        switch ( column )
        {
            case 0:
                return 'a';
            case 1:
                return 'b';
            case 2:
                return 'c';
            case 3:
                return 'd';
            case 4:
                return 'e';
            case 5:
                return 'f';
            case 6:
                return 'g';
            case 7:
                return 'h';
        }
        System.out.println( "col out of bounds" );
        return '%';
    }
}
