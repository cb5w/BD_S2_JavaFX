DROP VIEW IF EXISTS vue_absences;
DROP TABLE IF EXISTS PARTICIPER;
DROP TABLE IF EXISTS APPARTENIR;
DROP TABLE IF EXISTS DISCIPLINE;
DROP TABLE IF EXISTS PROMOTION;
DROP TABLE IF EXISTS SEANCE;
DROP TABLE IF EXISTS ENSEIGNANT;
DROP TABLE IF EXISTS ETUDIANT;
DROP TABLE IF EXISTS GROUPE;
DROP TABLE IF EXISTS FORMATION;

CREATE TABLE FORMATION(
   ID_Formation INT PRIMARY KEY,
   Annee_Formation INT NOT NULL,
   Libelle_Formation VARCHAR(50) NOT NULL
);

CREATE TABLE GROUPE(
   ID_Groupe INT PRIMARY KEY,
   Code_Groupe VARCHAR(50) NOT NULL,
   Libelle_Groupe VARCHAR(50) NOT NULL
);

CREATE TABLE ETUDIANT(
   INE_Etudiant VARCHAR(100) PRIMARY KEY,
   Nom_Etudiant VARCHAR(50) NOT NULL,
   Prenom_Etudiant VARCHAR(50) NOT NULL,
   Email_Etudiant VARCHAR(50) NOT NULL UNIQUE,
   Tel_Etudiant VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE ENSEIGNANT(
   ID_Enseignant INT PRIMARY KEY,
   Nom_Enseignant VARCHAR(50) NOT NULL,
   Prenom_Enseignant VARCHAR(50) NOT NULL,
   Email_Enseignant VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE SEANCE(
   ID_Seance INT PRIMARY KEY,
   Date_Seance DATE NOT NULL,
   Heure_Seance TIME NOT NULL,
   Duree_Seance INT NOT NULL,
   Type_Cours VARCHAR(50) NOT NULL,
   ID_Groupe INT NOT NULL,
   ID_Enseignant INT NOT NULL,
   FOREIGN KEY(ID_Groupe) REFERENCES GROUPE(ID_Groupe),
   FOREIGN KEY(ID_Enseignant) REFERENCES ENSEIGNANT(ID_Enseignant)
);

CREATE TABLE PROMOTION(
   ID_Promotion INT PRIMARY KEY,
   Libelle_Promotion VARCHAR(50) NOT NULL,
   Code_Promotion VARCHAR(50) NOT NULL,
   ID_Groupe INT NOT NULL,
   ID_Formation INT NOT NULL,
   FOREIGN KEY(ID_Groupe) REFERENCES GROUPE(ID_Groupe),
   FOREIGN KEY(ID_Formation) REFERENCES FORMATION(ID_Formation)
);

CREATE TABLE DISCIPLINE(
   ID_Discipline INT PRIMARY KEY,
   Code_Discipline VARCHAR(50) NOT NULL,
   Libelle_Discipline VARCHAR(50) NOT NULL,
   ID_Seance INT NOT NULL,
   ID_Promotion INT NOT NULL,
   FOREIGN KEY(ID_Seance) REFERENCES SEANCE(ID_Seance),
   FOREIGN KEY(ID_Promotion) REFERENCES PROMOTION(ID_Promotion)
);

CREATE TABLE APPARTENIR(
   ID_Groupe INT,
   INE_Etudiant VARCHAR(100),
   PRIMARY KEY(ID_Groupe, INE_Etudiant),
   FOREIGN KEY(ID_Groupe) REFERENCES GROUPE(ID_Groupe),
   FOREIGN KEY(INE_Etudiant) REFERENCES ETUDIANT(INE_Etudiant)
);

CREATE TABLE PARTICIPER(
   INE_Etudiant VARCHAR(100),
   ID_Seance INT,
   Etat_Presence VARCHAR(50) NOT NULL,
   CHECK (Etat_Presence IN ('present','absent','en retard','absence excusee','absence justifiee')),
   PRIMARY KEY(INE_Etudiant, ID_Seance),
   FOREIGN KEY(INE_Etudiant) REFERENCES ETUDIANT(INE_Etudiant),
   FOREIGN KEY(ID_Seance) REFERENCES SEANCE(ID_Seance)
);

INSERT INTO FORMATION (ID_Formation, Libelle_Formation, Annee_Formation) VALUES
(1, 'BUT Informatique', 2024);

INSERT INTO GROUPE (ID_Groupe, Code_Groupe, Libelle_Groupe) VALUES
(1, 'B1-TD1', 'TD groupe 1'),
(2, 'B1-TD2', 'TD groupe 2'),
(3, 'B1-TP1', 'TP groupe 1'),
(4, 'B1-TP2', 'TP groupe 2'),
(5, 'B1-TP3', 'TP groupe 3'),
(6, 'B1-ALT', 'Alternants'),
(7, 'B2-TD1', 'TD groupe 1'),
(8, 'B2-TP1', 'TP groupe 1');

INSERT INTO ENSEIGNANT (ID_Enseignant, Nom_Enseignant, Prenom_Enseignant, Email_Enseignant) VALUES
(1, 'Walkowiak', 'Yann', 'yann.walkowiak@univ-lemans.fr'),
(2, 'Brochard', 'Emmanuel', 'emmanuel.brochard@univ-lemans.fr'),
(3, 'Martin', 'Sophie', 'sophie.martin@univ-lemans.fr'),
(4, 'Dupont', 'Lucas', 'lucas.dupont@univ-lemans.fr'),
(5, 'Leroy', 'Claire', 'claire.leroy@univ-lemans.fr'),
(6, 'Moreau', 'Thomas', 'thomas.moreau@univ-lemans.fr');

INSERT INTO PROMOTION (ID_Promotion, Code_Promotion, Libelle_Promotion, ID_Groupe, ID_Formation) VALUES
(1, 'BUT1', 'BUT Informatique 1ere annee', 1, 1),
(2, 'BUT2', 'BUT Informatique 2eme annee', 7, 1),
(3, 'BUT3', 'BUT Informatique 3eme annee', 7, 1);

INSERT INTO SEANCE (ID_Seance, Date_Seance, Heure_Seance, Duree_Seance, Type_Cours, ID_Groupe, ID_Enseignant) VALUES
(1, DATE '2025-03-10', TIME '08:00:00', 120, 'TD', 1, 2),
(2, DATE '2025-03-10', TIME '10:15:00', 120, 'TP', 3, 2),
(3, DATE '2025-03-11', TIME '08:00:00', 90, 'CM', 1, 3),
(4, DATE '2025-03-12', TIME '13:30:00', 120, 'TP', 4, 4),
(5, DATE '2025-03-13', TIME '08:00:00', 60, 'CM', 1, 5),
(6, DATE '2025-03-14', TIME '10:15:00', 120, 'TP', 3, 1),
(7, DATE '2025-03-17', TIME '14:00:00', 120, 'EVALUATION', 2, 3),
(8, DATE '2025-03-18', TIME '08:00:00', 120, 'PROJET', 1, 2);

INSERT INTO DISCIPLINE (ID_Discipline, Code_Discipline, Libelle_Discipline, ID_Seance, ID_Promotion) VALUES
(1, 'R2.06', 'Exploitation d''une base de donnees', 1, 1),
(2, 'R2.01', 'Developpement oriente objet', 3, 1),
(3, 'R2.02', 'Developpement d''applications avec IHM', 4, 1),
(4, 'R2.07', 'Graphes', 5, 1),
(5, 'R2.08', 'Outils numeriques pour les statistiques', 6, 1),
(6, 'R2.09', 'Methodes numeriques', 7, 1);

INSERT INTO ETUDIANT (INE_Etudiant, Nom_Etudiant, Prenom_Etudiant, Email_Etudiant, Tel_Etudiant) VALUES
('1234567890A', 'Durand', 'Alice', 'alice.durand@etud.univ-lemans.fr', '0612345678'),
('2345678901B', 'Bernard', 'Thomas', 'thomas.bernard@etud.univ-lemans.fr', '0623456789'),
('3456789012C', 'Petit', 'Emma', 'emma.petit@etud.univ-lemans.fr', '0649713254'),
('4567890123D', 'Robert', 'Hugo', 'hugo.robert@etud.univ-lemans.fr', '0645678901'),
('5678901234E', 'Simon', 'Lea', 'lea.simon@etud.univ-lemans.fr', '0656789012'),
('6789012345F', 'Laurent', 'Maxime', 'maxime.laurent@etud.univ-lemans.fr', '0667890123'),
('7890123456G', 'Michel', 'Chloe', 'chloe.michel@etud.univ-lemans.fr', '0678901234'),
('8901234567H', 'Garcia', 'Nathan', 'nathan.garcia@etud.univ-lemans.fr', '0689012345');

INSERT INTO APPARTENIR (ID_Groupe, INE_Etudiant) VALUES
(1, '1234567890A'), (3, '1234567890A'),
(1, '2345678901B'), (3, '2345678901B'),
(2, '3456789012C'), (4, '3456789012C'),
(2, '4567890123D'), (4, '4567890123D'),
(1, '5678901234E'), (5, '5678901234E'),
(6, '6789012345F'),
(7, '7890123456G'), (8, '7890123456G'),
(7, '8901234567H'), (8, '8901234567H');

INSERT INTO PARTICIPER (INE_Etudiant, ID_Seance, Etat_Presence) VALUES
('1234567890A', 1, 'present'),
('2345678901B', 1, 'absent'),
('5678901234E', 1, 'en retard'),
('1234567890A', 2, 'present'),
('2345678901B', 2, 'present'),
('5678901234E', 2, 'present'),
('1234567890A', 3, 'present'),
('2345678901B', 3, 'absence excusee'),
('5678901234E', 3, 'present'),
('3456789012C', 4, 'present'),
('4567890123D', 4, 'absent'),
('1234567890A', 5, 'present'),
('2345678901B', 5, 'present'),
('5678901234E', 5, 'absence justifiee'),
('1234567890A', 6, 'absent'),
('2345678901B', 6, 'present'),
('5678901234E', 6, 'present'),
('3456789012C', 7, 'present'),
('4567890123D', 7, 'present'),
('1234567890A', 8, 'present'),
('2345678901B', 8, 'en retard'),
('5678901234E', 8, 'present');

CREATE VIEW vue_absences AS
SELECT
    e.INE_Etudiant,
    e.Nom_Etudiant,
    e.Prenom_Etudiant,
    f.Libelle_Formation,
    pr.Code_Promotion,
    g.Code_Groupe,
    d.Code_Discipline,
    d.Libelle_Discipline,
    s.Date_Seance,
    s.Heure_Seance,
    s.Duree_Seance,
    s.Type_Cours,
    p.Etat_Presence
FROM PARTICIPER p
JOIN ETUDIANT e ON p.INE_Etudiant = e.INE_Etudiant
JOIN SEANCE s ON p.ID_Seance = s.ID_Seance
JOIN GROUPE g ON s.ID_Groupe = g.ID_Groupe
JOIN DISCIPLINE d ON d.ID_Seance = s.ID_Seance
JOIN PROMOTION pr ON d.ID_Promotion = pr.ID_Promotion
JOIN FORMATION f ON pr.ID_Formation = f.ID_Formation;
