package il.co.myapp.tickets.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import il.co.myapp.tickets.controller.AppController;
import il.co.myapp.tickets.controller.NetworkController;
import il.co.myapp.tickets.model.RequestHistory;
import il.co.myapp.tickets.model.Ticket;
import il.co.myapp.tickets.utils.ParseNetworkError;

import static com.facebook.FacebookSdk.getApplicationContext;


public class TrafficTicketData {

    private static final String TAG = "TrafficTicketData";

    public void GetTicketsData(Context context, final AsyncTicketResponse ticketsCallBack) {

        JsonArrayRequest ticketsArrayRequest = new JsonArrayRequest(Request.Method.GET,
                URLS.GET_TICKETS_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray ticketsArray) {
                List<Ticket> ticketList = new ArrayList();
                for (int i = 0; i < ticketsArray.length(); i++) {
                    try {
                        JSONObject ticketObject = ticketsArray.getJSONObject(i);
                        ticketList.add(new Ticket(
                                ticketObject.get("DriverName").toString(),
                                ticketObject.get("VehiclePlateNumber").toString(),
                                ticketObject.get("PenaltyNumber").toString(),
                                ticketObject.get("PenaltyDate").toString(),
                                ticketObject.get("DayNumber").toString(),
                                ticketObject.get("SemelAvera").toString(),
                                ticketObject.get("PenaltyPoints").toString(),
                                ticketObject.get("DriverRequest").toString(),
                                ticketObject.get("OfficeStatus").toString(),
                                ticketObject.get("PenaltyNotes").toString(),
                                null,
                                ticketObject.get("createdAt").toString(),
                                ticketObject.get("PenaltyLocation").toString()
                                ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ticketsCallBack.TicketsDataReceived(ticketList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Failed to get tickets list");

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                String token = AppController.getInstance().get_token();
                headers.put("x-access-token", token);
                return headers;
            }
        };
        NetworkController.getInstance(context).addToRequestQueue(ticketsArrayRequest);
    }

    public void SubmitTicket(
            Context context,
            HashMap<String, String> ticketData,
            final AsyncTicketResponse ticketsCallBack) throws JSONException {


        JsonObjectRequest driversArrayRequest = new JsonObjectRequest
                (Request.Method.POST, URLS.POST_TICKETS_URL, new JSONObject(ticketData),
                        new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("tickets");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        List<Ticket> ticketList = new ArrayList();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject ticketObject = jsonArray.getJSONObject(i);

                                ticketList.add(new Ticket(
                                        ticketObject.get("DriverName").toString(),
                                        ticketObject.get("LicenseNumber").toString(),
                                        ticketObject.get("PenaltyNumber").toString(),
                                        ticketObject.get("PrintingDate").toString(),
                                        ticketObject.get("DayNumber").toString(),
                                        ticketObject.get("SemelAvera").toString(),
                                        ticketObject.get("PenaltyPoints").toString(),
                                        ticketObject.get("DriverRequest").toString(),
                                        ticketObject.get("OfficeStatus").toString(),
                                        ticketObject.get("PenaltyNotes").toString(),
                                        null,
                                        ticketObject.get("createdAt").toString(),
                                        ticketObject.get("PenaltyLocation").toString()
                                ));
                            } catch (JSONException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();

                            }
                        }
                        ticketsCallBack.TicketsDataReceived(ticketList);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorResponse = ParseNetworkError.GetErrorMessage(error);
                        Toast.makeText(getApplicationContext(),
                                "Failed. "+ errorResponse, Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                String token = AppController.getInstance().get_token();
                headers.put("x-access-token", token);
                return headers;
            }
        };

        NetworkController.getInstance(context).addToRequestQueue(driversArrayRequest);
    }


    private List<RequestHistory> GetRequestHistoryList(JSONArray requestHistory) {
        List<RequestHistory> requestHistoryList = new ArrayList();
        for (int z = 0; z < requestHistory.length(); z++) {

            JSONObject requestHistoryItem = null;
            try {
                requestHistoryItem = requestHistory.getJSONObject(z);
                requestHistoryList.add(new RequestHistory(
                        requestHistoryItem.getString("status"),
                        requestHistoryItem.getString("date")
                ));
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

        }
        return requestHistoryList;
    }
}
