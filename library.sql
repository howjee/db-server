/*
Navicat MySQL Data Transfer

Source Server         : xxx
Source Server Version : 50727
Source Host           : localhost:3306
Source Database       : library

Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001

Date: 2019-08-18 23:45:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `name` char(20) DEFAULT NULL,
  `author` char(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES ('c language', 'niuren');
INSERT INTO `book` VALUES ('java', 'lihairen');
INSERT INTO `book` VALUES ('python', 'yjj');

-- ----------------------------
-- Table structure for money_history
-- ----------------------------
DROP TABLE IF EXISTS `money_history`;
CREATE TABLE `money_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `type` int(11) NOT NULL COMMENT '0:充值,1:提现,2:保证金,3:递延费,4:建仓费,5:盈利分红,6:系统赠送金额',
  `value` float NOT NULL,
  `remain` float NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `k_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of money_history
-- ----------------------------

-- ----------------------------
-- Table structure for stocks
-- ----------------------------
DROP TABLE IF EXISTS `stocks`;
CREATE TABLE `stocks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `stock_number` varchar(255) NOT NULL,
  `stock_name` varchar(255) NOT NULL,
  `stock_price` float NOT NULL,
  `strategy_mode` int(11) NOT NULL COMMENT '0:1天免息,1:5天免息,2:10天免息,3:单周,4:双周,5:月息',
  `strategy_rate` float NOT NULL,
  `margin` float NOT NULL,
  `stop_loss_rate` float NOT NULL,
  `stop_profit_rate` float NOT NULL,
  `open_fee` float NOT NULL,
  `delay_fee` float NOT NULL,
  `auto_delay` tinyint(1) DEFAULT '1',
  `current_count` int(11) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_valid` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `k_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of stocks
-- ----------------------------
INSERT INTO `stocks` VALUES ('1', '1', '000063', '中兴通讯', '28.19', '0', '8', '500', '33.83', '26.22', '7.2', '15', '1', '50', '2019-08-18 22:59:02', '2019-08-18 23:12:31', '1');

-- ----------------------------
-- Table structure for stock_history
-- ----------------------------
DROP TABLE IF EXISTS `stock_history`;
CREATE TABLE `stock_history` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `stock_id` int(11) NOT NULL,
  `operate_type` int(11) DEFAULT '0' COMMENT '0: buy, 1: sell',
  `buy_price` float NOT NULL,
  `sell_price` float NOT NULL,
  `sell_count` int(11) NOT NULL,
  `remain_count` int(11) NOT NULL,
  `sell_mode` int(11) NOT NULL,
  `profit_rate` float NOT NULL,
  `profit` float NOT NULL,
  `buy_time` datetime NOT NULL,
  `sell_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `k_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of stock_history
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `money` double DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `k_nickname` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', '慕容雪村', 'http://…/avatar.jpg', '5000', '2019-08-18 21:37:50');
INSERT INTO `users` VALUES ('2', '魔力鸟', 'http://…/avatar2.jpg', '10000', '2019-08-18 21:37:50');
INSERT INTO `users` VALUES ('3', '科比', null, '8000', '2019-08-18 21:37:50');

-- ----------------------------
-- Table structure for user_auths
-- ----------------------------
DROP TABLE IF EXISTS `user_auths`;
CREATE TABLE `user_auths` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `identity_type` varchar(64) NOT NULL,
  `identifier` varchar(255) NOT NULL,
  `credential` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_auths
-- ----------------------------
INSERT INTO `user_auths` VALUES ('1', '1', 'email', '123@example.com', 'xxxx');
INSERT INTO `user_auths` VALUES ('2', '1', 'phone', '13888888888', 'xxxxx');
INSERT INTO `user_auths` VALUES ('3', '1', 'weibo', '23fdfasd', 'xxx');
INSERT INTO `user_auths` VALUES ('4', '2', 'username', 'moliniao', 'xxxx');
INSERT INTO `user_auths` VALUES ('5', '3', 'weixin', 'summerjdk', 'xxxx');
