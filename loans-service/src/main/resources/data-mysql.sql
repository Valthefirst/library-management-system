insert into patrons(patron_id, first_name, last_name, email_address, contact_method_preference, street_address, city, province, country, postal_code)
values('e5913a79-9b1e-4516-9ffd-06578e7af261', 'Vilma', 'Chawner', 'vchawner0@phoca.cz', 'MAIL', '8452 Anhalt Park', 'Chambly', 'Québec', 'Canada', 'J3L 5Y6');
insert into patrons (patron_id, first_name, last_name, email_address, contact_method_preference, street_address, city, province, country, postal_code)
values ('c3540a89-cb47-4c96-888e-ff96708db4d8', 'Alick', 'Ucceli', 'aucceli0@dot.gov', 'EMAIL', '73 Shoshone Road', 'Barraute', 'Québec', 'Canada', 'P0M 2T6');
insert into patrons (patron_id, first_name, last_name, email_address, contact_method_preference, street_address, city, province, country, postal_code)
values ('dd1ab8b0-ab17-4e03-b70a-84caa3871606', 'Ricky', 'Presslie', 'rpresslie1@domainmarket.com', 'HOME_PHONE', '24 Dorton Circle', 'Notre-Dame-des-Prairies', 'Québec', 'Canada', 'K6V 3J2');
insert into patrons (patron_id, first_name, last_name, email_address, contact_method_preference, street_address, city, province, country, postal_code)
values ('ba6c3e76-366e-44bb-8279-b41dc32dc456', 'Allx', 'Cholmondeley', 'acholmondeley2@weibo.com', 'CELL_PHONE', '61 Farragut Street', 'Senneterre', 'Québec', 'Canada', 'L0P 1J8');
insert into patrons (patron_id, first_name, last_name, email_address, contact_method_preference, street_address, city, province, country, postal_code)
values ('45772446-62f4-4ccb-b3f4-9393c186fa43', 'Gaspar', 'Russi', 'grussi3@un.org', 'MAIL', '3612 Del Mar Terrace', 'Sainte-Marthe-sur-le-Lac', 'Québec', 'Canada', 'H9K 0W3');
insert into patrons (patron_id, first_name, last_name, email_address, contact_method_preference, street_address, city, province, country, postal_code)
values ('30a4e2e3-fe9d-4903-bd23-b0a72b6c4ced', 'Hillard', 'Heamus', 'hheamus4@va.gov', 'EMAIL', '809 Blue Bill Park Park', 'Cowansville', 'Québec', 'Canada', 'J2K P4X');
insert into patrons (patron_id, first_name, last_name, email_address, contact_method_preference, street_address, city, province, country, postal_code)
values ('2bec7ec2-fe40-474f-a8cd-612f7790d24f', 'Darrin', 'Cadd', 'dcadd5@ning.com', 'HOME_PHONE', '9 Cambridge Road', 'Havre-Saint-Pierre', 'Québec', 'Canada', 'H4R 1R9');
insert into patrons (patron_id, first_name, last_name, email_address, contact_method_preference, street_address, city, province, country, postal_code)
values ('6b2214d8-8d17-42f3-82bd-1b9264f19c64', 'Susana', 'Maxfield', 'smaxfield6@themeforest.net', 'CELL_PHONE', '4509 Mifflin Road', 'Acton Vale', 'Québec', 'Canada', 'G0E N6E');
insert into patrons (patron_id, first_name, last_name, email_address, contact_method_preference, street_address, city, province, country, postal_code)
values ('a11014c8-85bc-4f8a-b627-7dfd645764ce', 'Elias', 'Plaxton', 'eplaxton7@house.gov', 'MAIL', '382 Dorton Terrace', 'London', 'Ontario', 'Canada', 'N6C 2E4');
insert into patrons (patron_id, first_name, last_name, email_address, contact_method_preference, street_address, city, province, country, postal_code)
values ('13526a90-506f-4981-a9da-06ba21fc610b', 'Ralina', 'Sussex', 'rsussex8@nymag.com', 'EMAIL', '624 Gale Alley', 'Waterloo', 'Ontario', 'Canada', 'N2L 7K8');
insert into patrons (patron_id, first_name, last_name, email_address, contact_method_preference, street_address, city, province, country, postal_code)
values ('cc9c2c7f-afc9-46fb-8119-17158e54d02f', 'Pen', 'Carruthers', 'pcarruthers9@gov.uk', 'HOME_PHONE', '3 4th Terrace', 'Hearst', 'Ontario', 'Canada', 'S4A 8Y2');

