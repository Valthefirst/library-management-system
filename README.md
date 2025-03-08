# Library Management System

## Project Description

This library management system aims to simplify core library tasks such as book lending and fee tracking. Developed with a RESTful Java Spring Boot backend, the system utilizes 
multiple relational databases (MySQL and PostgreSQL) and a document-based database (MongoDB) for optimal data storage and performance. Librarians can add books, manage lending, 
process fees, and maintain patron records. Planned future features include a search function, a reservation system and a graphical user interface.

---

## How to run the project

You first  must have Docker installed on your computer then, download the project and run ``docker compose up -–build`` in a terminal in the project folder.

---

## Design Class Diagram

Only the domains with a purple background have been implemented

![DDD-Library](https://github.com/user-attachments/assets/ea91fca6-3d20-4a78-8f81-ccec6981639a)

---

## C4 Diagrams

C4 L1 Diagram

![c4_l1_context_diagram-System_Context_Diagram_for_Library_System](https://github.com/user-attachments/assets/70811399-f99f-45c1-a43f-385cdae44aa0)

C4 L2 Diagram

![c4_l2_container_diagram-Container_Diagram_for_Library_System](https://github.com/user-attachments/assets/e91e9c79-a863-4991-b9cc-54644007917a)

---

## Endpoints

For Books
- /api/v1/catalogs/{catalogId}/books : GET ALL
- /api/v1/catalogs/{catalogId}/books/{bookId} : GET
- /api/v1/catalogs/{catalogId}/books : POST
- /api/v1/catalogs/{catalogId}/books/{bookId} : PUT
- /api/v1/catalogs/{catalogId}/books/{bookId} : PATCH
- /api/v1/catalogs/{catalogId}/books/{bookId} : DELETE 

For Catalogues
- /api/v1/catalogs : GET ALL
- /api/v1/catalogs/{catalogId} : GET
- /api/v1/catalogs : POST
- /api/v1/catalogs/{catalogId} : PUT
- /api/v1/catalogs/{catalogId} : DELETE 

For Fines
- /api/v1/fines : GET ALL
- /api/v1/fines/{fineId} : GET
- /api/v1/fines : POST
- /api/v1/fines/{fineId} : PUT
- /api/v1/fines/{fineId} : DELETE 

For Loans
- /api/v1/patrons/{patronId}/loans : GET ALL
- /api/v1/patrons/{patronId}/loans/{loanId} : GET
- /api/v1/patrons/{patronId}/loans : POST
- /api/v1/patrons/{patronId}/loans/{loanId} : PUT
- /api/v1/patrons/{patronId}/loans/{loanId} : DELETE 

For Patrons
- /api/v1/patrons : GET ALL
- /api/v1/patrons/{patronId} : GET
- /api/v1/patrons : POST
- /api/v1/patrons/{patronId} : PUT
- /api/v1/patrons/{patronId} : DELETE 

---

## Testing

For catalog-service

![catalog-service JaCoCo report](https://github.com/user-attachments/assets/9f12c5ce-053d-4531-aa12-747d11036c1d)

For fines-service

![fines-service JaCoCo report](https://github.com/user-attachments/assets/0d631622-7646-470d-b0e7-be9c9e4df709)

For loans-service

![loans-service JaCoCo report](https://github.com/user-attachments/assets/30fae40d-5f04-47b7-b7af-cbf3fda62ed4)

For patrons-service

![patrons-service JaCoCo report](https://github.com/user-attachments/assets/b5df06f1-b03d-41ac-9d48-cd414e43d694)
