package cc.seedland.oa.circulate.view.contacts;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.ImageView;
import android.widget.TextView;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.modle.bean.UserInfo;

/**
 * Sample token completion view for basic contact info
 *
 * Created on 9/12/13.
 * @author mgod
 */
public class ContactsCompletionView extends TokenCompleteTextView<UserInfo> {

    InputConnection testAccessibleInputConnection;

    public ContactsCompletionView(Context context) {
        super(context);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(final UserInfo person) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        TokenTextView token = (TokenTextView) l.inflate(R.layout.contact_token, (ViewGroup) getParent(), false);
        View view = l.inflate(R.layout.item_token_view, (ViewGroup) getParent(), false);
        TextView tvName = view.findViewById(R.id.tv_name);
//        token.setText(person.lastName);
        tvName.setText(person.lastName);
//        return token;
        return view;
    }

    @Override
    protected UserInfo defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        UserInfo userInfo = new UserInfo();
        userInfo.lastName = completionText;
        return userInfo;
    }

    @Override
    public InputConnection onCreateInputConnection(@NonNull EditorInfo outAttrs) {
        testAccessibleInputConnection = super.onCreateInputConnection(outAttrs);
        return testAccessibleInputConnection;
    }
}
