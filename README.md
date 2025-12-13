## Date: 10/12/2025
### By: Sara Jalal
### [Email](mailto:saraalkhozaae@gmail.com) | [Linkedin](https://www.linkedin.com/in/sara-alkhozaae) | [GitHub](https://github.com/sarajalal2004)
***
### ***Description***
#### A command line bank program, manages user accounts and transactions.
***
### ***Technologies Used***
* intelliJ
    * Java
* gitHub / gitBash
* drawio
* Trello
***
### ***Getting Started***
##### Rigester with unused username, preform your transaction and review history details.  
***
## ***Planning, development and Problem-Solving Strategy***
### Planning Dashborads and Diagrams
* [My Trello](https://trello.com/invite/b/692724a7b71e27ff50b2c02b/ATTI870a4ae28f7beec0d2cc8eb0bae5eccb8F0A4C99/my-bank) Including all the userstories
* ERD Diagram
![ER diagram](https://i.imgur.com/f5iGn8q.png)
 the planning process consists of Requirement analysis, system design encludes utilizing ERD diagram to stucture the system models which implements as classes and attributes, using the user stories in Trello dashboard for structured work process.

### Development process
 Step-by-Step Implementation: Developed classes incrementally, starting with basic account operations, then adding overdraft logic and user authentication.
 Test-Driven Development: Wrote JUnit tests for critical functionalities, including overdraft fees, withdrawal limits, and account validations, to ensure reliability.
 Debugging and Refinement: Continuously tested and debugged methods to handle exceptions and edge cases properly, ensuring smooth user experience.

### Problem-Solving Strategy
 Solve a Simpler Problem: Break down a complex problem into smaller, manageable parts to gain insight.

***
### ***Screenshots***
#### First menu
![First menu](https://i.imgur.com/s2UGoQ3.png)
#### Customer menu
![Customer menu](https://i.imgur.com/igP8Vwc.png)
#### Banker menu
![Banker menu](https://i.imgur.com/Prkp3en.png)

***
### ***Favourite Function***
* History details with filter **filterWithWords()**
  This function allow user to choose the filtering period and use **printDetail(LocalDateTime fromDateTime, LocalDateTime toDateTime)** to display filtered structered output based on user choice

***
### ***Feature points***
- [x] The IBANs is auto generated.
- [x] The Banker could assign banker role to other users.
- [ ] user could reactivate account without the banker action.
- [ ] revoke the banker role
- [ ] Allow banker to view transctions of customers

***
### ***Credits***
* [Trello](https://trello.com)  
* [Drawio](https://www.drawio.com/) for creating diagrams 
