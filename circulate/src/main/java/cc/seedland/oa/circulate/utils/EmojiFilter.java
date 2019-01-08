package cc.seedland.oa.circulate.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Created by Administrator.
 * @time 2019/1/8 0008 14:08
 * Description:
 */


public class EmojiFilter implements InputFilter {

    private Pattern mEmojiPattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher emojiMatcher = mEmojiPattern.matcher (source) ;

        if (emojiMatcher.find( )) {
            LogUtil.e("source: " + source.toString() + " is match.");
            return "";
        }
        return source;
    }

}

