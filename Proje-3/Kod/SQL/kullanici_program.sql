CREATE  TABLE IF NOT EXISTS  kullanici_program(user_id INT NOT NULL,izleme_tarihi DATE,izleme_suresi INT,hangi_bolum INT,puan INT,program_id INT NOT NULL,FOREIGN  KEY(user_id) REFERENCES kullanicilar(user_id),FOREIGN  KEY(program_id) REFERENCES programlar(program_id));
