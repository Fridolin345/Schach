package de.schach.game;

import de.schach.board.PieceColor;
import de.schach.board.Position;

public abstract class Player
{

    private final PieceColor playColor;
    private final Game game;

    public Player( Game game, PieceColor playColor )
    {
        this.game = game;
        this.playColor = playColor;
    }

    public final PieceColor getPlayColor()
    {
        return playColor;
    }

    public void movePiece( Position from, Position to )
    {
        game.movePiece( this, from, to );
    }

}