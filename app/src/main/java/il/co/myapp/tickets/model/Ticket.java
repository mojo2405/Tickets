package il.co.myapp.tickets.model;

import java.util.List;

public class Ticket {

    private String parsedDate;
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
        return " תאריך פניה: " + parsedDate + " סטוס טיפול: " + officeStatus + " ";
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getTicketDate() {
        return parsedDate;
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

    public Ticket(String driverName, String carNumber, String ticketNumber, String ticketDate, String ticketDay, String felonyClause, String points, String driverRequest, String officeStatus, String details, List<RequestHistory> _requestHistory) {
        this.ticketNumber = ticketNumber;
        this.carNumber = carNumber;
        this.driverName = driverName;
        this.felonyClause = felonyClause;
        this.points = points;
        this.driverRequest = driverRequest;
        this.officeStatus = officeStatus;
        this.details = details;
        this.ticketDay = ticketDay;
        this._requestHistory = _requestHistory;
        try {
            parsedDate = ticketDate.split(" ")[0];
            parsedTime = ticketDate.split(" ")[1];
        }
        catch (Exception e){
            parsedDate = "";
            parsedTime ="";
        }
    }

}
