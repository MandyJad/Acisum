CREATE TABLE cantor (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT, 
	nome VARCHAR(50) NOT NULL 
) ENGINE=InnoDB DEFAULT CHARSET=utf8; 

INSERT INTO cantor (nome) VALUES ('Leonardo');
INSERT INTO cantor (nome) VALUES ('Bruce');
INSERT INTO cantor (nome) VALUES ('Cristiano');

CREATE TABLE genero (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT, 
	nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO genero (nome) VALUES ('Sertanejo');
INSERT INTO genero (nome) VALUES ('Rock');

CREATE TABLE cifra (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT, 
	nome VARCHAR(150) NOT NULL, 
	pdf VARCHAR(200) NOT NULL,
	id_cantor BIGINT(20) NOT NULL,
	id_genero BIGINT(20) NOT NULL, 
	FOREIGN KEY (id_cantor) references cantor(id),
	FOREIGN KEY (id_genero) references genero(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO cifra (nome, pdf, id_cantor, id_genero) VALUES ('The Tropper', '/cifras/cantor_1/the_trooper.pdf', 2, 2);
