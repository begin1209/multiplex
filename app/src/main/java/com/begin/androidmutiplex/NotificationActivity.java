package com.begin.androidmutiplex;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = NotificationActivity.class.getSimpleName();

    private NotificationManager mNotifyManager;

    private Button mShowRemoteView;

    private Button mShowCustomRemoteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mShowRemoteView = (Button)findViewById(R.id.remote_view_notification);
        mShowCustomRemoteView = (Button)findViewById(R.id.custom_notification);
        mNotifyManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mShowRemoteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotifyManager.notify(1, createRemoteView());
            }
        });
        mShowCustomRemoteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "显示自定义");
                mNotifyManager.notify(2, createCustomRemoteView());
            }
        });


    }

    /**
     * 默认通知效果
     * @return
     */
    private Notification createRemoteView(){
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("测试标题");
        mBuilder.setContentText("测试通知内容");
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setTicker("测试通知来了");
        mBuilder.setAutoCancel(true);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        return mBuilder.build();
    }

    /**
     * Notification的自定义布局是RemoteViews，和其他RemoteViews一样，
     * 在自定义视图布局文件中，仅支持FrameLayout、LinearLayout、
     * RelativeLayout三种布局控件和AnalogClock、Chronometer、Button、ImageButton、
     * ImageView、ProgressBar、TextView、ViewFlipper、ListView、GridView、
     * StackView和AdapterViewFlipper这些显示控件，不支持这些类的子类或Android提供的其他控件。
     * 否则会引起ClassNotFoundException异常
     * @return
     */
    private Notification createCustomRemoteView(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.custom_notificaton);
        mRemoteViews.setTextViewText(R.id.custom_title, "改变自定义标题");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.left_image, pendingIntent);
        mBuilder.setContent(mRemoteViews);
        mBuilder.setContentIntent(pendingIntent);
        //图标必须设置，否则不会显示通知,此Icon为状态栏显示图标，不影响自定义布局
        mBuilder.setSmallIcon(R.mipmap.logo_ucsmy);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setTicker("自定义测试通知来了");
        mBuilder.setAutoCancel(true);
        Log.v(TAG, "创建自定义通知完成");
        return mBuilder.build();
    }
}
