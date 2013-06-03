meeting-scheduler-sample
========================

For TDD presentation - Business Requirements

Implement a basic (stupid) booking system for a conference room. By some strange reason, it has to deal with 
off-line booking. 

People post their booking requests to some front-end, and once a week you get a text file with working hours 
of the company, and all the meeting requests (for what day, for how long, by whom, submitted at what point it time) 
in random order. 

Your system should produce a calendar for the room, according to some business rules 
(first come, first served, only in office business hours, that sort of things).


Implemented as a basic maven project. Cf. unit test & code coverage.
