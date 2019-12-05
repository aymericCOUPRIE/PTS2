DROP TABLE IF EXISTS THEME;
CREATE TABLE THEME
(
    Id_Theme INT AUTO_INCREMENT,
    Nom_Theme VARCHAR(200) NOT NULL ,
    Desc_Theme VARCHAR(500) NOT NULL ,
    Nb_Question INT NOT NULL ,
    Adr_Image VARCHAR(500) NOT NULL ,
    Adr_Logo VARCHAR(500),
    Actif_Theme BOOLEAN,
    PRIMARY KEY (Id_Theme),
);
  

DROP TABLE IF EXISTS SCORE;
CREATE TABLE SCORE
(
    Pseudo_Joueur VARCHAR(200) ,
    Score_Joueur INT NOT NULL,
    Id_Theme INT NOT NULL,
    PRIMARY KEY (Pseudo_Joueur),
    FOREIGN KEY (Id_Theme) REFERENCES THEME(Id_Theme)
);


DROP TABLE IF EXISTS QUESTION;
CREATE TABLE QUESTION
(
    Id_Question INT AUTO_INCREMENT,
    Num_Question INT NOT NULL ,
    Int_Question VARCHAR(500) NOT NULL ,
    Id_THEME INT NOT NULL  ,
    PRIMARY KEY (Id_Question),
    FOREIGN KEY (Id_THEME) REFERENCES THEME(Id_THEME)
);


DROP TABLE IF EXISTS COORDONNEE;
CREATE TABLE COORDONNEE
(
    Id_Coordonnee INT AUTO_INCREMENT,
    Id_Zone INT NOT NULL,
    Coor_Zone_X FLOAT NOT NULL ,
    Coor_Zone_Y FLOAT NOT NULL ,
    Id_Question INT NOT NULL ,
    PRIMARY KEY (Id_Coordonnee),
    FOREIGN KEY (Id_Question) REFERENCES QUESTION(Id_Question )
);