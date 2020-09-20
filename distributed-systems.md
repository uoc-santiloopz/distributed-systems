# Distributed Systems


### Representing DS
* `Physical Models`: In terms of devices
* `Architectural Models`: In terms of computational and communicational tasks performed by the system
0* `Fundamental models`: an abstract perspective in order to examine individual aspects of a `distributed system`. Three important aspects of distributed systems: `interaction models`, which consider the `structure` and `sequencing` of the `communication between the elements` of the system; `failure models`, which consider the `ways in which a system may fail` to operate correctly and; `security models`, which consider `how the system is protected` against attempts to interfere with its correct operation or to steal its data.

### Physical Models
* `baseline models`: minimal physical model of a distributed system as an extensible set of computer nodes interconnected by a computer network for the required passing of messages.
* `Early distributed systems`: Such systems emerged in the late 1970s and early 1980s in response to the emergence of LAN technology, usually Ethernet. These systems typically consisted of between 10 and 100 nodes interconnected by a LAN.
* `Internet-scale distributed systems`: Building on this foundation, larger-scale distributed systems started to emerge in the 1990s in response to the dramatic growth of the Internet during this time (for example, the Google search engine was first launched in 1996).  
The level of heterogeneity in such systems is significant in terms of networks, computer architecture, operating systems, languages employed and the development teams involved. This has led to an increasing emphasis on open standards and associated middleware technologies such as `CORBA` and more recently, `web services`. Additional services were employed to provide `end-to-end quality of service` properties in such global system.
* `Contemporary distributed systems`: physical architecture with a significant increase in the level of heterogeneity embracing, for example, the tiniest embedded devices utilized in ubiquitous computing through to complex computational elements found in `Grid computing`. These systems deploy an increasingly varied set of networking technologies and offer a wide variety of applications and services. Such systems potentially involve up to hundreds of thousands of nodes.

### Architectural Models
