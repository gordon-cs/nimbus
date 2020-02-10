package com.vmware.nimbus.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.vmware.nimbus.R;
import com.google.android.material.tabs.TabLayout;
import com.vmware.nimbus.data.model.LoginModel;
import com.vmware.nimbus.ui.login.LoginActivity;
import com.vmware.nimbus.ui.main.adapters.SectionsPagerAdapter;

/**
 * The main activity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called after the main activity is created.
     * @param savedInstanceState - the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int i = 1;
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), i);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Logs out and returns to the login activity.
     */
    private void LogOut() {
        LoginModel.getInstance(getBaseContext()).setAuthenticated(false);
        LoginModel.getInstance(getBaseContext()).setApi_token("");

        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
        finish();
    }

    /**
     * Emails the developers with user feedback.
     */
    private void emailDev(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","dev@ajabbot.com", null));
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    /**
     * Called after the options menu is created.
     * @param menu - the Menu
     * @return - true after the menu is inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    /**
     * Called when an option is selected in the options menu.
     * @param item - the menu item that was selected
     * @return - true if one of the items was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.option_log_out_id) {
            LogOut();
            return true;
        }
        else if (id == R.id.option_contact_id) {
            emailDev();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
