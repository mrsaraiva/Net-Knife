package matrix.the.net_knife.network;

import matrix.the.net_knife.utils.ShellProcess;

/**
 * Created by Marcos Saraiva on 19/03/2016.
 */
public class Arp extends ShellProcess
{

    public boolean checkArgs(String[] args)
    {
        // No argument checks for Arp
        return true;
    }

    protected String getSystemCommand()
    {
        String[] args = getArgs();
        String cmd = "su -c ";

        if (checkArgs(args))
        {
            cmd += NetworkTools.networkToolBin.get("arp");
        }

        return cmd;
    }
}
