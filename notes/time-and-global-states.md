# Time and Global States


Time is really important but problematic in distributed systems. Each computer may have its own physical clock, but the clocks typically deviate, and we cannot synchronize them perfectly.  
In order to know at what time of day a particular event occurred at a particular computer it is necessary to **synchronize** its clock with an authoritative, external source of time.  

The timing of physical events was thus proved to be relative to the observer, and Newton’s notion of absolute physical time was shown to be without foundation. There is no special physical clock in the universe to which we can appeal when we want to measure intervals of time.

### History of processes
`history(pi) = hi = <ei 0, ei 1, ei 2, ... }>`

### Clocks
They work by reading oscillations on a certain christal. A clock read by the processor can be expressed as:
`Ci t = aHi t + B`. There is always a certain amount of error between `Ci t` and `t`.

### Physical clocks
The most accurate physical clocks use atomic oscillators, whose drift rate is about one part in 10^13 . The output of these atomic clocks is used as the standard for elapsed real time, known as `International Atomic Time`. Since 1967, the standard second has been defined as `9,192,631,770` periods of transition between the
two hyperfine levels of the ground state of Caesium-133 (Cs^133).  
`Coordinated Universal Time` – abbreviated as `UTC` (from the French equivalent) – is an international standard for timekeeping. It is based on atomic time, but a so-called `leap second` is inserted – or, more rarely, deleted – occasionally to keep it in step with astronomical time. `UTC` signals are synchronized and broadcast regularly from land-based radio stations and satellites covering many parts of the world. Satellite sources include the `Global Positioning System (GPS)`.

### Logical Time and Logical clocks
* If two events occurred at the same process, then they occurred in the order in which the process observes them.
* Whenever a message is sent between processes, the event of sending the message occurred before the event of receiving the message.  
In **Lampart/Logical clocks**, we establish a way of measuring causality based on timestamps, we increment events by one, and we do so when there is an event issued in process pi or when there is a message sent, and then received and including the calculation of the maximum possible delay. 