# Exercise 4

### Reading notes

#### Main ideas
With broad adoption of our system, we can opt for **vertical scaling**, increasing the size and capability of our system. This is expensive and rather limited by the size of the largest possible system available. Horizontal scaling is based on **sharding** or grouping data in different areas of our system, both approaches are not mutually exclusive.
Relying on database constraints to enforce data consistency couples the schema of our database to the nature of our system.

The **CAP theorem** states that there are three factors that are mutually-exclusive 
* **Consistency**: The client perceives that a set of operations has occurred all at once.
* **Availability**: Every operation must terminate in an intended response.
* **Partition tolerance**: Operations will complete, even if individual components are unavailable.
Specifically, a Web application can support, at most, only two of these properties with any database design. Obviously, **any horizontal scaling strategy is based on data partitioning**; therefore, **designers are forced to decide between consistency and availability**.

#### BASE vs ACID

#### ACID
Provide the following guarantees:
* **Atomicity**: All of the operations in the transaction will complete, or none will.
* **Consistency**: The database will be in a consistent state when the transaction begins and ends.
* **Isolation**: The transaction will behave as if it is the only operation being performed upon the database.
* **Durability**: Upon completion of the transaction, the operation will not be reversed.
ACID is based in a two-fase commit, in the first phase, all instances participating in the transaction are asked to validate that changes are possible, if this opperation fails in any of the parts, then all parts are asked to **roll-back** the data, otherwise in the second phase all parts commit the data.

#### BASE
* **Basically available**
* **Soft State**
* **Eventually Consistent**
Partial failure is tolerated so more availability is achieved. Identifying opportunities for relaxing consistency is difficult, also goes against mindset of stake-holders and developers. Estimates are great candidates, when this is not the case, buffering db updates in a messaging queue can be the way to go. Idempotent operations are really useful for this approach. Ordering in these queues can be an issue, but to be honest, web applications tend to be quite messy in the quality of the event sources, so messy outputs are expected. The eventually consistent part can be communicated via events. Event driven architecture to the rescue.