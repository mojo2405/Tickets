package il.co.myapp.tickets.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String ticketPlace;
    private String driverNotes;
    private String lastComment;

    public void setDriverRequest(String driverRequest) {
        this.driverRequest = driverRequest;
    }

    private List<RequestHistory> _requestHistory;

    public String getTicketDay() {
        return checkData(ticketDay);
    }

    public String getDriverName() {
        return checkData(driverName);
    }

    public String getTicketTitle() {
        return checkData(driverName);
    }

    public String getDescription() {
        return " תאריך פניה: " + ticketCreatedDate + "\n" +
                " סטוס טיפול: " + officeStatus + "\n" +
                " מספר דוח תנועה: " + ticketNumber + "\n" +
                " מספר רכב: " + carNumber;
    }

    public String getTicketNumber() {
        return checkData(ticketNumber);
    }

    public String getTicketDate() {
        return checkData(ticketDate);
    }

    public String getTicketTime() {
        return checkData(parsedTime);
    }


    public String getFelonyClause() {
        return checkData(felonyClause);
    }

    public String getPoints() {
        return checkData(points);
    }

    public String getDriverRequest() {
        return checkData(driverRequest);
    }

    public String getCarNumber() {
        return checkData(carNumber);
    }

    public String getOfficeStatus() {
        return checkData(officeStatus);
    }

    public String getDetails() {
        return checkData(details);
    }

    public List<RequestHistory> get_requestHistory() {
        return _requestHistory;
    }

    public String getTicketPlace() {
        return checkData(ticketPlace);
    }

    public String getDriverNotes() {
        return checkData(driverNotes);
    }

    public String getLastComment() {

        return checkData(lastComment);
    }

    private String checkData(String data){
        if(data == null || data == "null"){
            return "אין מידע";

        }
        else {
            return data;
        }
    }



    public Ticket(String driverName, String carNumber, String ticketNumber, String ticketDate,
                  String ticketDay, String felonyClause, String points, String driverRequest,
                  String officeStatus, String details, List<RequestHistory> _requestHistory,
                  String ticketCreatedDate, String ticketPlace, String driverNotes, String lastComment) {

        HashMap<String, String> map;

        this.ticketNumber = ticketNumber;
        this.carNumber = carNumber;
        this.driverName = driverName;
        this.felonyClause = felonyClause;
        this.points = points;
        this.driverRequest = driverRequest;
        this.officeStatus = officeStatus;
        this.details = details;
        this.ticketDay = ticketDay;
        this.ticketPlace = ticketPlace;
        this.driverNotes = driverNotes;
        this.lastComment = lastComment;
        this.ticketCreatedDate = convertFormat(ticketCreatedDate);

        this._requestHistory = _requestHistory;

        map = getDateAndTime(ticketDate);
        this.ticketDate = map.get("Date");
        this.parsedTime = map.get("Time");


    }

    public static HashMap<String, String> getDateAndTime(String inputDate) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Date", "");
        map.put("Time", "");

        Log.v(TAG, "Got time input " + inputDate);
        Pattern datePattern = Pattern.compile("(\\w+[\\.\\-\\/]\\w+[\\.\\-\\/]\\w+)");
        Pattern timePattern = Pattern.compile("(\\d+:\\d+[:\\d]+)");

        Matcher dateMatcher = datePattern.matcher(inputDate);
        if (dateMatcher.find()) {
            map.put("Date", dateMatcher.group(1));
        }

        Matcher timeMatcher = timePattern.matcher(inputDate);
        if (timeMatcher.find()) {
            map.put("Time", timeMatcher.group(1));
        }


        return map;
    }

    public static String convertFormat(String inputDate) {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            inputDate = inputDate.replace('T', ' ');
            inputDate = inputDate.replace(".000Z", "");

            Date d = df1.parse(inputDate);
            return df2.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return "";
    }


}
