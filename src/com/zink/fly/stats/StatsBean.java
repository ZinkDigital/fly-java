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
import java.io.IOException;

/**
 *
 * @author nigel
 */
public class StatsBean {
    
    private static StringCodec codec = new StringCodec();
    
    private String typeName; 
    private int typeChannel;
    
    private long entryCount; 
    private long writes;
    
    private long totalReads;
    private long matchedReads;
   
    private long totalTakes;
    private long matchedTakes;

    private long notifyWriteTmpls;
    private long notifyTakeTmpls;
    

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeChannel() {
        return typeChannel;
    }

    public void setTypeChannel(int typeChannel) {
        this.typeChannel = typeChannel;
    }

    public long getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(long entryCount) {
        this.entryCount = entryCount;
    }

    public long getWrites() {
        return writes;
    }

    public void setWrites(long writes) {
        this.writes = writes;
    }

    public long getTotalReads() {
        return totalReads;
    }

    public void setTotalReads(long totalReads) {
        this.totalReads = totalReads;
    }

    public long getMatchedReads() {
        return matchedReads;
    }

    public void setMatchedReads(long matchedReads) {
        this.matchedReads = matchedReads;
    }

    public long getTotalTakes() {
        return totalTakes;
    }

    public void setTotalTakes(long totalTakes) {
        this.totalTakes = totalTakes;
    }

    public long getMatchedTakes() {
        return matchedTakes;
    }

    public void setMatchedTakes(long matchedTakes) {
        this.matchedTakes = matchedTakes;
    }

     public long getNotifyWriteTmpls() {
        return notifyWriteTmpls;
    }

    public void setNotifyWriteTmpls(long notifyWriteTmpls) {
        this.notifyWriteTmpls = notifyWriteTmpls;
    }

    public long getNotifyTakeTmpls() {
        return notifyTakeTmpls;
    }

    public void setNotifyTakeTmpls(long notifyTakeTmpls) {
        this.notifyTakeTmpls = notifyTakeTmpls;
    }
     
    
      
    public static StatsBean makeBeanFromStream(DataInputStream dis) throws IOException {
        StatsBean bean = null;

        bean = new StatsBean();
        bean.setTypeName(codec.readString(dis));
        bean.setTypeChannel(dis.readInt());
        bean.setEntryCount(dis.readLong());
        bean.setTotalReads(dis.readLong());
        bean.setMatchedReads(dis.readLong());
        bean.setTotalTakes(dis.readLong());
        bean.setMatchedTakes(dis.readLong());
        bean.setWrites(dis.readLong());
        bean.setNotifyWriteTmpls(dis.readLong());
        bean.setNotifyTakeTmpls(dis.readLong());

        return bean;
    }


}
