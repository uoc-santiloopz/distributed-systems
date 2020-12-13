/*
 * Copyright (c) Joan-Manuel Marques 2013. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of the practical assignment of Distributed Systems course.
 *
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this code.  If not, see <http://www.gnu.org/licenses/>.
 */

package recipes_service.tsae.data_structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Joan-Manuel Marques, Daniel Lázaro Iglesias
 * December 2012
 */
public class TimestampMatrix implements Serializable {

    private static final long serialVersionUID = 3331148113387926667L;
    ConcurrentHashMap<String, TimestampVector> timestampMatrix = new ConcurrentHashMap<String, TimestampVector>();

    public TimestampMatrix(List<String> participants) {
        // create and empty TimestampMatrix
        for (String participant : participants) {
            timestampMatrix.put(participant, new TimestampVector(participants));
        }
    }

    /**
     * @param node
     * @return the timestamp vector of node in this timestamp matrix
     */
    synchronized TimestampVector getTimestampVector(String node) {
        return timestampMatrix.get(node);
    }

    /**
     * Merges two timestamp matrix taking the elementwise maximum
     *
     * @param tsMatrix
     */
    public  synchronized void updateMax(TimestampMatrix tsMatrix) {
        Set<String> hosts = tsMatrix.timestampMatrix.keySet();

        for (String host : hosts) {
            TimestampVector timestampVector = timestampMatrix.get(host);
            TimestampVector foreignTimestampVector = tsMatrix.getTimestampVector(host);

            if (timestampVector != null)
                timestampVector.updateMax(foreignTimestampVector);
        }

    }

    /**
     * substitutes current timestamp vector of node for tsVector
     *
     * @param node
     * @param tsVector
     */
    public synchronized void update(String node, TimestampVector tsVector) {
        if (timestampMatrix.get(node) == null) {
            timestampMatrix.put(node, tsVector);
        } else {
            timestampMatrix.replace(node, tsVector);
        }
    }

    /**
     * @return a timestamp vector containing, for each node,
     * the timestamp known by all participants
     */
    public synchronized TimestampVector minTimestampVector() {
        TimestampVector minVector = null;
        Set<String> hosts = timestampMatrix.keySet();

        // Loop to find the min timestamp vector
        for (String host : hosts) {
            TimestampVector tsVector = timestampMatrix.get(host);
            if (minVector == null) {
                // initialize min vector in case it is not
                minVector = tsVector.clone();
            } else {
                // else set min vector to the min of both current host vector and buffered minVector
                minVector.mergeMin(tsVector);
            }
        }
        return minVector;
    }

    /**
     * clone
     */
    public synchronized TimestampMatrix clone() {
        Set<String> hosts = timestampMatrix.keySet();
        ConcurrentHashMap<String, TimestampVector> newMatrix = new ConcurrentHashMap<String, TimestampVector>();

        for (String host : hosts) {
            TimestampVector cloneTSV = timestampMatrix.get(host).clone();
            newMatrix.put(host, cloneTSV);
        }

        List<String> hostsList = new ArrayList<String>(hosts);
        TimestampMatrix clonedTSMatrix = new TimestampMatrix(hostsList);
        clonedTSMatrix.timestampMatrix = newMatrix;

        return clonedTSMatrix;
    }

    /**
     * equals
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return this.toString().equals(obj.toString());
    }


    /**
     * toString
     */
    @Override
    public synchronized String toString() {
        String all = "";
        if (timestampMatrix == null) {
            return all;
        }
        for (Enumeration<String> en = timestampMatrix.keys(); en.hasMoreElements(); ) {
            String name = en.nextElement();
            if (timestampMatrix.get(name) != null)
                all += name + ":   " + timestampMatrix.get(name) + "\n";
        }
        return all;
    }
}
