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
import com.zink.fly.Notifiable;
import com.zink.fly.NotifyHandler;
import com.zink.fly.NotifyHandlerReturningEntry;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;


import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Method Codec
 *
 * This is where methods on the implementing interface get converted into into
 * the binary format calls. The binary formats of the methods are then passsed
 * to the remoter to carry them over to the fly host.
 *
 * This does not get involved in the evolution of Entries - that happens in the
 * VersionLink which is twined' with the MethodCodec.
 *
 * A non evolvable stub could eassily be made by excluding the VersionLink and
 * implementing the evolution preamble to the space direcly in this class.
 * Performance, of course, could be improved in this case.
 *
 */

public class MethodCodec {
    
    //
    // Set up the header to make a well formed Fly request
    // Add the opcode to the header to get the full header
    //
    // FAB1 in honour of the great Gerry Anderson
    // FAB2 for version 2 (Sorry Gerry, you are still The Daddy)
    public static final int FLY_HEADER = 0xFAB20000;
     //
    // Fly OpCodes (there is an order preference here)
    //
    private static final int PING = 0;
    private static final int READ = 1;
    private static final int TAKE = 2;
    private static final int WRITE = 3;
    private static final int NOTIFY = 4;
 
    // although may not ever make any sense to contact the
    // server it is reserved in the 'namespace' anyway
    private static final int SNAPSHOT = 5;
    private static final int READ_MANY = 6;
    private static final int TAKE_MANY = 7; 
    private static final int WRITE_MANY = 8;
    
    private static final int STATS = 9;
    
    // new notify codes 
    private static final int NOTIFY_WRITE = 20;
    private static final int NOTIFY_TAKE = 21;
    private static final int NOTIFY_WRITE_OBJECT = 22;
    private static final int NOTIFY_TAKE_OBJECT = 23;
    
    // *** Type support methods ***
    // Declare the structure of a newly discovered Entry type to the
    // Fly core. This may suggest a new Entry layout to the Server
    // in which case the server will reply with its view of how to structure
    // a well formed entry.
    public static final int CLASS_STRCUTURE_PREAMBLE = 10;
    
    //
    // TypeChain for object layout control and remoter for link to
    // Fly host
    //
    private TypeChain typeChain;
    private Remoter remoter;
    
    //
    // Streams for encoding the space operations into byte arrays
    //
    private ByteArrayOutputStream bos = 
                    new ByteArrayOutputStream(FieldCodec.DEFAULT_BUFFER_SIZE);
    private DataOutputStream dos = new DataOutputStream(bos);
    private DataInputStream dis = null;
    


    // TODO : This is way too tighly wired - as part of adding real evolution
    // undo and remake
    public MethodCodec(Remoter remoter, FieldCodec fieldCodec) {
        this.remoter = remoter;
        typeChain = new TypeChain(remoter, fieldCodec);
        NotifyMessageDispatcher nmd = new NotifyMessageDispatcher(typeChain);
        remoter.setNotifyMessageDispatcher(nmd);
        dis = remoter.getDataInputStream();
    }
    
    
    
    public synchronized Object read(Object tmpl, long timeout) {
        // get ready for replys and return values
        Object ret = null;
    
        try {
            // reset the output stream
            bos.reset();
            dos.writeInt(FLY_HEADER ^ READ);            
            dos.writeInt(typeChain.getChannel(tmpl));
            typeChain.writeObject(dos, tmpl);
            dos.writeLong(timeout);
            
            // send the op and get the reply
            long size = remoter.sendOperation(bos.toByteArray());
            if (size > 0) {
                ret = typeChain.readObject(dis, size, tmpl.getClass());
            }
        } catch (Exception ex) {
            throw new FlyAccessException(ex);
        }
        remoter.setMessageComplete();
        return ret;
    }
    
  
    
    public synchronized Object take(Object tmpl, long timeout) {
        Object ret = null;
        
        try {
            // reset the output stream
            bos.reset();
            // encode the header and operation
            dos.writeInt(FLY_HEADER ^ TAKE);
            dos.writeInt(typeChain.getChannel(tmpl));
            typeChain.writeObject(dos, tmpl);
            dos.writeLong(timeout);
            
            // send the op and get the reply
            byte [] outputBuffer = bos.toByteArray();
            
            long size = remoter.sendOperation(outputBuffer); 
            
            if (size > 0) {
                ret = typeChain.readObject(dis, size, tmpl.getClass());
            }
        }
        
        catch (Exception ex) {
            throw new FlyAccessException(ex);
        }
        remoter.setMessageComplete();
        return ret;
    }
    
    
        
    Object snapshot(Object template) {
        return template;
    }

    
    public synchronized long write(Object object, long lease) {
        long grantedLease = 0L;
        
        try {
            // reset the output stream
            bos.reset();
            // encode the header and operation
            dos.writeInt(FLY_HEADER ^ WRITE);
            int channel = typeChain.getChannel(object);
            dos.writeInt(channel);
            
            // since LBI2 need to write the UUID
            typeChain.writeIDObject(dos, object, UUID.randomUUID());
            dos.writeLong(lease);
            
            // send the op and get the reply
            grantedLease = remoter.sendOperation(bos.toByteArray());
           
        } catch (Exception ex) {
            throw new FlyAccessException(ex);
        }
        remoter.setMessageComplete();
        return grantedLease;
    }
    
    
    
