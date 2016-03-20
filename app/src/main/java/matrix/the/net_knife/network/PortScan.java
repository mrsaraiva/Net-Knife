package matrix.the.net_knife.network;

import matrix.the.net_knife.utils.ShellProcess;

/**
 * Created by Marcos Saraiva on 19/03/2016.
 */
public class PortScan extends ShellProcess
{

    public boolean checkArgs(String[] args)
    {
        // Make sure the first argument is a valid host name or IPv4 address
        return NetworkTools.isHostName(args[0]) || NetworkTools.isIPv4Adress(args[0]);
    }

    protected String getSystemCommand()
    {
        String[] args = getArgs();
        String cmd = "su -c ";

        if (checkArgs(args))
        {
            cmd += NetworkTools.networkToolBin.get("pscan");
        }

        return cmd;
    }
}
