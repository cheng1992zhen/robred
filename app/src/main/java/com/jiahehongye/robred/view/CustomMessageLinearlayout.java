package com.jiahehongye.robred.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiahehongye.robred.R;

/**
 * Created by Administrator on 2016/12/1.
 */
public class CustomMessageLinearlayout extends LinearLayout {

    private TextView cMessageRedIcon;
    private TextView cMessageTvNumber;
    private TextView cMessageTvTimeDate;

    public CustomMessageLinearlayout(Context context) {
        super(context, null);
        init(context, null);

    }

    public CustomMessageLinearlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomMessageLinearlayout);
        Drawable messageIcon = ta.getDrawable(R.styleable.CustomMessageLinearlayout_messageIcon);
        String messageTimeDate = ta.getString(R.styleable.CustomMessageLinearlayout_messageTimeDate);
        String messageTypeDes = ta.getString(R.styleable.CustomMessageLinearlayout_messageTypeDes);
        String messageTypeName = ta.getString(R.styleable.CustomMessageLinearlayout_messageTypeName);
        ta.recycle();

        LayoutInflater.from(context).inflate(R.layout.custom_message_linearlayout, this);
        ImageView cMessageIvIcon = (ImageView) findViewById(R.id.message_iv_icon);
        TextView cMessageTvTypeName = (TextView) findViewById(R.id.message_tv_typename);
        TextView cMessageTvTypeDes = (TextView) findViewById(R.id.message_tv_typedes);
        cMessageTvTimeDate = (TextView) findViewById(R.id.message_tv_date);
        cMessageRedIcon = (TextView) findViewById(R.id.message_iv_red_icon);

        if (messageIcon != null) {
            cMessageIvIcon.setImageDrawable(messageIcon);
        }
        cMessageTvTypeName.setText(messageTypeName);
        cMessageTvTypeDes.setText(messageTypeDes);
        cMessageTvTimeDate.setText(messageTimeDate);

    }

    public void setUnreadCount(int unreadCount) {
        cMessageTvNumber.setText(String.valueOf(unreadCount));
    }

    public void setDate(String date) {
        if (cMessageTvTimeDate != null && date != null) {
            cMessageTvTimeDate.setText(date.toString());
        }
    }

    public void showUnreadMsgView() {
        if (cMessageRedIcon != null) {
            cMessageRedIcon.setVisibility(View.VISIBLE);
        }

    }

    public void hideUnreadMsgView() {
        if (cMessageRedIcon != null) {
            cMessageRedIcon.setVisibility(View.INVISIBLE);
        }
    }


}
