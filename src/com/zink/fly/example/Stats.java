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

package com.zink.fly.example;


import com.zink.fly.stats.StatsBean;
import com.zink.fly.stats.StatsDecoder;
import java.net.Socket;
import java.util.Date;


/**
 * Assemble some tools from the stats to  produce a stream
 * of logging that can be read easily by Splunk for example
 *
 * @author nigel
 */
public class Stats {

    
    public static void main(String [] args) throws InterruptedException {
        try {

            //final String HOST = "localhost";
            final String HOST = "192.168.2.13";
            final int DEFAULT_PORT = 4396;
            final int SAMPLE_TIME = 1 * 1000;
            
            Socket socket = new Socket(HOST, DEFAULT_PORT);
            StatsDecoder statser = new StatsDecoder();

            while (Boolean.TRUE) {
                formatForSplunk(statser.stats(socket));
                Thread.sleep(SAMPLE_TIME);
            }
        } catch (Exception ex) {
            System.out.println("No connection - check Fly is running on local host :" + ex.getMessage());
        }
        
    }



    
    public static void formatForSplunk(StatsBean[] beans) {

        String dateStr = (new Date()).toString();
        for (StatsBean bean : beans) {
            String logFormat = dateStr;
            logFormat += " type=" + bean.getTypeName();
            logFormat += " entryCount=" + bean.getEntryCount();
            logFormat += " writes=" + bean.getWrites();
            logFormat += " totalReads=" + bean.getTotalReads();
            logFormat += " matchedReads=" + bean.getMatchedReads();
            logFormat += " totalTakes=" + bean.getTotalReads();
            logFormat += " matchedTakes=" + bean.getMatchedReads();
            logFormat += " notifyOnWrites=" + bean.getNotifyWriteTmpls();
            logFormat += " notifyOnTakes=" + bean.getNotifyTakeTmpls();
            System.out.println(logFormat);
        }
    }
    
}
