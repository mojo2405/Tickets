package il.co.myapp.tickets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import il.co.myapp.tickets.R;

public class OptionsActivity extends AppCompatActivity {

    Button cancelReportButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        cancelReportButton = (Button) findViewById(R.id.cancelReport);
        cancelReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.askForLawyerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ContactLawyerActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.askForJudgeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ContactLawyerActivity.class);
                startActivity(intent);
            }
        });
    }
}
