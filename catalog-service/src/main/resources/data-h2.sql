-- Modify the synopsis column to varchar(550)
ALTER TABLE books ALTER COLUMN synopsis VARCHAR(550);

-- Modify the isbn column to unique
ALTER TABLE books ADD CONSTRAINT unique_isbn UNIQUE (isbn);
-- ALTER TABLE books ALTER COLUMN isbn LONG UNIQUE;

insert into books(isbn, catalog_id, title, collection, edition, publisher, synopsis, language, status, first_name, last_name)
values(9789390183524, 'd846a5a7-2e1c-4c79-809c-4f3f471e826d', 'The Great Gatsby', 'F. Scott Fitzgerald', '', 'Scribner',
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
