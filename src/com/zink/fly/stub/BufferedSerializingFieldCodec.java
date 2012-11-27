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

import com.zink.fly.FieldCodec;
import com.zink.fly.FlyAccessException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;



/**
 *
 * @author nigel
 */
public class BufferedSerializingFieldCodec implements FieldCodec {


    // buffers and streams for coding java objects into byte arrays
    private ByteArrayOutputStream baos = null;
    private ObjectOutputStream oos = null;

    // and from byte [] to objects
    private ByteBufferInputStream bbis = null;
    private ObjectInputStream ois = null;


    public BufferedSerializingFieldCodec() {
       //System.out.println(this.getClass().getName());
       initStreams();
    }


    private void initStreams() {
        try {
            // set up the output strems
            baos = new ByteArrayOutputStream(this.DEFAULT_BUFFER_SIZE);
            oos = new ObjectOutputStream(baos);
            oos.flush();
            byte[] bytes = baos.toByteArray();
            baos.reset();
            oos.reset();

            // sey up the input strems
            bbis = new ByteBufferInputStream(ByteBuffer.wrap(bytes));
            ois = new ObjectInputStream(bbis);

        } catch (IOException ex) {
            throw new FlyAccessException(ex);
        }

    }



    public  byte [] writeField(Object field) {
        try {
            baos.reset();
            oos.writeObject(field);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            throw new FlyAccessException(ex);
        }
        return baos.toByteArray();
    }


    public Object readField(byte [] fieldBytes) {
        Object object = null;
        try {
            bbis.switchBuffer(ByteBuffer.wrap(fieldBytes));
            object = ois.readObject();
        } catch (Exception ex) {
           throw new FlyAccessException(ex);
        }
        return object;

    }

    public Object readField(InputStream inStream) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}