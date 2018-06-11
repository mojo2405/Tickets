package il.co.myapp.tickets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


import org.json.JSONObject;

import il.co.myapp.tickets.R;
import il.co.myapp.tickets.controller.AppController;
import il.co.myapp.tickets.data.AsyncLoginResponse;
import il.co.myapp.tickets.model.User;

public class LoginActivity extends FragmentActivity
{
    private static final String TAG = LoginActivity.class.getSimpleName();

    private User user = new User();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.nextButtonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSuccess(user);
            }
        });



        findViewById(R.id.nextButtonLogin).setVisibility(View.GONE);
        findViewById(R.id.hiLoginTextPage).setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if signed in in Facebook
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            Log.v(TAG,"FACEBOOK TOKEN IS " + accessToken.getToken());
            getFacebookDetails(accessToken);

        }

        // Check if signed in in Google
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {

        } else {


            Log.v(TAG,"GOOGLE TOKEN IS " + account.getIdToken());
            user.setName(account.getDisplayName());
            user.setEmail(account.getEmail());

            setHelloText();

            findViewById(R.id.facebook_login_fragment).setVisibility(View.GONE);
            findViewById(R.id.nextButtonLogin).setVisibility(View.VISIBLE);
        }

    }



    public void getFacebookDetails(final AccessToken accessToken) {



        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject,
                                            GraphResponse response) {

                        // Getting FB User Data
                        Bundle facebookData = getFacebookData(jsonObject);
                        user.setName(facebookData.getString("name"));
                        user.setEmail(facebookData.getString("email"));
                        user.setAccessToken(accessToken.getToken());
                        setHelloText();

                        findViewById(R.id.google_login_fragment).setVisibility(View.GONE);
                        findViewById(R.id.nextButtonLogin).setVisibility(View.VISIBLE);

//                        loginSuccess(user);


                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");


            bundle.putString("idFacebook", id);
            if (object.has("name"))
                bundle.putString("name", object.getString("name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));




        } catch (Exception e) {
            Log.d(TAG, "BUNDLE Exception : "+e.toString());
        }

        return bundle;
    }

    private void setHelloText () {
        findViewById(R.id.hiLoginTextPage).setVisibility(View.VISIBLE);
        TextView textView = (TextView) findViewById(R.id.hiLoginTextPage);
        String message = (String) textView.getText();
        textView.setText(String.format(message, user.getName()));
    }



    public void loginSuccess (final User user) {


        user.preformRegister(this, new AsyncLoginResponse() {
            @Override
            public void LoginResponseReceived(String response) {
                if (response == "Success") {
//                    progressBar.setVisibility(View.GONE);
                    AppController.getInstance().setUser(user);
                    startActivity(new Intent(LoginActivity.this, TicketDetailsActivity.class));
                } else {
//                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),
                            response, Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
