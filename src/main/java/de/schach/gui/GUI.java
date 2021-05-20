package de.schach.gui;

import de.schach.board.Board;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GUI extends JFrame
{

    public static DrawNotation notation = new DrawNotation();

    private final Board gameBoard = Board.create();
    JPanel rightSide;
    JPanel leftSide;
    public GUI() throws IOException
    {
        this.setLayout( new BorderLayout() );
        this.setSize( 1400, 900 );
        this.setLocationRelativeTo( null );
        this.setDefaultCloseOperation( EXIT_ON_CLOSE );

        Border b1 = BorderFactory.createLineBorder( Color.red, 5 );
        Border b2 = BorderFactory.createLineBorder( Color.green, 2 );

        DrawField drawField = new DrawField( gameBoard );
        drawField.setBounds( 10, 50, 510, 510 );
        drawField.setBorder( b1 );

        leftSide = new JPanel();
        leftSide.setPreferredSize( new Dimension( 550, 1000 ) );
        leftSide.setLayout( null );
        leftSide.add( drawField );

        rightSide = new JPanel();
        rightSide.setPreferredSize( new Dimension( 550, 800 ) );
        rightSide.setLayout( null );
        notation.setBounds( 0, 0, 600, 800 );
        rightSide.add( notation );


        this.add( leftSide, BorderLayout.WEST );
        //this.add( new Button(), BorderLayout.CENTER );
        this.add( rightSide, BorderLayout.CENTER );
        this.setVisible( true ); //test
        repaint();
    }

}
