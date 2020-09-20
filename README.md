# Distributed Systems

NEXT: Coulouris, 2.3

Some concerns of distributed systems are:
* **Security**: a DS is so secre as the most unsecure part of it.
* **Scalability**
* **Failure handling**
	Strategies:
	* Detecting failure
	* Masking failure
	* Failure tolerance
	* Failure recovery
	* Redundancy
* **Independent failure of a component**
* **Concurrency of components**
* **No global clock**: there are limits to the accuracy with which the computers in a network can synchronize their clocks – there is no single global notion of the correct time. All information flows in between components through `messages`.
* **Heterogeneity**
* **Openness**: how reusable are the features of a certain distributed system.
* **Transparency**: the internal details must remain transparent to the user.
	* `Access transparency` enables local and remote resources to be accessed using identical operations.
	* `Location transparency` enables resources to be accessed without knowledge of their physical or network location (for example, which building or IP address).
	* `Concurrency transparency` enables several processes to operate concurrently using shared resources without interference between them
	* `Replication transparency` enables multiple instances of resources to be used to increase reliability and performance without knowledge of the replicas by users or application programmers.
	* `Failure transparency` enables the concealment of faults, allowing users and application programs to complete their tasks despite the failure of hardware or software components.
	* `Mobility transparency` allows the movement of resources and clients within a system without affecting the operation of users or programs.
	* `Performance transparency` allows the system to be reconfigured to improve performance as loads vary.
	* `Scaling transparency` allows the system and applications to expand in scale without change to the system structure or the application algorithms. 
