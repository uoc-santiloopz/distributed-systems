# Transactions and Concurrency Control


The goal of transactions is to ensure that **all of the objects managed by a server remain in a consistent state** when they are accessed by multiple transactions and in the presence of server crashes.

### Transactions
In some situations, clients require a sequence of separate requests to a server to be atomic in the sense that:
* They are free from interference by operations being performed on behalf of other concurrent clients.
* Either all of the operations must be completed successfully or they must have no effect at all in the presence of server crashes.

#### Atomicity
In all of these contexts, a **transaction** applies to recoverable objects and is intended to be `atomic`. It is often called an `atomic transaction`. There are **two aspects** to atomicity:
* **All or nothing**: A transaction either completes successfully, in which case the effects of all of its operations are recorded in the objects
	1. `Failure atomicity`: The effects are atomic even when the server crashes.
	1. `Durability`: After a transaction has completed successfully, all its effects are saved in permanent storage. We use the term ‘permanent storage’ to refer to files held on disk or another permanent medium. Data saved in a file will survive if the server process crashes.

#### Isolation
Each transaction must be performed without interference from other transactions; in other words, the intermediate effects of a transaction must not be visible to other transactions. The box below introduces a mnemonic, ACID, for remembering the properties of atomic transactions.

### ACID
* **Atomicity**: a transaction must be all or nothing
* **Consistency**: a transaction takes the system from one consistent state to another
* **Isolation**
* **Durability**

Normally, a transaction completes when the client makes a `closeTransaction` request. If the transaction has progressed normally, the reply states that the transaction is `committed` – this constitutes a promise to the client that all of the changes requested in the transaction are permanently recorded and that any future transactions that access the same data will see the results of all of the changes made during the transaction.  
Alternatively, the transaction may have to `abort` for one of several reasons related to the nature of the transaction itself, to conflicts with another transaction or to the crashing of a process or computer. When a transaction is aborted the parties involved (the recoverable objects and the coordinator) must ensure that none of its effects are visible to future transactions, either in the objects or in their copies in permanent storage.

### Serial equivalence
If each of several transactions is known to have the correct effect when it is done on its own, then we can infer that if these transactions are done one at a time in some order the combined effect will also be correct. An interleaving of the operations of transactions in which the combined effect is the same as if the transactions
had been performed one at a time in some order is a serially equivalent interleaving. When we say that two different transactions have the same effect as one another, we mean that the read operations return the same values and that the instance variables of the objects have the same values at the end. The use of serial equivalence as a criterion for correct concurrent execution prevents the occurrence of lost updates and inconsistent retrievals.

### Conflicting operations
When we say that a pair of operations conflicts we mean that their combined effect depends on the order in which they are executed. To simplify matters we consider a pair of operations, read and write. read accesses the value of an object and write changes its value. The effect of an operation refers to the value of an object set by a write operation and the result returned by a read operation.

### Exclusive Locks
A simple example of a serializing mechanism is the use of `exclusive locks`. In this locking scheme, the server attempts to lock any object that is about to be used by any operation of a client’s transaction. If a client requests access to an object that is already locked due to another client’s transaction, the request is suspended and the client must wait until the object is unlocked.

### Deadlocks
The use of locks can lead to deadlock. Consider the use of locks. In a deadlock situation – two transactions are waiting, and each is dependent on the other to release a lock so it can resume. Deadlock is a state in which each member of a group of transactions is waiting for some other member to release a lock.  
Deadlocks can be imprefectly prevented through heuristic analysis or detected throgh cycle detection in lock requests.  
`Lock timeouts` are a method for resolution of deadlocks that is commonly used. Each lock is given a limited period in which it is invulnerable. After this time, a lock becomes vulnerable. Provided that no other transaction is competing for the object that is locked, an object with a vulnerable lock remains locked. However, if any other transaction is waiting to access the object protected by a vulnerable lock, the lock is
broken (that is, the object is unlocked) and the waiting transaction resumes. The transaction whose lock has been broken is normally aborted.

### Atomic commit protocols
The `simple one-phase atomic commit protocol` is inadequate, because it does not allow a server to make a unilateral decision to abort a transaction when the client requests a commit.  
The `two-phase commit protocol` is designed to allow any participant to abort its part of a transaction. Due to the requirement for atomicity, if one part of a transaction is aborted, then the whole transaction must be aborted. In the first phase of the protocol, each participant votes for the transaction to be committed or aborted. Once a participant has voted to commit a transaction, it is not allowed to abort it. Therefore, before a
participant votes to commit a transaction, it must ensure that it will eventually be able to carry out its part of the commit protocol, even if it fails and is replaced in the interim. A participant in a transaction is said to be in a prepared state for a transaction if it will eventually be able to commit it. To make sure of this, each participant saves in permanent storage all of the objects that it has altered in the transaction, together with its status – prepared.  
In the second phase of the protocol, every participant in the transaction carries out the `joint decision`. If any one participant votes to `abort`, then the decision must be to abort the transaction. If all the participants vote to commit, then the decision is to commit the transaction.

### The two phase commit protocol