package il.co.myapp.tickets.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import il.co.myapp.tickets.R;
import il.co.myapp.tickets.menu.MenuActivity;

public class QAActivity extends MenuActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);
    }
}
