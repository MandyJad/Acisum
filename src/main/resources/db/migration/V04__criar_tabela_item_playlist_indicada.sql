CREATE TABLE item_playlist_indicada (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT, 
	id_playlist BIGINT(20) NOT NULL, 
	id_cifra BIGINT(20) NOT NULL, 
	pedidos INT(3) NOT NULL,
	FOREIGN KEY (id_playlist) references playlist(id), 
	FOREIGN KEY (id_cifra) references cifra(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;