package de.schach;

import java.util.HashSet;
import java.util.Set;

public enum PieceType
{

    KING( 1, 'k', true, new Vector( 1, 1 ), new Vector( 1, 0 ), new Vector( 0, 1 ) ),
    QUEEN( 2, 'q', false, new Vector( 1, 1 ), new Vector( 1, 0 ), new Vector( 0, 1 ) ),
    BISHOP( 3, 'b', false, new Vector( 1, 1 ) ),
    KNIGHT( 4, 'n', true, new Vector( 2, 1 ), new Vector( 1, 2 ) ),
    ROCK( 5, 'r', false, new Vector( 1, 0 ), new Vector( 0, 1 ) ),
    PAWN( 6, 'p', true, new Vector( 0, 1 ) );

    private final byte representative;
    private final char fenChar;
    private final boolean onlyOneStep;
    private final Vector[] baseMoveVectors; //Nur eine Richtung | Nur Basis-Vektoren

    PieceType( int representative, char fenChar, boolean onlyOneStep, Vector... baseMoveVectors )
    {
        this.representative = (byte) representative;
        this.fenChar = fenChar;
        this.baseMoveVectors = baseMoveVectors;
        this.onlyOneStep = onlyOneStep;
    }

    public static PieceType fromChar( char fenData )
    {
        for ( PieceType type : values() )
        {
            if ( type.fenChar == fenData )
                return type;
        }
        return null;
    }

    public static PieceType fromByte( byte data )
    {
        for ( PieceType type : values() )
        {
            if ( type.representative == data )
                return type;
        }
        return null;
    }

    public char getFenChar()
    {
        return fenChar;
    }

    public byte getRepresentative()
    {
        return representative;
    }

    private Set<Vector> getMoveVectors( Position position, Vector offensiveDirection )
    {
        Set<Vector> moves = new HashSet<>();
        if ( this == PAWN )
        {
            //Forward
            moves.add( offensiveDirection );
            //Schräg zum Schlagen
            moves.add( offensiveDirection.add( new Vector( 1, 0 ) ) );
            moves.add( offensiveDirection.add( new Vector( -1, 0 ) ) );
            //2 Forward
            moves.add( offensiveDirection.multiply( 2 ) );
        }
        else
        {
            int reach = onlyOneStep ? 1 : 8;
            for ( Vector vector : baseMoveVectors )
            {
                Vector oneWay = new Vector( vector ).multiply( reach ); //just up
                Vector otherWay = new Vector( vector ).multiply( -reach ); //invert x,y

                Vector oneWaySide = new Vector( vector ); //invert x
                oneWaySide.setX( oneWaySide.getX() * -1 );
                oneWaySide = oneWaySide.multiply( reach );

                Vector otherWaySide = new Vector( vector ); //invert y
                otherWaySide.setY( otherWaySide.getY() * -1 );
                otherWaySide = otherWaySide.multiply( reach );
            }
        }
        return moves;
    }

    public Set<Position> getAllPossibleMoves( Position position, Vector offensiveDirection )
    {
        return null; //TODO
    }

    public Set<Position> getAllPossibleMoves( Position position, int opponentRow )
    {
        Set<Position> positions = new HashSet<>();
        if ( this == PAWN )
        {
            int rowDiff = position.getRow() - opponentRow;
            int direction = rowDiff / Math.abs( rowDiff );
            //Forward
            positions.add( position.move( new Vector( 0, direction ) ) );
            //Schräg zum Schlagen
            positions.add( position.move( new Vector( 1, direction ) ) );
            positions.add( position.move( new Vector( -1, direction ) ) );
            //2 Forward
            positions.add( position.move( new Vector( 0, direction ).multiply( 2 ) ) );
        }
        else
        {
            //Für alle Basis-Richtungen
            for ( Vector vector : baseMoveVectors )
            {
                Vector current = vector;
                for ( int i = 0; i < ( onlyOneStep ? 1 : 8 ); i++ )
                {
                    Vector oneWay = new Vector( current ); //just up
                    Vector otherWay = new Vector( current ).multiply( -1 ); //invert x,y
                    Vector oneWaySide = new Vector( current ); //invert x
                    oneWaySide.setX( oneWaySide.getX() * -1 );
                    Vector otherWaySide = new Vector( current ); //invert y
                    otherWaySide.setY( otherWaySide.getY() * -1 );

                    positions.add( position.move( oneWay ) );
                    positions.add( position.move( oneWaySide ) );
                    positions.add( position.move( otherWay ) );
                    positions.add( position.move( otherWaySide ) );

                    current = current.add( vector );
                }
            }
        }
        //Alle Positionen außerhalb des Brettes entfernen
        positions.removeIf( pos -> !pos.isValidOnBoard() );
        return positions;
    }

}