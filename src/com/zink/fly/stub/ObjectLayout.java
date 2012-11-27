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

package com.zink.fly.stub;


import com.zink.fly.FlyAccessException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ObjectLayout {
    //
    private String className;
    private List<FieldInfo> infos = new ArrayList<FieldInfo>();
    private int channel;
    private StringCodec stringCodec = new StringCodec();
    private FieldFilter filter = new FieldFilter();

    //
    // Before any evolution goes on, the TypeChain is responsible for proposing
    // an entry layout to the FlyServer. 
    // 
    public ObjectLayout(Object object) {

        className = object.getClass().getName();

        // get fields and the sub types
        // Field[] fields = object.getClass().getDeclaredFields();
        List<Field> fields = getAllFields(object.getClass());

        // now run over 
        for (Field field : fields) {
            if (filter.applyFilter(field)) {
                try {
                    String type = field.getType().getName();
                    String name = field.getName();
                    infos.add(new FieldInfo(type, name));
                
                 } catch (Exception ex) { 
                        throw new FlyAccessException(ex);
        
                }
            }
        }
    }

    // create this entry layout from an input stream using a dumby 
    // parameter to make sure the 
    public ObjectLayout(DataInputStream dis, int ignored) {
        try {
            className = stringCodec.readString(dis);
            channel = dis.readInt();
            int size = dis.readInt();

            for (int i = 0; i < size; i++) {
                String type = stringCodec.readString(dis);
                String name = stringCodec.readString(dis);
                infos.add(new FieldInfo(type, name));
            }
        } catch (Exception ex) {
            throw new FlyAccessException(ex);
        }
    }
    //
    // For the purposes of sending an entry layout to the server
    //
    public void write(DataOutputStream dos) {
        try {
            // write the class name
            stringCodec.writeString(dos, className);
            dos.writeInt(channel);
            dos.writeInt(getInfos().size());

            for (FieldInfo info : getInfos()) {
                stringCodec.writeString(dos, info.getType());
                stringCodec.writeString(dos, info.getName());
            }
        } catch (Exception ex) {
            throw new FlyAccessException(ex);
        }
    }

    /**
     * @return the channel
     */
    public int getChannel() {
        return channel;
    }

    /**
     * @param channel
     *            the channel to set
     */
    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String toString() {
        String str = new String();
        str += "Layout for :" + className + "\n";
        str += "Type channel :" + channel + "\n";
        for (FieldInfo info : getInfos()) {
            str += "Type: " + info.getType() + "\tName: " + info.getName() + "\n";
        }
        return str;
    }

    public List<FieldInfo> getInfos() {
        return infos;
    }


    // recurse the inheritance tree and get all of the fields
    static public List<Field> getAllFields(Class type) {
         List<Field> fields = new LinkedList<Field>();

         // add this classes fields
         for (Field field : type.getDeclaredFields() ) {
            fields.add(field);
         }

         // add the super classes fields
         if (type.getSuperclass() != null) {
            fields.addAll(getAllFields(type.getSuperclass()));
         }

         return fields;
    }


}
