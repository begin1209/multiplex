package com.begin.androidmutiplex.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.begin.androidmutiplex.R;
import com.begin.androidmutiplex.interfaces.ICDialogListener;
import com.begin.androidmutiplex.ui.base.BaseDialog;


/**
 * 通用Dialog自定义,统一对话框风格
 * @Author zhouy
 * @Date 2017-09-22
 */

public class CommonDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private View mLayoutView;
    private TextView mTitleView;
    private TextView mMsgView;
    private TextView mPosView;
    private TextView mNegView;
    private ICDialogListener mICDialogListener;

    public CommonDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CommonDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public void setICDialogListener(ICDialogListener listener) {
        this.mICDialogListener = listener;
    }

    private void init(Context context){
        mContext = context;
        mLayoutView = LayoutInflater.from(mContext).inflate(R.layout.dialog_common, null);
        setContentView(mLayoutView);
        setCanceledOnTouchOutside(false);
        mTitleView = (TextView)mLayoutView.findViewById(R.id.common_dialog_title);
        mMsgView = (TextView)mLayoutView.findViewById(R.id.common_dialog_msg);
        mPosView = (TextView)mLayoutView.findViewById(R.id.common_dialog_confirm);
        mPosView.setOnClickListener(this);
        mNegView = (TextView)mLayoutView.findViewById(R.id.common_dialog_cancel);
        mNegView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_dialog_cancel:
                if(null != mICDialogListener){
                    mICDialogListener.cancel(this);
                }
                break;
            case R.id.common_dialog_confirm:
                if(null != mICDialogListener){
                    mICDialogListener.ok(this);
                }
                break;
        }
    }


    public static class Builder{
        private String title;
        private String message;
        private String positiveText;
        private String negativeText;
        private Context mContext;


        public Builder(Context context){
            mContext = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }


        public Builder setPositiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder setNegativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public CommonDialog build(){
            CommonDialog dialog = new CommonDialog(mContext);
            dialog.mTitleView.setText(title);
            dialog.mMsgView.setText(message);
            dialog.mPosView.setText(positiveText);
            dialog.mNegView.setText(negativeText);
            return dialog;
        }
    }
}
