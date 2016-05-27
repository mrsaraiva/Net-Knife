package matrix.the.net_knife.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import matrix.the.net_knife.R;

/**
 * Created by JoaoLuiz on 06/04/2016.
 */
public class CounterUtil
{
    private static final String[] sections = {
            "PING",
            "TRACEROUTE",
            "ARP",
            "WHOIS",
            "DNS_LOOKUP",
            "PORT_SCANNER",
            "WIFI_SCANNER",
            "HOST_MONITOR"
    };

    private static final int[] navIds = {
            R.id.nav_ping,
            R.id.nav_traceroute,
            R.id.nav_arp,
            R.id.nav_dnslookup,
            R.id.nav_whois,
            R.id.nav_portscanner,
            R.id.nav_wifiscanner,
//            R.id.nav_hostmonitor
    };

    public static int getNavIdBySescion(Section section)
    {
        for (int i = 0; i < sections.length; i++)
        {
            if (section.key.equals(sections[i]))
            {
                return navIds[i];
            }
        }
        return 0;
    }

    public static ArrayList<Section> getTopSections(SharedPreferences sharedPreferences)
    {
        ArrayList<Section> tops = new ArrayList<Section>();

        for (String _section : sections)
        {
            Integer sectionValue = sharedPreferences.getInt(_section, 0);
            tops.add(new Section(_section, sectionValue));
        }

        Collections.sort(tops, new Comparator<Section>()
        {
            @Override
            public int compare(Section sec1, Section sec2)
            {
                return sec2.value.compareTo(sec1.value);
            }
        });

        for (int i = tops.size() - 1;  i > 3; i--)
        {
            tops.remove(tops.get(i));
        }

        System.out.println(tops);

        return tops;
    }

    public static class Section {
        public String key;
        public Integer value;

        public Section(String key, Integer value)
        {
            this.key    = key;
            this.value  = value;
        }

        public String toString()
        {
            return String.format("%s -> %d", this.key, this.value);
        }
    }
}
