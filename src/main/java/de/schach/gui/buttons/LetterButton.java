package de.schach.gui.buttons;

import javax.swing.*;
import java.awt.*;

public class LetterButton extends JButton
{

    public LetterButton( char letter )
    {
        super( String.valueOf( letter ) );
        setBackground( Color.WHITE );
    }

}