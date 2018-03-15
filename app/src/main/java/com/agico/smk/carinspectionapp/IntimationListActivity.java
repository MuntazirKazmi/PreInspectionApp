package com.agico.smk.carinspectionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.agico.smk.carinspectionapp.Adapters.IntimationsAdapter;
import com.agico.smk.carinspectionapp.SOAP.API_Tasks.API_TASK;
import com.agico.smk.carinspectionapp.SOAP.ENUMS.STATUS;

public class IntimationListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public IntimationsAdapter recycler_adapter;
    public API_TASK.FinalSubmit finalSubmit;
    public API_TASK.RefreshIntimations refreshTask;
    RecyclerView recycler_intimations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intimation_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(GravityCompat.START);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.under_process);
        recycler_adapter = new IntimationsAdapter(this);
        recycler_intimations = findViewById(R.id.recycler_intimations);
        recycler_intimations.setHasFixedSize(true);
        recycler_intimations.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler_intimations.setAdapter(recycler_adapter);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.intimation_list, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Snackbar.make(findViewById(R.id.toolbar),"Settings Clicked",Snackbar.LENGTH_SHORT).show();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.under_process) {
            recycler_adapter
                    .setStatus(STATUS.Under_Process);
        } else if (id == R.id.processed) {
            recycler_adapter
                    .setStatus(STATUS.Processed);
        } else if (id == R.id.pending) {
            recycler_adapter
                    .setStatus(STATUS.Pending);
        } else if (id == R.id.view_profile) {

        } else if (id == R.id.logout) {
            startActivity(new Intent(IntimationListActivity.this, InspectorLoginActivity.class));
            finishAffinity();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public void refreshAll() {
        if (refreshTask == null) {
            refreshTask = new API_TASK.RefreshIntimations(this);
            refreshTask.execute();
        }
    }

}
