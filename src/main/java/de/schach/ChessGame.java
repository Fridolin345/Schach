package de.schach;

import de.schach.gui.GUI;
import de.schach.gui.Language;
import de.schach.gui.language.LanguageSetting;
import de.schach.util.Debug;

import java.io.IOException;

public class ChessGame
{

    public static void main( String[] args ) throws IOException
    {
        Debug.setDebug( true );
        test();
        LanguageSetting.getInstance().change( Language.de_DE );
        new GUI();
    }

    private static void test()
    {
        //to do some tests
    }
}