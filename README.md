
## Prerequisites

- Docker
- Docker Compose

## Getting Started

## To run the application, use the following command:
docker-compose up --build
This will build the Docker images and start the containers as specified in your docker-compose.yml file.

## API Endpoints
The application provides the following RESTful endpoints:

Create Space
Creates a new space with the default name "stc-assessments".
POST http://localhost:8080/spaces/create
No parameters required.

Create Folder
Creates a new folder under the "stc-assessments" space.


POST http://localhost:8080/spaces/createFolder

folderName: Name of the new folder.
userEmail: Email of the user performing the operation.
Example request:
http://localhost:8080/spaces/createFolder?folderName=backend&userEmail=userEdit@example.com

Create File
Creates a new file under a specified folder.
POST http://localhost:8080/files/create

Parameters:
folderId: ID of the folder where the file will be created.
fileName: Name of the new file.
fileContent: Content of the file in binary format.
userEmail: Email of the user performing the operation.

GraphQL API
Allows interaction with the application's data using GraphQL queries and mutations.
POST http://localhost:8080/graphql
You can use GraphQL queries to fetch or mutate data as per the application's GraphQL schema.

Download File
Downloads a file if the user has the necessary permissions.
GET http://localhost:8080/files/download/{fileId}/{userEmail}

Path variables:
fileId: ID of the file to download.
userEmail: Email of the user attempting to download the file.
Example request:
http://localhost:8080/files/download/1/userEdit@example.com

Users
The following users are available with their respective permissions:

userView@example.com: User with VIEW permission.
userEdit@example.com: User with EDIT permission.
