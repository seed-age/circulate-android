package com.ecology.view.seedland.circulate.view.contacts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ecology.view.seedland.circulate.R;


/**
 * Created by mgod on 5/27/15.
 * <p>
 * Simple custom view example to show how to get selected events from the token
 * view. See ContactsCompletionView and contact_token.xml for usage
 */
public class TokenTextView extends TextView {

    public TokenTextView(Context context) {
        super(context);
    }

    public TokenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setCompoundDrawablesWithIntrinsicBounds(0, 0, selected ? R.drawable.ic_cancel_white_24dp : 0, 0);
    }
}
