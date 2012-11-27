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

package com.zink.fly;

/**
 *
 * @author Nigel
 */
public interface NotifyHandler extends Notifiable {
    
    /**
     * One of the parameters to any notify method is a class that implements
     * a Notifiable interface. NotifyHandler simply extends Notifiable with
     * a method that is called when an object in one of the notifies queues
     * matches the notify template object.
     *
     * If you need a copy of the matching entry returned then use NotifyHandler
     */
    void templateMatched();
    
}


