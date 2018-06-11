package il.co.myapp.tickets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import il.co.myapp.tickets.R;
import il.co.myapp.tickets.menu.MenuActivity;

public class WelcomeActivity extends MenuActivity {

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OptionsActivity.class);
                startActivity(intent);
            }
        });
    }
}
