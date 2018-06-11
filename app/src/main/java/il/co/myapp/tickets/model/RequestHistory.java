package il.co.myapp.tickets.model;



public class RequestHistory {
    private String _status;
    private String _date;

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public RequestHistory(String status, String date) {
        this._status = status;
        this._date = date;
    }
}
