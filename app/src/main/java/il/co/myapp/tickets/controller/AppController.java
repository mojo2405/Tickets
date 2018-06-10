package il.co.myapp.tickets.controller;

import il.co.myapp.tickets.model.User;

public class AppController {
    private static AppController ourInstance = new AppController();
//    private static AppController ourInstance = null;
    public static final String TAG = AppController.class
            .getSimpleName();

    private User user;
    private String token;


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
}