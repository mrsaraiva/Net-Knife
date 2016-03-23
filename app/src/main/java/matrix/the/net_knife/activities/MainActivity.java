package matrix.the.net_knife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import matrix.the.net_knife.R;
import matrix.the.net_knife.network.NetworkTools;
import matrix.the.net_knife.utils.ContextBean;
import matrix.the.net_knife.utils.SystemProperties;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ContextBean.setLocalContext(getApplicationContext());

        System.out.println("Phone CPU architecture is: " + SystemProperties.get("ro.product.cpu.abilist"));

        NetworkTools.createSymlinks();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String ARG_ITEM_ID = null;
        Intent intent = null;

        switch (id)
        {
            case R.id.nav_ping:
                intent = new Intent(MainActivity.this, Ping.class);
                intent.putExtra("ARG_ITEM_ID", "1");
                MainActivity.this.startActivity(intent);
                break;
            case R.id.nav_traceroute:
                intent = new Intent(MainActivity.this, Traceroute.class);
                intent.putExtra("ARG_ITEM_ID", "2");
                MainActivity.this.startActivity(intent);
                break;
            case R.id.nav_arp:
                intent = new Intent(MainActivity.this, Arp.class);
                intent.putExtra("ARG_ITEM_ID", "3");
                MainActivity.this.startActivity(intent);
                break;
            case R.id.nav_dnslookup:
                intent = new Intent(MainActivity.this, DNSLookup.class);
                intent.putExtra("ARG_ITEM_ID", "4");
                MainActivity.this.startActivity(intent);
                break;
            case R.id.nav_whois:
                intent = new Intent(MainActivity.this, Whois.class);
                intent.putExtra("ARG_ITEM_ID", "5");
                MainActivity.this.startActivity(intent);
                break;
            case R.id.nav_portscanner:
                intent = new Intent(MainActivity.this, PortScanner.class);
                intent.putExtra("ARG_ITEM_ID", "6");
                MainActivity.this.startActivity(intent);
                break;
            case R.id.nav_wifiscanner:
                intent = new Intent(MainActivity.this, WifiScanner.class);
                intent.putExtra("ARG_ITEM_ID", "6");
                MainActivity.this.startActivity(intent);
                break;
            case R.id.nav_hostmonitor:
                intent = new Intent(MainActivity.this, HostMonitor.class);
                intent.putExtra("ARG_ITEM_ID", "6");
                MainActivity.this.startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
