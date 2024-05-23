# ECE 651 - Spring 24 - Team Project - Evolution 1

## Team #8

## Team Members
*Please complete your team information:*
- *Nick Carroll / nc230*
- *Khaliun (Haliunaa) Munkhuu / km632*
- *Wenxuan Su / ws202*
- *Yixin Zhang / yz696*

## LEAD TA
- **Your Project LEAD TA is**: *Zhengge Tang*
- Your team will have one lead TA, to which you can reach out for clarification on the specifications.
- You can ask for help from any TA, but regarding specifications, the LEAD TA definitions will have precedence over all other TAs. 

## Logistics
Before we dive into the requirements, here are a few things you need to know about the logistics of this project:
- We expect you to perform **good project management: estimating the time of tasks, setting intermediate deadlines, tracking progress, and adjusting your schedule as needed. For this evolution of your project, we have provided a spreadsheet with one possible task breakdown. You may use our task breakdown, or create your own. You may use a spreadsheet, GitLab issues, or any other tool you want. However, you must perform this project management and discuss it with your TA by 3/19. You may of course revise this as you go.
- Please make **UML diagrams** of your planned design. Set up an appointment to discuss the initial version with your TA no later than 3/19. Revise them as needed and submit final versions with your final code. This is part of your design grade.
- Keep in mind that **change is the only constant** in software engineering and prepare for it. 
- We expect you to **use issues, feature branches, pull requests, and perform code reviews**. The process you use for software development is also part of your grade.
- As always, we expect you to produce **clean code**:  to include comments, have good variable names, clean formatting, and well-abstracted methods. Your group should define its own coding standard, and you should all ensure that you follow it.
- Your TA is your “customer” but also your mentor. You should meet with them frequently. You should meet at least twice per week while working on an evolution. You should have at least one “sprint review” style meeting per evolution, but may have more. There should be no surprises in grading—you should know what your TA thinks of your project throughout.
- As we discussed in class, if you are having team problems, you should attempt to resolve them yourselves, and if that fails, involve your TA and/or professor. Whether your team is working well or poorly together, we ask that each of you do an individual contribution assessment at the end of each evolution. We will post a link to this form later.

### List of things you will be graded on:
- Functionality
- Design
- Documentation (including initial UML review + final UML diagrams). 
- Testing (at least branch coverage)
- Other Code Quality Factors (e.g., naming, formatting, smells).
- Process + project management (Issue tracking, CI/CD, Code Reviews, etc).

### Policies and Integrity
- You need to create your own design and code. 
- Gathering code from the internet and reusing it is not an acceptable practice for this course. 
- The use of AI-driven tools (like ChatGPT, Copilot, etc.) is not allowed.
- The use of pre-existing solutions (design patterns excluded) is also not acceptable in this course.
- Reviewing other systems as examples is acceptable *but* you must document the sources and include those in your repository.
- Violation of the previous policies is considered academic misconduct and it will be reported to the proper university officials.

## User Stories
During lecture time, most professors take attendance to record which students attend sessions. 
Some professors take attendance using a signing sheet which is circulated among the students to sign their names. 
Others use a traditional roll call, where the professor calls out the name of each student one at a time and updates an attendance spreadsheet. 
Both are tedious processes that may take five to ten minutes in large enrollment courses or even more outside the classroom.

Your team was assigned to design a system to ease this process. 
This program should run on a text-based terminal and should be operating system independent (i.e., may run on Windows, Mac, Linux, etc.)

Keep in mind that the information about enrollment and attendance is sensitive, so security mechanisms should be considered. 

Professors don't want to spend time outside the classroom inputting values from a paper attendance sheet more than students want to sign it. 

The professor starts the classroom by taking attendance. 
For this, they would like the system to present the name of the student on the screen, and then mark the student present or absent. 
This should be dynamic, so doing this should be a matter of a keypress. 

In many cases, students may arrive a few minutes late. 
So, at the end of the session, those students may approach the professor and the professor may change the attendance to either present or most probably tardy.
The system may allow options of browsing or searching to locate students that will be marked tardy.

The system should record the attendance in plain text files.
Also, an export option should be provided for other formats like `json`, `xml` or `custom`.

As classes may be large, before the first day, the professor can load the roster from a `csv` file. The format of that file may vary, including headers or not, column order, or data. 

As the semester progresses, other students may enroll late to the class as well as others may drop from the class. For students who dropped out, the professor is interested in keeping attendance records, but the student's name should not appear in the attendance-taking process anymore. 

At the end of the class week, the system should send a report to the student with their current attendance records. 

In addition, every time that the student's attendance is changed (e.g., from absent to tardy) the system should send a notification to the student. Initially, those notifications will be over email, though SMS or other methods could be added later on.

In many cases, students desire that a different name be displayed during the attendance-taking process. 
Thus, they can reach out to the professor asking them to change their name. 
Given that, some university systems don't allow name changes, both the legal name and the display name should be kept in sync. 


## Submission
- Project Submission is through GitLab
    - Make sure to commit, push, and generate a release on GitLab before the deadline.
- Late submission policy
    - Late submissions are not allowed without a completed request before the deadline. 
    - Grade penalty policy applies as described on day one slides.
- Documentation 
    - Diagrams and their sources must be included in a folder called documentation in the GitLab repository. 
        - Although you can use any tool, the tool needs to be freely available.
        - PDF printouts should also be included. 
        - We recommend [StarUML](https://staruml.io) to generate the diagrams. *You can use it as an evaluation, and including a white rectangle in the back helps you to generate good printouts.*
    - Software Requirement Specification (SRS) Document
        - Document clearly the requirements of the software. 
    - Design Decisions
        - Include a document discussing the design decisions you made. E.g., which design patterns did you consider and why did you select one over the others. 
