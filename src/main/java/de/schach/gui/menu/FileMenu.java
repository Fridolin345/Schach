package de.schach.gui.menu;

import de.schach.gui.Language;
import de.schach.gui.language.LanguageSetting;
import de.schach.util.Debug;

import javax.swing.*;

public class FileMenu extends JMenu
{

    public FileMenu()
    {
        super( "File" );
        createButtons();
    }

    void createButtons()
    {

        removeAll();

        Language lang = LanguageSetting.getInstance().current();

        JMenuItem open = new JMenuItem( lang.text( "open", "Open" ) + "..." );
        JMenuItem openRecently = new JMenuItem( lang.text( "open_recently", "Recently Opened" ) ); //doesnt really work like this
        JMenuItem save = new JMenuItem( lang.text( "save", "Save" ) + "..." );
        JMenuItem print = new JMenuItem( lang.text( "print", "Print" ) + "..." );
        JMenuItem account = new JMenuItem( lang.text( "account", "Account" ) + "..." );
        JMenuItem exit = new JMenuItem( lang.text( "exit", "Exit" ) + "..." );

        open.addActionListener( ( e ) -> open() );
        save.addActionListener( ( e ) -> save() );
        print.addActionListener( ( e ) -> print() );
        account.addActionListener( ( e ) -> account() );
        exit.addActionListener( ( e ) -> exit() );

        add( open );
        add( openRecently );
        add( save );
        add( print );
        add( account );
        addSeparator();
        add( exit );

    }

    void open()
    {
        Debug.log( "File -> Open" );
    }

    void save()
    {
        Debug.log( "File -> Save" );
    }

    void print()
    {
        Debug.log( "File -> Print" );
    }

    void account()
    {
        Debug.log( "File -> Account" );
    }

    void exit()
    {
        //standard java exit with no errors (0 = code for no errors)
        System.exit( 0 );
    }

}