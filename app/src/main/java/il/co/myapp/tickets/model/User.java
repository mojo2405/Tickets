package il.co.myapp.tickets.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.util.HashMap;

import il.co.myapp.tickets.TicketDetailsActivity;
import il.co.myapp.tickets.controller.AppController;
import il.co.myapp.tickets.controller.NetworkController;
import il.co.myapp.tickets.data.AsyncLoginResponse;
import il.co.myapp.tickets.data.URLS;
import il.co.myapp.tickets.utils.ParseNetworkError;

public class User {

    private String email;
    private String name;
    private String accessToken;


    private static final String TAG = User.class.getSimpleName();

    public User () {

    }

    public User(String namw, String email) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void preformRegister (Context context, final AsyncLoginResponse loginCallBack) {


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = URLS.SOCIAL_LOGIN_URL;

        // Request a string response from the provided URL.
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(getLoginParams()), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    AppController.getInstance().set_token(response.getString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null != loginCallBack) loginCallBack.LoginResponseReceived("Success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorResponse = ParseNetworkError.GetErrorMessage(error);
                if (null != loginCallBack) loginCallBack.LoginResponseReceived(errorResponse);
            }
        });

        NetworkController.getInstance(context).addToRequestQueue(loginRequest);

    }

    private HashMap<String, String> getLoginParams() {
        HashMap<String, String> loginParams = new HashMap<>();
        loginParams.put("name", getName());
        loginParams.put("access_token", getAccessToken());
        loginParams.put("email", getEmail());
//        loginParams.put("pushToken", FirebaseInstanceId.getInstance().getToken());
        return loginParams;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
