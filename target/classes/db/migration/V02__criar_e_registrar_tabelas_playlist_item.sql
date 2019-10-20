CREATE TABLE playlist (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT, 
	id_cantor BIGINT(20) NOT NULL,
	--data_apresentacao DATE NOT NULL,
	nome VARCHAR(50) NOT NULL,
	FOREIGN KEY (id_cantor) references cantor(id) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE item_playlist (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT, 
	id_playlist BIGINT(20) NOT NULL, 
	id_cifra BIGINT(20) NOT NULL, 
	FOREIGN KEY (id_playlist) references playlist(id), 
	FOREIGN KEY (id_cifra) references cifra(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