insert into patron_phonenumbers(patron_id, type, number) values(1, 'HOME', '515-555-5555');
insert into patron_phonenumbers(patron_id, type, number) values(1, 'MOBILE', '514-555-4444');
INSERT INTO patron_phonenumbers (patron_id, type, number) VALUES
                                                              (4, 'HOME', '514-555-1234'),
                                                              (4, 'MOBILE', '514-555-5678'),
                                                              (6, 'HOME', '514-555-9876'),
                                                              (2, 'MOBILE', '514-555-4321'),
                                                              (8, 'HOME', '514-555-2468'),
                                                              (5, 'MOBILE', '514-555-1357'),
                                                              (7, 'HOME', '514-555-3691'),
                                                              (7, 'MOBILE', '514-555-7801');


insert into books(isbn, catalog_id, title, collection, edition, publisher, synopsis, language, status, first_name, last_name)
values(9781566199094, 'd846a5a7-2e1c-4c79-809c-4f3f471e826d', 'The Great Gatsby', 'F. Scott Fitzgerald', '', 'Scribner',
       'The Great Gatsby is a novel written by American author F. Scott Fitzgerald that follows a cast of characters living in the fictional towns of West Egg and East Egg on prosperous Long Island in the summer of 1922. The story primarily concerns the young and mysterious millionaire Jay Gatsby and his quixotic passion and obsession with the beautiful former debutante Daisy Buchanan.',
       'English', 'DAMAGED', 'F. Scott', 'Fitzgerald');
INSERT INTO books (isbn, catalog_id, title, collection, edition, publisher, synopsis, language, status, first_name, last_name)
VALUES (9780132350884, 'd846a5a7-2e1c-4c79-809c-4f3f471e826d', 'Clean Code: A Handbook of Agile Software Craftsmanship', 'Software Development', '1st', 'Prentice Hall',
        'Clean Code is divided into three parts. The first describes the principles, patterns, and practices of writing clean code. The second part consists of several case studies of increasing complexity. Each case study is an exercise in cleaning up code—of transforming a code base that has some problems into one that is sound and efficient. The third part is the payoff: a single chapter containing a list of heuristics and “smells” gathered while creating the case studies.',
        'English', 'AVAILABLE', 'Robert', 'C. Martin');
INSERT INTO books (isbn, catalog_id, title, collection, edition, publisher, synopsis, language, status, first_name, last_name)
VALUES (9780201633610, 'd846a5a7-2e1c-4c79-809c-4f3f471e826d', 'Design Patterns: Elements of Reusable Object-Oriented Software', 'Software Development', '1st', 'Addison-Wesley Professional',
        'Design Patterns is a modern classic in the literature of object-oriented development, offering timeless and elegant solutions to common problems in software design. It describes patterns for managing object creation, composing objects into larger structures, and coordinating control flow between objects.',
        'English', 'BORROWED', 'Erich', 'Gamma');
INSERT INTO books (isbn, catalog_id, title, collection, edition, publisher, synopsis, language, status, first_name, last_name)
VALUES (9780321125217, 'd846a5a7-2e1c-4c79-809c-4f3f471e826d', 'Domain-Driven Design: Tackling Complexity in the Heart of Software', 'Software Architecture', '1st', 'Addison-Wesley Professional', 'Domain-Driven Design fills that need. This is not a book about specific technologies. It offers readers a systematic approach to domain-driven design, presenting an extensive set of design best practices, experience-based techniques, and fundamental principles that facilitate the development of software projects facing complex domains.',
        'English', 'AVAILABLE', 'Eric', 'Evans');
