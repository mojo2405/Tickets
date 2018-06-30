package il.co.myapp.tickets.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import il.co.myapp.tickets.activities.ContactLawyerActivity;
import il.co.myapp.tickets.activities.ContactUsActivity;
import il.co.myapp.tickets.activities.DashboardActivity;
import il.co.myapp.tickets.activities.DisclaimerActivity;
import il.co.myapp.tickets.activities.LoginActivity;
import il.co.myapp.tickets.R;
import il.co.myapp.tickets.activities.QAActivity;
import il.co.myapp.tickets.activities.TicketDetailsActivity;
import il.co.myapp.tickets.controller.AppController;

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

            case R.id.contact_us_menu_item:
                intent = new Intent(this.getApplication(),ContactUsActivity.class);
                startActivity(intent);
                return true;

            case R.id.qa_menu_item:
                intent = new Intent(this.getApplication(),QAActivity.class);
                startActivity(intent);
                return true;

            case R.id.disclaimer_menu_item:
                intent = new Intent(this.getApplication(),DisclaimerActivity.class);
                startActivity(intent);
                return true;

            case R.id.connect_menu_item:
                intent = new Intent(this.getApplication(),LoginActivity.class);
                startActivity(intent);
                return true;

            case R.id.ticket_list_menu_item:
                if ( null != AppController.getInstance().get_token() ){
                    intent = new Intent(this.getApplication(),DashboardActivity.class);
                    startActivity(intent);
                    return true;

                }else {
                    intent = new Intent(this.getApplication(),LoginActivity.class);
                    intent.putExtra("nextScreen", DashboardActivity.class.getCanonicalName());
                    startActivity(intent);
                    return true;
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
