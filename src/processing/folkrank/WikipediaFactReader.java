/**
 * Copyright (c) 2006-2009, NEPOMUK Consortium
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice, 
 *       this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the 
 * 	documentation and/or other materials provided with the distribution.
 *
 *     * Neither the name of the NEPOMUK Consortium nor the names of its 
 *       contributors may be used to endorse or promote products derived from 
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 **/
package processing.folkrank;

import common.UserData;
import file.WikipediaReader;

public class WikipediaFactReader implements FactReader {

	private WikipediaReader reader;
	private int trainSize;
	private int lineIndex;
	private int tagIndex;
    private int noOfDimensions;

    public WikipediaFactReader (WikipediaReader reader, int trainSize, int noOfDimensions) {
        this.noOfDimensions = noOfDimensions;
        this.reader = reader;
        this.trainSize = trainSize;
        this.lineIndex = 0;
        this.tagIndex = -1;
    }

    public String[] getFact() throws FactReadingException {
    	UserData data = this.reader.getUserLines().get(this.lineIndex);
    	String[] fact = new String[this.noOfDimensions];

    	fact[0] = data.getTags().get(this.tagIndex).toString();
    	fact[1] = Integer.toString(data.getUserID());
    	fact[2] = Integer.toString(data.getWikiID());
    	
    	return fact;
    }

    public boolean hasNext() throws FactReadingException {
    	if (this.lineIndex < this.trainSize) {
    		UserData data = this.reader.getUserLines().get(this.lineIndex);
    		if (++this.tagIndex < data.getTags().size()) {
    			return true;
    		} else {
    			this.tagIndex = 0;
    			if (++this.lineIndex < this.trainSize) {
    				return true;
    			}
    		}
    	}
    	return false;
    }

    public void reset() throws FactReadingException {
    	this.lineIndex = 0;
    	this.tagIndex = -1;
    }

    public int getNoOfDimensions() throws FactReadingException {
        return this.noOfDimensions;
    }

    public void close() throws FactReadingException {
    	reset();
    }
}