INSERT INTO books (isbn, catalog_id, title, collection, edition, publisher, synopsis, language, status, first_name, last_name)
VALUES (9780545162074, '51aea50b-12ad-4a43-84c0-9af2f632929e', 'Harry Potter and the Philosopher''s Stone', 'Harry Potter Series', '1st', 'Scholastic',
        'Harry Potter has never even heard of Hogwarts when the letters start dropping on the doormat at number four, Privet Drive. Addressed in green ink on yellowish parchment with a purple seal, they are swiftly confiscated by his grisly aunt and uncle. Then, on Harry''s eleventh birthday, a great beetle-eyed giant of a man called Rubeus Hagrid bursts in with some astonishing news: Harry Potter is a wizard, and he has a place at Hogwarts School of Witchcraft and Wizardry. An incredible adventure is about to begin!',
        'English', 'AVAILABLE', 'J.K.', 'Rowling');
INSERT INTO books (isbn, catalog_id, title, collection, edition, publisher, synopsis, language, status, first_name, last_name)
VALUES (9780545791328, '51aea50b-12ad-4a43-84c0-9af2f632929e', 'Harry Potter and the Chamber of Secrets', 'Harry Potter Series', '1st', 'Scholastic',
        'The Dursleys were so mean and hideous that summer that all Harry Potter wanted was to get back to the Hogwarts School for Witchcraft and Wizardry. But just as he''s packing his bags, Harry receives a warning from a strange, impish creature named Dobby who says that if Harry Potter returns to Hogwarts, disaster will strike.',
        'English', 'AVAILABLE', 'J.K.', 'Rowling');
INSERT INTO books (isbn, catalog_id, title, collection, edition, publisher, synopsis, language, status, first_name, last_name)
VALUES(9780439358071, '51aea50b-12ad-4a43-84c0-9af2f632929e', 'Harry Potter and the Prisoner of Azkaban', 'Harry Potter Series', '1st', 'Scholastic',
       'For twelve long years, the dread fortress of Azkaban held an infamous prisoner named Sirius Black. Convicted of killing thirteen people with a single curse, he was said to be the heir apparent to the Dark Lord, Voldemort. Now he has escaped, leaving only two clues as to where he might be headed: Harry Potter''s defeat of You-Know-Who was Black''s downfall as well. And the Azkaban guards heard Black muttering in his sleep, "He''s at Hogwarts... he''s at Hogwarts."',
       'English', 'LOST', 'J.K.', 'Rowling');
INSERT INTO books (isbn, catalog_id, title, collection, edition, publisher, synopsis, language, status, first_name, last_name)
VALUES (9780765311788, 'ea82dca7-abed-4db2-923e-7a2186f1e3db', 'Dune', 'Dune Series', '1st', 'Tor Books',
        'Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides, heir to a noble family tasked with ruling an inhospitable world where the only thing of value is the “spice” melange, a drug capable of extending life and enhancing consciousness. Coveted across the known universe, melange is a prize worth killing for.',
        'English', 'BORROWED', 'Frank', 'Herbert');
INSERT INTO books (isbn, catalog_id, title, collection, edition, publisher, synopsis, language, status, first_name, last_name)
VALUES (9780312867819, 'ea82dca7-abed-4db2-923e-7a2186f1e3db', 'Neuromancer', 'Sprawl Trilogy', '1st', 'Ace Books',
        'Case was the sharpest data-thief in the matrix—until he crossed the wrong people and they crippled his nervous system, banishing him from cyberspace. Now a mysterious new employer has recruited him for a last-chance run at an unthinkably powerful artificial intelligence. With a streetwise trickster and a vengeful catwoman by his side, Case embarks on an adventure that ups the ante on an entire genre of fiction.',
        'English', 'LOST', 'William', 'Gibson');
