package il.co.myapp.tickets.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import il.co.myapp.tickets.activities.NewTicketActivity;

public class Ticket {

    private static final String TAG = Ticket.class.getSimpleName();

    private String ticketDate;
    private String ticketCreatedDate;
    private String parsedTime;
    private String ticketDay;
    private String driverName;
    private String ticketNumber;
    private String felonyClause;
    private String points;
    private String driverRequest;
    private String officeStatus;
    private String carNumber;
    private String details;

    public void setDriverRequest(String driverRequest) {
        this.driverRequest = driverRequest;
    }

    private List<RequestHistory> _requestHistory;

    public String getTicketDay() {
        return ticketDay;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getTicketTitle() {
        return driverName;
    }

    public String getDescription() {
        return " תאריך פניה: " + ticketCreatedDate + "\n" +
                " סטוס טיפול: " + officeStatus + "\n" +
                 " מספר דוח תנועה: " + ticketNumber + "\n" +
                " מספר רכב: " + carNumber ;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public String getTicketTime() {
        return parsedTime;
    }


    public String getFelonyClause() {
        return felonyClause;
    }

    public String getPoints() {
        return points;
    }

    public String getDriverRequest() {
        return driverRequest;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getOfficeStatus() {
        return officeStatus;
    }

    public String getDetails() {
        return details;
    }

    public List<RequestHistory> get_requestHistory() {
        return _requestHistory;
    }

    public Ticket(String driverName, String carNumber, String ticketNumber, String ticketDate,
                  String ticketDay, String felonyClause, String points, String driverRequest,
                  String officeStatus, String details, List<RequestHistory> _requestHistory,
                  String ticketCreatedDate) {
        this.ticketNumber = ticketNumber;
        this.carNumber = carNumber;
        this.driverName = driverName;
        this.felonyClause = felonyClause;
        this.points = points;
        this.driverRequest = driverRequest;
        this.officeStatus = officeStatus;
        this.details = details;
        this.ticketDay = ticketDay;

        this.ticketCreatedDate = convertFormat(ticketCreatedDate);

        this._requestHistory = _requestHistory;
        try {
            this.ticketDate = ticketDate.split(" ")[0];
            parsedTime = ticketDate.split(" ")[1];

        }
        catch (Exception e){
            this.ticketDate = "";
            parsedTime ="";
        }


    }

    public static String convertFormat(String inputDate) {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        try{
            inputDate = inputDate.replace('T', ' ');
            inputDate = inputDate.replace(".000Z", "");

            Date d = df1.parse(inputDate);
            return df2.format(d);
        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
        return "";
    }

}
