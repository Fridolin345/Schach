package de.schach.gui.language;

import de.schach.gui.Language;
import de.schach.gui.settings.Setting;

public class LanguageSetting implements Setting<Language>
{

    private static final LanguageSetting instance = new LanguageSetting();

    public static LanguageSetting getInstance()
    {
        return instance;
    }

    private Language language;

    private LanguageSetting()
    {
        this.language = Language.de_DE;
    }

    @Override
    public void change( Language newValue )
    {
        if ( newValue != null && newValue != this.language )
        {
            this.language = newValue;
            updateLanguage();
        }
    }

    @Override
    public Language current()
    {
        return this.language;
    }

    private void updateLanguage()
    {
        //TODO
    }

}