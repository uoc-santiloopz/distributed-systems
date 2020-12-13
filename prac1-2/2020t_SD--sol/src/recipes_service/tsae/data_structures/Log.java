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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import recipes_service.data.Operation;
//LSim logging system imports sgeag@2017
//import lsim.coordinator.LSimCoordinator;
import edu.uoc.dpcs.lsim.logger.LoggerManager.Level;
import lsim.library.api.LSimLogger;

/**
 * @author Joan-Manuel Marques, Daniel Lázaro Iglesias
 * December 2012
 *
 */
public class Log implements Serializable{
	// Only for the zip file with the correct solution of phase1.Needed for the logging system for the phase1. sgeag_2018p 
	//	private transient LSimCoordinator lsim = LSimFactory.getCoordinatorInstance();
	// Needed for the logging system sgeag@2017
	//	private transient LSimWorker lsim = LSimFactory.getWorkerInstance();

	private static final long serialVersionUID = -4864990265268259700L;
	/**
	 * This class implements a log, that stores the operations
	 * received  by a client.
	 * They are stored in a ConcurrentHashMap (a hash table),
	 * that stores a list of operations for each member of 
	 * the group.
	 */
	private ConcurrentHashMap<String, List<Operation>> log= new ConcurrentHashMap<String, List<Operation>>();  

	public Log(List<String> participants){
		// create an empty log
		for (Iterator<String> it = participants.iterator(); it.hasNext(); ){
			log.put(it.next(), new Vector<Operation>());
		}
	}

	/**
	 * inserts an operation into the log. Operations are 
	 * inserted in order. If the last operation for 
	 * the user is not the previous operation than the one 
	 * being inserted, the insertion will fail.
	 * 
	 * @param op
	 * @return true if op is inserted, false otherwise.
	 */
	public boolean add(Operation op){
		String participantId = op.getTimestamp().getHostid();
		List<Operation> currentLog = log.get(participantId);

		// add if empty
		if (currentLog.isEmpty()) {
			currentLog.add(op);
			return true;
		}

		Operation lastOperation = currentLog.get(currentLog.size() - 1);
		// add if more recent
		if (op.getTimestamp().compare(lastOperation.getTimestamp()) > 0) {
			currentLog.add(op);
			return true;
		}

		return false;
	}
	
	/**
	 * Checks the received summary (sum) and determines the operations
	 * contained in the log that have not been seen by
	 * the proprietary of the summary.
	 * Returns them in an ordered list.
	 * @param summary
	 * @return list of operations
	 */
	public List<Operation> listNewer(TimestampVector summary) {
		List<Operation> newerOperations = new Vector<Operation>();

		// Iterate over every node in the log and buffer all missing operations
		for (Enumeration<String> keys = this.log.keys(); keys.hasMoreElements();) {
			// Get the current key
			String currentNode = keys.nextElement();
			// Retrieve the log operations for the current node
			List<Operation> currentNodeOperations = this.log.get(currentNode);
			Timestamp lastNodeTimestamp = summary.getLast(currentNode);
			// Iterate over operations and add the ones missing in log
			for (Operation currentOperation : currentNodeOperations) {
				// Retrieve timestamp for operation
				Timestamp currentOperationTimestamp = currentOperation.getTimestamp();
				// Check if operation is already registered
				if (currentOperationTimestamp.compare(lastNodeTimestamp) > 0) {
					newerOperations.add(currentOperation);
				}
			}
		}
		return newerOperations;
	}

	/**
	 * Removes from the log the operations that have
	 * been acknowledged by all the members
	 * of the group, according to the provided
	 * ackSummary. 
	 * @param ack: ackSummary.
	 */
	public synchronized void purgeLog(TimestampMatrix ack){

		TimestampVector minAck = ack.minTimestampVector();

		// retrieve host's keys
		Set<String> hosts = log.keySet();

		for (String host : hosts) {
			// Extract list of ops from a given host
			List<Operation> logOp = log.get(host);
			// Obtain last host timestamp
			Timestamp lastTimestamp = minAck.getLast(host);

			// Basically here we remove all operations which are older
			// Than the last timestamp
			if (lastTimestamp != null) {
				for (int i = 0; i < logOp.size(); i++) {
					Operation op = logOp.get(i);
					if (!(op.getTimestamp().compare(lastTimestamp) > 0)) {
						logOp.remove(i);
					}
				}
			}
		}

	}

	/**
	 * equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Log) {
			Log log = (Log) obj;
			return this.toString().equals(log.toString());
		}
		return false;
	}

	/**
	 * toString
	 */
	@Override
	public synchronized String toString() {
		String name="";
		for(Enumeration<List<Operation>> en=log.elements();
			en.hasMoreElements(); ){
			List<Operation> sublog=en.nextElement();
			for (Operation operation : sublog) {
				name += operation.toString() + "\n";
			}
		}
		
		return name;
	}
}
