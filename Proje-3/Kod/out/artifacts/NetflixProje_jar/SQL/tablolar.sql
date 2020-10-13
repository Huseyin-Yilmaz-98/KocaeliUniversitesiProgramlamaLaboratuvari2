CREATE TABLE IF NOT EXISTS kullanicilar ( user_id INTEGER PRIMARY KEY AUTOINCREMENT, name CHAR(60) NOT NULL, email CHAR(60) NOT NULL, password CHAR(30), birthday DATE);
CREATE TABLE IF NOT EXISTS turler  (tur_id INTEGER PRIMARY KEY AUTOINCREMENT,name CHAR (60) NOT NULL);
CREATE  TABLE  IF NOT EXISTS programlar(program_id INTEGER PRIMARY KEY AUTOINCREMENT,name CHAR(100) NOT NULL,bolum_sayisi INT NOT NULL ,sure INT NOT NULL ,tip CHAR (20) NOT NULL);
CREATE  TABLE IF NOT EXISTS tur_program(tur_program_id INTEGER PRIMARY KEY AUTOINCREMENT,program_id INT,tur_id INT,FOREIGN KEY(tur_id) REFERENCES turler(tur_id),FOREIGN KEY(program_id) REFERENCES programlar(program_id));
CREATE  TABLE IF NOT EXISTS  kullanici_program(user_id INT NOT NULL,izleme_tarihi DATE,izleme_suresi INT,hangi_bolum INT,puan INT,program_id INT NOT NULL,FOREIGN  KEY(user_id) REFERENCES kullanicilar(user_id),FOREIGN  KEY(program_id) REFERENCES programlar(program_id));
