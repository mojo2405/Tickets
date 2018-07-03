package il.co.myapp.tickets.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import il.co.myapp.tickets.R;
import il.co.myapp.tickets.controller.AppController;
import il.co.myapp.tickets.menu.MenuActivity;
import il.co.myapp.tickets.model.Ticket;

public class TicketDetailsActivity extends MenuActivity {

    private TextView driversName;
    private TextView carsNumber;
    private TextView ticketNumber;
    private TextView ticketDate;
    private TextView ticketHour;
    private TextView ticketPlace;
    private TextView ticketDayOfWeek;
    private TextView ticketClause;
    private TextView ticketPoints;
    private TextView ticketDriversOfficeStatus;
    private TextView ticketDriversDetails;
    private TextView ticketDriversNotes;
    private TextView ticketLastComment;
    private Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);
        carsNumber = (TextView) findViewById(R.id.ticketDetailsCarNumberId);
        driversName = (TextView) findViewById(R.id.ticketDetailsDriverNameId);
        ticketNumber = (TextView) findViewById(R.id.ticketDetailsTicketNumberId);
        ticketDate = (TextView) findViewById(R.id.ticketDetailsCarTicketDateId);
        ticketDayOfWeek = (TextView) findViewById(R.id.ticketDetailsCarTicketDayId);
        ticketHour = (TextView) findViewById(R.id.ticketDetailsCarTicketHourId);
        ticketPlace = (TextView) findViewById(R.id.ticketDetailsCarTicketPlaceId);
        ticketClause = (TextView) findViewById(R.id.ticketDetailsCarTicketClauseId);
        ticketPoints = (TextView) findViewById(R.id.ticketDetailsCarTicketPointsId);
        ticketDriversOfficeStatus = (TextView) findViewById(R.id.ticketDetailsOfficeStatusId);
        ticketDriversDetails = (TextView) findViewById(R.id.ticketDetailsDetailsId);
        ticketDriversNotes = (TextView)  findViewById(R.id.ticketDetailsDriverNotesId);
        ticketLastComment = (TextView)  findViewById(R.id.ticketLastCommentId);


        ticket = AppController.getInstance().get_viewableTicket();


        if (ticket != null) {
            driversName.setText(ticket.getDriverName());
            carsNumber.setText(ticket.getCarNumber());
            ticketNumber.setText(ticket.getTicketNumber());
            ticketDate.setText(ticket.getTicketDate());
            ticketClause.setText(ticket.getFelonyClause());
            ticketPoints.setText(ticket.getPoints());
            ticketDriversOfficeStatus.setText(ticket.getOfficeStatus());
            ticketDriversDetails.setText(ticket.getDetails());
            ticketDayOfWeek.setText(ticket.getTicketDay());
            ticketHour.setText(ticket.getTicketTime());
            ticketPlace.setText(ticket.getTicketPlace());
            ticketDriversNotes.setText(ticket.getDriverNotes());
            if(ticket.getLastComment() != "null"){
                ticketLastComment.setText(ticket.getLastComment());
            } else {
                ticketLastComment.setText("אין הערות מהמשרד");
            }
        }
    }
}
