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


package com.zink.fly.stats;



import com.zink.fly.stub.StringCodec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author nigel
 */
public class StatsDecoder {
   
    private static final byte [] statsOp = new byte[] { (byte)0xfa, (byte)0xb2, (byte)0x0, (byte)0x09 };
    private static final String EMPTY_STRING = "";
    private static final StringCodec codec = new StringCodec();
     

    public StatsBean[] stats (Socket sock) {
        return getStatsArray(sock, EMPTY_STRING);
    }
    
    
    public StatsBean stats(Socket sock, String className) {
        return getStatsArray(sock, className)[0];
    }
        
 
    
    public StatsBean [] getStatsArray(Socket sock, String className) {
        StatsBean [] stats = null;
        try {
            DataInputStream dis = new DataInputStream(sock.getInputStream());
            DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
            dos.write(statsOp);
            codec.writeString(dos, className);
            
            return getStatsArray(dis);
            
        } catch (Exception exp ) {
            // ignored - null array means no stats
        }
        return stats;
    }
    
    
    
    public StatsBean[] getStatsArray(DataInputStream dis) throws IOException { 
            long statsCount = dis.readLong();
            StatsBean [] stats = new StatsBean[(int)statsCount];
            for (int i = 0; i < statsCount; i++) {
                stats[i] = StatsBean.makeBeanFromStream(dis);
            }
            return stats;
    }
}
