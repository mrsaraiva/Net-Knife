package matrix.the.net_knife.network;

import matrix.the.net_knife.utils.ShellProcess;

/**
 * Created by Marcos Saraiva on 19/03/2016.
 */
public class PortScan extends ShellProcess
{

    public boolean checkArgs(String[] args)
    {
        String startPort = args[0].substring(3);
        String endPort = args[1].substring(3);
        String host = args[2];

        // Make sure the last argument is a valid host name or IPv4 address
        return
        (
                (NetworkTools.isValidPort(startPort)) &&
                (NetworkTools.isValidPort(endPort)) &&
                (NetworkTools.isHostName(host) || NetworkTools.isIPv4Adress(host))
        );
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
