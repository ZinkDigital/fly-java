/*
 * Copyright (c) 2006-2012 Zink Digital Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.zink.fly.kit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Discovery conector makes a unicast (TCP/IP) connection to a 'remote' space
 * running on a reacable IP address. It can then reply to requests on the
 * local network for discovey request from space clients. This a means
 * that FlyFinder and FlyCache can be used in all client without direct
 * reference to IP addresses etc.
 *
 * This makes EC2 instances, for example, able to run clients without
 * reconfiguring or recompiling code. So this forms part of Cortez
 * and may also be useful in other situations.
 *
 * @author nigel
 *
 */
public class DiscoveryConnector implements RequestHandler , Callable {


    private static final int DEFAULT_PING_INTERVAL = 5 * 1000;
    private static final int DEFAULT_PING_TIMEOUT = 2 * 1000;

    private InetAddress remoteAddress;
    private FlyPinger pinger;
    private int pingInterval = DEFAULT_PING_INTERVAL;

    private ExecutorService requestListener;
    private MulticastResponder responder;

    private boolean currentlyConnected = false;
    private String [] tags;


    public DiscoveryConnector(InetAddress address) {
        remoteAddress = address;
        pinger = new FlyPinger(DEFAULT_PING_TIMEOUT);
        responder = new MulticastResponder();
        requestListener = Executors.newSingleThreadExecutor();
        requestListener.submit(new MulticastRequestListener(this));
    }


    public Object call()  {

        try {
             boolean output = true;

             while ( !Thread.interrupted() ) {
                try
                    {
                    tags = pinger.ping(remoteAddress);
                    if (tags != null && tags.length !=0 ) {
                        tags[0] += ":" + remoteAddress.getHostAddress();
                        if (!currentlyConnected) {
                            currentlyConnected = true;
                            output = true;
                        }
                    } else {
                        if (currentlyConnected) {
                            currentlyConnected = false;
                            output = true;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (output) {
                    if (currentlyConnected)
                        System.out.println("<~> " + remoteAddress.getHostAddress());
                    else
                        System.out.println(">~< " + remoteAddress.getHostAddress());
                    output = false;
                }
                
                Thread.sleep(pingInterval);
             }
        } catch (Exception ex) {
            System.err.println("Tunnel thread broken " + ex.toString());
        } finally {
           // stop the listner what ever the outcome
            requestListener.shutdown();
        }

       return null;
    }


    private final StringBuilder sb = new StringBuilder();
    private final char SPACE_CHAR = ' ';


    public void request(String id) {
        // could check that ID is 0xFLY but it doent matter we
        // can just reply.
        if (currentlyConnected) {
            sb.setLength(0);
            boolean first = Boolean.TRUE;
            for (String tag : tags) {
                if (first == true) {
                    first = Boolean.FALSE;
                } else {
                    sb.append(SPACE_CHAR);
                }
                sb.append(tag);
            }
            responder.sendResponse(sb.toString());
        }
    }



    public static void main(String [] addresses) throws UnknownHostException {

        if (addresses.length < 1) {
            System.out.println("Usage : DiscoveryConnector IPAddress [IPAddress  ...]");
            System.exit(1);
        }

        ExecutorService tunnels = Executors.newCachedThreadPool();
        try {
            for (String address : addresses) {
                InetAddress netAddress = InetAddress.getByName(address);
                tunnels.submit(new DiscoveryConnector(netAddress));
            }
       } catch (Exception ex) {
            System.err.println("Failed to start DiscoveryConnector with the following error.");
            ex.printStackTrace(System.err);
            tunnels.shutdownNow();
       }
    }
}
