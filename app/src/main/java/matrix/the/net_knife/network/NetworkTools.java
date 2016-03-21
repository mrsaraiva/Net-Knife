package matrix.the.net_knife.network;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import matrix.the.net_knife.utils.CommonUtil;
import matrix.the.net_knife.utils.ProcessStream.ProcessStreamReader;
import matrix.the.net_knife.utils.ShellProcess;
import matrix.the.net_knife.utils.ShellProcess.OnComplete;
import matrix.the.net_knife.utils.ThreadProcess;

/**
 * Created by Marcos Saraiva on 19/03/2016.
 */
public class NetworkTools
{
    public static final String appDir = CommonUtil.getDataDir();
    public static final String busyBoxBin = appDir + "/lib/" + CommonUtil.getBusyboxBin();
    public static final String busyBoxSymlink = appDir + "/busybox";
    public static Map<String, String> networkToolBin = new HashMap<String, String>();

    static
    {
        networkToolBin.put("arp", appDir + "/arp");
        networkToolBin.put("nslookup", appDir + "/nslookup");
        networkToolBin.put("ping", appDir + "/ping");
        networkToolBin.put("pscan", appDir + "/pscan");
        networkToolBin.put("traceroute", appDir + "/traceroute");
        networkToolBin.put("whois", appDir + "/whois");
    }

    /**
     * An array of sample (dummy) items.
     */
    public static List<NetworkTool> ITEMS = new ArrayList<NetworkTool>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, NetworkTool> ITEM_MAP = new HashMap<String, NetworkTool>();

    static
    {
        // Local device status utilities
        addItem(new NetworkTool("1", "Ping", new Ping()));
        addItem(new NetworkTool("2", "Trace", new Traceroute()));
        addItem(new NetworkTool("3", "Arp", new Arp()));
        addItem(new NetworkTool("4", "DNS Lookup", new DNSLookup()));
        addItem(new NetworkTool("5", "Whois", new Whois()));
        addItem(new NetworkTool("6", "Port Scan", new PortScan()));
        //addItem(new NetworkTool("7", "Wi-Fi Scanner", new WifiScan()));
        //addItem(new NetworkTool("8", "Host Monitor", new HostMonitor()));
        //addItem(new NetworkTool("9", "Netstat", new NetStat()));
        //addItem(new NetworkTool("10", "Netcfg", new NetCfg()));
        //addItem(new NetworkTool("11", "IP Tools", new IPTools()));
    }

    /**
     * Checks if is hostname.
     *
     * @param arg the arg
     * @return true, if is host name
     */
    public static boolean isHostName(String arg)
    {
        // Check if arg is a valid hostname
        return arg.length() > 0
                && (arg.matches("^(?=.{1,255}$)[0-9A-Za-z](?:(?:[0-9A-Za-z]|-){0,61}[0-9A-Za-z])?(?:\\.[0-9A-Za-z](?:(?:[0-9A-Za-z]|-){0,61}[0-9A-Za-z])?)*\\.?$"));
    }

    /**
     * Checks if is IPv4 adress.
     *
     * @param arg the arg
     * @return true, if is IPv4 adress
     */
    public static boolean isIPv4Adress(String arg)
    {
        // Check if arg is a valid IPv4 address
        return arg.length() > 0
                && arg.matches("^[0-9]+$");
    }

    public static boolean isValidPort(String arg)
    {
        int port = Integer.parseInt(arg);

        return arg.length() > 0
                && (port > 0) && (port < 65536);
    }

    /**
     * Adds the item.
     *
     * @param item the item
     */
    private static void addItem(NetworkTool item)
    {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void createSymlinks()
    {
        try
        {
            Process su = Runtime.getRuntime().exec("su");
            System.out.println("Checking if Busybox symlink exists...");
            File f = new File(busyBoxSymlink);
            if (f.exists() && !f.isDirectory())
            {
                System.out.println("Busybox symlink already exists, don't need to do anything");
            }
            else
            {
                Process p = Runtime.getRuntime().exec("ln -s " + busyBoxBin + " " + busyBoxSymlink);
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
                else
                {
                    System.out.println("Busybox symlink created! Now checking all the others...");
                }

            }

            for (Map.Entry<String, String> entry : networkToolBin.entrySet())
            {
                String key = entry.getKey();
                String value = entry.getValue();

                System.out.println("Checking if " + key + " symlink exists");
                File symlink = new File(value);
                if (symlink.exists() && !symlink.isDirectory())
                {
                    System.out.println(key + " symlink already exists, don't need to do anything");
                }
                else
                {
                    String cmd = "ln -s " + busyBoxSymlink + " " + value;
                    System.out.println(cmd);
                    Process p = Runtime.getRuntime().exec(cmd);
                    p.waitFor();
                    int len;
                    if ((len = p.getErrorStream().available()) > 0)
                    {
                        System.out.println(p.exitValue());
                        byte[] buf = new byte[len];
                        p.getErrorStream().read(buf);
                        System.out.println("Couldn't create " + key + " symlink :(");
                        System.out.println("Command error:\t\"" + new String(buf) + "\"");
                    }
                    else
                    {
                        System.out.println(key + " symlink created!");
                    }
                }
            }

        }
        catch (Exception e)
        {
            // stub
            e.printStackTrace();
        }
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class NetworkTool
    {

        /**
         * The id.
         */
        public String id;

        /**
         * The content.
         */
        public String content;

        /**
         * The worker.
         */
        public ShellProcess worker = null;

        /**
         * The tworker.
         */
        public ThreadProcess tworker = null;

        /**
         * The reader.
         */
        public ProcessStreamReader reader;

        /**
         * Instantiates a new network tool.
         *
         * @param id      the id
         * @param content the content
         * @param worker  the worker
         */
        public NetworkTool(String id, String content, ShellProcess worker)
        {
            this.id = id;
            this.content = content;
            this.worker = worker;
        }

        /**
         * Instantiates a new network tool.
         *
         * @param id      the id
         * @param content the content
         * @param worker  the worker
         */
        public NetworkTool(String id, String content, ThreadProcess worker)
        {
            this.id = id;
            this.content = content;
            this.tworker = worker;
        }

        /**
         * Start.
         *
         * @param args     the args
         * @param reader   the reader
         * @param complete the complete
         */
        public void start(String[] args, ProcessStreamReader reader, OnComplete complete)
        {
            if (this.worker != null)
            {
                this.worker.setArgs(args);
                this.worker.setReader(reader);
                this.worker.setComplete(complete);
                new Thread(this.worker).start();
            }
            else if (this.tworker != null)
            {
                this.tworker.setArgs(args);
                this.tworker.setComplete(complete);
                new Thread(this.tworker).start();
            }
        }

        @Override
        public String toString()
        {
            return content;
        }
    }
}