INSERT INTO books (isbn, catalog_id, title, collection, edition, publisher, synopsis, language, status, first_name, last_name)
VALUES (9780765308481, 'ea82dca7-abed-4db2-923e-7a2186f1e3db', 'Hyperion', 'Hyperion Cantos', '1st', 'Spectra',
        'On the world called Hyperion, beyond the law of the Hegemony of Man, there waits the creature called the Shrike. There are those who worship it. There are those who fear it. And there are those who have vowed to destroy it. In the Valley of the Time Tombs, where huge, brooding structures move backward through time, the Shrike waits for them all.',
        'English', 'AVAILABLE', 'Dan', 'Simmons');

insert into catalogs (catalog_id, type, size) values ('d846a5a7-2e1c-4c79-809c-4f3f471e826d', 'Adult', 4);
INSERT INTO catalogs (catalog_id, type, size) VALUES
                                                  ('51aea50b-12ad-4a43-84c0-9af2f632929e', 'Kids', 3),
                                                  ('ea82dca7-abed-4db2-923e-7a2186f1e3db', 'Teen', 3),
                                                  ('a80f6903-6735-4036-bc21-3d9a2d8e27e0', 'Romance', 0),
                                                  ('f40d7e9e-1b3e-4717-9f02-252fec1edb86', 'Thriller', 0),
                                                  ('63c0a86a-2de6-4d7d-8275-d546628c3084', 'Historical Fiction', 0),
                                                  ('8297975a-a01d-433c-9ac7-c8565fc95b5e', 'Horror', 0),
                                                  ('448b5ee1-4445-4213-84cf-0dc9150d82e9', 'Adventure', 0),
                                                  ('04668502-0fec-4972-a401-e096354df26a', 'Young Adult', 0),
                                                  ('5efb2d57-180f-4ab0-b030-fdbd2c26b883', 'Biography', 0),
                                                  ('e125b033-591e-464d-91de-beaf360c3d48', 'Self-Help', 0);

INSERT INTO fines (fine_id, amount, reason, is_paid) VALUES
                                                         ('ef23ab6e-d614-47b9-95d0-d66167ae5081', 0.50, 'Late return', 'true'),
                                                         ('549ac9a5-076c-4954-b735-38426ba365c0', 5.75, 'Late return', 'false'),
                                                         ('5a059803-2f79-4e15-84bd-cfbffc048053', 1.25, 'Late return', 'true'),
                                                         ('413d4695-787c-479d-9fb6-b316e94345c0', 0.75, 'Late return', 'false'),
                                                         ('ab8d01f4-8a7c-4c06-9fd1-60ae848c9bff', 2.50, 'Late return', 'false'),
                                                         ('5b4d6c59-ce18-467e-a5c7-df5bca05fd94', 2.50, 'Late return', 'false'),
                                                         ('9cedbd09-2ae5-44cc-aaef-11974e2f78e7', 2.50, 'Late return', 'false'),
                                                         ('02fa3edc-1616-48f4-a1d7-1f203e7e7415', 2.50, 'Late return', 'false'),
                                                         ('b8887143-c9cf-4e63-960a-eb34de15fc9d', 2.50, 'Late return', 'false'),
                                                         ('07dfa96a-3c6c-4225-b601-b18d206fc24d', 2.50, 'Late return', 'false');

INSERT INTO loan_books (loan_id, isbn) VALUES (1, 9781566199094);
INSERT INTO loan_books (loan_id, isbn) VALUES (2, 9780132350884);
INSERT INTO loan_books (loan_id, isbn) VALUES (3, 9780201633610);
INSERT INTO loan_books (loan_id, isbn) VALUES (4, 9780321125217);
INSERT INTO loan_books (loan_id, isbn) VALUES (5, 9780545162074);
INSERT INTO loan_books (loan_id, isbn) VALUES (6, 9780545791328);
INSERT INTO loan_books (loan_id, isbn) VALUES (7, 9780439358071);
INSERT INTO loan_books (loan_id, isbn) VALUES (8, 9780765311788);
INSERT INTO loan_books (loan_id, isbn) VALUES (9, 9780312867819);
INSERT INTO loan_books (loan_id, isbn) VALUES (10, 9780765308481);

