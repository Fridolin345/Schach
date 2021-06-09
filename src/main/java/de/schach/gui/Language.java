package de.schach.gui;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public enum Language
{

    de_DE,
    en_GB;

    public Properties load() throws IOException
    {
        Properties lang = new Properties();
        FileInputStream inStream = new FileInputStream( "assets\\lang\\" + name() + ".properties" );
        lang.load( new InputStreamReader( inStream, StandardCharsets.UTF_8 ) );
        return lang;
    }

    public String text( String key, String defaultString )
    {
        try
        {
            Properties load = load();
            return load.getProperty( key, defaultString );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        return defaultString;
    }

}