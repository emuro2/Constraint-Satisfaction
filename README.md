Constraint-Satisfaction
=======================

Constraint Satisfaction Problems 

Assignment link: http://www.cs.illinois.edu/~slazebni/fall13/assignment2.html

Description:

Scheduling is a common CSP. You are to find a feasible meeting schedule that satisfies the constraints of each of the employees in a company. The description of our scheduling problem is as follows:

There is a set of M employees.
There is a set of N meetings, each of which has a duration of one time unit.
The schedule has a maximum number T of time slots.
Each employee has a set of meetings that he or she must attend.
The scheduled time slots for meetings must enable the participating employees to travel between the respective meeting locations.

First, give a formal specification of this problem as a CSP (i.e., what are the variables, what are the domains of the variables, and what are the constraints). Then, implement backtracking search to find an assignment that satisfies each of these three problems (see below for the explanation of the problem files and format of the solution). In your report, give your solution (or say that none exists), as well as the number of attempted variable assignments for each instance. Experiment with different variable and value selection heuristics, as well as (optionally) with more advanced inference methods, to try to reduce the amount of backtracking as much as possible. Discuss your choices in the report.
Format of the problem files

Each problem file describes an instance of the scheduling problem, specifying the total number of meetings to schedule, the number of employees, which meetings each employee must attend, the times required to travel between meetings, and the total number of available time slots in the schedule. Notice that two meetings may be assigned to the same time slot as long there is no employee that must be in both of them. Consider the following sample problem:
Number of meetings: 5
Number of employees: 6 
Number of time slots: 5

Meetings each employee must attend:
1: 2 5
2: 2 3  
3: 1 5
4: 1 4 5
5: 2 4
6: 1 3

// This specifies the time required to travel from one meeting location to the other.
// For example, getting from meeting 2 to meeting 3 requires one time slot, 
// and getting from meeting 1 to meeting 5 requires two time slots.
Travel time between meetings: 
     1 2 3 4 5
 1:  0 1 2 1 2
 2:  1 0 1 1 1 
 3:  2 1 0 1 1 
 4:  1 1 1 0 1 
 5:  2 1 1 1 0 
Here is the solution for the sample problem (you should report yours in a similar format):
Meeting 1 is scheduled at time 1
Meeting 2 is scheduled at time 1
Meeting 3 is scheduled at time 5
Meeting 4 is scheduled at time 3
Meeting 5 is scheduled at time 5
