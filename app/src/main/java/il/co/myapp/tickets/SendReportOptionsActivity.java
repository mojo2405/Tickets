package il.co.myapp.tickets;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import il.co.myapp.tickets.fragments.CameraActionFragment;


public class SendReportOptionsActivity extends FragmentActivity {

    private static final String TAG = SendReportOptionsActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report_options);

        TextView textView = (TextView) findViewById(R.id.hiText);
        String message = (String) textView.getText();

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        textView.setText(String.format(message, name));


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}

