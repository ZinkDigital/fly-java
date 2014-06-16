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

import com.zink.fly.FlyAccessException;
import java.net.InetAddress;

/**
 * @author nigel
 */
public class FlyServerRep {

    private static int DEFAULT_FLY_PORT = 4396;
    
    private String [] flyTags;
    private int flyPort;
    private InetAddress flyAddr;
    

    FlyServerRep(InetAddress addr, String[] tags) {
        flyAddr = addr;
        flyPort = DEFAULT_FLY_PORT;
        flyTags = tags;
        parseFirstTag(tags[0]);     // may overwrite the above
    }
    

    /*
     * The first tag may contain an adrress and port in which case they get
     * written.
     */
    private void parseFirstTag(String firstTag) {
        if (firstTag != null) {
            try {
                String [] sshLike = firstTag.split("[@:]");

                // if we have at least an @ then we must assume its an address
                // encoded in an ssh style -> name@address<:port>
                if (sshLike[1] != null) {
                        flyAddr = InetAddress.getByName(sshLike[1]);
                        // remove the address from the first tag for
                        // later matching
                        flyTags[0] = sshLike[0];
                }

                if (sshLike[2] != null) {
                    setFlyPort(Integer.parseInt(sshLike[2]));
                }

            }  catch (Exception ex) {
                new FlyAccessException("Malformed first tag in reply from server " + firstTag, ex);
            }
        }
    }


    
    public InetAddress getFlyAddr() {
        return flyAddr;
    }

    public void setFlyAddr(InetAddress flyAddr) {
        this.flyAddr = flyAddr;
    }

    public int getFlyPort() {
        return flyPort;
    }

    public void setFlyPort(int flyPort) {
        this.flyPort = flyPort;
    }

    public String[] getFlyTags() {
        return flyTags;
    }

    public void setFlyTags(String[] flyTags) {
        this.flyTags = flyTags;
    }
   
    
    /**
     * Test if the tags in this server rep match the seach tags
     * 
     * 
     * @param tags The tags are being search for by the user
     * @return true if the tags match, false otherwise
     */
    public boolean tagsMatch(String[] tags) {
        // if the tags from the space are null it doenst exist
        if (getFlyTags() == null) return false;
        // if the supplied tags are null then it has to match 
        if (tags == null) return true;
        // run over the supplied tags check the space matches all of them.
        for (String tag : tags) {
            if (!containsTag(flyTags, tag)) return false;
        }
        return true;
    }
    
    
    private boolean containsTag(String[] flyTags, String tag) {
        for (String flyTag : flyTags) {
            if (tag.equals(flyTag)) return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

   
}
