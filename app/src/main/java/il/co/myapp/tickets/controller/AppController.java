package il.co.myapp.tickets.controller;

import il.co.myapp.tickets.model.Ticket;
import il.co.myapp.tickets.model.User;

public class AppController {
    private static AppController ourInstance = new AppController();
//    private static AppController ourInstance = null;
    public static final String TAG = AppController.class
            .getSimpleName();

    private User user;
    private String token;
    private Ticket _viewableTicket;
    

    public static AppController getInstance() {
        if(null == ourInstance){
            ourInstance = new AppController();
        }
        return ourInstance;
    }

    protected AppController() {}

    public void setUser(User user) { this.user = user; }
    public User getUser() { return user; }

    public void set_token(String token) {
        this.token = token;
    }

    public String get_token() {
        return token;
    }

    public Ticket get_viewableTicket() {
        return _viewableTicket;
    }

    public void set_viewableTicket(Ticket _viewableTicket) {
        this._viewableTicket = _viewableTicket;
    }



}
