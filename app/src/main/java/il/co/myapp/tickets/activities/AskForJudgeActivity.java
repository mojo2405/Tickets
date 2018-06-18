package il.co.myapp.tickets.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import il.co.myapp.tickets.R;

public class AskForJudgeActivity extends AppCompatActivity {

    private static final String TAG = AskForJudgeActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_judge);
    }
}
