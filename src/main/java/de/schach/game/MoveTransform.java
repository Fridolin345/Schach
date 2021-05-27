package de.schach.game;

import de.schach.board.*;

public class MoveTransform
{

    private Position start;
    private Position target;
    private Piece pieceToMove;

    public MoveTransform( Position start, Position target, Piece pieceToMove )
    {
        this.start = start;
        this.target = target;
        this.pieceToMove = pieceToMove;
    }

    //{color}{type}{startrow}{startcol}{targetrow}{targetcol}
    //target length: 6 chars
    public String encode()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( pieceToMove.getColor().name().toLowerCase().charAt( 0 ) )
                .append( pieceToMove.getPieceType().getFenChar() )
                .append( start.getRow() )
                .append( start.getColumn() )
                .append( target.getRow() )
                .append( target.getColumn() );
        return builder.toString();
    }

    //{color}{type}{startrow}{startcol}{targetrow}{targetcol}
    //source length: 6 chars
    public static MoveTransform decode( String encoded )
    {
        char color = encoded.charAt( 0 );
        char type = encoded.charAt( 1 );
        char startrow = encoded.charAt( 2 );
        char startcol = encoded.charAt( 3 );
        char targetrow = encoded.charAt( 4 );
        char targetcol = encoded.charAt( 5 );
        return new MoveTransform( Position.ofBoard( Integer.parseInt( String.valueOf( startrow ) ), Integer.parseInt( String.valueOf( startcol ) ) ),
                Position.ofBoard( Integer.parseInt( String.valueOf( targetrow ) ), Integer.parseInt( String.valueOf( targetcol ) ) ),
                Piece.getPiece( PieceType.fromChar( type ), color == 'w' ? PieceColor.WHITE : color == 'b' ? PieceColor.BLACK : null ) );
    }

}