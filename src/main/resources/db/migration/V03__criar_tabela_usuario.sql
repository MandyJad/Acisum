CREATE TABLE usuario (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT, 
	email VARCHAR(100) NOT NULL,
	senha VARCHAR(64) NOT NULL,
	id_cantor BIGINT(20) NOT NULL,
	FOREIGN KEY (id_cantor) references cantor(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO usuario (email, senha, id_cantor) values ('admin@admin.com', '1234', 2);