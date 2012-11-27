
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 *
 * @author nigel
 */
public class CompressingFieldCodec implements FieldCodec {

    private final int BUFFER_SIZE = 1024;


    // buffer for coding java objects into byte arrays
    private ByteArrayOutputStream bos = new ByteArrayOutputStream(BUFFER_SIZE);

    public byte[] writeField(Object field) {
        ObjectOutputStream oos = null;
        try {
            bos.reset();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(field);
            oos.flush();
        } catch (IOException ex) {
            throw new FlyAccessException(ex);
        }
        return compress(bos.toByteArray());
    }


    public Object readField(byte[] fieldBytes) {
        Object object = null;

        ByteArrayInputStream bis = new ByteArrayInputStream(decompress(fieldBytes));
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(bis);
            object = ois.readObject();
        } catch (Exception ex) {
            throw new FlyAccessException(ex);
        }

        return object;

    }

    private byte[] compress(byte[] toCompress) {
        // set up the required buffers
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Deflater compressor = new Deflater();   // move to fields
            compressor.setLevel(Deflater.BEST_COMPRESSION); // move to ctor
            compressor.setInput(toCompress);
            compressor.finish();

            byte[] buffer = new byte[BUFFER_SIZE];
            while (!compressor.finished()) {
                compressor.deflate(buffer);
                baos.write(buffer);
            }
            baos.close();
        } catch (IOException ex) {
            throw new FlyAccessException("Compression failed in field codec");
        }
        return baos.toByteArray();
    }

    private byte[] decompress(byte[] toDecompress) {
        Inflater decompressor = new Inflater();
        decompressor.setInput(toDecompress);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(toDecompress.length);
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
                bos.close();
            }
        } catch (Exception ex) {
            throw new FlyAccessException("Decompression failed in field codec");
        }

        return bos.toByteArray();
    }
}
