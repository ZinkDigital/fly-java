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


import com.zink.fly.Fly;
import com.zink.fly.kit.FlyFinder;

/**
 *
 * @author nigel
 */
public class WriteManyReads {
    
     
    public static void main(String[] args) {

        // parse for non default args
        int iterations = 1000000;
        if (args.length > 0) {
            iterations = Integer.parseInt(args[0]);
        }                 
                
        Fly fly = new FlyFinder().find();
        if (fly == null) {
            System.err.println("Failed to find a Fly Server running on the local network");
            System.exit(1);
        }

        final int payloadSize = 100;

        // set up an object to write to the space
        FlyEntry object = new FlyEntry();
        object.name = "Fly 1";
        object.reference = new Integer(17);
        object.setPayloadOfSize(payloadSize);


        // set up a tempate to match the above object 
        FlyEntry template = new FlyEntry();
        template.name = "Fly 1";
        template.reference = null;    // match any value in this template
        template.payload = null;      // ditto


        System.out.println("Processing 1 write and " + iterations + " reads");
        long start = System.currentTimeMillis();
        fly.write(object, iterations * 100);
        for (int i = 0; i < iterations; i++) {       
            fly.read(template, 0L);
        }
        long end = System.currentTimeMillis();
        float timeInSeconds = (float) (end - start) / 1000.0f;
        System.out.println("Which took " + timeInSeconds + " seconds\n");
    }
  
}
