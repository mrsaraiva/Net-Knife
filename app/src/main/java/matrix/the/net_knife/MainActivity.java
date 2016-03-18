package matrix.the.net_knife;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try
        {
            Process su = Runtime.getRuntime().exec("su");
            System.out.println("Creating Busybox symlink...");
            Process p = Runtime.getRuntime().exec("ln -s /data/data/matrix.the.net_knife/lib/libbusybox.so /data/data/matrix.the.net_knife/busybox");
            p.waitFor();
            int len;
            if ((len = p.getErrorStream().available()) > 0)
            {
                System.out.println(p.exitValue());
                byte[] buf = new byte[len];
                p.getErrorStream().read(buf);
                System.out.println("Couldn't create Busybox symlink :(");
                System.out.println("Command error:\t\"" + new String(buf) + "\"");
            }

                System.out.println("Busybox symlink created!");
        }
        catch (Exception e)
        {
            // stub
            e.printStackTrace();
        }
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

        switch (id)
        {
            case R.id.nav_ping:
                Intent pingIntent = new Intent(MainActivity.this, Ping.class);
                MainActivity.this.startActivity(pingIntent);
                break;
            case R.id.nav_traceroute:
                Intent traceIntent = new Intent(MainActivity.this, Traceroute.class);
                MainActivity.this.startActivity(traceIntent);
                break;
            case R.id.nav_arp:
                Intent arpIntent = new Intent(MainActivity.this, Arp.class);
                MainActivity.this.startActivity(arpIntent);
                break;
            case R.id.nav_dnslookup:
                Intent dnsIntent = new Intent(MainActivity.this, DNSLookup.class);
                MainActivity.this.startActivity(dnsIntent);
                break;
            case R.id.nav_whois:
                Intent whoisIntent = new Intent(MainActivity.this, Whois.class);
                MainActivity.this.startActivity(whoisIntent);
                break;
            case R.id.nav_portscanner:
                Intent portScannerIntent = new Intent(MainActivity.this, PortScanner.class);
                MainActivity.this.startActivity(portScannerIntent);
                break;
            case R.id.nav_wifiscanner:
                Intent wifiScannerIntent = new Intent(MainActivity.this, WifiScanner.class);
                MainActivity.this.startActivity(wifiScannerIntent);
                break;
            case R.id.nav_hostmonitor:
                Intent hostMonitorIntent = new Intent(MainActivity.this, HostMonitor.class);
                MainActivity.this.startActivity(hostMonitorIntent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
