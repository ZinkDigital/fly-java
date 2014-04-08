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

import com.zink.fly.FieldCodec;
import com.zink.fly.Fly;
import com.zink.fly.FlyAccessException;
import com.zink.fly.stub.FlyStub;
import com.zink.fly.stub.SerializingFieldCodec;
import java.util.Collection;



/**
 *
 * @author nigel
 */
public class FlyFinder  {
     
    private static final int FINDING_SLEEP = 10;  //ms
    
    private static final int FIND_MAX = 11;
    private static final int FIND_ALL_MAX = 21;
    
    private final FieldCodec fieldCodec;
    private FlyCache cache = new FlyCache();
    
    /*
     * create a finder that uses the default Codec 
     */
    public FlyFinder() {
        this(new SerializingFieldCodec());
    }
    
    
    /**
     * Use the supplied field codec to use non java serialized entries
     * 
     * @param fieldCodec
     */
    public FlyFinder(FieldCodec fieldCodec) {
        this.fieldCodec = fieldCodec;
    } 
    
    
    /**
     * Find any Fly instance on the local sub net and return a reference to it
     *
     * @return a reference to a Fly interface
     */
    public Fly find() {
        Fly fly = null;
        
        try {
            cache.start(); 
            Collection<FlyServerRep> reps = cache.getAllReps();
            
            // wait for any space to be found
            int sleeps;
            for ( sleeps = 0 ; reps.isEmpty() && sleeps < FIND_MAX; sleeps++) {
                if ((sleeps+1) % 5 == 0 ) {
                    cache.issueRequest();
                }
                Thread.sleep(FINDING_SLEEP);
                reps = cache.getAllReps();
            }
            
            // either something is found or we ran out of time
            if (!reps.isEmpty()) {
                FlyServerRep rep = (FlyServerRep)reps.toArray()[0];
                fly = new FlyStub(rep.getFlyAddr(), fieldCodec);
            }
        } catch (Exception ex) {
           throw new FlyAccessException(ex);
        } finally {
           cache.terminate();
        }

        postCheck(fly);

        return fly;
    }

    private void postCheck(Fly fly) {
        if (fly == null) {
            throw new FlyAccessException("Cannot find Fly");
        }
    }

	/**
     * Find a Fly instance on the local sub net that mathces the tag supplied 
     * in the array of tags supplied. 
     * 
     * 
     * @param tag - the tag used to match with the FlySpace tags
     * @return a reference to a Fly interface - null if none found
     */            
    public Fly find(String tag) {
        return find(new String[] { tag });
    }
    
    
    /**
     * Find a Fly instance on the local sub net that matches the tags supplied 
     * in the array of tags supplied. All of the tags provided as a parameter 
     * must match those provided by the Fly instance. 
     * 
     * 
     * @param tags - the tags used to match with the FlySpace tags
     * @return a reference to a Fly interface - null if none found
     */         
    public Fly find(String[] tags) {
        Fly fly = null;
        try {
            cache.start();
            for (int sleeps = 0; fly == null && sleeps < FIND_ALL_MAX; sleeps++) {
                if ( (sleeps+1) % 5 == 0 ) {
                    cache.issueRequest();
                }
                Thread.sleep(FINDING_SLEEP);
                Collection<FlyServerRep> reps = cache.getAllReps();
                for (FlyServerRep rep : reps) {
                    if (rep.tagsMatch(tags)) {
                        fly = new FlyStub(rep.getFlyAddr(), fieldCodec);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            throw new FlyAccessException(ex);
        } finally {
            cache.terminate();
        }

        postCheck(fly);

        return fly;
    }
}



