package com.agico.smk.carinspectionapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.agico.smk.carinspectionapp.adapters.IntimationsAdapter;
import com.agico.smk.carinspectionapp.soap.api_tasks.API_TASK;
import com.agico.smk.carinspectionapp.soap.enums.STATUS;

public class IntimationListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public IntimationsAdapter recycler_adapter;
    public API_TASK.FinalSubmit finalSubmit;
    public API_TASK.RefreshIntimations refreshTask;

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
        RecyclerView recycler_intimations = findViewById(R.id.recycler_intimations);
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
            if (finalSubmit == null)
                new AlertDialog.Builder(this)
                        .setTitle("Are You Sure")
                        .setMessage("Do you really want to exit this application?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                IntimationListActivity.super.onBackPressed();
                            }
                        })
                        .setNegativeButton("No", null)
                        .create()
                        .show();
            else
                Snackbar.make(findViewById(R.id.recycler_intimations), "Please Wait for task to finish.", Snackbar.LENGTH_SHORT).show();
        }
    }

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
