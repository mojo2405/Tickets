package il.co.myapp.tickets.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.HashMap;

import il.co.myapp.tickets.activities.ContactLawyerActivity;
import il.co.myapp.tickets.controller.AppController;
import il.co.myapp.tickets.controller.NetworkController;
import il.co.myapp.tickets.data.AsyncLeadResponse;
import il.co.myapp.tickets.data.URLS;
import il.co.myapp.tickets.utils.ParseNetworkError;

public class Lead {

    private String name;
    private String phone;
    private String email;

    private static final String TAG = Lead.class.getSimpleName();

    public Lead(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }



    public Lead () {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void submitLead(final Context applicationContext, final AsyncLeadResponse asyncLeadResponse) {
        // Request a string response from the provided URL.
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST,
                URLS.ADD_LEAD_URL, new JSONObject(getLeadParams()), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v(TAG,"Success");
                if (null != asyncLeadResponse) asyncLeadResponse.NewLeadResponseReceived("Success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorResponse = ParseNetworkError.GetErrorMessage(error);
                Toast.makeText(applicationContext,errorResponse,Toast.LENGTH_LONG).show();
                if (null != asyncLeadResponse) asyncLeadResponse.NewLeadResponseReceived(errorResponse);
            }
        });

        NetworkController.getInstance(applicationContext).addToRequestQueue(loginRequest);
    }

    private HashMap<String, String> getLeadParams() {
        HashMap<String, String> leadParams = new HashMap<>();
        leadParams.put("name", getName());
        leadParams.put("phone", getPhone());
        leadParams.put("email", getEmail());
        return leadParams;
    }
}
