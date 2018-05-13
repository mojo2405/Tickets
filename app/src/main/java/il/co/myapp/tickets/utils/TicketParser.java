package il.co.myapp.tickets.utils;

import android.text.format.DateFormat;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iliagerman on 07/11/2017.
 */

public class TicketParser {

    private final HashMap<String, EditText> ticketFields;
    private List<String> detectedWords;

    public TicketParser(List<String> detectedWords, HashMap<String, EditText> ticketFields) {
        this.detectedWords = detectedWords;
        this.ticketFields = ticketFields;
    }

    private void GetDriverName() {
        for (int i = 1; i < detectedWords.size(); i++) {

            if (detectedWords.get(i).equals("לכבוד")) {
                ticketFields.get("DriverName").setText(detectedWords.get(i + 1) + " " + detectedWords.get(i + 2));
            }
        }
    }

    private void GetAddress() {
        for (int i = 1; i < detectedWords.size(); i++) {

            if (detectedWords.get(i).equals(":") && detectedWords.get(i + 1).equals("אל")) {
                ticketFields.get("ZipCode").setText(detectedWords.get(i + 2));
                ticketFields.get("City").setText(detectedWords.get(i + 3));
                ticketFields.get("HouseNumber").setText(detectedWords.get(i + 5));
                ticketFields.get("Street").setText(detectedWords.get(i + 6));


            }
        }
    }

    private void SetPenaltyNumber() {
        for (int i = 1; i < detectedWords.size(); i++) {
            if (detectedWords.get(i).equals("דוח") && detectedWords.get(i - 1).equals(":")) {
                ticketFields.get("PenaltyNumber").setText(detectedWords.get(i - 2));

                return;
            }
        }
    }

    private void SetPenaltyIssueDate() {
        for (int i = 1; i < detectedWords.size(); i++) {
            String temp = detectedWords.get(i);
            if (detectedWords.get(i).equals("תאריך")) {
                ticketFields.get("PrintingDate").setText(detectedWords.get(i - 1));
                return;
            }
        }
    }

    private void SetPenaltyCommitDate() {
        for (int i = 1; i < detectedWords.size(); i++) {
            String temp = detectedWords.get(i);
            if (detectedWords.get(i).equals("תאריך")) {
                ticketFields.get("PenaltyDate").setText(detectedWords.get(i - 1));
                return;
            }
        }
    }

    private void GetPassportOrId() {
        for (int i = 1; i < detectedWords.size(); i++) {
            if (detectedWords.get(i).equals("דרכון")) {
                String idNumber = detectedWords.get(i - 1);
                if (idNumber.length() == 9) {
                    ticketFields.get("IdPassportType").setText("תעודת זהות");
                } else {
                    ticketFields.get("IdPassportType").setText("דרכון");
                }
                ticketFields.get("IdPassportNumber").setText(idNumber);
                return;
            }
        }
    }

    private void GetPenaltyDay(){
        String penaltyDate = ticketFields.get("PenaltyDate").getText().toString();
        if(penaltyDate==null)
            return;


        Date date = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = date_format.parse(penaltyDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int reslut = calendar.get(Calendar.DAY_OF_WEEK);
        switch (reslut) {
            case Calendar.SUNDAY:
                ticketFields.get("DayNumber").setText("ראשון");
                return;
            case Calendar.MONDAY:
                ticketFields.get("DayNumber").setText("שני");
                return;
            case Calendar.TUESDAY:
                ticketFields.get("DayNumber").setText("שלשי");
                return;
            case Calendar.WEDNESDAY:
                ticketFields.get("DayNumber").setText("רביעי");
                return;
            case Calendar.THURSDAY:
                ticketFields.get("DayNumber").setText("חמישי");
                return;
            case Calendar.FRIDAY:
                ticketFields.get("DayNumber").setText("שישי");
                return;
            case Calendar.SATURDAY:
                ticketFields.get("DayNumber").setText("שבת");
                return;
        }




    }


    private void SetCarNumber() {
        for (int i = 1; i < detectedWords.size(); i++) {
            if (detectedWords.get(i).equals("רכב") && detectedWords.get(i + 1).equals("מספר")) {
                ticketFields.get("VehiclePlateNumber").setText(detectedWords.get(i - 1));

                return;
            }
        }
    }

    private void SetLicenseNumber() {
        for (int i = 1; i < detectedWords.size(); i++) {
            if (detectedWords.get(i).equals("נהיגה") && detectedWords.get(i + 1).equals("רשיון")) {
                ticketFields.get("LicenseNumber").setText(detectedWords.get(i - 1));

                return;
            }
        }
    }

    private void SetSemelSaif() {
        for (int i = 1; i < detectedWords.size(); i++) {
            if (detectedWords.get(i).equals("סמל") && detectedWords.get(i - 1).equals("סעיף")) {
                ticketFields.get("SeifTakana").setText(detectedWords.get(i - 3));

                return;
            }
        }
    }

    private void SetAveralSaif() {
        for (int i = 1; i < detectedWords.size(); i++) {
            if (detectedWords.get(i).equals("סרט") && detectedWords.get(i + 1).equals("מספר")) {
                ticketFields.get("SemelAvera").setText(detectedWords.get(i - 3));

                return;
            }
        }
    }

    private void SetPenaltyPoints() {
        for (int i = 1; i < detectedWords.size(); i++) {
            if (detectedWords.get(i).equals("נקודות") && detectedWords.get(i - 1).equals(":")) {
                ticketFields.get("PenaltyPoints").setText(detectedWords.get(i - 2));

                return;
            }
        }
    }

    private void SetPenaltyDescription() {
        String description = "";
        for (int i = 1; i < detectedWords.size(); i++) {
            if (detectedWords.get(i).equals("תיאור") && detectedWords.get(i - 1).equals("המעשה")) {
                for (int y = 0; y < 10; y++) {
                    description = description + " " + detectedWords.get(i - 3 - y);
                }
                ticketFields.get("PenaltyDescription").setText(description);
                return;
            }
        }
    }

    private void SetPenaltyTime() {
        for (int i = 1; i < detectedWords.size(); i++) {
            if (detectedWords.get(i).equals("בשעה")) {

                ticketFields.get("PenaltyDate").setText(detectedWords.get(i - 1) + " " + ticketFields.get("PenaltyDate").getText());
                return;
            }
        }
    }

    public HashMap<String, EditText> GetParsedFields() {
        GetDriverName();
        GetAddress();
        SetPenaltyCommitDate();
        SetPenaltyIssueDate();
        SetPenaltyNumber();
        SetCarNumber();
        GetPassportOrId();
        SetSemelSaif();
        SetLicenseNumber();
        SetAveralSaif();
        SetPenaltyPoints();
        SetPenaltyDescription();
        SetPenaltyTime();
        GetPenaltyDay();
        return ticketFields;
    }


}
