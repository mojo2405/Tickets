package il.co.myapp.tickets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import il.co.myapp.tickets.R;
import il.co.myapp.tickets.menu.MenuActivity;

public class NewTicketOptionsActivity extends MenuActivity{


    private ImageButton captureReportImage, newTicketIdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ticket_options);

        captureReportImage = findViewById(R.id.newTicketCaptureReportId);
        captureReportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTicketOptionsActivity.this, NewTicketActivity.class);
                intent.putExtra("startCamera", true);
                startActivity(intent);
            }
        });


        newTicketIdButton = findViewById(R.id.newTicketIdButton);
        newTicketIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewTicketOptionsActivity.this, NewTicketActivity.class));
            }
        });

    }
}
