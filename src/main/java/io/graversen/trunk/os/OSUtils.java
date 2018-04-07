package io.graversen.trunk.os;

import java.awt.*;
import java.net.URI;
import java.util.Map;

public class OSUtils
{
    public static void openInDefaultBrowser(String url)
    {
        if (Desktop.isDesktopSupported())
        {
            try
            {
                URI webUrl = new URI(url);
                Desktop.getDesktop().browse(webUrl);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Unable to browse to target URL", e);
            }
        }
    }

    public static boolean isHeadless()
    {
        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return graphicsEnvironment.isHeadlessInstance();
    }

    public OS getOperatingSystem()
    {
        String osName = System.getProperty("os.name").toLowerCase();

        if (isWindows(osName))
        {
            return OS.Windows;
        }
        else if (isMac(osName))
        {
            return OS.MacOS;
        }
        else if (isLinux(osName))
        {
            return OS.Linux;
        }
        else
        {
            return OS.Unknown;
        }
    }

    private boolean isWindows(String osName)
    {
        return (osName.contains("win"));
    }

    private boolean isMac(String osName)
    {
        return (osName.contains("mac"));
    }

    private boolean isLinux(String osName)
    {
        return (osName.contains("nix") || osName.contains("nux") || osName.contains("aix"));
    }

    public String getDeviceName()
    {
        Map<String, String> env = System.getenv();

        if (env.containsKey("COMPUTERNAME"))
        {
            return env.get("COMPUTERNAME");
        }
        else if (env.containsKey("HOSTNAME"))
        {
            return env.get("HOSTNAME");
        }
        else if (env.containsKey("USER"))
        {
            return env.get("USER");
        }
        else
        {
            return "Unknown Device";
        }
    }

}
