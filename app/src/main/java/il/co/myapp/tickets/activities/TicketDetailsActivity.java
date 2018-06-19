package il.co.myapp.tickets.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import il.co.myapp.tickets.R;
import il.co.myapp.tickets.controller.AppController;
import il.co.myapp.tickets.menu.MenuActivity;
import il.co.myapp.tickets.model.Ticket;

public class TicketDetailsActivity extends MenuActivity {

    private EditText driversName;
    private EditText carsNumber;
    private EditText ticketNumber;
    private EditText ticketDate;
    private EditText ticketHour;
    private EditText ticketPlace;
    private EditText ticketDayOfWeek;
    private EditText ticketClause;
    private EditText ticketPoints;
    private EditText ticketDriversRequest;
    private EditText ticketDriversOfficeStatus;
    private EditText ticketDriversDetails;
    private Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);
        carsNumber = (EditText) findViewById(R.id.ticketDetailsCarNumberId);
        driversName = (EditText) findViewById(R.id.ticketDetailsDriverNameId);
        ticketNumber = (EditText) findViewById(R.id.ticketDetailsTicketNumberId);
        ticketDate = (EditText) findViewById(R.id.ticketDetailsCarTicketDateId);
        ticketDayOfWeek = (EditText) findViewById(R.id.ticketDetailsCarTicketDayId);
        ticketHour = (EditText) findViewById(R.id.ticketDetailsCarTicketHourId);
        ticketPlace = (EditText) findViewById(R.id.ticketDetailsCarTicketPlaceId);
        ticketClause = (EditText) findViewById(R.id.ticketDetailsCarTicketClauseId);
        ticketPoints = (EditText) findViewById(R.id.ticketDetailsCarTicketPointsId);
        ticketDriversRequest = (EditText) findViewById(R.id.ticketDetailsCarTicketDriversRequestId);
        ticketDriversOfficeStatus = (EditText) findViewById(R.id.ticketDetailsOfficeStatusId);
        ticketDriversDetails = (EditText) findViewById(R.id.ticketDetailsDetailsId);


        ticket = AppController.getInstance().get_viewableTicket();


        if (ticket != null) {
            driversName.setText(ticket.getDriverName());
            carsNumber.setText(ticket.getCarNumber());
            ticketNumber.setText(ticket.getTicketNumber());
            ticketDate.setText(ticket.getTicketDate());
            ticketClause.setText(ticket.getFelonyClause());
            ticketPoints.setText(ticket.getPoints());
            ticketDriversRequest.setText(ticket.getDriverRequest());
            ticketDriversOfficeStatus.setText(ticket.getOfficeStatus());
            ticketDriversDetails.setText(ticket.getDetails());
            ticketDayOfWeek.setText(ticket.getTicketDay());
            ticketHour.setText(ticket.getTicketTime());
        }
    }
}