INSERT INTO loans (loan_id, patron_id, fine_id, status, borrowed_date, due_date, returned_date)
VALUES ('1f8f92c6-49f0-4be7-81cc-464b92de11eb', 'e5913a79-9b1e-4516-9ffd-06578e7af261', 'ef23ab6e-d614-47b9-95d0-d66167ae5081', 'EXPIRED', '2024-02-01', '2024-02-15', null);
INSERT INTO loans (loan_id, patron_id, fine_id, status, borrowed_date, due_date, returned_date)
VALUES ('d014011c-827c-44f5-bb9c-e75282dd9464', 'e5913a79-9b1e-4516-9ffd-06578e7af261', null, 'ACTIVE', '2024-02-10', '2024-03-01', null);
INSERT INTO loans (loan_id, patron_id, fine_id, status, borrowed_date, due_date, returned_date)
VALUES ('c1865f4b-8bbe-4582-b0e4-0595ad28e19b', 'e5913a79-9b1e-4516-9ffd-06578e7af261', null, 'RETURNED', '2024-01-20', '2024-02-10', '2024-02-10');
INSERT INTO loans (loan_id, patron_id, fine_id, status, borrowed_date, due_date, returned_date)
VALUES ('49ed1f0b-747c-43d2-b487-2c45ee746b68', 'c3540a89-cb47-4c96-888e-ff96708db4d8', '549ac9a5-076c-4954-b735-38426ba365c0', 'EXPIRED', '2024-02-05', '2024-02-20', null);
INSERT INTO loans (loan_id, patron_id, fine_id, status, borrowed_date, due_date, returned_date)
VALUES ('38b06365-6c97-4990-b063-ccfad1516b65', 'c3540a89-cb47-4c96-888e-ff96708db4d8', null, 'ACTIVE', '2024-02-15', '2024-03-05', null);
INSERT INTO loans (loan_id, patron_id, fine_id, status, borrowed_date, due_date, returned_date)
VALUES ('3d9552f7-0305-414c-8708-a96697f61b10', 'c3540a89-cb47-4c96-888e-ff96708db4d8', null, 'RETURNED', '2024-02-08', '2024-02-28', '2024-02-20');
INSERT INTO loans (loan_id, patron_id, fine_id, status, borrowed_date, due_date, returned_date)
VALUES ('1ffd3d41-f669-4753-bfa0-c75522704b99', 'dd1ab8b0-ab17-4e03-b70a-84caa3871606', '5a059803-2f79-4e15-84bd-cfbffc048053', 'EXPIRED', '2024-02-20', '2024-03-05', null);
INSERT INTO loans (loan_id, patron_id, fine_id, status, borrowed_date, due_date, returned_date)
VALUES ('e2859902-5e91-45d8-b2e7-93fc7abcae67', 'dd1ab8b0-ab17-4e03-b70a-84caa3871606', null, 'ACTIVE', '2024-02-25', '2024-03-15', null);
INSERT INTO loans (loan_id, patron_id, fine_id, status, borrowed_date, due_date, returned_date)
VALUES ('48dd2bd5-1546-48dd-9947-4eef6a4d0e19', 'dd1ab8b0-ab17-4e03-b70a-84caa3871606', null, 'RETURNED', '2024-02-12', '2024-03-02', '2024-02-29');
INSERT INTO loans (loan_id, patron_id, fine_id, status, borrowed_date, due_date, returned_date)
VALUES ('d7ffdd25-2ca6-40a6-9e71-18d009d3eb3a', 'ba6c3e76-366e-44bb-8279-b41dc32dc456', '413d4695-787c-479d-9fb6-b316e94345c0', 'EXPIRED', '2024-02-02', '2024-02-17', null);
