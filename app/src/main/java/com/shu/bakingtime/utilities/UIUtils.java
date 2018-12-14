package com.shu.bakingtime.utilities;

import android.content.Context;

import com.amulyakhare.textdrawable.TextDrawable;
import com.shu.bakingtime.R;

public class UIUtils {

    public static TextDrawable createInitialLetterDrawable(char letter, Context context ){
        TextDrawable drawable = TextDrawable.builder()
                .buildRect(Character.toString(letter), context.getResources().getColor(R.color.colorPrimaryDark));
        return drawable;
    }

    public static char getFirstLetter(String name) {
        if(name == null)
            return '?';
        return name.charAt(0);
    }
}
