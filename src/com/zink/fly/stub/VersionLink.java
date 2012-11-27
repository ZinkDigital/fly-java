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

/** 
 * The VersionLink stores both the evolution response and the channel relating
 * to the associated class type
 * 
 *  It also stores both views of the class layout so that it can link (map) between 
 *  them.
 *  
 * @author nigel
 *
 */


public class VersionLink {
	
	// the layouts for both views of the class
	private ObjectLayout stubLayout;
	private ObjectLayout hostLayout;
	
	// the type channel that the client should always ues to refer to this 
	// type 
	private int channel;

	// the response from the server about the proposed EntryLayout
	private long evolutionResponse;
	
	
	
	public VersionLink(ObjectLayout stubLayout, ObjectLayout hostLayout, int channel, long evolutionResponse) {
		super();
		this.stubLayout = stubLayout;
		this.hostLayout = hostLayout;
		this.channel = channel;
		this.evolutionResponse = evolutionResponse;
	}
	
	


	/**
	 * @return the channel
	 */
	public int getChannel() {
		return channel;
	}
	/**
	 * @param channel the channel to set
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}
	/**
	 * @return the evolutionResponse
	 */
	public long getEvolutionResponse() {
		return evolutionResponse;
	}
	/**
	 * @param evolutionResponse the evolutionResponse to set
	 */
	public void setEvolutionResponse(long evolutionResponse) {
		this.evolutionResponse = evolutionResponse;
	}
	/**
	 * @return the hostLayout
	 */
	public ObjectLayout getHostLayout() {
		return hostLayout;
	}
	/**
	 * @param hostLayout the hostLayout to set
	 */
	public void setHostLayout(ObjectLayout hostLayout) {
		this.hostLayout = hostLayout;
	}
	/**
	 * @return the stubLayout
	 */
	public ObjectLayout getStubLayout() {
		return stubLayout;
	}
	/**
	 * @param stubLayout the stubLayout to set
	 */
	public void setStubLayout(ObjectLayout stubLayout) {
		this.stubLayout = stubLayout;
	}

	
	// 
	
	
	
	
}
