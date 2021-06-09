package de.schach.gui.buttons;

import de.schach.util.Debug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayButton extends JButton implements ActionListener
{

    public PlayButton()
    {
        super( "Play" );
        setBackground( Color.WHITE );
        addActionListener( this );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        //play button was pressed
        Debug.log( "Play was pressed" );
    }

}
