CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `balance` double NOT NULL,
  `user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `trade_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `charge` double NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `flag` int(11) NOT NULL,
  `trade_no` varchar(255) DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;