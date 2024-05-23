# ECE 651 - Spring 24 - Team Project - Evolution 3 - Final
## Deadline
- **Evolution 3 must be completed by 11:59 pm on Apr 29th.** *(always check canvas deadline)*

## Request
Your application keeps rocking the faculty but many of them, including administrators, complained about the console-like application. 

For this evolution, you are asked to 
1. Implement a standalone, JavaFX application for administration. 
    - this should have all the functionality that the stand-alone applications had before (student management --add, remove, enroll--, user management, etc. )
2. Implement a GUI for taking attendance. This GUI can be a stand-alone application for a computer, web-based or mobile application. 
    - this should have the faculty and student functionality. 

## Constraints
- Your architecture must still be **client/server** (can be a monolith, event-driven, microservice, etc.).
- Must be implemented using Java. *If you have concerns regarding what you plan to use, please ask!*
- **No AI-generated code.**

## Deliverables
- **Project Management**:
    - All relevant sprint documentation.
    - Task breakdown, estimation, assignment and actual effort (hours). 
        - *Ideally, a spreadsheet* otherwise a form of a report.
    - *Issues*. All tasks, bugs, etc. should be tracked on GitLab.
    - *Code Review*:
        - Merge Requests. Including code reviews. 
- **Updated Requirements**
    - Clear specification of requirements discussed with your Lead TA and approved. 
    - Use Case mapping. 
- **System Design**:
    - Updated class diagram. 
    - Changes impact report. A summary of how the new changes impacted your previous solution (evol 2). 
- **System Implementation**
    - Java implementation. 
- **Installation and Configuration Instructions**
    - A document for sysadmins with instructions on how to install and configure the system.

## Submission
- All deliverables must be submitted to GitLab. 
- A Release and Tag must be created before the deadline. 

## Extra Credit 
As for extra credit (E.C.), we offer the opportunity to improve this process. 
We described the attendance-taking process as a manual process where the instructor will manually call each student by name. 

**Can you make it better?** That's the E.C. 
I.e., can you make the process take about 3 minutes in a class of 100 students? or 300? 
Can you make it so the instructor doesn't need to do anything?

### Extra Credit Requirements
- Your application should prevent cheating. I.e., if a student is marked as present, the student must be in the class. 
    - For instance, displaying on the projector a code (e.g., AB123) for the student to enter on the application is not a good solution, as someone may share it over SMS with a friend who is not in class.
- Your team should be available to meet with instructors during *finals week* for you to present and defend your E.C..

### Extra Credit Value
- If your solution is complete, correct, and follow the guidelines discussed in this course (design principles, design patterns, testing, UI/UX), each team member will get a full letter grade jump (10 marks) in the final grade. That is, a C- will become B-, a B- will become A- and so on. 
- This is a high-stakes extra credit, _high risk, high reward_.

### Extra Credit Submission and Deliverables
- Same deliverables as before, including now what you did for the E.C.
- All deliverables must be submitted to GitLab. 
- A Release and Tag (`extra-credit`) must be created before the deadline. 