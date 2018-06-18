package il.co.myapp.tickets.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import il.co.myapp.tickets.R;
import il.co.myapp.tickets.data.AsyncLeadResponse;
import il.co.myapp.tickets.model.Lead;

public class ContactUsActivity extends AppCompatActivity {

    private static final String TAG = ContactUsActivity.class.getSimpleName();
    private Button submitLeadButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        submitLeadButton = (Button) findViewById(R.id.sendButtonContactUs);
        submitLeadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText)findViewById(R.id.nameContactUs);
                EditText phone = (EditText)findViewById(R.id.phoneContactUs);
                EditText mail = (EditText)findViewById(R.id.mailContactUs);

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
                        }
                        Log.w(TAG, response);
                    }
                });

            }
        });
    }
}
