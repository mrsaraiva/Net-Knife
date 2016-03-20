package matrix.the.net_knife.network;

import matrix.the.net_knife.utils.ShellProcess;

/**
 * Created by Marcos Saraiva on 19/03/2016.
 */
public class Traceroute extends ShellProcess
{

    public boolean checkArgs(String[] args)
    {
        // Make sure the first argument is a valid host name or IPv4 address
        return NetworkTools.isHostName(args[1]) || NetworkTools.isIPv4Adress(args[1]);
    }

    protected String getSystemCommand()
    {
        String[] args = getArgs();
        String cmd = "su -c ";

        if (checkArgs(args))
        {
            cmd += NetworkTools.networkToolBin.get("traceroute");
        }

        return cmd;
    }
}
