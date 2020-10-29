# Time Stamped Anti-Entropy Protocol


It's a `weak consistency` protocol for `data dissemination`. It prodices `reliable`, `eventual` delivery of issued operations.  
Pairs of `replicas` periodically exchange update messages; in this way updates eventually `propagate`
to all replicas. It is a fault tolerant and consistent protocol. The protocol is extremely robust in the face of site and network failure, and it scales well to large numbers of replicas.

### Overview
The replication is `dynamic` in that new servers can be added or removed to accommodate changes in demand. The system is `asynchronous`, and servers are as independent as possible.  
it never requires `synchronous cooperation of large numbers of sites`. This improves its ability to handle both communication and site failures.  
Eventually or `weakly consistent` replication protocols do *not* perform `synchronous updates`. Instead, updates are first delivered to one site, then `propagated asynchronously` to others.  
Since the protocol is `fault tolerant`, messages will be delivered to every process in the group even if processes temporarily fail or are disconnected from the network.  
It also incorporates a `related group membership mechanism` that handles adding and removing processes from the
`replica group`. The tradeoffs are that the protocol may have to delay message delivery (it is a blocking
protocol), that replicas must maintain logs on disk that are not compromised by failure and recovery, and
that timestamp information must be appended to every message.  
Each replica maintains *three* data structures: a `message log` and two `timestamp vectors`. These should all
be maintained on `stable storage`, so they are not corrupted when the site or process crashes. Stable storage
is required to strictly meet reliability guarantees. Each site must also maintain a `clock` that is loosely
synchronized with other sites.  
The `message log` contains messages that have been received by a process. The messages are `stamped`
with the *identity of the replica that initiated the message and a timestamp*. Message are entered into the log
on receipt, and *removed* when all other replicas have received them.  
Replicas maintain a `summary timestamp vector` to record *what updates they have received*. The timestamp vector holds *one timestamp for every replica in the group*. Replica A records a timestamp `t` for replica B when replica A has received all update messages sent from B up to time `t`: The vector provides a fast mechanism for transmitting summary information about the state of a replica.  
Each replica also maintains an `acknowledgment timestamp vector` to *record what messages have been acknowledged* by other replicas. A replica can determine that every other replica has observed a particular message by looking only at its local acknowledgment vector. If replica A holds a timestamp t for replica B; replica A knows that B has received every message from any sender with timestamp less than or equal to t: Process B periodically sets its entry in its acknowledgment vector to the minimum timestamp recorded in its summary vector.
From time to time, a process A will select a partner process B and start an `anti-entropy session`. A session begins with the two processes allocating a session timestamp, then exchanging their summary and acknowledgment vectors. Each process determines if it has messages the other has perhaps not yet observed, by detecting that some of its summary timestamps are greater than the corresponding ones of its partner. These messages are retrieved from the log and sent to the other process using a reliable stream protocol.  
The session ends with an `exchange of acknowledgment messages`. If any step of the exchange fails, either
process can abort the session.
At the end of a successful session, both processes have received the same set of messages. Processes A
and B set their summary and acknowledgment vectors to the elementwise maximum of their current vector
and the one received from the other process.
After anti-entropy sessions have completed, update messages can be delivered from the log to the
database, and *unneeded log entries can be purged*. *A log entry can be purged when every other process has
observed it*, which is *true when the minimum timestamp in the acknowledgment vector is greater than the
timestamp on the log entry*.

### Reliable, eventual message delivery
This means that every functioning group member will receive every message, but the message may require a `finite
unbounded time for delivery`. These guarantees reflect a tension between an application’s needs for timely information, accurate information, and reliability.  
`Eventual delivery` trades `latency` for `reliability`. The message delivery component can mask out transient network and host failures by delaying messages and resending them after the fault is repaired. It also allows messages to be batched together form transmission, which is often more efficient than transmitting each message singly. Both of these features are especially important for mobile systems that can be disconnected from the Internet for extended periods.  
In the `timestamped anti-entropy (TSAE)` each principal periodically contacts another principal, and the two exchange messages from their logs until both logs contain the same set of messages. The TSAE protocol maintains extra data structures that summarize the messages each principal has received, and uses this information to guide the exchanges.
There are *two versions* of the TSAE protocol: one that requires `loosely-synchronized clocks`, and one that does not.
One important feature of `TSAE` is that it *delivers messages in batches*. Consider the stream of messages sent from a particular principal. Those messages will be received by other principals in batches, where each batch is a run of messages, with no messages missing in the middle of the run. When a principal receives a batch, the run of messages will immediately follow any messages already received from that sender. In this way principals receive streams of messages, without ever observing a “gap” in the sequence.
The `TSAE protocol` provides additional features that are necessary for information services. The protocol can be composed with a `message ordering component` to produce *specific message ordering guarantees*. The ordering component makes use of the batching property to reduce overhead.  
Timestamps are used in every component to represent temporal relations and to name events. A timestamp consists of a sample of the clock at a host, and is represented as the tuple The clock resolution must be fine enough that every important event in a principal, such as sending a message or performing anti-entropy, can be given a unique timestamp.
Timestamps can be organized into `timestamp vectors`. A timestamp vector is a set of timestamps, each from a different principal, indexed by their host identifiers. It represents a snapshot of the state of communication in a system. In particular, it represents a cut of the communication.  

### The message log
The `message log` contains messages that have been received by a `principal`. A `timestamped message` is entered into the log *on receipt*, and removed when all other principals have also received it.  
Principals maintain a `summary timestamp vector` to record what updates they have observed. The vector contains *one timestamp for every group member*, and each member has received every message with lesser timestamps.  

A	1	3	5	12			12
B	3	 					 3
C	2	3	4				 4

// Message log 				// Summary Vector

Recall that messages are transmitted in batches, and that there are never gaps in the `message sequence`, so the timestamp of the latest message indicates that every message with an earlier timestamp has been received.  
It is used during an `anti-entropy exchange` to determine which messages have not yet been received by a principal, and two principals can compare their `summary vectors` to measure how far out of date they are with respect to each other.  
Each principal also maintains information about `message acknowledgments`. Rather than explicitly send an `acknowledgment` for every message, the information in the summary vector is used to build a summary acknowledgment. As long as principals use `loosely-synchronized clocks`, the smallest timestamp in the summary vector can be used as a single acknowledgement timestamp for all messages.

### Purging the message log
The `message log` must be periodically `purged` so that it does not grow without bound. Even if there is no log, as in applications that work directly from an application database unneeded `death certificates` must be purged. Message purging is correct if it does not interfere with message propagation, and if every message is eventually purged.  
Whether a `message log` is used or not, a `message or death certificate can safely be removed when every member principal has received it`. This condition can be detected when *the message is earlier than all events in the acknowledgment timestamp vector*.


Host B executes operation B4
Host C executes operation C3
Host A does an anti-entropy session with host C
Host B executes operation B5
Host A does an anti-entropy session with host B
Host A executes operation A4
Host C executes operation C4
Host A does an anti-entropy session with host C
Host A does an anti-entropy session with host B