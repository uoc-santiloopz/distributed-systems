# High Avalability


**Increased availability**: Users require services to be highly available. That is, the proportion of time for which a service is accessible with reasonable response times should be close to 100%.

### Replica Managers
In the general model of replica management, a collection of replica managers provides a service to clients. The clients see a service that gives them access to objects (for example, diaries or bank accounts), which in fact are replicated at the managers.

### Fault Tolerance
Capacity of the system to continue offering a service when one or more part of it fail.

#### Passive (primary-backup) replication
there is at any one time a single `primary replica manager` and one or more secondary replica managers – ‘backups’ or ‘slaves’. In the pure form of the model, front ends communicate only with the primary replica manager to obtain the service. The primary replica manager executes the operations and sends copies of the updated data to the backups. **If the primary fails, one of the backups is promoted to act as the primary**.

#### Active replication
In the active model of replication for fault tolerance, the replica managers are state machines that play equivalent roles and are organized as a group. Front ends multicast their requests to the group of replica managers and all the replica managers process the request independently but identically and reply. If any replica
manager crashes, this need have no impact upon the performance of the service, since the remaining replica managers continue to respond in the normal way.

### Gossip Architecture
Gossip architecture as a framework for implementing highly available services by replicating data close to the points where groups of clients need it. The name reflects the fact that the replica managers exchange `gossip` messages periodically in order to convey the updates they have each received from clients.
