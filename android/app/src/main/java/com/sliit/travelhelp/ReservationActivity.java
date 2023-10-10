//this code is for reservation activity in the system

package com.sliit.travelhelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Toolbar toolbar = findViewById(R.id.toolbar_reservation_activity);
        toolbar.setTitle("Reservations");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            // Handle the menu item click
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.menu_account){
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.menu_reservation){
            Intent intent = new Intent(this, ReservationActivity.class);
            startActivity(intent);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }

    }
}