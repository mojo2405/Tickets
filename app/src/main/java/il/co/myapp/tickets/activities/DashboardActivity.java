package il.co.myapp.tickets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import il.co.myapp.tickets.R;
import il.co.myapp.tickets.adapters.TicketAdapter;
import il.co.myapp.tickets.data.AsyncTicketResponse;
import il.co.myapp.tickets.data.TrafficTicketData;
import il.co.myapp.tickets.menu.MenuActivity;
import il.co.myapp.tickets.model.Ticket;

public class DashboardActivity extends MenuActivity {

    private static final String TAG = DashboardActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ProgressBar progressBar;

    private List<Ticket> _ticketsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        progressBar = (ProgressBar) findViewById(R.id.dashboardProgressBar);

        mRecyclerView = (RecyclerView) findViewById(R.id.ticketsRecyclerViewId);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        progressBar.setVisibility(View.VISIBLE);

        TrafficTicketData trafficTicket = new TrafficTicketData();
        trafficTicket.GetTicketsData(this,new AsyncTicketResponse() {
            @Override
            public void TicketsDataReceived(List<Ticket> tickets) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, tickets.toString());
                _ticketsList = tickets;
                mAdapter = new TicketAdapter(getApplicationContext(), _ticketsList);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }
}
