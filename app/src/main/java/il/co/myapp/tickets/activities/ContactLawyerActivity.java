package il.co.myapp.tickets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;

import il.co.myapp.tickets.R;
import il.co.myapp.tickets.data.AsyncLeadResponse;
import il.co.myapp.tickets.data.AsyncTicketResponse;
import il.co.myapp.tickets.data.TrafficTicketData;
import il.co.myapp.tickets.menu.MenuActivity;
import il.co.myapp.tickets.model.Lead;
import il.co.myapp.tickets.model.Ticket;

public class ContactLawyerActivity extends MenuActivity {

    private static final String TAG = ContactLawyerActivity.class.getSimpleName();

    private Button submitLeadButton;
    private TextView mainMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_lawyer);
        mainMessage = (TextView) findViewById(R.id.contactPageMessage);

        submitLeadButton = (Button) findViewById(R.id.sendButtonContactForm);
        submitLeadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText)findViewById(R.id.nameContactForm);
                EditText phone = (EditText)findViewById(R.id.phoneContactForm);
                EditText mail = (EditText)findViewById(R.id.mailContactForm);

                new Lead(
                        name.getText().toString(),
                        phone.getText().toString(),
                        mail.getText().toString()
                ).submitLead(getApplicationContext(),new AsyncLeadResponse() {
                    @Override
                    public void NewLeadResponseReceived(String response) {
                        if (response == "Success") {
                            Toast.makeText(getApplicationContext(),R.string.submitLeadSucess,
                                    Toast.LENGTH_LONG).show();
                            submitLeadButton.setEnabled(false);
                            mainMessage.setText(R.string.submitLeadSucess);
                        }
                        Log.w(TAG, response);
                    }
                });

            }
        });
    }
}
