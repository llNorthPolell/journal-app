# journal-app
Journal app built using React and Firebase. Note Firebase is being used to prototype the frontend application features. Prototype of the App can be accessed via https://journal-app-75df1.firebaseapp.com.
Due to the limitations of Firebase, a Spring Boot backend has been developed (see Server folder), and the front end is currently undergoing migration to use this backend. 


# Why use Journal App?
Do you live with piles of books scattered across the room? Tired of scrolling through Excel pages to write down progress for the day? Do you wish you can "ctrl-z" and "ctrl-F" in real life? Journal App (don't have a name yet) can solve all of these problems. Journal App provides users with an intuitive UI to help them with journal-keeping tasks. Journal App is suitable for things like practice journals, personal project logs, research journals, and more, as it will provide users with a variety of dashboard widgets like graphs, charts, small scrum boards, or scribbleboards.


## Implemented Features

### Client
- Google authentication
- Journal creation
- Journal entry creation and update
- dynamic widget creation via widget menu
- dynamic journal entry filtering

### Server
- Google authentication (with JWTs)
- journal management
- journal entry management
- dashboard management
- goal tracking 



## Known Issues
### Client
- ui responsiveness (working on main functionality first)


## Backlog
### Client
- interactive widgets such as todo list, scribbleboard and personal kanban board
- deletion of journal, journal entry and dashboard config
- edit dashboard
- goals screen
- journal entries and search results screens
