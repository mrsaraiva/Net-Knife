package matrix.the.net_knife.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.widget.Button;

import java.lang.reflect.Type;
import java.util.List;
import java.util.zip.Inflater;

import matrix.the.net_knife.R;
import matrix.the.net_knife.network.NetworkTools;
import matrix.the.net_knife.utils.ContextBean;
import matrix.the.net_knife.utils.CounterUtil;
import matrix.the.net_knife.utils.SystemProperties;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPref;
    private Button favoriteButtonOne;
    private Button favoriteButtonTwo;
    private Button favoriteButtonThree;
    private Button favoriteButtonFour;
    private Typeface font;

    private static final String MY_PREF = "PREFERENCES";

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

        sharedPref = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        font = Typeface.createFromAsset(this.getAssets(), "fontawesome-webfont.ttf");

        favoriteButtonOne   = (Button) this.findViewById(R.id.main_btn_favorites_one);
        favoriteButtonTwo   = (Button) this.findViewById(R.id.main_btn_favorites_two);
        favoriteButtonThree = (Button) this.findViewById(R.id.main_btn_favorites_three);
        favoriteButtonFour  = (Button) this.findViewById(R.id.main_btn_favorites_four);

        setFavoritiesButtons();
    }

    private void setFavoritiesButtons()
    {
        final List<CounterUtil.Section> sections = CounterUtil.getTopSections(sharedPref);

        Button buttons[] = {
            favoriteButtonOne,
            favoriteButtonTwo,
            favoriteButtonThree,
            favoriteButtonFour
        };

        for (int i = 0; i < 4; i++)
        {
            Button button = buttons[i];
            final CounterUtil.Section section = sections.get(i);

            if (section.value > 0)
            {
                button.setTypeface(font);
                button.setTextSize(22);
                button.setVisibility(View.VISIBLE);
                button.setText(getButtonSectionText(section));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSelectedSession(CounterUtil.getNavIdBySescion(section));
                    }
                });
            }
            else
            {
                button.setVisibility(View.INVISIBLE);
            }
        }
    }

    public String getButtonSectionText(CounterUtil.Section section)
    {
        if ( section.key.equals("PING") )
        {
            return "PING";
        }
        if ( section.key.equals("TRACEROUTE") )
        {
            return "TRACEROUTE";
        }
        if ( section.key.equals("ARP") )
        {
            return "ARP";
        }
        if ( section.key.equals("WHOIS") )
        {
            return "WHOIS";
        }
        if ( section.key.equals("DNS_LOOKUP") )
        {
            return "DNS_LOOKUP";
        }
        if ( section.key.equals("PORT_SCANNER") )
        {
            return "PORT_SCANNER";
        }
        if ( section.key.equals("WIFI_SCANNER") )
        {
            return "WIFI_SCANNER";
        }
        if ( section.key.equals("HOST_MONITOR") )
        {
            return "HOST_MONITOR";
        }
        
        return "";
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

        getSelectedSession(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getSelectedSession(int id)
    {
        String ARG_ITEM_ID = null;
        Intent intent = null;
        String section = null;
        Integer oldValue = null;

        switch (id)
        {
            case R.id.nav_ping:
                intent = new Intent(MainActivity.this, Ping.class);
                intent.putExtra("ARG_ITEM_ID", "1");
                MainActivity.this.startActivity(intent);
                section = "PING";
                break;
            case R.id.nav_traceroute:
                intent = new Intent(MainActivity.this, Traceroute.class);
                intent.putExtra("ARG_ITEM_ID", "2");
                MainActivity.this.startActivity(intent);
                section = "TRACEROUTE";
                break;
            case R.id.nav_arp:
                intent = new Intent(MainActivity.this, Arp.class);
                intent.putExtra("ARG_ITEM_ID", "3");
                MainActivity.this.startActivity(intent);
                section = "ARP";
                break;
            case R.id.nav_dnslookup:
                intent = new Intent(MainActivity.this, DNSLookup.class);
                intent.putExtra("ARG_ITEM_ID", "4");
                MainActivity.this.startActivity(intent);
                section = "WHOIS";
                break;
            case R.id.nav_whois:
                intent = new Intent(MainActivity.this, Whois.class);
                intent.putExtra("ARG_ITEM_ID", "5");
                MainActivity.this.startActivity(intent);
                section = "DNS_LOOKUP";
                break;
            case R.id.nav_portscanner:
                intent = new Intent(MainActivity.this, PortScanner.class);
                intent.putExtra("ARG_ITEM_ID", "6");
                MainActivity.this.startActivity(intent);
                section = "PORT_SCANNER";
                break;
            case R.id.nav_wifiscanner:
                intent = new Intent(MainActivity.this, WifiScanner.class);
                intent.putExtra("ARG_ITEM_ID", "6");
                MainActivity.this.startActivity(intent);
                section = "WIFI_SCANNER";
                break;
            case R.id.nav_hostmonitor:
                intent = new Intent(MainActivity.this, HostMonitor.class);
                intent.putExtra("ARG_ITEM_ID", "6");
                MainActivity.this.startActivity(intent);
                section = "HOST_MONITOR";
                break;
        }

//        CounterUtil.getTopSections(null);

        SharedPreferences.Editor editor = sharedPref.edit();

        oldValue = sharedPref.getInt(section, 0);
        editor.putInt(section, oldValue + 1);

        editor.commit();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        setFavoritiesButtons();
    }
}
