/*
 Navicat Premium Data Transfer

 Source Server         : db
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : localhost:3306
 Source Schema         : library

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 25/10/2019 22:20:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for articles
-- ----------------------------
DROP TABLE IF EXISTS `articles`;
CREATE TABLE `articles`  (
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `body` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`title`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of articles
-- ----------------------------
INSERT INTO `articles` VALUES ('文章一', 'eeeee/-/谢谢谢谢xxxxxxxxxx', '2019-10-08 16:30:19');
INSERT INTO `articles` VALUES ('文章三', 'eeeee/-/谢谢谢谢sss', '2019-10-08 16:30:19');
INSERT INTO `articles` VALUES ('文章二', 'eeeee/-/谢谢谢谢eee', '2019-10-08 16:30:19');
INSERT INTO `articles` VALUES ('文章五', 'eeeee/-/谢谢谢谢', '2019-10-08 16:19:51');
INSERT INTO `articles` VALUES ('文章四', 'eeeee/-/谢谢谢谢', '2019-10-08 16:19:51');

-- ----------------------------
-- Table structure for configs
-- ----------------------------
DROP TABLE IF EXISTS `configs`;
CREATE TABLE `configs`  (
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `body` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of configs
-- ----------------------------
INSERT INTO `configs` VALUES ('法定节假日', '{2019:[0101,0501]}', '2019-10-19 15:53:02');

-- ----------------------------
-- Table structure for kcb_stocks
-- ----------------------------
DROP TABLE IF EXISTS `kcb_stocks`;
CREATE TABLE `kcb_stocks`  (
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `inuse` tinyint(1) NOT NULL DEFAULT 1,
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of kcb_stocks
-- ----------------------------
INSERT INTO `kcb_stocks` VALUES ('大秦铁路', '09920', 1, '2019-10-05 13:32:26');
INSERT INTO `kcb_stocks` VALUES ('华山', '09dd0', 0, '2019-10-09 16:49:52');

-- ----------------------------
-- Table structure for money_history
-- ----------------------------
DROP TABLE IF EXISTS `money_history`;
CREATE TABLE `money_history`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `type` int(11) NOT NULL COMMENT '0:充值,1:提现,2:保证金,3:递延费,4:建仓费,5:盈利分红,6:系统赠送金额',
  `value` double NOT NULL,
  `remain` double NOT NULL,
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `k_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of money_history
-- ----------------------------
INSERT INTO `money_history` VALUES (1, 1, 0, 111, 50000, '2019-10-14 23:16:55', NULL);
INSERT INTO `money_history` VALUES (2, 1, 1, -110, 50111, '2019-10-14 23:19:01', NULL);
INSERT INTO `money_history` VALUES (3, 3, 3, 15, 8000, '2019-10-14 23:43:02', NULL);
INSERT INTO `money_history` VALUES (4, 3, 3, 15, 8015, '2019-10-17 23:43:00', NULL);
INSERT INTO `money_history` VALUES (5, 5, 0, 1, 0, '2019-10-19 13:26:28', NULL);
INSERT INTO `money_history` VALUES (6, 5, 0, 1222, 1, '2019-10-19 13:26:45', NULL);
INSERT INTO `money_history` VALUES (7, 5, 1, -1222, 1223, '2019-10-19 13:36:37', NULL);
INSERT INTO `money_history` VALUES (8, 5, 6, 234, 1, '2019-10-19 13:38:07', NULL);
INSERT INTO `money_history` VALUES (9, 5, 2, 500, 235, '2019-10-19 14:41:36', NULL);
INSERT INTO `money_history` VALUES (10, 5, 4, 7.199999809265137, 235, '2019-10-19 14:41:36', NULL);
INSERT INTO `money_history` VALUES (11, 5, 2, 2000, -272.20001220703125, '2019-10-19 14:41:59', NULL);
INSERT INTO `money_history` VALUES (12, 5, 4, 23.100000381469727, -272.20001220703125, '2019-10-19 14:41:59', NULL);
INSERT INTO `money_history` VALUES (13, 3, 3, 15, 8030, '2019-10-22 11:35:22', NULL);
INSERT INTO `money_history` VALUES (14, 3, 3, 8, 8030, '2019-10-22 11:35:22', NULL);
INSERT INTO `money_history` VALUES (15, 3, 3, 15, 8053, '2019-10-24 01:38:55', NULL);
INSERT INTO `money_history` VALUES (16, 3, 3, 8, 8053, '2019-10-24 01:38:55', NULL);
INSERT INTO `money_history` VALUES (17, 3, 3, 15, 8076, '2019-10-24 23:43:00', NULL);
INSERT INTO `money_history` VALUES (18, 3, 3, 8, 8076, '2019-10-24 23:43:00', NULL);

-- ----------------------------
-- Table structure for order_history
-- ----------------------------
DROP TABLE IF EXISTS `order_history`;
CREATE TABLE `order_history`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `stock_id` int(11) NOT NULL,
  `operate_type` int(11) NULL DEFAULT 0 COMMENT '0: buy, 1: sell',
  `buy_price` float NOT NULL,
  `sell_price` float NOT NULL,
  `sell_count` int(11) NOT NULL,
  `remain_count` int(11) NOT NULL,
  `sell_mode` int(11) NOT NULL,
  `profit_rate` float NOT NULL,
  `profit` float NOT NULL,
  `buy_time` datetime(0) NOT NULL,
  `sell_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `k_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `stock_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `stock_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `stock_price` float NOT NULL,
  `strategy_mode` int(11) NOT NULL COMMENT '0:1天免息,1:5天免息,2:10天免息,3:单周,4:双周,5:月息',
  `strategy_rate` float NOT NULL,
  `margin` float NOT NULL,
  `stop_loss_rate` float NOT NULL,
  `stop_profit_rate` float NOT NULL,
  `open_fee` float NOT NULL,
  `delay_fee` float NOT NULL,
  `auto_delay` tinyint(1) NULL DEFAULT 1,
  `current_count` int(11) NOT NULL,
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `is_valid` tinyint(1) NULL DEFAULT 1,
  `status` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `k_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 3, '000063', '中兴通讯', 28.19, 0, 8, 500, 5, 0, 7.2, 15, 1, 50, '2019-10-07 20:27:05', '2019-10-07 21:10:57', 1, 1);
INSERT INTO `orders` VALUES (2, 3, '601857', '中国石油', 6.16, 1, 2500, 2000, 8.88, 5.729, 23.1, 8, 1, 2500, '2019-10-08 11:29:25', '2019-10-09 17:38:53', 1, 0);
INSERT INTO `orders` VALUES (3, 3, '601857', '中国石油', 6.16, 1, 2500, 2000, 7.392, 5.729, 23.1, 8, 1, 2500, '2019-10-08 11:46:28', '2019-10-08 11:46:28', 1, 0);
INSERT INTO `orders` VALUES (4, 3, '601857', '中国石油', 6.16, 1, 2500, 2000, 7.392, 5.729, 23.1, 8, 1, 2500, '2019-10-08 11:46:34', '2019-10-08 11:46:34', 1, 0);
INSERT INTO `orders` VALUES (5, 3, '601857', '中国石油', 6.16, 1, 2500, 2000, 7.392, 5.729, 23.1, 8, 1, 2500, '2019-10-08 11:47:36', '2019-10-08 11:47:36', 1, 0);
INSERT INTO `orders` VALUES (6, 3, '601857', '中国石油', 6.16, 1, 2500, 2000, 7.392, 5.729, 23.1, 8, 1, 2500, '2019-10-08 11:48:10', '2019-10-19 14:41:59', 1, 1);
INSERT INTO `orders` VALUES (7, 5, '000063', '中兴通讯', 28.19, 0, 8, 500, 33.83, 33.837, 7.2, 15, 1, 50, '2019-10-19 14:02:13', '2019-10-19 14:53:46', 1, 0);

-- ----------------------------
-- Table structure for url_permission
-- ----------------------------
DROP TABLE IF EXISTS `url_permission`;
CREATE TABLE `url_permission`  (
  `URI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `permission` int(11) NOT NULL DEFAULT 0 COMMENT '0:普通用户,1:管理员, 2,超级用户',
  PRIMARY KEY (`URI`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_auths
-- ----------------------------
DROP TABLE IF EXISTS `user_auths`;
CREATE TABLE `user_auths`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `identity_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `identifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `credential` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `identify_index`(`identity_type`, `identifier`) USING BTREE,
  UNIQUE INDEX `k_identifier`(`identifier`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_auths
-- ----------------------------
INSERT INTO `user_auths` VALUES (1, 1, 'email', '123@example.com', 'xxxx');
INSERT INTO `user_auths` VALUES (2, 1, 'phone', '13888888888', 'xxxxx');
INSERT INTO `user_auths` VALUES (3, 1, 'weibo', '23fdfasd', 'xxx');
INSERT INTO `user_auths` VALUES (4, 2, 'username', 'moliniao', 'xxxx');
INSERT INTO `user_auths` VALUES (5, 3, 'weixin', 'summerjdk', 'xxxx');
INSERT INTO `user_auths` VALUES (6, 4, 'phone', '15809217191', 'root');
INSERT INTO `user_auths` VALUES (7, 5, 'phone', '18092787601', '1234567');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `money` double NULL DEFAULT 0,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `permission` int(11) NOT NULL DEFAULT 0 COMMENT '0:普通用户,1:管理员, 2,超级用户',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `k_nickname`(`nickname`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '慕容雪村', 'http://…/avatar.jpg', 50001, '2019-08-18 21:37:50', 1);
INSERT INTO `users` VALUES (2, '魔力鸟', 'http://…/avatar2.jpg', 10000, '2019-08-18 21:37:50', 0);
INSERT INTO `users` VALUES (3, '科比', NULL, 8099, '2019-08-18 21:37:50', 0);
INSERT INTO `users` VALUES (4, 'root', NULL, 0, '2019-10-19 10:43:54', 2);
INSERT INTO `users` VALUES (5, 'summer', NULL, -2295.2999877929688, '2019-10-19 11:22:11', 1);

SET FOREIGN_KEY_CHECKS = 1;
