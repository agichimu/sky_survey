# Simple Survey Application
## Introduction
--
This repository contains the code for a simple Survey application developed as a pre-interview task for Sky World Limited. The application is split into three components: Database, REST API, and User Interface.
--
Components
Database

Design an Entity Relationship Diagram (ERD)
Implement the database named sky_survey_db using MySQL or Postgres
REST API

Implement a REST API using Java or PHP
Define the following endpoints:
/api/questions (GET) - Fetch list of questions
/api/questions/responses (PUT) - Submit responses to the questions
/api/questions/responses (GET) - Fetch submitted responses (supports pagination and filtering)
/api/questions/responses/certificates/{id} (GET) - Download a certificate by providing its ID
Provide a Postman Collection documenting the above endpoints with sample responses
User Interface

Create a UI for the application using React.js (preferably in TypeScript) for web developers
For mobile developers, use Flutter, React Native, or Android
The UI should consist of two pages: Survey Form and Survey Responses
Survey Form
Stepped form with questions as steps
Fetch questions from the API endpoint /api/questions
Next and Previous buttons to navigate through questions
Validation for required questions
Final step with a preview of collected data and a Submit button to submit responses to the API endpoint /api/questions/responses
Survey Responses
Fetch and display submitted responses from the API endpoint /api/questions/responses
Pagination and filtering by email_address
Ability to download certificates using the API endpoint /api/questions/responses/certificates/{id}
Include navigation to switch between Survey Form and Survey Responses pages
Repository Structure
simple-survey-client: Contains the UI code (React.js or mobile framework).
simple-survey-api:
ERD_Diagram.png: ERD Diagram image.
database.sql: SQL file for creating the sky_survey_db database.
api: REST API code.
postman_collection.json: Postman Collection documenting API endpoints.
Setup and Deployment
Database
Execute the SQL queries in simple-survey-api/database.sql to create the sky_survey_db database.
REST API
Navigate to simple-survey-api/api.
Run the API using your preferred Java or PHP environment.
User Interface
Navigate to simple-survey-client.
Install dependencies: npm install or yarn install.
Run the UI: npm start or yarn start.
Deployment (Optional)
Deploy the API and UI to a hosting service of your choice.
Provide public URLs for access.
Submission
GitHub Repositories:
Link to simple-survey-client GitHub Repository
Link to simple-survey-api GitHub Repository
Deployed Web Application:
Link to the public deployed web application (if applicable)
APK (for mobile developers, if applicable):
Upload the APK to the simple-survey-client GitHub Repository.
Email Submission:
Send an email to skyworldrecruitments@gmail.com with the above links.
Ensure all attached materials are fully functional.
