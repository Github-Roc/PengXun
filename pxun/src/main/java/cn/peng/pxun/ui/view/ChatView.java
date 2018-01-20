package cn.peng.pxun.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.peng.pxun.utils.ScreenUtil;

/**
 * Created by msi on 2017/1/1.
 */

public class ChatView extends LinearLayout{
    private Context mContext;

    public ChatView(Context context) {
        this(context,null);
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public TextView getTextView(int textSize, int textColor){
        TextView tv = new TextView(mContext);
        tv.setTextSize(textSize);
        tv.setTextColor(textColor);
        addView(tv);
        return tv;
    }

    public ImageView getImageView(int width,int height){
        ImageView iv = new ImageView(mContext);
        LayoutParams linearParams = new LayoutParams(ScreenUtil.dip2px(mContext,width),ScreenUtil.dip2px(mContext,height));
        linearParams.bottomMargin = ScreenUtil.dip2px(mContext,3);
        iv.setLayoutParams(linearParams);
        addView(iv);
        return iv;
    }
}
