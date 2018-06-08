package il.co.myapp.tickets.menu;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import il.co.myapp.tickets.ContactLawyerActivity;
import il.co.myapp.tickets.DisclaimerActivity;
import il.co.myapp.tickets.LoginActivity;
import il.co.myapp.tickets.R;

public class MenuActivity extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        Intent intent;

        switch (item.getItemId())
        {
            case R.id.about_menu_item:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                Toast.makeText(this, "About is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.contact_us_menu_item:
                intent = new Intent(this.getApplication(),ContactLawyerActivity.class);
                startActivity(intent);
                return true;

            case R.id.qa_menu_item:
                Toast.makeText(this, "QA is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.disclaimer_menu_item:
                intent = new Intent(this.getApplication(),DisclaimerActivity.class);
                startActivity(intent);
                return true;

            case R.id.connect_menu_item:
                intent = new Intent(this.getApplication(),LoginActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
