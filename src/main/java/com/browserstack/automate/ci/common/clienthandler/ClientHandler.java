package com.browserstack.automate.ci.common.clienthandler;

import com.browserstack.appautomate.AppAutomateClient;
import com.browserstack.automate.AutomateClient;
import com.browserstack.automate.ci.common.enums.ProjectType;
import com.browserstack.automate.ci.common.proxysettings.JenkinsProxySettings;
import com.browserstack.client.BrowserStackClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintStream;

public class ClientHandler {

    /**
     * Returns BrowserStackClient based on Project, i.e Automate or App Automate.
     * Also decides and sets the proxy for the client
     * @param project ProjectType
     * @param username Username of BrowserStack
     * @param accessKey Access Key of BrowserStack
     * @param customProxy Custom Proxy String
     * @param logger Logger
     * @return BrowserStackClient
     */
    public static BrowserStackClient getBrowserStackClient(@Nonnull final ProjectType project, @Nonnull final String username,
                                                           @Nonnull final String accessKey, @Nullable final String customProxy,
                                                           @Nullable final PrintStream logger) {
        BrowserStackClient client = decideAndGetClient(project, username, accessKey);

        JenkinsProxySettings proxy;
        if (customProxy != null) {
            proxy = new JenkinsProxySettings(customProxy, logger);
        } else {
            proxy = new JenkinsProxySettings(logger);
        }

        if (proxy.hasProxy()) {
            client.setProxy(proxy.getHost(), proxy.getPort(), proxy.getUsername(), proxy.getPassword());
        }

        return client;
    }

    /**
     * Initializes BrowserStack client based on project type
     * @param project ProjectType
     * @param username Username of BrowserStack
     * @param accessKey Access Key of BrowserStack
     * @return BrowserStackClient
     */
    private static BrowserStackClient decideAndGetClient(@Nonnull final ProjectType project, @Nonnull final String username, @Nonnull final String accessKey) {
        if (project == ProjectType.APP_AUTOMATE) {
            return new AppAutomateClient(username, accessKey);
        }

        return new AutomateClient(username, accessKey);
    }
}
