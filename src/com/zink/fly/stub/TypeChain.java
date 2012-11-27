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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class TypeChain {
    
    // State variable to mark an empty entry
    private final long EMPTY_ENTRY = 0x7FFFFFFF;
    
    // the remoter for sending and receiving the info
    private Remoter remoter;
    private DataInputStream dis;
    private ObjectCodec objCodec;
    private FieldFilter filter = new FieldFilter();

 
    // Makes empty objects
    private EmptyObjectFactory objectFact = new EmptyObjectFactory();
    
    // The map of Bridges that maps between the various views of the Objects layouts
    private Map<Class, VersionLink> bridges = new HashMap<Class, VersionLink>();
    

    public TypeChain(Remoter remoter, FieldCodec fieldCodec) {
        try {
            this.objCodec = new ObjectCodec(fieldCodec);
            this.remoter = remoter;
            dis = remoter.getDataInputStream();
  
        }
       catch (Exception exp) {
           throw new FlyAccessException("Could not make TypeChain", exp);
       }
    }
    
    
    //
    // Get Channel will initiate the negotiation with the server
    //
    // Check if we need pass this Entry through the mapper
    // if it doenst match the structure of the entry in the server.
    //
    public synchronized int getChannel(Object object) {
        // first find the Vesion bridge of this entry within fly
        Class type = object.getClass();
        VersionLink bridge = (VersionLink)bridges.get(type);
        
        // If the layout is null we havent seen this type before so
        // a negotiation is required
        if (bridge == null) {
            // check that the entry is public
            int modifiers = object.getClass().getModifiers();
            if (!Modifier.isPublic(modifiers)) {
                throw new IllegalArgumentException("Fly entries must be public :" + object.getClass());
            }
            // derive the layout of this fly entry
            ObjectLayout stubLayout = new ObjectLayout(object);
            
            // create the bridge from the this layout and the layout that the
            // fly host responds with
            bridge = createBridge(stubLayout);
            bridges.put(type, bridge);
        }
        
        return bridge.getChannel();
    }
    
    
    // some io buffers for comms
    private ByteArrayOutputStream bos = 
                new ByteArrayOutputStream(FieldCodec.DEFAULT_BUFFER_SIZE);
    private DataOutputStream dos = new DataOutputStream(bos);

    
    //
    // Chat with the fly host to establish the agreed layout.
    // This may be a number of proposals as part of an entry negotiation
    // but in this case this is not the case.
    //
    private VersionLink createBridge(ObjectLayout stubLayout) {
        
        // using countered becuase this may be a more than one pass
        // evolution.
        VersionLink bridge = null;
       
        
        try {
            // reset the underlying byte array.
            bos.reset();
            // encode the header and operation
            dos.writeInt(MethodCodec.FLY_HEADER + MethodCodec.CLASS_STRCUTURE_PREAMBLE);
            // write the entry layout onto the stream
            stubLayout.write( dos );
            
            // send the op and get the reply
            long response = remoter.sendOperation( bos.toByteArray());
            
            // for object layout changes check the repsonse here
            
            // now read in the layout object that the server countered with
            ObjectLayout countered = new ObjectLayout(dis, 0);
            
            // and clear the read queue
            remoter.setMessageComplete();
            
            // put the response in both entries
            int channel = countered.getChannel();
            stubLayout.setChannel(channel);
            
            bridge = new VersionLink(stubLayout,countered,channel,response);
            
        } catch (Exception ex) { 
            throw new FlyAccessException(ex);
        }
        
        return bridge;
    }
    
   
    // TODO - even more refactoring required
    public void writeObject(DataOutputStream outStream, Object object) {
        writeIDObject(outStream,object,null);
    }
    
    
    public void writeIDObject(DataOutputStream outStream, Object object, UUID id) {
        // so this would perform the evolve given the stub and
        // and hostLayout
        
        // Lookup the bridge for the this object
        VersionLink bridge = (VersionLink)bridges.get(object.getClass());
        
        // perform the mapping here if required (assuming not)
        if (bridge.getEvolutionResponse() == 0) {
            try {
                // write the number of fields
                int fieldCount = bridge.getHostLayout().getInfos().size();
                outStream.writeInt(fieldCount);
                
                if (id != null) {                
                    outStream.writeLong(id.getMostSignificantBits());
                    outStream.writeLong(id.getLeastSignificantBits());
                }
                      
                for (Field field : ObjectLayout.getAllFields(object.getClass())) {
                    if (filter.applyFilter(field)) {
                        field.setAccessible(Boolean.TRUE);
                        objCodec.writeObject(outStream,  (Serializable)field.get(object));
                    }  
                }
                
            } catch (Exception ex) {
               throw new FlyAccessException(ex);
            }
        } else {
            String msg = "Entry layout in Fly server differs to " + object.getClass().getName();
            throw new FlyAccessException(msg);
        }      
    }
    
    
    public Object readObject(DataInputStream dis, long size, Class type) {
        Object ret = null;
        try {

            ret = objectFact.makeEmptyObject(type);
            
            // read the UUID 
            long msb = dis.readLong();
            long lsb = dis.readLong();
            // TODO - Assemble the UUID here 

            // if the entry is empty then we are done
            if (size == EMPTY_ENTRY ) {
                return ret;
            }

            
            int passingFields = 0; 
            for ( Field field : ObjectLayout.getAllFields(ret.getClass()) ) {
                if ( filter.applyFilter(field)  ) {
                   Object fieldValue = objCodec.readObject(dis);
                   field.setAccessible(Boolean.TRUE);
                   field.set(ret, fieldValue);
                   ++passingFields;
                }
            }
            if (passingFields != size ) {            
                throw new IllegalStateException("Fly Internal Type Error : filtered fields do not match");
            }   
           
        } catch (Exception ex) {
            throw new FlyAccessException(ex);
        }
        return ret;
    }
    
    
}
