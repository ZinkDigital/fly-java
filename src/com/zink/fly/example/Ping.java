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

import com.zink.fly.FlyPrime;
import com.zink.fly.kit.FlyFinder;

/**
 *
 * @author nigel
 */
public class Ping {
    
    public static void main(String [] args) throws InterruptedException {
    
        System.out.println("Ping started.");
        
        int shots = 100;
        if (args.length > 0) {
            shots = Integer.parseInt(args[0]);
        }
         
        System.out.println("Ready to play " + shots + " shots");
        
        FlyFinder flyFinder = new FlyFinder();
        FlyPrime fly = flyFinder.find();
        if (null == fly) {
            System.err.println("Failed to find a Fly Server running on the local network");
            System.exit(1);
        }
        
        Ball template = new Ball();
        template.player = "Pong";  // looking for ball that has been hit by Pong
        template.batted = null;     // match any value for batted counter
        
        Ball gameBall = (Ball) fly.take(template, 0L);

        if (gameBall == null) {
            System.out.println("No ball in play");
            serveBall(fly);
            System.out.println("Served Ball - Please start a Pong");
        }  else {
            System.out.println("Recieved ball - game on!");
            returnBall(fly,gameBall);
        }      
                
        int myShots = 1;
        while ( myShots  < shots ) {
            Ball ball = fly.take(template, 0L);
            if (ball != null) {
                returnBall(fly,ball);
                myShots += 1;
                if ( myShots % 10 == 0) System.out.print(".");
            } else {
                Thread.sleep(10);
            }
        }
        System.out.println("\nPlayed all my " + myShots + " shots");    
        }
        
       
   private static void serveBall(FlyPrime fly) {
       Ball gameBall = new Ball();
       gameBall.batted  = new Integer(1);
       gameBall.player = "Ping";
       fly.write(gameBall, 60*1000L);
   } 
    
    
   private static void returnBall(FlyPrime fly, Ball ball ) {     
       int hitCount=ball.batted.intValue();
       ball.batted = new Integer(hitCount + 1);
       ball.player = "Ping";
       fly.write(ball, 1*1000L);
   }
      
        
}
