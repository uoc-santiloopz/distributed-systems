# Exercise 5

Based on the struggle to transition from local to remote computing in the new Cloud based era.  

Challenges: `granular middle-ware and locality`, `memory disaggregation`, `virtualization`, `elasticprogramming models`, and `optimized deployment`.  

**Transparency** is an archetypal challenge in distributed systemsthat has not yet been adequately solved. Transparency implies theconcealment from the user and the application programmer of thecomplexities of distributed systems.  

The **cloud button** will not be possible becauseof latency, memory access, concurrency and partial failure.  

The **Serverless End Game** (enabling transparency) will arrivewhen all computing resources (compute, storage, memory) can beoffered in a disaggregated way with unlimited flexible scaling.  

The **DDC** path is probably the more direct but also the more shocking for the distributed systems community.  In line with recent industrial trends on **Disaggregated Data centers (DDC)**,  itimplies a distributed OS transparently leveraging disaggregatedhardware resources like processing, memory or storage.  

The **DDC paradigm** is presenting **server-centric cluster technologies** as obsolete. But current mature Cloud technologies are builton top of server-centric models with commodity hardware and Ethernet networks. Hardware resource disaggregation is interesting, but it still relies on server-centric clusters for scaling.  

Deep learning motivating this transition.

For now, locality still plays a key role in stateful distributed applications. For example:
* where huge data movements stillare a penalty and memory-locality can be still useful to avoid dataserialization costs.
* where specialized hardware like GPUs mustbe used
* some iterative machine-learning algorithms
* simulators, interactive agents or actors

### Challenges:
* `granular middle-ware and locality`: still an issue. We foresee that `next-generation container technologies` may enable `inter-container communication` and provide affinity services for grouping related entities.
* `memory disaggregation`: Compute and memory locality (similarto mammalian brain where memory and processing aredeeply interconnected) may considerably enhance computational efficiency.
* `virtualization`: Software-based virtualization is a more lightweight alternative, as opposed to Docker and Linux containers.
* `elasticprogramming models`: To fulfill the vision of disaggregation and transparency, it will also be critical to provide tools for developers, enabling them to code both locally and remotely in the same manner with full transparency. Developers will need to beable to use tools to debug, monitor, profile, and if necessary, access control planes to optimize their applications for costand performance.
* `optimized deployment`. Future Cloud orchestration services will explicitly leverage data dependencies and execution requirements for improving workloads and resource management thanks to machine learning techniques.

### Conclussions
We argue that full transparency will be possible soon in the Cloud thanks to low latency resource disaggregation. We foresee that next generation serverless technologies may overcome the limitations exposed by Waldo et al.