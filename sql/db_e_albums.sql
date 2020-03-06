/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50728
Source Host           : localhost:3306
Source Database       : db_e_albums

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date: 2020-03-06 10:42:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_bgm
-- ----------------------------
DROP TABLE IF EXISTS `tb_bgm`;
CREATE TABLE `tb_bgm` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id，主键自增',
  `bgm_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '背景音乐名',
  `bgm_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'bgm链接',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='背景音乐表';

-- ----------------------------
-- Table structure for tb_bgm_photo
-- ----------------------------
DROP TABLE IF EXISTS `tb_bgm_photo`;
CREATE TABLE `tb_bgm_photo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id,主键自增',
  `bgm_id` int(11) NOT NULL COMMENT '背景音乐id',
  `photo_id` int(11) NOT NULL COMMENT '照片id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='照片，背景音乐关联表';

-- ----------------------------
-- Table structure for tb_effects
-- ----------------------------
DROP TABLE IF EXISTS `tb_effects`;
CREATE TABLE `tb_effects` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id,主键自增',
  `effect` int(1) DEFAULT NULL COMMENT '特效，1,2,3中特效',
  `photo_id` int(11) DEFAULT NULL COMMENT '照片表id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='特效表';

-- ----------------------------
-- Table structure for tb_photo
-- ----------------------------
DROP TABLE IF EXISTS `tb_photo`;
CREATE TABLE `tb_photo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id，主键自增',
  `photo_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '照片名',
  `photo_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片链接',
  `photo_type` int(1) NOT NULL COMMENT '照片类型，0-3，分别代表人物，风景，建筑，美食',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `photo_descri` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '照片描述',
  `photo_bgm` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '照片背景音乐',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='照片表';

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` int(9) NOT NULL AUTO_INCREMENT COMMENT 'id,主键自增',
  `username` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `sex` int(1) NOT NULL COMMENT '性别，0代表男性，1代表女性',
  `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_manager` int(1) NOT NULL DEFAULT '0' COMMENT '是否是管理员，0代表不是，1代表是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------
-- Table structure for tb_user_photo
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_photo`;
CREATE TABLE `tb_user_photo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id，主键自增',
  `user_id` int(11) NOT NULL COMMENT 'user表id',
  `photo_id` int(11) NOT NULL COMMENT '照片表id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户，照片关联表';
