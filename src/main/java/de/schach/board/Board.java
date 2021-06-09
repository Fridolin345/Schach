package de.schach.board;

import de.schach.exception.FenSyntaxException;
import de.schach.logic.BoardLogic;
import de.schach.util.Vector;
import de.schach.util.*;

import java.util.*;

public class Board
{

    public static final Position TOP_LEFT_CORNER = Position.ofBoard( 0, 0 );
    public static final Position TOP_RIGHT_CORNER = Position.ofBoard( 0, 7 );
    public static final Position BOTTOM_LEFT_CORNER = Position.ofBoard( 7, 0 );
    public static final Position BOTTOM_RIGHT_CORNER = Position.ofBoard( 7, 7 );

    private byte[] board;
    private Position enPassantPosition = null;
    private boolean[] allowedCastles = new boolean[4]; // [K,Q,k,q]

    private PieceColor currentlyAtTurn;

    public Board()
    {
        loadFromFen( "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" );
    }

    private Board( Board board )
    {
        this.board = Arrays.copyOf( board.board, board.board.length );
        this.enPassantPosition = board.enPassantPosition;
        this.allowedCastles = board.allowedCastles;
        this.enPassantPosition = board.enPassantPosition;
        this.currentlyAtTurn = board.currentlyAtTurn;
    }

    public static Board createCopy( Board board )
    {
        return new Board( board );
    }

    public static Board create()
    {
        return new Board();
    }

    public void reset()
    {
        board = new byte[8 * 8];
    }

    public void setBoard( byte[] board )
    {
        this.board = board;
    }

    public Board getInvertedCopy()
    {
        Board board = new Board( this );
        board.board = ArrayUtil.rowFlippedBoard( this.board );
        return board;
    }

    public BoardLogic getLogic()
    {
        return BoardLogic.ofBoard( this );
    }

    public boolean isEnPassant( Position position )
    {
        return position.equals( this.enPassantPosition );
    }

    public void setEnPassantPosition( Position enPassantPosition )
    {
        this.enPassantPosition = enPassantPosition;
        Debug.log( "New EnPassant position:" + this.enPassantPosition );
    }

    public void unsetEnPassantPosition()
    {
        this.enPassantPosition = null;
    }

    public boolean isAllowedToCastle( PieceColor color, boolean kingSide )
    {
        return kingSide ? allowedCastles[( color == PieceColor.BLACK ? 2 : 0 )] : allowedCastles[( color == PieceColor.BLACK ? 3 : 1 )];
    }

    public void setNotAllowedToCastle( PieceColor color, boolean kingSide )
    {
        if ( kingSide )
            allowedCastles[( color == PieceColor.BLACK ? 2 : 0 )] = false;
        else
            allowedCastles[( color == PieceColor.BLACK ? 3 : 1 )] = false;
    }

    public Piece getPiece( Position position )
    {
        byte pieceData = board[position.getBoardPosition()];
        return Piece.fromByte( pieceData );
    }

    public boolean isPieceAt( Position position )
    {
        return position != null && board[position.getBoardPosition()] != 0;
    }

    public void setPiece( Position position, Piece piece )
    {
        board[position.getBoardPosition()] = piece == null ? 0 : piece.toByte();
    }

    public void removePiece( Position position )
    {
        setPiece( position, null );
    }

    public void movePiece( Position from, Position to )
    {
        Piece piece = getPiece( from );
        setPiece( to, piece );
        setPiece( from, null );
        getLogic().postMoveChanges( piece, from, from.getDiff( to ) );
    }

    public List<Position> getAllPiecesForOneColor( PieceColor pc )
    {
        ArrayList<Position> allPositions = new ArrayList<>();

        Board.TOP_LEFT_CORNER.iterateTo( BOTTOM_RIGHT_CORNER, position ->
        {
            Piece temp = getPiece( position );
            if ( temp.getColor() == pc )
            {
                allPositions.add( position );
            }
        } );
        return allPositions;
    }

    public PieceColor topColor()
    {
        return PieceColor.BLACK;
    }

    //changing turn, so the other player can move
    //white -> black and black -> white
    public void changeTurn()
    {
        currentlyAtTurn = currentlyAtTurn.invert();
    }

    //who is currently allowed to move a piece
    public PieceColor whosTurn()
    {
        return currentlyAtTurn;
    }

    public Vector getOffensiveDirection( PieceColor color )
    {
        return color == PieceColor.WHITE ? new Vector( 0, -1 ) : new Vector( 0, 1 );
    }

    public PieceColor pieceColorAt( Position position )
    {
        return !isPieceAt( position ) ? null : getPiece( position ).getColor();
    }

    public void loadFromFen( String fen )
    {

        reset();
        String squares = fen.substring( 0, fen.indexOf( ' ' ) );
        String data = fen.substring( fen.indexOf( ' ' ) + 1 );

        String[] settings = data.split( " " );

        //Who's turn
        boolean whitesTurn = settings[0].equalsIgnoreCase( "w" );
        currentlyAtTurn = whitesTurn ? PieceColor.WHITE : PieceColor.BLACK;
        Debug.log( "currentTurn: " + currentlyAtTurn.name() );

        //Castles
        Arrays.fill( allowedCastles, false );
        if ( !settings[1].equalsIgnoreCase( "-" ) )
        {
            for ( int i = 0; i < settings[1].length(); i++ )
            {
                switch ( settings[1].charAt( i ) )
                {
                    case 'K':
                        allowedCastles[0] = true;
                        break;
                    case 'Q':
                        allowedCastles[1] = true;
                        break;
                    case 'k':
                        allowedCastles[2] = true;
                        break;
                    case 'q':
                        allowedCastles[3] = true;
                        break;
                }
            }
        }

        //EnPassant
        if ( !settings[2].equalsIgnoreCase( "-" ) )
        {
            this.enPassantPosition = Position.ofNotation( settings[2] );
        }
        else this.enPassantPosition = null;

        String[] ranks = squares.split( "/" );

        if ( !whitesTurn )
            ArrayUtil.invertArray( ranks ); //black should always be top

        Scanner scanner;
        int currentField = 0;
        for ( String rank : ranks )
        {
            scanner = new Scanner( rank );
            scanner.useDelimiter( "\\r*" );
            while ( scanner.hasNext() )
            {
                if ( scanner.hasNext( "\\d" ) ) //number
                {
                    int amount = scanner.nextInt();
                    for ( int i = 0; i < amount; i++ )
                    {
                        board[currentField++] = 0;
                    }
                }
                else if ( scanner.hasNext( "\\w" ) ) //letter
                {
                    char pieceToken = scanner.next().charAt( 0 );
                    PieceType type = PieceType.fromChar( Character.toLowerCase( pieceToken ) );
                    PieceColor color = Character.isUpperCase( pieceToken ) ? PieceColor.WHITE : PieceColor.BLACK;
                    Piece piece = Piece.getPiece( type, color );
                    board[currentField++] = piece.toByte();
                }
                else
                {
                    throw new FenSyntaxException( "Invalid rank in FEN string: \"" + rank + "\"; cant read \"" + scanner.next() + "\"" );
                }
            }
        }

    }

}
