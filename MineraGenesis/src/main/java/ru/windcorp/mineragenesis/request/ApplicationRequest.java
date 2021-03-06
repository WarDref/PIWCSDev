/* 
 * MineraGenesis Minecraft mod
 * Copyright (C) 2019  Javapony and contributors
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package ru.windcorp.mineragenesis.request;

public abstract class ApplicationRequest {
	
	private static short nextRequestId = 0;
	private final short requestId;
	
	private final ChunkLocator chunk;
	
	public ApplicationRequest(ChunkLocator chunk) {
		this.requestId = nextRequestId++;
		this.chunk = chunk;
	}

	public short getRequestId() {
		return requestId;
	}
	
	public ChunkLocator getChunk() {
		return chunk;
	}

	@Override
	public String toString() {
		return "ApplicationRequest[requestId=" + requestId + ", chunk=" + chunk + "]";
	}

}