    synchronized Collection readMany(Object template, long matchLimit, long ignore) {
        Collection entries = new ArrayList((int)matchLimit);
         try {
            // reset the output stream
            bos.reset();
            
            // Format = 
            // | Header | type channel : ZnInt | template : Entry | limit : ZnLong |  ignore : ZnLong |
            dos.writeInt(FLY_HEADER ^ READ_MANY);
            int typeChannel = typeChain.getChannel(template);
            dos.writeInt(typeChannel);
            typeChain.writeObject(dos, template);
            dos.writeLong(matchLimit);
            dos.writeLong(ignore);
            
            // send the op and get the reply
            long entryCount = remoter.sendOperation(bos.toByteArray());
                      

            for (int i = 0; i < entryCount; i++  ) {
                long size = dis.readLong();
                entries.add(typeChain.readObject(dis, size, template.getClass()));
            }
            
        } catch (Exception ex) {
            throw new FlyAccessException(ex);
        }
        remoter.setMessageComplete();
        return entries;
    }

   
    synchronized Collection takeMany(Object template, long matchLimit) {
    
        Collection entries = new ArrayList((int)matchLimit);
         try {
            // reset the output stream
            bos.reset();
            
            // Format = 
            // | Header | type channel : ZnInt | template : Entry | limit : ZnLong |  
            dos.writeInt(FLY_HEADER ^ TAKE_MANY);
            dos.writeInt(typeChain.getChannel(template));
            typeChain.writeObject(dos, template);
            dos.writeLong(matchLimit);

            // send the op and get the reply
            long entryCount =  remoter.sendOperation(bos.toByteArray());
                       
            for (int i = 0; i < entryCount; i++  ) {
                long size = dis.readLong();
                entries.add(typeChain.readObject(dis, size, template.getClass()));
            }
        } catch (Exception ex) {
            throw new FlyAccessException(ex);
        }
        remoter.setMessageComplete();
        return entries;
    }


    
    boolean notifyWrite(Object template, Notifiable handler, long leaseTime) {
        
        int opCode = 0;
        if (handler instanceof NotifyHandler) {
            opCode = FLY_HEADER ^ NOTIFY_WRITE;
        }
        if (handler instanceof NotifyHandlerReturningEntry) {
            opCode = FLY_HEADER ^ NOTIFY_WRITE_OBJECT;
        }
        checkNotifyOpcode(opCode);

        long notifyToken = notifyWrite(opCode, template, leaseTime);
        remoter.addNotifyDetails(notifyToken, handler, template.getClass());
        remoter.setMessageComplete();
        return Boolean.TRUE;
    }

    
    private synchronized long notifyWrite(int opCode, Object object, long lease) {
        long notifyToken = 0L;
        
        try {
            // reset the output stream
            bos.reset();
            
            dos.writeInt(opCode);
            int channel = typeChain.getChannel(object);
            dos.writeInt(channel);
            typeChain.writeObject(dos, object);
            dos.writeLong(lease);
            
            // send the op and get the reply
            notifyToken = remoter.sendOperation(bos.toByteArray());
            
        } catch (Exception ex) {
            throw new FlyAccessException(ex);
        }
        remoter.setMessageComplete();
        return notifyToken;
    }

    
    boolean notifyTake(Object template, Notifiable handler, long leaseTime) {
        
        int opCode = 0;
        if (handler instanceof NotifyHandler) {
            opCode = FLY_HEADER ^ NOTIFY_TAKE;
        }
        if (handler instanceof NotifyHandlerReturningEntry) {
            opCode = FLY_HEADER ^ NOTIFY_TAKE_OBJECT;
        }
        checkNotifyOpcode(opCode);
        
        long notifyToken = notifyTake(opCode, template, leaseTime);
        remoter.addNotifyDetails(notifyToken, handler, template.getClass());
        remoter.setMessageComplete();
        return Boolean.TRUE;
    }

    
    private synchronized long notifyTake(int opCode, Object object, long lease) {
        long notifyToken = 0L;
        
        try {
            // reset the output stream
            bos.reset();
            
            dos.writeInt(opCode);
            int channel = typeChain.getChannel(object);
            dos.writeInt(channel);
            typeChain.writeObject(dos, object);
            dos.writeLong(lease);
            
            // send the op and get the reply
            notifyToken = remoter.sendOperation(bos.toByteArray());
            
        } catch (Exception ex) {
            throw new FlyAccessException(ex);
        }
        remoter.setMessageComplete();
        return notifyToken;
    }


    private void checkNotifyOpcode(int opCode) {
        if (opCode == 0) {
            String msg = "Handler must implement NotifyHandler or NotifyHandlerReturningEntry";
            throw new IllegalArgumentException(msg);
        }

    }

    
}
