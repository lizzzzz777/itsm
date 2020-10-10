/*
SQLyog Community v10.4 
MySQL - 5.6.32 : Database - itsm5db
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `act_evt_log` */


-- ----------------------------
-- Table structure for `itsm_cmdb_headline`
-- ----------------------------
DROP TABLE IF EXISTS `itsm_cmdb_headline`;
CREATE TABLE `itsm_cmdb_headline` (
  `id` int(11) NOT NULL,
  `headline` varchar(255) DEFAULT NULL COMMENT '大屏标题',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `itsm_ves_group_user`;
CREATE TABLE `itsm_ves_group_user` (
  `group_id` INT(11) DEFAULT NULL COMMENT '组id',
  `user_id` INT(11) DEFAULT NULL COMMENT '用户id',
  `del_status` INT(11) DEFAULT '1' COMMENT '0禁止删除',
  `account` VARCHAR(255) DEFAULT NULL COMMENT '用户名'
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '工作组关联人成员表';


DROP TABLE IF EXISTS `itsm_ves_group`;
CREATE TABLE `itsm_ves_group` (
  `id` INT(11) NOT NULL COMMENT '组id',
  `name` VARCHAR(255) DEFAULT NULL COMMENT '组名称',
  `create_date` DATETIME DEFAULT NULL COMMENT '创建时间',
  `remarks` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `group_type` INT(11) DEFAULT NULL COMMENT '1默认组',
  `catalogue_id` INT(11) DEFAULT NULL COMMENT '关联的业务服务id',
  `catalogue_name` VARCHAR(255) DEFAULT NULL COMMENT '关联的业务服务名称',
  `group_status` INT(11) DEFAULT '1' COMMENT '0禁止删除',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='工作组管理';

-- ----------------------------
-- Table structure for `itsm_duty_main_week`
-- ----------------------------
DROP TABLE IF EXISTS `itsm_duty_main_week`;
CREATE TABLE `itsm_duty_main_week` (
  `id` int(11) NOT NULL COMMENT '开始时间',
  `startTime` int(11) DEFAULT NULL,
  `overTime` int(11) DEFAULT NULL,
  `dutyId` int(255) DEFAULT NULL COMMENT '值班日期id',
  `typeUserId` int(11) DEFAULT NULL COMMENT '值班人id',
  `dutyTypeId` int(11) DEFAULT NULL COMMENT '值班班次id',
  `typeUserName` varchar(255) DEFAULT NULL COMMENT '值班人名称',
  `dutyState` int(10) unsigned DEFAULT NULL COMMENT '0正常1申请换班中4申请请假中',
  `typeName` varchar(255) DEFAULT NULL COMMENT '班次名称',
  `dutyDate` datetime DEFAULT NULL,
  `dutyName` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `arrangeState` int(11) DEFAULT NULL COMMENT '排班状态',
  `bigenDate` date DEFAULT NULL COMMENT '顺延开始时间',
  `endDate` date DEFAULT NULL COMMENT '顺延结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `itsm_file_path`;
CREATE TABLE `itsm_file_path` (
  `id` bigint(255) NOT NULL COMMENT 'id',
  `annexId` varchar(255) NOT NULL COMMENT '附件关联ID',
  `appendixType` int(11) DEFAULT NULL COMMENT '附件类型 1工单 2巡检',
  `filePath` varchar(255) DEFAULT NULL COMMENT '附件地址',
  `fileId` bigint(20) DEFAULT NULL COMMENT '附件ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_physical`;
CREATE TABLE `itsm_cmdb_physical` (
  `id` bigint(255) NOT NULL,
  `tasksId` int(125) NOT NULL COMMENT '任务id',
  `propertyId` varchar(125) NOT NULL COMMENT '资产id',
  `propertyState` int(10) NOT NULL COMMENT '资产状态 0-异常  1-正常',
  `site` varchar(255) NOT NULL COMMENT '二维码地址',
  `name` varchar(255) NOT NULL COMMENT '二维码名称 (资产名称)',
  `yardId` bigint(125) NOT NULL COMMENT '二维码id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `plat_org_domain_relation`;
CREATE TABLE `plat_org_domain_relation` (
  `organizationId` bigint(20) NOT NULL COMMENT '组织ID',
  `domainId` bigint(20) NOT NULL COMMENT '域ID',
  PRIMARY KEY (`organizationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `plat_user_domain_relation`;
CREATE TABLE `plat_user_domain_relation` (
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `domainId` bigint(20) NOT NULL COMMENT '域Id',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;







DROP TABLE IF EXISTS `itsm_cmdb_invent`;
CREATE TABLE `itsm_cmdb_invent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `taskName` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `state` int(11) DEFAULT NULL COMMENT '状态 0进行中 1待发布 2已完成',
  `should` varchar(255) DEFAULT NULL COMMENT '应盘',
  `normal` int(11) DEFAULT NULL COMMENT '正常 0否 1是',
  `takeTime` datetime DEFAULT NULL COMMENT '盘点时间',
  `userId` int(11) DEFAULT NULL COMMENT '盘点人id',
  `inventoryPeople` varchar(255) DEFAULT NULL COMMENT '盘点人',
  `inventoryRepor` varchar(255) DEFAULT NULL COMMENT '盘点报告',
  `inventoryResults` varchar(255) DEFAULT NULL COMMENT '盘点结果',
  `typeId` varchar(125) DEFAULT NULL COMMENT '类型id',
  `locationId` varchar(125) DEFAULT NULL COMMENT '位置id',
  `assignId` text DEFAULT NULL COMMENT '指定id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `itsm_alarm_rules`;

CREATE TABLE `itsm_alarm_rules` (
  `id` bigint(20) NOT NULL COMMENT '告警规则编号',
  `state` int(11) NOT NULL COMMENT '告警状态（0停用，1启用）',
  `appKey` varchar(32) NOT NULL COMMENT 'appKey',
  `alarm_module` varchar(50) NOT NULL COMMENT '告警模块编号',
  `alarm_type` varchar(20) NOT NULL COMMENT '告警类型编号',
  `alarm_level` varchar(20) NOT NULL COMMENT '告警级别编号',
  `ip_range` varchar(200) NOT NULL COMMENT '告警IP范围',
  `order_level` varchar(20) NOT NULL COMMENT '工单级别',
  `service_catalog_id` bigint(20) NOT NULL COMMENT '服务目录编号',
  `service_service_id` bigint(20) NOT NULL COMMENT '服务编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `act_evt_log`;

CREATE TABLE `act_evt_log` (
  `LOG_NR_` bigint(20) NOT NULL AUTO_INCREMENT,
  `TYPE_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TIME_STAMP_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DATA_` longblob,
  `LOCK_OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
  `IS_PROCESSED_` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`LOG_NR_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DROP TABLE IF EXISTS `itsm_cmdb_asset`;
CREATE TABLE `itsm_cmdb_asset` (
  `id` int(11) NOT NULL,
  `_name` varchar(765) DEFAULT NULL,
  `_code` varchar(765) DEFAULT NULL,
  `icon` varchar(765) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  `isPreset` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `itsm_asset_borrow`;
CREATE TABLE `itsm_asset_borrow` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `borrowReturnNumber` varchar(125) NOT NULL COMMENT '借用归还单号',
  `describe` varchar(255) DEFAULT NULL COMMENT '描述',
  `borrowReturnTime` datetime NOT NULL COMMENT '借用归还时间',
  `borrowUnit` int(11) DEFAULT NULL COMMENT '借用归还单位',
  `returnResult` int(11) DEFAULT NULL COMMENT '借用归还结果',
  `phoneWay` varchar(20) DEFAULT NULL COMMENT '联系人',
  `borrowPeople` varchar(11) DEFAULT NULL COMMENT '借用人',
  `itemIds` varchar(255) DEFAULT NULL COMMENT '资产IDs',
  `processKey` varchar(125) DEFAULT NULL COMMENT '流程定义key',
  `assetNumber` bigint(20) DEFAULT NULL COMMENT '资产数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_asset_outofstorage`;
CREATE TABLE `itsm_asset_outofstorage` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `describe` varchar(255) NOT NULL COMMENT '描述',
  `createTime` datetime DEFAULT NULL COMMENT '时间',
  `assetStatus` int(11) DEFAULT NULL COMMENT '资产状态0出库 1入库',
  `relatedPerson` varchar(125) DEFAULT NULL COMMENT '相关人',
  `assetsSource` int(11) DEFAULT NULL COMMENT '资产来源 0采购 1外界',
  `assetsNumber` bigint(20) DEFAULT NULL COMMENT '资产数量',
  `itemIds` varchar(255) NOT NULL COMMENT '资产ID',
  `processKey` varchar(125) NOT NULL COMMENT '流程定义KEY',
  `warehouseNumber` varchar(255) NOT NULL COMMENT '出入库单号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_asset_repair`;
CREATE TABLE `itsm_asset_repair` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `repairNumber` varchar(125) NOT NULL COMMENT '维修单号',
  `describe` varchar(255) DEFAULT NULL COMMENT '描述',
  `repairTime` datetime DEFAULT NULL COMMENT '维修时间',
  `repairUnit` varchar(125) DEFAULT NULL COMMENT '维修单位',
  `repairResult` int(11) DEFAULT NULL COMMENT '维修结果 0维修中 1 维修完毕',
  `processKey` varchar(125) DEFAULT NULL COMMENT '流程定义key',
  `itemIds` varchar(255) DEFAULT NULL COMMENT '资产ids',
  `assetNumber` bigint(20) DEFAULT NULL COMMENT '资产数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `itsm_asset_scrap`;
CREATE TABLE `itsm_asset_scrap` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `scrapNumber` varchar(255) DEFAULT NULL COMMENT '报废单号',
  `describe` varchar(255) DEFAULT NULL COMMENT '描述',
  `scrapTime` datetime DEFAULT NULL COMMENT '报废时间',
  `assetNumber` bigint(20) DEFAULT NULL COMMENT '资产数量',
  `itemIds` varchar(255) DEFAULT NULL COMMENT '资产Ids',
  `processKey` varchar(125) DEFAULT NULL COMMENT '流程定义key',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `itsm_asset_use`;
CREATE TABLE `itsm_asset_use` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `recRetNumber` varchar(125) NOT NULL COMMENT '领用归还单号',
  `describe` varchar(255) DEFAULT NULL COMMENT '描述',
  `useTime` datetime DEFAULT NULL COMMENT '领用时间',
  `leader` varchar(125) DEFAULT NULL COMMENT '领用人',
  `status` int(11) DEFAULT NULL COMMENT '0领用 1归还',
  `assetNumber` bigint(20) DEFAULT NULL COMMENT '资产数量',
  `leaderId` bigint(20) DEFAULT NULL COMMENT '领用人id',
  `leaderTheDep` varchar(125) DEFAULT NULL COMMENT '领用人所属部门',
  `itemIds` varchar(255) DEFAULT NULL COMMENT '资产id',
  `processKey` varchar(125) DEFAULT NULL COMMENT '流程定义key',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `itsm_asset_record`;
CREATE TABLE `itsm_asset_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `assetOperationId` bigint(20) NOT NULL COMMENT '资产操作ID',
  `assetOperationType` varchar(125) DEFAULT NULL COMMENT '资产操作类型',
  `assetOperationPeople` varchar(125) DEFAULT NULL COMMENT '资产操作人',
  `assetItemId` bigint(20) DEFAULT NULL COMMENT '资产id',
  `assetLeader` varchar(125) DEFAULT NULL COMMENT '资产领用人',
  `assetUnit` varchar(125) DEFAULT NULL COMMENT '资产部门',
  `assetHead` varchar(125) DEFAULT NULL COMMENT '资产负责人',
  `assetType` varchar(125) DEFAULT NULL COMMENT '资产类型',
  `assetNumber` varchar(255) DEFAULT NULL COMMENT '资产编码',
  `assetName` varchar(125) DEFAULT NULL COMMENT '资产名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `itsm_event_evaluate`;
CREATE TABLE `itsm_event_evaluate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `score` int(11) NOT NULL,
  `evaluate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Table structure for table `act_ge_bytearray` */

DROP TABLE IF EXISTS `act_ge_bytearray`;

CREATE TABLE `act_ge_bytearray` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BYTES_` longblob,
  `GENERATED_` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_FK_BYTEARR_DEPL` (`DEPLOYMENT_ID_`),
  CONSTRAINT `ACT_FK_BYTEARR_DEPL` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_ge_property` */

DROP TABLE IF EXISTS `act_ge_property`;

CREATE TABLE `act_ge_property` (
  `NAME_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `VALUE_` varchar(300) COLLATE utf8_bin DEFAULT NULL,
  `REV_` int(11) DEFAULT NULL,
  PRIMARY KEY (`NAME_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_hi_actinst` */

DROP TABLE IF EXISTS `act_hi_actinst`;

CREATE TABLE `act_hi_actinst` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `ACT_ID_` varchar(255) COLLATE utf8_bin NOT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `CALL_PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACT_NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ACT_TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `ASSIGNEE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `END_TIME_` datetime(3) DEFAULT NULL,
  `DURATION_` bigint(20) DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_HI_ACT_INST_START` (`START_TIME_`),
  KEY `ACT_IDX_HI_ACT_INST_END` (`END_TIME_`),
  KEY `ACT_IDX_HI_ACT_INST_PROCINST` (`PROC_INST_ID_`,`ACT_ID_`),
  KEY `ACT_IDX_HI_ACT_INST_EXEC` (`EXECUTION_ID_`,`ACT_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_hi_attachment` */

DROP TABLE IF EXISTS `act_hi_attachment`;

CREATE TABLE `act_hi_attachment` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `URL_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `CONTENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TIME_` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_hi_comment` */

DROP TABLE IF EXISTS `act_hi_comment`;

CREATE TABLE `act_hi_comment` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TIME_` datetime(3) NOT NULL,
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACTION_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `MESSAGE_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `FULL_MSG_` longblob,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_hi_detail` */

DROP TABLE IF EXISTS `act_hi_detail`;

CREATE TABLE `act_hi_detail` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACT_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TIME_` datetime(3) NOT NULL,
  `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DOUBLE_` double DEFAULT NULL,
  `LONG_` bigint(20) DEFAULT NULL,
  `TEXT_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TEXT2_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_HI_DETAIL_PROC_INST` (`PROC_INST_ID_`),
  KEY `ACT_IDX_HI_DETAIL_ACT_INST` (`ACT_INST_ID_`),
  KEY `ACT_IDX_HI_DETAIL_TIME` (`TIME_`),
  KEY `ACT_IDX_HI_DETAIL_NAME` (`NAME_`),
  KEY `ACT_IDX_HI_DETAIL_TASK_ID` (`TASK_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_hi_identitylink` */

DROP TABLE IF EXISTS `act_hi_identitylink`;

CREATE TABLE `act_hi_identitylink` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `GROUP_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_HI_IDENT_LNK_USER` (`USER_ID_`),
  KEY `ACT_IDX_HI_IDENT_LNK_TASK` (`TASK_ID_`),
  KEY `ACT_IDX_HI_IDENT_LNK_PROCINST` (`PROC_INST_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_hi_procinst` */

DROP TABLE IF EXISTS `act_hi_procinst`;

CREATE TABLE `act_hi_procinst` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `BUSINESS_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `END_TIME_` datetime(3) DEFAULT NULL,
  `DURATION_` bigint(20) DEFAULT NULL,
  `START_USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `START_ACT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `END_ACT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `SUPER_PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  UNIQUE KEY `PROC_INST_ID_` (`PROC_INST_ID_`),
  KEY `ACT_IDX_HI_PRO_INST_END` (`END_TIME_`),
  KEY `ACT_IDX_HI_PRO_I_BUSKEY` (`BUSINESS_KEY_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_hi_taskinst` */

DROP TABLE IF EXISTS `act_hi_taskinst`;

CREATE TABLE `act_hi_taskinst` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_DEF_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ASSIGNEE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `CLAIM_TIME_` datetime(3) DEFAULT NULL,
  `END_TIME_` datetime(3) DEFAULT NULL,
  `DURATION_` bigint(20) DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `PRIORITY_` int(11) DEFAULT NULL,
  `DUE_DATE_` datetime(3) DEFAULT NULL,
  `FORM_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_HI_TASK_INST_PROCINST` (`PROC_INST_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_hi_varinst` */

DROP TABLE IF EXISTS `act_hi_varinst`;

CREATE TABLE `act_hi_varinst` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `REV_` int(11) DEFAULT NULL,
  `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DOUBLE_` double DEFAULT NULL,
  `LONG_` bigint(20) DEFAULT NULL,
  `TEXT_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TEXT2_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_TIME_` datetime(3) DEFAULT NULL,
  `LAST_UPDATED_TIME_` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_HI_PROCVAR_PROC_INST` (`PROC_INST_ID_`),
  KEY `ACT_IDX_HI_PROCVAR_NAME_TYPE` (`NAME_`,`VAR_TYPE_`),
  KEY `ACT_IDX_HI_PROCVAR_TASK_ID` (`TASK_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_id_group` */

DROP TABLE IF EXISTS `act_id_group`;

CREATE TABLE `act_id_group` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_id_info` */

DROP TABLE IF EXISTS `act_id_info`;

CREATE TABLE `act_id_info` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `USER_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `VALUE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PASSWORD_` longblob,
  `PARENT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_id_membership` */

DROP TABLE IF EXISTS `act_id_membership`;

CREATE TABLE `act_id_membership` (
  `USER_ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `GROUP_ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`USER_ID_`,`GROUP_ID_`),
  KEY `ACT_FK_MEMB_GROUP` (`GROUP_ID_`),
  CONSTRAINT `ACT_FK_MEMB_GROUP` FOREIGN KEY (`GROUP_ID_`) REFERENCES `act_id_group` (`ID_`),
  CONSTRAINT `ACT_FK_MEMB_USER` FOREIGN KEY (`USER_ID_`) REFERENCES `act_id_user` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_id_user` */

DROP TABLE IF EXISTS `act_id_user`;

CREATE TABLE `act_id_user` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `FIRST_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `LAST_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EMAIL_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PWD_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PICTURE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_procdef_info` */

DROP TABLE IF EXISTS `act_procdef_info`;

CREATE TABLE `act_procdef_info` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `INFO_JSON_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  UNIQUE KEY `ACT_UNIQ_INFO_PROCDEF` (`PROC_DEF_ID_`),
  KEY `ACT_IDX_INFO_PROCDEF` (`PROC_DEF_ID_`),
  KEY `ACT_FK_INFO_JSON_BA` (`INFO_JSON_ID_`),
  CONSTRAINT `ACT_FK_INFO_JSON_BA` FOREIGN KEY (`INFO_JSON_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
  CONSTRAINT `ACT_FK_INFO_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_re_deployment` */

DROP TABLE IF EXISTS `act_re_deployment`;

CREATE TABLE `act_re_deployment` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `DEPLOY_TIME_` timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_re_model` */

DROP TABLE IF EXISTS `act_re_model`;

CREATE TABLE `act_re_model` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LAST_UPDATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `VERSION_` int(11) DEFAULT NULL,
  `META_INFO_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EDITOR_SOURCE_VALUE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EDITOR_SOURCE_EXTRA_VALUE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`),
  KEY `ACT_FK_MODEL_SOURCE` (`EDITOR_SOURCE_VALUE_ID_`),
  KEY `ACT_FK_MODEL_SOURCE_EXTRA` (`EDITOR_SOURCE_EXTRA_VALUE_ID_`),
  KEY `ACT_FK_MODEL_DEPLOYMENT` (`DEPLOYMENT_ID_`),
  CONSTRAINT `ACT_FK_MODEL_DEPLOYMENT` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`),
  CONSTRAINT `ACT_FK_MODEL_SOURCE` FOREIGN KEY (`EDITOR_SOURCE_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
  CONSTRAINT `ACT_FK_MODEL_SOURCE_EXTRA` FOREIGN KEY (`EDITOR_SOURCE_EXTRA_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_re_procdef` */

DROP TABLE IF EXISTS `act_re_procdef`;

CREATE TABLE `act_re_procdef` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin NOT NULL,
  `VERSION_` int(11) NOT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `RESOURCE_NAME_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DGRM_RESOURCE_NAME_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `HAS_START_FORM_KEY_` tinyint(4) DEFAULT NULL,
  `HAS_GRAPHICAL_NOTATION_` tinyint(4) DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`),
  UNIQUE KEY `ACT_UNIQ_PROCDEF` (`KEY_`,`VERSION_`,`TENANT_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_ru_event_subscr` */

DROP TABLE IF EXISTS `act_ru_event_subscr`;

CREATE TABLE `act_ru_event_subscr` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `EVENT_TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `EVENT_NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACTIVITY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `CONFIGURATION_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_EVENT_SUBSCR_CONFIG_` (`CONFIGURATION_`),
  KEY `ACT_FK_EVENT_EXEC` (`EXECUTION_ID_`),
  CONSTRAINT `ACT_FK_EVENT_EXEC` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_ru_execution` */

DROP TABLE IF EXISTS `act_ru_execution`;

CREATE TABLE `act_ru_execution` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BUSINESS_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `SUPER_EXEC_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `IS_ACTIVE_` tinyint(4) DEFAULT NULL,
  `IS_CONCURRENT_` tinyint(4) DEFAULT NULL,
  `IS_SCOPE_` tinyint(4) DEFAULT NULL,
  `IS_EVENT_SCOPE_` tinyint(4) DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) DEFAULT NULL,
  `CACHED_ENT_STATE_` int(11) DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_EXEC_BUSKEY` (`BUSINESS_KEY_`),
  KEY `ACT_FK_EXE_PROCINST` (`PROC_INST_ID_`),
  KEY `ACT_FK_EXE_PARENT` (`PARENT_ID_`),
  KEY `ACT_FK_EXE_SUPER` (`SUPER_EXEC_`),
  KEY `ACT_FK_EXE_PROCDEF` (`PROC_DEF_ID_`),
  CONSTRAINT `ACT_FK_EXE_PARENT` FOREIGN KEY (`PARENT_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_EXE_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
  CONSTRAINT `ACT_FK_EXE_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ACT_FK_EXE_SUPER` FOREIGN KEY (`SUPER_EXEC_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_ru_identitylink` */

DROP TABLE IF EXISTS `act_ru_identitylink`;

CREATE TABLE `act_ru_identitylink` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `GROUP_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_IDENT_LNK_USER` (`USER_ID_`),
  KEY `ACT_IDX_IDENT_LNK_GROUP` (`GROUP_ID_`),
  KEY `ACT_IDX_ATHRZ_PROCEDEF` (`PROC_DEF_ID_`),
  KEY `ACT_FK_TSKASS_TASK` (`TASK_ID_`),
  KEY `ACT_FK_IDL_PROCINST` (`PROC_INST_ID_`),
  CONSTRAINT `ACT_FK_ATHRZ_PROCEDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
  CONSTRAINT `ACT_FK_IDL_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_TSKASS_TASK` FOREIGN KEY (`TASK_ID_`) REFERENCES `act_ru_task` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_ru_job` */

DROP TABLE IF EXISTS `act_ru_job`;

CREATE TABLE `act_ru_job` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LOCK_OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EXCLUSIVE_` tinyint(1) DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `RETRIES_` int(11) DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`),
  KEY `ACT_FK_JOB_EXCEPTION` (`EXCEPTION_STACK_ID_`),
  CONSTRAINT `ACT_FK_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `act_ge_bytearray` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_ru_task` */

DROP TABLE IF EXISTS `act_ru_task`;

CREATE TABLE `act_ru_task` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TASK_DEF_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ASSIGNEE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DELEGATION_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PRIORITY_` int(11) DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `DUE_DATE_` datetime(3) DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `FORM_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_TASK_CREATE` (`CREATE_TIME_`),
  KEY `ACT_FK_TASK_EXE` (`EXECUTION_ID_`),
  KEY `ACT_FK_TASK_PROCINST` (`PROC_INST_ID_`),
  KEY `ACT_FK_TASK_PROCDEF` (`PROC_DEF_ID_`),
  CONSTRAINT `ACT_FK_TASK_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_TASK_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
  CONSTRAINT `ACT_FK_TASK_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `act_ru_variable` */

DROP TABLE IF EXISTS `act_ru_variable`;

CREATE TABLE `act_ru_variable` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DOUBLE_` double DEFAULT NULL,
  `LONG_` bigint(20) DEFAULT NULL,
  `TEXT_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TEXT2_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_VARIABLE_TASK_ID` (`TASK_ID_`),
  KEY `ACT_FK_VAR_EXE` (`EXECUTION_ID_`),
  KEY `ACT_FK_VAR_PROCINST` (`PROC_INST_ID_`),
  KEY `ACT_FK_VAR_BYTEARRAY` (`BYTEARRAY_ID_`),
  CONSTRAINT `ACT_FK_VAR_BYTEARRAY` FOREIGN KEY (`BYTEARRAY_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
  CONSTRAINT `ACT_FK_VAR_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_VAR_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `itsm_apple_user_type` */

DROP TABLE IF EXISTS `itsm_apple_user_type`;

CREATE TABLE `itsm_apple_user_type` (
  `id` int(11) DEFAULT NULL,
  `apply_user_type` varchar(255) DEFAULT NULL COMMENT '申请人所属类别（客户所属类别）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_applicant` */

DROP TABLE IF EXISTS `itsm_applicant`;

CREATE TABLE `itsm_applicant` (
  `applicant_id` varchar(50) NOT NULL,
  `source` tinyint(1) DEFAULT NULL COMMENT '来源',
  `customer_type` tinyint(1) DEFAULT NULL COMMENT '客户类型',
  `report_user` varchar(50) NOT NULL COMMENT '申报人',
  `contact_phone` varchar(20) NOT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `contact_user` varchar(50) DEFAULT NULL COMMENT '客户联系人',
  `customer_phone` varchar(20) DEFAULT NULL COMMENT '客户电话',
  `customer_email` varchar(50) DEFAULT NULL COMMENT '客户邮箱',
  `regional_location` varchar(50) DEFAULT NULL COMMENT '区域位置',
  `scheduled_maintenance_time` datetime DEFAULT NULL COMMENT '预约维修时间',
  `eventmgr_id` varchar(50) DEFAULT NULL COMMENT '报障表主键',
  PRIMARY KEY (`applicant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_cimanage` */

DROP TABLE IF EXISTS `itsm_cimanage`;

CREATE TABLE `itsm_cimanage` (
  `CIManageId` varchar(50) NOT NULL,
  `serialNumber` varchar(255) DEFAULT NULL,
  `modelNumber` varchar(255) DEFAULT NULL COMMENT '型号',
  `assetState` varchar(20) DEFAULT NULL,
  `responsiblePerson` varchar(255) DEFAULT NULL,
  `equipmentSource` varchar(255) DEFAULT NULL,
  `configCoding` varchar(255) DEFAULT NULL,
  `configName` varchar(255) DEFAULT NULL,
  `use` varchar(255) DEFAULT NULL,
  `machineName` varchar(255) DEFAULT NULL,
  `subordinateUnits` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `configType` varchar(255) DEFAULT NULL,
  `ipAddress` varchar(255) DEFAULT NULL,
  `dormitoryPosition` varchar(255) DEFAULT NULL,
  `configRemark` varchar(255) DEFAULT NULL,
  `assetNumber` varchar(255) DEFAULT NULL,
  `contractNo` varchar(255) DEFAULT NULL,
  `trademarkNo` varchar(255) DEFAULT NULL,
  `salveAfterService` varchar(255) DEFAULT NULL,
  `depositSite` varchar(255) DEFAULT NULL,
  `transportDate` datetime DEFAULT NULL,
  `usePeople` varchar(255) DEFAULT NULL,
  `useDepartment` varchar(255) DEFAULT NULL,
  `maintenanceBeginTime` datetime DEFAULT NULL,
  `maintenanceEndTime` datetime DEFAULT NULL,
  `maintenanceContract` varchar(255) DEFAULT NULL,
  `trademark` varchar(255) DEFAULT NULL,
  `assetBelong` varchar(255) DEFAULT NULL,
  `purchaseContractNo` varchar(255) DEFAULT NULL,
  `maintainer` varchar(255) DEFAULT NULL,
  `useUnit` varchar(255) DEFAULT NULL,
  `userPeople` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_cmdb_attribute` */

DROP TABLE IF EXISTS `itsm_cmdb_attribute`;
CREATE TABLE `itsm_cmdb_attribute` (
  `attrKey` int(11) NOT NULL AUTO_INCREMENT COMMENT '属性ID',
  `attrName` varchar(50) DEFAULT NULL COMMENT '属性名称',
  `attrType` int(11) DEFAULT NULL COMMENT '属性类型1文本类型2日期类型',
  `isVisable` int(2) unsigned DEFAULT NULL COMMENT '是否在列表中显示0不显示1显示',
  `attrCode` varchar(100) DEFAULT NULL COMMENT '属性编码',
  `attrDataSource` varchar(100) DEFAULT NULL COMMENT '属性数据源',
  `attrDataSourceType` varchar(100) DEFAULT NULL COMMENT '属性数据源类型',
  PRIMARY KEY (`attrKey`),
  UNIQUE KEY `attrKey` (`attrKey`),
  UNIQUE KEY `attrCode` (`attrCode`)
) ENGINE=InnoDB AUTO_INCREMENT=878618 DEFAULT CHARSET=utf8 COMMENT='配置项属性';

/*Table structure for table `itsm_cmdb_catalog` */

DROP TABLE IF EXISTS `itsm_cmdb_catalog`;

CREATE TABLE `itsm_cmdb_catalog` (
  `catalogId` int(11) NOT NULL COMMENT '分类ID',
  `catalogName` varchar(200) DEFAULT NULL COMMENT '分类名称',
  `parentId` int(11) DEFAULT NULL COMMENT '上级分类',
  `attributePBIds` varchar(255) DEFAULT NULL COMMENT '该分类下的所有继承的基本属性ID',
  `attributeMBIds` varchar(255) DEFAULT NULL COMMENT '该分类下所有的新建基本属性(可修改)',
  `attributePPIds` varchar(255) DEFAULT NULL COMMENT '该分类下的所有继承私有属性的ID',
  `attributeMPIds` varchar(255) DEFAULT NULL COMMENT '该分类下所有新建私有属性(可修改)',
  `code` varchar(50) DEFAULT NULL COMMENT '分类代码',
  PRIMARY KEY (`catalogId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置分类';

/*Table structure for table `itsm_cmdb_catalog_attr` */

DROP TABLE IF EXISTS `itsm_cmdb_catalog_attr`;

CREATE TABLE `itsm_cmdb_catalog_attr` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `catalogId` int(11) NOT NULL COMMENT '分类表ID外键',
  `attrKey` int(11) NOT NULL COMMENT '属性表ID外键',
  PRIMARY KEY (`id`),
  KEY `FK_CATALOG` (`catalogId`),
  KEY `FK_ATTRIBUTE` (`attrKey`),
  CONSTRAINT `FK_CATALOG` FOREIGN KEY (`catalogId`) REFERENCES `itsm_cmdb_catalog` (`catalogId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='目录属性关联';

/*Table structure for table `itsm_cmdb_contract` */

DROP TABLE IF EXISTS `itsm_cmdb_contract`;

CREATE TABLE `itsm_cmdb_contract` (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `name` varchar(255) DEFAULT NULL COMMENT '合同名称',
  `po_number` varchar(255) DEFAULT NULL COMMENT 'po号',
  `purchaser` varchar(255) DEFAULT NULL COMMENT '采购人',
  `contract_date` datetime DEFAULT NULL COMMENT '签约日期',
  `effective_date` datetime DEFAULT NULL COMMENT '生效日期',
  `expiry_date` datetime DEFAULT NULL COMMENT '失效日期',
  `amount` int(11) DEFAULT NULL COMMENT '总金额',
  `expiration_date` datetime DEFAULT NULL COMMENT '质保截止日期',
  `reminder_date` int(11) DEFAULT NULL COMMENT '质保提醒时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '合同备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合同信息表';

/*Table structure for table `itsm_cmdb_contract_supplier_rel` */

DROP TABLE IF EXISTS `itsm_cmdb_contract_supplier_rel`;

CREATE TABLE `itsm_cmdb_contract_supplier_rel` (
  `id` int(11) NOT NULL,
  `contract_id` int(11) DEFAULT NULL COMMENT '合同id',
  `supplier_id` int(11) DEFAULT NULL COMMENT '供应商id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合同-供应商-关联关系表';

/*Table structure for table `itsm_cmdb_delivery` */

DROP TABLE IF EXISTS `itsm_cmdb_delivery`;

CREATE TABLE `itsm_cmdb_delivery` (
  `id` int(11) NOT NULL COMMENT '主键',
  `out_no` varchar(255) DEFAULT NULL COMMENT '出库单号',
  `out_name` varchar(255) DEFAULT NULL,
  `out_date` datetime DEFAULT NULL COMMENT '出库时间',
  `type` int(20) DEFAULT NULL COMMENT '出库类型1暂借2出库3归还',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作人',
  `time_limit` int(11) DEFAULT NULL COMMENT '出库期限7一星期30一个月90一季度',
  `warehouse_id` int(11) DEFAULT NULL,
  `contract_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='出库记录表';

/*Table structure for table `itsm_cmdb_delivery_return` */

DROP TABLE IF EXISTS `itsm_cmdb_delivery_return`;

CREATE TABLE `itsm_cmdb_delivery_return` (
  `id` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL COMMENT '1归还2延期',
  `time_limit` int(11) DEFAULT NULL COMMENT '延期时长(单位：天)',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_cmdb_delivery_warehouse_rel` */

DROP TABLE IF EXISTS `itsm_cmdb_delivery_warehouse_rel`;

CREATE TABLE `itsm_cmdb_delivery_warehouse_rel` (
  `id` int(11) NOT NULL COMMENT '主键',
  `warehouse_rel_id` int(11) DEFAULT NULL COMMENT '出库设备ID',
  `delivery_id` int(11) DEFAULT NULL COMMENT '出库表ID',
  `number` int(20) DEFAULT NULL COMMENT '出库数量',
  `equipment_name` varchar(255) DEFAULT NULL COMMENT '设备名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_cmdb_equipment` */

DROP TABLE IF EXISTS `itsm_cmdb_equipment`;

CREATE TABLE `itsm_cmdb_equipment` (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `name` varchar(255) DEFAULT NULL COMMENT '设备名称',
  `model` varchar(255) DEFAULT NULL COMMENT '设备型号',
  `number` int(20) DEFAULT NULL COMMENT '合同数量',
  `price` double(10,2) DEFAULT NULL COMMENT '设备单价',
  `supplier_id` int(11) DEFAULT NULL COMMENT '供应商id',
  `catalog_id` int(11) DEFAULT NULL COMMENT '库存分类ID',
  `contract_id` int(11) DEFAULT NULL COMMENT '合同ID',
  `remain_number` int(11) DEFAULT NULL COMMENT '入库之后剩余数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='库存设备表';

/*Table structure for table `itsm_cmdb_inventory` */

DROP TABLE IF EXISTS `itsm_cmdb_inventory`;

CREATE TABLE `itsm_cmdb_inventory` (
  `id` int(11) NOT NULL COMMENT '库存主键ID',
  `entry_no` varchar(255) DEFAULT NULL COMMENT '入库单号',
  `entry_name` varchar(255) DEFAULT NULL COMMENT '入库名称',
  `entry_date` datetime DEFAULT NULL COMMENT '入库时间',
  `status` int(11) DEFAULT NULL COMMENT '库存状态0备用1维护',
  `operater` varchar(255) DEFAULT NULL COMMENT '操作人',
  `warehouse_id` int(11) DEFAULT NULL COMMENT '所属仓库id',
  `contract_id` int(11) DEFAULT NULL COMMENT '关联合同ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='入库记录表';

/*Table structure for table `itsm_cmdb_inventory_warehouse_rel` */

DROP TABLE IF EXISTS `itsm_cmdb_inventory_warehouse_rel`;
CREATE TABLE `itsm_cmdb_inventory_warehouse_rel` (
  `id` int(11) NOT NULL COMMENT '主键',
  `warehouse_rel_id` int(11) DEFAULT NULL COMMENT '库存设备关联表ID',
  `inventory_id` int(11) DEFAULT NULL COMMENT '入库记录ID',
  `number` int(20) DEFAULT NULL COMMENT '记录数量',
  `equipment_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_cmdb_data_auditing` */

DROP TABLE IF EXISTS `itsm_cmdb_data_auditing`;
CREATE TABLE `itsm_cmdb_data_auditing` (
  `id` BIGINT(20) NOT NULL COMMENT '设备主键',
  `name` VARCHAR(100) COMMENT '设备名称',
  `belong_type` VARCHAR(20) COMMENT '所属类型',
  `ip` VARCHAR(20) COMMENT 'IP地址',
  `change_date` DATETIME COMMENT '变更时间',
  `item_content` TEXT COMMENT '变更内容',
   is_del INT(10) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='数据同步待审表';

/*Table structure for table `itsm_cmdb_data_audited` */

DROP TABLE IF EXISTS `itsm_cmdb_data_audited`;
CREATE TABLE `itsm_cmdb_data_audited` (
  `id` bigint(20) primary key COMMENT '设备主键',
  `name` varchar(100) COMMENT '设备名称',
  `belong_type` VARCHAR(20) NOT NULL COMMENT '所属类型',
  `ip` varchar(20) COMMENT 'IP地址',
  `change_date` datetime COMMENT '变更时间',
  `item_content` TEXT COMMENT '变更内容',
  is_del int(10) default 0,
  checkId bigint(20) COMMENT '删除前id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据同步已审表';

/*Table structure for table `itsm_cmdb_item` */

DROP TABLE IF EXISTS `itsm_cmdb_item`;
CREATE TABLE `itsm_cmdb_item` (
  `id` bigint(20) NOT NULL COMMENT 'CI项编号主键',
  `ci_number` varchar(100) NOT NULL COMMENT '配置项编号',
  `name` varchar(100) NOT NULL COMMENT '资产名称',
  `organization` varchar(200) NOT NULL COMMENT '该ci项所属组织id',
  `description` varchar(200) DEFAULT NULL COMMENT '资产描述',
  `model_id` bigint(20) NOT NULL COMMENT '资产模型',
  `category_2` bigint(20) unsigned NOT NULL COMMENT '模型二级分类',
  `category_3` bigint(20) NOT NULL COMMENT '模型三级分类',
  `is_del` int(2) NOT NULL COMMENT '逻辑删除0删除，1未删除',
  `create_Date` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `FK_ITEM_CATALOG` (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='cmdb属性';

/*Table structure for table `itsm_cmdb_item_attr` */

DROP TABLE IF EXISTS `itsm_cmdb_item_attr`;
CREATE TABLE `itsm_cmdb_item_attr` (
  `itemId` int(11) NOT NULL,
  `attrKey` int(11) NOT NULL,
  `attrValue` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`itemId`,`attrKey`),
  KEY `FK_IA_ITEM` (`itemId`),
  KEY `FK_IA_ATTRIBUTE` (`attrKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项属性';

/*Table structure for table `itsm_cmdb_supplier` */

DROP TABLE IF EXISTS `itsm_cmdb_supplier`;

CREATE TABLE `itsm_cmdb_supplier` (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `name` varchar(255) DEFAULT NULL COMMENT '供应商名称',
  `level` int(15) DEFAULT NULL COMMENT '供应商级别0高1中2低',
  `address` varchar(255) DEFAULT NULL COMMENT '供应商地址',
  `website` varchar(255) DEFAULT NULL COMMENT '供应商网址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='供应商表';

/*Table structure for table `itsm_cmdb_supplier_catalog_rel` */

DROP TABLE IF EXISTS `itsm_cmdb_supplier_catalog_rel`;

CREATE TABLE `itsm_cmdb_supplier_catalog_rel` (
  `id` int(11) NOT NULL COMMENT '供应商类型id',
  `supplier_id` int(11) DEFAULT NULL COMMENT '供应商ID',
  `catalog_id` int(11) DEFAULT NULL COMMENT '分类ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='供应商-资产分类-关联关系表';

/*Table structure for table `itsm_cmdb_supplier_connect` */

DROP TABLE IF EXISTS `itsm_cmdb_supplier_connect`;

CREATE TABLE `itsm_cmdb_supplier_connect` (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `connect_name` varchar(255) DEFAULT NULL COMMENT '联系人姓名',
  `connect_tel` varchar(255) DEFAULT NULL COMMENT '联系人电话',
  `connect_email` varchar(255) DEFAULT NULL COMMENT '联系人邮箱',
  `connect_wechat` varchar(255) DEFAULT NULL COMMENT '联系人微信',
  `connect_qq` varchar(255) DEFAULT NULL COMMENT '联系人qq',
  `supplier_id` int(11) DEFAULT NULL COMMENT '供应商ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='供应商联系人-销售经理表';

/*Table structure for table `itsm_cmdb_warehouse` */

DROP TABLE IF EXISTS `itsm_cmdb_warehouse`;

CREATE TABLE `itsm_cmdb_warehouse` (
  `id` int(11) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '仓库名称',
  `area` varchar(255) DEFAULT NULL COMMENT '仓库所属区域',
  `address` varchar(255) DEFAULT NULL COMMENT '仓库地址',
  `manager` varchar(255) DEFAULT NULL COMMENT '仓库管理员',
  `connect_tel` varchar(255) DEFAULT NULL COMMENT '联系电话',
  `connect_weChat` varchar(255) DEFAULT NULL COMMENT '微信',
  `connect_QQ` varchar(255) DEFAULT NULL COMMENT 'QQ',
  `isdelete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0为否(正常状态) ;1为是(删除状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='仓库表';

/*Table structure for table `itsm_cmdb_warehouse_equipment_rel` */

DROP TABLE IF EXISTS `itsm_cmdb_warehouse_equipment_rel`;

CREATE TABLE `itsm_cmdb_warehouse_equipment_rel` (
  `id` int(11) NOT NULL COMMENT '主键',
  `warehouse_id` int(11) DEFAULT NULL COMMENT '仓库ID',
  `equipment_id` int(11) DEFAULT NULL COMMENT '设备ID',
  `equipment_name` varchar(255) DEFAULT NULL COMMENT '设备名称',
  `number` int(20) DEFAULT NULL COMMENT '库存数量',
  `inventory_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_comment_user` */

DROP TABLE IF EXISTS `itsm_comment_user`;

CREATE TABLE `itsm_comment_user` (
  `commentId` int(11) NOT NULL,
  `userId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`commentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_dictionary` */

DROP TABLE IF EXISTS `itsm_dictionary`;

CREATE TABLE `itsm_dictionary` (
  `id` int(10) unsigned NOT NULL COMMENT '主键',
  `pid` int(10) unsigned DEFAULT NULL COMMENT '上级ID',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_document` */

DROP TABLE IF EXISTS `itsm_document`;

CREATE TABLE `itsm_document` (
  `id` varchar(255) DEFAULT NULL,
  `documentName` varchar(255) DEFAULT NULL COMMENT '上传文档名',
  `uploadPeople` varchar(255) DEFAULT NULL COMMENT '上传人',
  `uploadTime` datetime DEFAULT NULL COMMENT '上传时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_duty_approve` */

DROP TABLE IF EXISTS `itsm_duty_approve`;

CREATE TABLE `itsm_duty_approve` (
  `id` int(11) NOT NULL COMMENT '主键',
  `dutyPeopleId` int(11) DEFAULT NULL COMMENT '申请人id',
  `dutyType` varchar(255) DEFAULT NULL COMMENT '申请类型 ，换班/请假',
  `dutyMainId` int(11) DEFAULT NULL COMMENT '申请主表ID',
  `dutyClassId` int(11) DEFAULT NULL COMMENT '申请班次id',
  `dutyState` int(11) DEFAULT '0' COMMENT '审核状态，0待审核，1已通过，2已驳回',
  `shiftMainId` int(11) DEFAULT NULL COMMENT '换班主表ID',
  `shiftClassId` int(11) DEFAULT NULL COMMENT '换班班次id',
  `shiftPeopleId` int(11) DEFAULT NULL COMMENT '换班人/顶班人ID',
  `reason` varchar(255) DEFAULT NULL COMMENT '申请原因',
  `dutyOpinion` varchar(255) DEFAULT NULL COMMENT '审批意见',
  `approveDate` datetime DEFAULT NULL COMMENT '申请时间',
  `noPassReason` varchar(255) DEFAULT NULL COMMENT '请假不同过原因',
  `dutyDate` datetime DEFAULT NULL COMMENT '值班日期',
  `shiftDate` datetime DEFAULT NULL COMMENT '换班日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='换班/请假审批表';

/*Table structure for table `itsm_duty_main` */

DROP TABLE IF EXISTS `itsm_duty_main`;

DROP TABLE IF EXISTS `itsm_duty_main`;
CREATE TABLE `itsm_duty_main` (
  `dutyId` int(11) NOT NULL COMMENT '值班日期表-主键',
  `dutyDate` date DEFAULT NULL COMMENT '值班日期(年月日)',
  `dutyStates` int(11) DEFAULT NULL COMMENT '状态 1-按周排班',
  `listId` bigint(11) DEFAULT NULL COMMENT '列表id',
  PRIMARY KEY (`dutyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='值班日期表'

/*Table structure for table `itsm_duty_main_user` */

DROP TABLE IF EXISTS `itsm_duty_main_user`;
CREATE TABLE `itsm_duty_main_user` (
  `id` int(11) NOT NULL COMMENT '值班人员表-主键',
  `dutyId` int(11) DEFAULT NULL COMMENT '值班日期表id',
  `typeUserId` int(11) DEFAULT NULL COMMENT '值班人id',
  `dutyTypeId` int(11) DEFAULT NULL COMMENT '值班班次类型id',
  `typeUserName` varchar(255) DEFAULT NULL COMMENT '值班人名称',
  `dutyState` int(11) NOT NULL DEFAULT '0' COMMENT '0正常1申请换班中4申请请假中',
  `startTime` int(11) DEFAULT NULL COMMENT '值班开始时间',
  `endTime` int(11) DEFAULT NULL COMMENT '值班结束时间',
  `typeName` varchar(255) DEFAULT NULL COMMENT '班次名称',
  `dutyDate` datetime DEFAULT NULL COMMENT '值班日期',
  `dutyName` varchar(255) DEFAULT NULL,
  `contactsId` bigint(20) DEFAULT NULL COMMENT '联系人id',
  `dutyStatus` tinyint(4) DEFAULT '0' COMMENT '0未开始  1上班中 2已下班 3迟到打卡',
  `onDutyDate` datetime DEFAULT NULL COMMENT '上班打卡时间',
  `offDutyDate` datetime DEFAULT NULL COMMENT '下班打卡时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='值班人员表'

/*Table structure for table `itsm_duty_type` */

DROP TABLE IF EXISTS `itsm_duty_type`;

CREATE TABLE `itsm_duty_type` (
  `typeId` int(11) NOT NULL COMMENT '班次类型-主键',
  `name` varchar(50) DEFAULT NULL COMMENT '班次类型名称',
  `startTime` varchar(50) DEFAULT NULL COMMENT '班次开始时间',
  `endTime` varchar(50) DEFAULT NULL COMMENT '班次结束时间',
  `phone` varchar(50) DEFAULT NULL COMMENT '班次联系电话',
  `acrossDay` int(11) DEFAULT NULL COMMENT '是否隔天0否1是',
  `domainId` bigint(20) DEFAULT NULL COMMENT '域ID',
  PRIMARY KEY (`typeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='值班班次';


/*Table structure for table `itsm_duty_type_record` */

DROP TABLE IF EXISTS `itsm_duty_type_record`;

CREATE TABLE `itsm_duty_type_record` (
  `dutyId` int(11) NOT NULL COMMENT '值班日期id',
  `typeId` int(11) DEFAULT NULL COMMENT '值班班次id',
  `name` varchar(255) DEFAULT NULL COMMENT '值班班次名称',
  `startTime` int(11) DEFAULT NULL COMMENT '值班开始时间',
  `endTime` int(11) DEFAULT NULL COMMENT '值班结束时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_duty_type_user` */

DROP TABLE IF EXISTS `itsm_duty_type_user`;

CREATE TABLE `itsm_duty_type_user` (
  `id` int(11) NOT NULL COMMENT '主键',
  `typeId` int(11) DEFAULT NULL COMMENT '班次ID',
  `userId` int(11) DEFAULT '0' COMMENT '班次人员ID',
  `userName` varchar(255) DEFAULT NULL COMMENT '班次人员姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='班次人员表';

/*Table structure for table `itsm_event_apply_user` */

DROP TABLE IF EXISTS `itsm_event_apply_user`;

CREATE TABLE `itsm_event_apply_user` (
  `id` int(11) unsigned NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '申请人名字',
  `email` varchar(255) DEFAULT NULL COMMENT '申请人邮箱',
  `mobile` varchar(255) DEFAULT NULL COMMENT '申请人联系电话',
  `type` int(11) unsigned DEFAULT NULL COMMENT '客户类型1个人2内部员工3教育局4环保局5其他',
  `contract_name` varchar(255) DEFAULT NULL COMMENT '联系人名字',
  `contract_email` varchar(255) DEFAULT NULL COMMENT '联系人邮箱',
  `contract_mobile` varchar(255) DEFAULT NULL COMMENT '联系人电话',
  `contract_qq` varchar(255) DEFAULT NULL COMMENT '联系人QQ',
  `contract_weChat` varchar(255) DEFAULT NULL COMMENT '联系人微信',
  `qq` varchar(255) DEFAULT NULL COMMENT '申请人QQ',
  `weChat` varchar(255) DEFAULT NULL COMMENT '申请人微信',
  `apply_post` varchar(255) DEFAULT NULL COMMENT '申请人职务',
  `customRankLong` int(20) unsigned DEFAULT NULL COMMENT '级别',
  `belongOrganization` bigint(20) unsigned DEFAULT NULL COMMENT '部门id',
  `belongDomains` bigint(20) unsigned DEFAULT NULL COMMENT '所属域id',
  `address` varchar(50) DEFAULT NULL COMMENT '地址',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_event_asset_ship` */


DROP TABLE IF EXISTS `itsm_event_asset_ship`;

CREATE TABLE `itsm_event_asset_ship` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `event_id` int(11) unsigned DEFAULT NULL COMMENT '关联工单的ID',
  `asset_item_id` int(11) unsigned DEFAULT NULL COMMENT '关联资产ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_service_level`;
CREATE TABLE `itsm_service_level` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '事件级别值',
  `levelValue` varchar(20) NOT NULL COMMENT '事件级别值',
  `serviceLevelValue` varchar(10) NOT NULL COMMENT '服务级别',
  `importantValue` varchar(10) DEFAULT '2' COMMENT '重要程度值',
  `urgencyValue` varchar(10) DEFAULT '2' COMMENT '紧急程度值',
  `slaWithTime` varchar(20) DEFAULT NULL COMMENT '响应时限',
  `slaSalveTime` varchar(20) DEFAULT NULL COMMENT '解决时限',
  `lastSetTime` datetime DEFAULT NULL COMMENT '最后一次设置时间',
  `description` varchar(255) DEFAULT NULL COMMENT '服务级别描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_event_attachment` */

DROP TABLE IF EXISTS `itsm_event_attachment`;

CREATE TABLE `itsm_event_attachment` (
  `id` int(11) unsigned NOT NULL COMMENT '主键ID',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `upload_user_id` int(11) unsigned DEFAULT NULL COMMENT '上传人ID',
  `upload_time` datetime DEFAULT NULL COMMENT '附件上传时间',
  `file_id` int(11) unsigned DEFAULT NULL COMMENT '对应文档库的ID',
  `event_id` int(11) unsigned DEFAULT NULL COMMENT '工单ID',
  `upload_user_type` int(11) unsigned DEFAULT NULL COMMENT '上传人的类型1提单人2服务台3一线工程师4二线工程师5服务台经理',
  `upload_node` int(11) DEFAULT NULL COMMENT '上传节点1提交工单2解决工单3转派',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_event_freeze` */

DROP TABLE IF EXISTS `itsm_event_freeze`;

CREATE TABLE `itsm_event_freeze` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `task_id` int(11) DEFAULT NULL,
  `event_id` varchar(255) DEFAULT NULL COMMENT '工单ID',
  `freeze_time` datetime DEFAULT NULL COMMENT '冻结时间',
  `thaw_time` datetime DEFAULT NULL COMMENT '解冻时间',
  `freeze_user_id` int(11) unsigned DEFAULT NULL COMMENT '冻结操作人Id',
   `thaw_user_id` int(11) DEFAULT NULL,
  `freeze_reason` varchar(255) DEFAULT NULL COMMENT '冻结原因',
  `rank` int(11) unsigned DEFAULT NULL COMMENT '冻结次数',
  PRIMARY KEY (`id`),
  KEY `FK_FREEZE_EVENT` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_event_knowledge` */

DROP TABLE IF EXISTS `itsm_event_knowledge`;

CREATE TABLE `itsm_event_knowledge` (
  `id` int(11) NOT NULL,
  `event_id` int(11) unsigned DEFAULT NULL COMMENT '工单ID',
  `knowledge_id` int(11) unsigned DEFAULT NULL COMMENT '知识库ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_event_redeploy` */

DROP TABLE IF EXISTS `itsm_event_redeploy`;

CREATE TABLE `itsm_event_redeploy` (
  `id` int(11) NOT NULL COMMENT '主键',
  `proc_id` int(11) DEFAULT NULL COMMENT '流程ID',
  `task_id` int(11) DEFAULT NULL COMMENT '任务ID',
  `task_name` varchar(255) DEFAULT NULL COMMENT '转派名称',
  `pre_user_account` varchar(255) DEFAULT NULL COMMENT '转派前工程师ID',
  `next_user_account` varchar(255) DEFAULT NULL COMMENT '转派后工程师ID',
  `comment` varchar(500) DEFAULT NULL COMMENT '转派说明',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作人账号',
  `create_time` datetime DEFAULT NULL COMMENT '转派时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_event_reminder` */

DROP TABLE IF EXISTS `itsm_event_reminder`;

CREATE TABLE `itsm_event_reminder` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `sheetId` int(11) unsigned DEFAULT NULL COMMENT '工单ID',
  `number` int(11) unsigned DEFAULT NULL COMMENT '催单次数',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  KEY `FK_EVENTMGR_REMINDER` (`sheetId`),
  CONSTRAINT `FK_EVENTMGR_REMINDER` FOREIGN KEY (`sheetId`) REFERENCES `itsm_eventmgr` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_event_review` */

DROP TABLE IF EXISTS `itsm_event_review`;

CREATE TABLE `itsm_event_review` (
  `ID` int(50) unsigned NOT NULL COMMENT '主键',
  `procId` varchar(255) DEFAULT NULL COMMENT '流程实例ID',
  `taskId` varchar(255) DEFAULT NULL COMMENT '任务ID',
  `reviewDate` datetime DEFAULT NULL COMMENT '创建日期',
  `reviewAccount` varchar(255) DEFAULT NULL COMMENT '参与人用户名',
  `reviewName` varchar(255) DEFAULT NULL COMMENT '参与人名字',
  `reviewDesc` varchar(255) DEFAULT NULL COMMENT '解决描述',
  `nodeName` varchar(255) DEFAULT NULL COMMENT '节点名称',
  `nodeDesc` varchar(255) DEFAULT NULL COMMENT '节点描述',
  `isDispatch` int(11) unsigned DEFAULT NULL COMMENT '是否同级之间转派0否1是',
  `processMode` varchar(255) DEFAULT NULL COMMENT '处理方式:1提交服务台；2转一线；3转二线；4解决;5服务台受理；6提交服务台经理审核7通过8驳回',
  `next` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_event_satisfaction` */

DROP TABLE IF EXISTS `itsm_event_satisfaction`;

CREATE TABLE `itsm_event_satisfaction` (
  `id` int(50) unsigned NOT NULL COMMENT '主键',
  `satisfiedStars` int(11) DEFAULT '0' COMMENT '满意指数',
  `satisfiedContent` varchar(255) DEFAULT NULL COMMENT '满意度评价描述',
  `satisfiedLabel` varchar(255) DEFAULT NULL COMMENT '满意度标签',
  `createTime` date DEFAULT NULL COMMENT '评价提交时间',
  `eventSheetId` varchar(255) DEFAULT NULL COMMENT '对应的工单ID',
  `satisfiedLabelContent` varchar(255) DEFAULT NULL COMMENT '标签内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_event_satisfaction_history` */

DROP TABLE IF EXISTS `itsm_event_satisfaction_history`;

CREATE TABLE `itsm_event_satisfaction_history` (
  `id` int(50) unsigned NOT NULL COMMENT '主键',
  `satisfiedStars` int(11) DEFAULT '0' COMMENT '满意指数',
  `satisfiedContent` varchar(255) DEFAULT NULL COMMENT '满意度评价描述',
  `satisfiedLabel` varchar(255) DEFAULT NULL COMMENT '满意度标签',
  `createTime` date DEFAULT NULL COMMENT '评价提交时间',
  `eventSheetId` varchar(255) DEFAULT NULL COMMENT '对应的工单ID',
  `satisfiedLabelContent` varchar(255) DEFAULT NULL COMMENT '标签内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_eventmgr` */

DROP TABLE IF EXISTS `itsm_eventmgr`;

CREATE TABLE `itsm_eventmgr` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `worksheetId` varchar(50) DEFAULT NULL COMMENT '工单编号',
  `request_type` tinyint(1) DEFAULT NULL COMMENT '请求类型1保障2申请3咨询4投诉5其他',
  `urgency` tinyint(1) DEFAULT NULL COMMENT '紧急程度1紧急2高3中4低',
  `priorityId` int(50) unsigned DEFAULT NULL COMMENT '优先级',
  `slaId` int(50) unsigned DEFAULT NULL COMMENT '关联SLA',
  `theme` varchar(200) NOT NULL COMMENT '主题',
  `key_word` varchar(200) DEFAULT NULL COMMENT '关键字',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `create_user` bigint(20) unsigned DEFAULT NULL COMMENT '创建用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `source` int(10) unsigned DEFAULT NULL COMMENT '来源1电话2邮件3移动端(微信,QQ等)4监控系统5其他6自助服务台',
  `status` tinyint(1) unsigned DEFAULT NULL COMMENT '审批状态1完成2未分派3已冻结4处理中5待评价6审批中',
  `process_instanceId` varchar(50) DEFAULT NULL COMMENT '流程实例id',
  `apply_user_id` int(11) unsigned DEFAULT NULL COMMENT '申请人ID',
  `location` varchar(255) DEFAULT NULL COMMENT '区域位置',
  `appointment_time` datetime DEFAULT NULL COMMENT '预约维修时间',
  `acceptance_time` datetime DEFAULT NULL COMMENT '受理时间',
  `serviceCatalogId` int(50) unsigned DEFAULT NULL COMMENT '服务类型',
  `accept` int(10) unsigned zerofill DEFAULT NULL COMMENT '是否受理1受理0未受理',
  `solve_time` datetime DEFAULT NULL COMMENT '解决时间',
  `created_knowledge` tinyint(1) DEFAULT NULL,
  `processKey` varchar(255) DEFAULT NULL,
  `isDraft` tinyint(2) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `PRIORITY_EVENT_FK` (`priorityId`),
  KEY `SLA_EVENT_FK` (`slaId`),
  KEY `USER_EVENT_FK` (`create_user`),
  KEY `SATISFIED_EVENT_FK` (`accept`),
  KEY `SERVICE_EVENT_FK` (`serviceCatalogId`),
  CONSTRAINT `PRIORITY_EVENT_FK` FOREIGN KEY (`priorityId`) REFERENCES `itsm_service_priority` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SERVICE_EVENT_FK` FOREIGN KEY (`serviceCatalogId`) REFERENCES `itsm_service_service` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SLA_EVENT_FK` FOREIGN KEY (`slaId`) REFERENCES `itsm_service_sla` (`slaId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_eventmgr_repeat` */

DROP TABLE IF EXISTS `itsm_eventmgr_repeat`;

CREATE TABLE `itsm_eventmgr_repeat` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `worksheetId` varchar(50) DEFAULT NULL COMMENT '工单编号',
  `request_type` tinyint(1) DEFAULT NULL COMMENT '请求类型1保障2申请3咨询4投诉5其他',
  `urgency` tinyint(1) DEFAULT NULL COMMENT '紧急程度1紧急2高3中4低',
  `priorityId` int(50) unsigned DEFAULT NULL COMMENT '优先级',
  `slaId` int(50) unsigned DEFAULT NULL COMMENT '关联SLA',
  `theme` varchar(200) NOT NULL COMMENT '主题',
  `key_word` varchar(200) DEFAULT NULL COMMENT '关键字',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_user` bigint(20) unsigned DEFAULT NULL COMMENT '创建用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `source` int(10) unsigned DEFAULT NULL COMMENT '来源1电话2邮件3移动端(微信,QQ等)4监控系统5其他6自助服务台',
  `apply_user_id` int(11) unsigned DEFAULT NULL COMMENT '申请人ID',
  `location` varchar(255) DEFAULT NULL COMMENT '区域位置',
  `appointment_time` datetime DEFAULT NULL COMMENT '预约维修时间',
  `serviceCatalogId` int(50) unsigned DEFAULT NULL COMMENT '服务类型',
  `mainEventId` int(11) DEFAULT NULL COMMENT '主工单ID',
  PRIMARY KEY (`id`),
  KEY `PRIORITY_EVENT_FK` (`priorityId`),
  KEY `SLA_EVENT_FK` (`slaId`),
  KEY `USER_EVENT_FK` (`create_user`),
  KEY `SERVICE_EVENT_FK` (`serviceCatalogId`),
  CONSTRAINT `itsm_eventmgr_repeat_ibfk_1` FOREIGN KEY (`priorityId`) REFERENCES `itsm_service_priority` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `itsm_eventmgr_repeat_ibfk_2` FOREIGN KEY (`serviceCatalogId`) REFERENCES `itsm_service_service` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `itsm_eventmgr_repeat_ibfk_3` FOREIGN KEY (`slaId`) REFERENCES `itsm_service_sla` (`slaId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_knowledge_collect` */

DROP TABLE IF EXISTS `itsm_knowledge_collect`;

CREATE TABLE `itsm_knowledge_collect` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `userId` int(11) DEFAULT NULL COMMENT '收藏人ID',
  `knowledgeId` int(11) unsigned DEFAULT NULL COMMENT '知识ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_knowledge_document` */

DROP TABLE IF EXISTS `itsm_knowledge_document`;

CREATE TABLE `itsm_knowledge_document` (
  `documentId` int(11) NOT NULL COMMENT '文档id',
  `documentName` varchar(50) DEFAULT NULL COMMENT '文档名称',
  `uploadPerson` varchar(255) DEFAULT NULL COMMENT '上传人',
  `uploadTime` datetime DEFAULT NULL COMMENT '上传时间',
  `knowledgeId` int(20) DEFAULT NULL COMMENT '文档所属项目id',
  `documentPath` varchar(255) DEFAULT NULL COMMENT '附件路径'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_knowledge_mian` */

DROP TABLE IF EXISTS `itsm_knowledge_mian`;

CREATE TABLE `itsm_knowledge_mian` (
  `id` int(11) NOT NULL,
  `knowledgeNumber` varchar(50) DEFAULT NULL COMMENT '知识编号',
  `knowledgeTitle` varchar(255) DEFAULT NULL COMMENT '知识标题',
  `knowledgeKey` varchar(255) DEFAULT NULL COMMENT '关键字',
  `useMember` int(11) DEFAULT NULL COMMENT '知识适用范围0所有人员1用户2系统管理员',
  `knowledgeType` int(11) DEFAULT NULL COMMENT '知识分类',
  `narrative` longtext COMMENT '知识概述',
  `methodsStep` longtext COMMENT '方法步骤',
  `knowledgeState` int(11) DEFAULT NULL COMMENT '知识状态，1发布，2停用，3未审核，4拟稿中，5不通过,6通过',
  `knowledgeQuote` int(11) DEFAULT NULL COMMENT '知识引用次数',
  `knowledgeBrowse` int(11) DEFAULT NULL COMMENT '知识浏览量',
  `createMan` int(11) DEFAULT NULL COMMENT '知识创建人',
  `createTime` datetime DEFAULT NULL COMMENT '知识创建时间',
  `knowledgeSource` int(11) DEFAULT NULL COMMENT '知识来源,1表示提醒提交，2表示外来传入',
  `requestType` varchar(255) DEFAULT NULL COMMENT '请求类型：1报障，2申请，3咨询，4投诉，5其他',
  `relationEventId` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_knowledge_mould` */

DROP TABLE IF EXISTS `itsm_knowledge_mould`;

/* jwt 知识模板绑定类型id*/
CREATE TABLE `itsm_knowledge_mould` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '序号',
  `mouldName` varchar(255) DEFAULT NULL COMMENT '名称',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `mouldFileId` varchar(255) DEFAULT NULL COMMENT '模板关联的文件id',
  `mouldFileName` varchar(255) DEFAULT NULL COMMENT '模板文件名',
  `createMan` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `mouldId` varchar(255) DEFAULT NULL COMMENT '模板id',
  `typeName` VARCHAR(50) NULL DEFAULT NULL COMMENT '知识模板类型名称(冗余字段)' COLLATE 'utf8_general_ci',
	`typeId` INT(20) NULL DEFAULT NULL COMMENT '知识模板类型id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

/*jwt 知识模板类型 */
CREATE TABLE `itsm_knowledge_mould_type` (
	`id` INT(20) NULL DEFAULT NULL COMMENT '类型id',
	`name` VARCHAR(200) NULL DEFAULT NULL COMMENT '类型名称' COLLATE 'utf8mb4_general_ci'
)
COMMENT='知识库模板类型'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


/*Table structure for table `itsm_membermanage` */

DROP TABLE IF EXISTS `itsm_membermanage`;

CREATE TABLE `itsm_membermanage` (
  `id` varchar(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_notice` */

DROP TABLE IF EXISTS `itsm_notice`;

CREATE TABLE `itsm_notice` (
  `notice_id` int(11) NOT NULL,
  `notice_number` varchar(255) DEFAULT NULL COMMENT '序号',
  `notice_subManId` varchar(255) DEFAULT NULL COMMENT '发布人',
  `notice_release_time` datetime DEFAULT NULL COMMENT '发布日期',
  `notice_title` varchar(255) DEFAULT NULL COMMENT '标题',
  `notice_effect_length` int(11) DEFAULT NULL COMMENT '有效期',
  `notice_state` int(11) DEFAULT NULL COMMENT '状态',
  `notice_disEffect_time` datetime DEFAULT NULL COMMENT '失效日期',
  `notice_content` varchar(5000) DEFAULT NULL COMMENT '内容',
  `domainId` bigint(20) DEFAULT NULL COMMENT '所属域id',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_pm_comment` */

DROP TABLE IF EXISTS `itsm_pm_comment`;

CREATE TABLE `itsm_pm_comment` (
  `commentId` int(11) NOT NULL COMMENT '主键',
  `content` varchar(255) DEFAULT NULL COMMENT '评论内容',
  `userId` bigint(20) unsigned DEFAULT NULL COMMENT '评论人ID',
  `taskId` int(20) DEFAULT NULL COMMENT '任务id',
  `submitDate` datetime DEFAULT NULL COMMENT '提交日期',
  `userName` varchar(255) DEFAULT NULL COMMENT '评论人姓名',
  PRIMARY KEY (`commentId`),
  KEY `FK_comment_user` (`userId`),
  KEY `FK_comment_task` (`taskId`),
  CONSTRAINT `FK_comment_task` FOREIGN KEY (`taskId`) REFERENCES `itsm_pm_task` (`taskId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_comment_user` FOREIGN KEY (`userId`) REFERENCES `plat_user` (`c_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_pm_department` */

DROP TABLE IF EXISTS `itsm_pm_department`;

CREATE TABLE `itsm_pm_department` (
  `departmentId` int(11) NOT NULL COMMENT '主键',
  `departmentName` varchar(255) DEFAULT NULL COMMENT '部门名称',
  `parentId` int(11) NOT NULL COMMENT '上级部门ID',
  PRIMARY KEY (`departmentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_pm_doccatalog` */

DROP TABLE IF EXISTS `itsm_pm_doccatalog`;

CREATE TABLE `itsm_pm_doccatalog` (
  `catalogId` int(11) NOT NULL COMMENT '主键',
  `catalogName` varchar(255) DEFAULT NULL COMMENT '文件分类名称',
  `parentId` int(11) DEFAULT NULL COMMENT '上级文件分类',
  PRIMARY KEY (`catalogId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_pm_document` */

DROP TABLE IF EXISTS `itsm_pm_document`;

CREATE TABLE `itsm_pm_document` (
  `documentId` int(11) NOT NULL COMMENT '文档id',
  `documentName` varchar(50) DEFAULT NULL COMMENT '文档名称',
  `uploadPerson` varchar(255) DEFAULT NULL COMMENT '上传人',
  `uploadTime` datetime DEFAULT NULL COMMENT '上传时间',
  `documentTypeId` int(11) DEFAULT NULL COMMENT '文件分类ID',
  `projectId` int(20) DEFAULT NULL COMMENT '文档所属项目id',
  `taskId` int(20) DEFAULT NULL COMMENT '文档所属任务id',
  `documentType` varchar(255) DEFAULT NULL COMMENT '文档类型',
  `documentSource` varchar(255) DEFAULT NULL COMMENT '文档来源，1从任务中添加，2从项目中添加，3从文档库添加',
  `documentPath` varchar(255) DEFAULT NULL COMMENT '附件路径'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_pm_member` */

DROP TABLE IF EXISTS `itsm_pm_member`;

CREATE TABLE `itsm_pm_member` (
  `memberId` int(11) NOT NULL COMMENT '主键',
  `memberName` varchar(255) DEFAULT NULL COMMENT '成员名称',
  `userName` varchar(50) DEFAULT NULL COMMENT '用户名',
  `departmentId` int(11) DEFAULT NULL COMMENT '所属部门',
  `phoneNumber` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `webChatId` varchar(50) DEFAULT NULL COMMENT '微信号',
  `QQNumber` varchar(50) DEFAULT NULL COMMENT 'QQ号码',
  PRIMARY KEY (`memberId`),
  KEY `FK_DEPARTMENT` (`departmentId`),
  CONSTRAINT `FK_DEPARTMENT` FOREIGN KEY (`departmentId`) REFERENCES `itsm_pm_department` (`departmentId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_pm_project` */

DROP TABLE IF EXISTS `itsm_pm_project`;

CREATE TABLE `itsm_pm_project` (
  `projectId` int(11) NOT NULL COMMENT '主键',
  `projectName` varchar(255) DEFAULT NULL COMMENT '项目名称',
  `projectNumber` varchar(50) DEFAULT NULL COMMENT '项目编号',
  `createMan` varchar(50) DEFAULT NULL COMMENT '创建人',
  `address` varchar(50) DEFAULT NULL COMMENT '项目地址',
  `amount` double(255,0) DEFAULT NULL COMMENT '项目金额',
  `principal` varchar(50) DEFAULT NULL COMMENT '项目负责人',
  `acceptor` varchar(50) DEFAULT NULL COMMENT '项目验收人',
  `maintainer` varchar(50) DEFAULT NULL COMMENT '项目维护人',
  `brief` varchar(255) DEFAULT NULL COMMENT '项目简介',
  `devOrganization` varchar(255) DEFAULT NULL COMMENT '建设单位',
  `desOrganization` varchar(255) DEFAULT NULL COMMENT '设计单位',
  `linkMan` varchar(255) DEFAULT NULL COMMENT '项目联系人',
  `linkNumber` varchar(255) DEFAULT NULL COMMENT '联系电话',
  `supervisionUnit` varchar(255) DEFAULT NULL COMMENT '监理单位',
  `HealthState` int(11) DEFAULT NULL COMMENT '项目状态1正常2风险3失控',
  `projectStatus` int(11) DEFAULT NULL COMMENT '项目状态1待开展2进行中3已完成',
  `projectStartTime` datetime DEFAULT NULL COMMENT '项目开始时间',
  `projectEndTime` datetime DEFAULT NULL COMMENT '项目结束时间',
  `stateBrief` varchar(1000) DEFAULT NULL COMMENT '状态描述',
  PRIMARY KEY (`projectId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_pm_project_member` */

DROP TABLE IF EXISTS `itsm_pm_project_member`;

CREATE TABLE `itsm_pm_project_member` (
  `projectId` int(11) NOT NULL,
  `memberId` int(11) NOT NULL,
  `memberName` varchar(255) DEFAULT NULL COMMENT '项目人员名称',
  KEY `fk_project` (`projectId`),
  KEY `fk_member` (`memberId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_pm_projectannex` */

DROP TABLE IF EXISTS `itsm_pm_projectannex`;

CREATE TABLE `itsm_pm_projectannex` (
  `projectId` int(50) DEFAULT NULL COMMENT '对应项目id',
  `annexId` int(11) DEFAULT NULL COMMENT '附件id',
  `annexName` varchar(255) DEFAULT NULL COMMENT '附件名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_pm_task` */

DROP TABLE IF EXISTS `itsm_pm_task`;

CREATE TABLE `itsm_pm_task` (
  `taskId` int(11) NOT NULL COMMENT '主键',
  `taskName` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `taskStartTime` datetime DEFAULT NULL COMMENT '任务开始时间',
  `taskDeadline` datetime DEFAULT NULL COMMENT '任务截止时间',
  `progressPhoto` varchar(255) DEFAULT NULL COMMENT '任务进展照片',
  `attachment` varchar(255) DEFAULT NULL COMMENT '附件',
  `priority` int(11) DEFAULT NULL COMMENT '优先级1高2中3低',
  `jobLocation` varchar(255) DEFAULT NULL COMMENT '任务地址',
  `milestone` int(11) DEFAULT NULL COMMENT '是否是里程碑任务1是0否',
  `projectId` int(11) DEFAULT NULL COMMENT '所属项目ID',
  `taskStatus` int(11) DEFAULT NULL COMMENT '任务状态1待开展2进行中3已完成',
  `completeTime` datetime DEFAULT NULL COMMENT '项目完成时间',
  `taskCreateMan` varchar(255) DEFAULT NULL COMMENT '任务创建人',
  `taskMoney` varchar(255) DEFAULT NULL COMMENT '任务金额',
  `taskDutyMan` varchar(255) DEFAULT NULL COMMENT '任务负责人',
  `taskCheckMan` varchar(255) DEFAULT NULL COMMENT '任务验收人',
  `taskMaintainMan` varchar(255) DEFAULT NULL COMMENT '任务维护人',
  `designCompany` varchar(255) DEFAULT NULL COMMENT '设计单位',
  `buildCompany` varchar(255) DEFAULT NULL COMMENT '建设单位',
  `contactMan` varchar(255) DEFAULT NULL COMMENT '联系人',
  `contactPhone` varchar(255) DEFAULT NULL COMMENT '联系电话',
  `controlCompany` varchar(255) DEFAULT NULL COMMENT '监理单位',
  `projectName` varchar(255) DEFAULT NULL COMMENT '项目名称',
  `taskCode` varchar(255) DEFAULT NULL COMMENT '任务编号',
  `taskEndTime` datetime DEFAULT NULL COMMENT '任务实际结束时间',
  `taskSubmitMan` varchar(255) DEFAULT NULL COMMENT '任务提交人id',
  `createDate` datetime DEFAULT NULL COMMENT '任务创建时间',
  PRIMARY KEY (`taskId`),
  KEY `FK_project_task` (`projectId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_pm_task_member` */

DROP TABLE IF EXISTS `itsm_pm_task_member`;

CREATE TABLE `itsm_pm_task_member` (
  `task_member_id` int(11) NOT NULL COMMENT '主键',
  `taskId` int(11) DEFAULT NULL COMMENT '任务ID',
  `memberId` int(11) DEFAULT NULL COMMENT '人员ID',
  `memberName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`task_member_id`),
  KEY `FK_task` (`taskId`),
  KEY `FK_task_member` (`memberId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_qi_job_details` */

DROP TABLE IF EXISTS `itsm_qi_job_details`;

CREATE TABLE `itsm_qi_job_details` (
  `sched_name` varchar(120) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `job_name` varchar(200) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `job_group` varchar(200) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `description` varchar(250) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `job_class_name` varchar(250) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `is_durable` varchar(1) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `is_nonconcurrent` varchar(1) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `is_update_data` varchar(1) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `requests_recovery` varchar(1) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `job_data` blob COMMENT 'å­—æ®µæ³¨é‡Š',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`),
  KEY `idx_ves_qa_j_req_recovery` (`sched_name`,`requests_recovery`),
  KEY `idx_ves_qa_j_grp` (`sched_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_qi_locks` */

DROP TABLE IF EXISTS `itsm_qi_locks`;

CREATE TABLE `itsm_qi_locks` (
  `sched_name` varchar(120) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `lock_name` varchar(40) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_qi_paused_trigger_grps` */

DROP TABLE IF EXISTS `itsm_qi_paused_trigger_grps`;

CREATE TABLE `itsm_qi_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `trigger_group` varchar(200) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_qi_scheduler_state` */

DROP TABLE IF EXISTS `itsm_qi_scheduler_state`;

CREATE TABLE `itsm_qi_scheduler_state` (
  `sched_name` varchar(120) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `instance_name` varchar(200) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `last_checkin_time` bigint(13) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `checkin_interval` bigint(13) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_qi_simple_triggers` */

DROP TABLE IF EXISTS `itsm_qi_simple_triggers`;

CREATE TABLE `itsm_qi_simple_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `trigger_name` varchar(200) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `trigger_group` varchar(200) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `repeat_count` bigint(7) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `repeat_interval` bigint(12) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `times_triggered` bigint(10) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `itsm_qi_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `itsm_qi_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_qi_simprop_triggers` */

DROP TABLE IF EXISTS `itsm_qi_simprop_triggers`;

CREATE TABLE `itsm_qi_simprop_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `trigger_name` varchar(200) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `trigger_group` varchar(200) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `str_prop_1` varchar(512) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `str_prop_2` varchar(512) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `str_prop_3` varchar(512) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `int_prop_1` int(11) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `int_prop_2` int(11) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `long_prop_1` bigint(20) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `long_prop_2` bigint(20) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `bool_prop_1` varchar(1) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `bool_prop_2` varchar(1) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `itsm_qi_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `itsm_qi_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_qi_triggers` */

DROP TABLE IF EXISTS `itsm_qi_triggers`;

CREATE TABLE `itsm_qi_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `trigger_name` varchar(200) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `trigger_group` varchar(200) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `job_name` varchar(200) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `job_group` varchar(200) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `description` varchar(250) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `next_fire_time` bigint(13) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `prev_fire_time` bigint(13) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `priority` int(11) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `trigger_state` varchar(16) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `trigger_type` varchar(8) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `start_time` bigint(13) NOT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `end_time` bigint(13) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `calendar_name` varchar(200) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `misfire_instr` smallint(2) DEFAULT NULL COMMENT 'å­—æ®µæ³¨é‡Š',
  `job_data` blob COMMENT 'å­—æ®µæ³¨é‡Š',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `idx_ves_qa_t_j` (`sched_name`,`job_name`,`job_group`),
  KEY `idx_ves_qa_t_jg` (`sched_name`,`job_group`),
  KEY `idx_ves_qa_t_c` (`sched_name`,`calendar_name`),
  KEY `idx_ves_qa_t_g` (`sched_name`,`trigger_group`),
  KEY `idx_ves_qa_t_state` (`sched_name`,`trigger_state`),
  KEY `idx_ves_qa_t_n_state` (`sched_name`,`trigger_name`,`trigger_group`,`trigger_state`),
  KEY `idx_ves_qa_t_n_g_state` (`sched_name`,`trigger_group`,`trigger_state`),
  KEY `idx_ves_qa_t_next_fire_time` (`sched_name`,`next_fire_time`),
  KEY `idx_ves_qa_t_nft_st` (`sched_name`,`trigger_state`,`next_fire_time`),
  KEY `idx_ves_qa_t_nft_misfire` (`sched_name`,`misfire_instr`,`next_fire_time`),
  KEY `idx_ves_qa_t_nft_st_misfire` (`sched_name`,`misfire_instr`,`next_fire_time`,`trigger_state`),
  KEY `idx_ves_qa_t_nft_st_misfire_grp` (`sched_name`,`misfire_instr`,`next_fire_time`,`trigger_group`,`trigger_state`),
  CONSTRAINT `itsm_qi_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `itsm_qi_job_details` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_service_catalog` */

DROP TABLE IF EXISTS `itsm_service_catalog`;

CREATE TABLE `itsm_service_catalog` (
  `id` int(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '服务所属分类',
  `pid` varchar(255) DEFAULT NULL COMMENT '父id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务目录';

/*Table structure for table `itsm_service_priority` */

DROP TABLE IF EXISTS `itsm_service_priority`;

CREATE TABLE `itsm_service_priority` (
  `id` int(10) unsigned NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '优先级名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_service_service` */

DROP TABLE IF EXISTS `itsm_service_service`;

CREATE TABLE `itsm_service_service` (
  `id` int(50) unsigned NOT NULL,
  `state` int(11) DEFAULT NULL COMMENT '状态',
  `serviceName` varchar(255) NOT NULL COMMENT '服务名称',
  `serviceProfile` varchar(255) DEFAULT NULL COMMENT '服务简介',
  `catalogId` int(11) DEFAULT NULL COMMENT '服务所属分类ID',
  `eventLevel` varchar(10) DEFAULT NULL COMMENT '事件级别',
  `directSubTime` datetime DEFAULT NULL COMMENT '提交时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `itsm_service_sla` */

DROP TABLE IF EXISTS `itsm_service_sla`;

CREATE TABLE `itsm_service_sla` (
  `slaId` int(50) unsigned NOT NULL,
  `slaName` varchar(50) DEFAULT NULL,
  `state` int(10) unsigned DEFAULT NULL COMMENT '状态0停用1在用',
  `slaDescribe` varchar(255) DEFAULT NULL COMMENT 'sla描述',
  `slaWithTime` varchar(255) DEFAULT NULL COMMENT '响应时限',
  `slaSalveTime` varchar(255) DEFAULT NULL COMMENT '解决时限',
  `preMinute` varchar(255) DEFAULT NULL COMMENT '提前多少分钟',
  `overTopMinute` varchar(255) DEFAULT NULL COMMENT '超过多少分钟',
  `priorityId` int(11) DEFAULT NULL COMMENT '优先级',
  PRIMARY KEY (`slaId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SLA';

/*Table structure for table `itsm_service_sla_ship` */

DROP TABLE IF EXISTS `itsm_service_sla_ship`;

CREATE TABLE `itsm_service_sla_ship` (
  `id` int(50) unsigned NOT NULL COMMENT '主键',
  `serviceId` int(50) unsigned DEFAULT NULL COMMENT '服务类型ID',
  `slaId` int(50) unsigned DEFAULT NULL COMMENT 'SLA主键',
  PRIMARY KEY (`id`),
  KEY `SERVICE_FK` (`serviceId`),
  KEY `SLA_FK` (`slaId`),
  CONSTRAINT `SERVICE_FK` FOREIGN KEY (`serviceId`) REFERENCES `itsm_service_service` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SLA_FK` FOREIGN KEY (`slaId`) REFERENCES `itsm_service_sla` (`slaId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_sys_customer` */

DROP TABLE IF EXISTS `itsm_sys_customer`;

CREATE TABLE `itsm_sys_customer` (
  `customer_id` int(11) NOT NULL,
  `customer_name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `customer_login_name` varchar(255) DEFAULT NULL COMMENT '登录账号',
  `customer_department` varchar(255) DEFAULT NULL COMMENT '所属部门',
  `customer_position` varchar(255) DEFAULT NULL COMMENT '职务',
  `customer_password` varchar(255) DEFAULT NULL COMMENT '密码',
  `customer_confirm_password` varchar(255) DEFAULT NULL COMMENT '确认密码',
  `customer_tel` varchar(255) DEFAULT NULL COMMENT '手机',
  `customer_email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_system_right_button` */

DROP TABLE IF EXISTS `itsm_system_right_button`;

CREATE TABLE `itsm_system_right_button` (
  `right_id` int(20) NOT NULL,
  `button_id` varchar(30) NOT NULL,
  `button_name` varchar(50) DEFAULT NULL,
  `button_des` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`right_id`,`button_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `plat_company` */

DROP TABLE IF EXISTS `plat_company`;

CREATE TABLE `plat_company` (
  `company_id` bigint(20) NOT NULL,
  `company_short_name` varchar(100) NOT NULL,
  `company_full_name` varchar(100) NOT NULL,
  `company_parentId` bigint(20) NOT NULL,
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `plat_organization` */

DROP TABLE IF EXISTS `plat_organization`;

CREATE TABLE `plat_organization` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `sort` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `plat_right` */

DROP TABLE IF EXISTS `plat_right`;

CREATE TABLE `plat_right` (
  `id` int(10) unsigned NOT NULL COMMENT '字段注释',
  `NAME` varchar(20) NOT NULL COMMENT '字段注释',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `file_id` bigint(20) DEFAULT NULL COMMENT 'column remarks',
  `url` varchar(200) NOT NULL COMMENT '字段注释',
  `description` varchar(200) DEFAULT NULL COMMENT 'column remarks',
  `type` tinyint(3) unsigned DEFAULT '1' COMMENT '可取0、1：0表示系统内部url；1表示系统外部url；',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '值越大，排位越靠后',
  `pid` int(10) unsigned DEFAULT '0' COMMENT '保留字段,默认为0表示根权限',
  `is_select` tinyint(4) DEFAULT '0' COMMENT '表示新增角色时，该页签权限是否默认选中，0表示默认不选中，1表示默认选中',
  `is_role_used` tinyint(4) DEFAULT '1' COMMENT '1、角色可用 ，0、角色不可用',
  `STATUS` tinyint(4) DEFAULT '1' COMMENT '1、启用 0、停用',
  `is_new_tag` tinyint(4) DEFAULT '1',
  `json` varchar(100) DEFAULT 'deafult',
  `grpcode` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='页面访问权限表';

/*Table structure for table `plat_role` */

DROP TABLE IF EXISTS `plat_role`;

CREATE TABLE `plat_role` (
  `id` bigint(20) NOT NULL COMMENT '字段注释',
  `NAME` varchar(30) NOT NULL COMMENT '字段注释',
  `STATUS` smallint(6) NOT NULL DEFAULT '1' COMMENT '1启用，0停用',
  `is_operate` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0-不可操作 1-可操作 （域管理员、管理员不可操作）',
  `creator_id` bigint(20) NOT NULL DEFAULT '0',
  `created_time` datetime NOT NULL COMMENT '字段注释',
  `description` varchar(200) DEFAULT NULL COMMENT 'column remarks',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色基本信息表';

/*Table structure for table `plat_role_right_rel` */

DROP TABLE IF EXISTS `plat_role_right_rel`;

CREATE TABLE `plat_role_right_rel` (
  `role_id` bigint(20) NOT NULL,
  `right_id` bigint(20) NOT NULL,
  `button_ids` varchar(100) NOT NULL,
  PRIMARY KEY (`role_id`,`right_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `plat_user` */

DROP TABLE IF EXISTS `plat_user`;

CREATE TABLE `plat_user` (
  `c_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `c_name` varchar(20) NOT NULL COMMENT '姓名',
  `c_sex` smallint(6) DEFAULT NULL COMMENT '性别',
  `c_account` varchar(32) NOT NULL COMMENT '账号',
  `c_password` varchar(64) DEFAULT NULL COMMENT '密码',
  `c_user_type` smallint(6) NOT NULL DEFAULT '1' COMMENT '1普通用户；2域管理员；3系统管理员；4超级管理员',
  `c_mobile` varchar(20) DEFAULT NULL COMMENT '电话',
  `c_email` varchar(64) DEFAULT NULL COMMENT 'email',
  `c_status` smallint(6) NOT NULL DEFAULT '1' COMMENT '0停用；1启用',
  `c_creator_id` bigint(20) NOT NULL COMMENT '创建人',
  `c_created_time` datetime NOT NULL COMMENT '创建时间',
  `c_lock_type` smallint(6) DEFAULT NULL COMMENT '锁定类型 1、被管理员锁定 2、密码输入超过规定次数 3、密码有效期超过规定时间',
  `c_lock_time` datetime DEFAULT NULL COMMENT '锁定时间',
  `c_update_pass_time` datetime DEFAULT NULL COMMENT '修改密码时间',
  `c_pass_error_cnt` smallint(6) DEFAULT '0' COMMENT '密码错误次数',
  `c_json` varchar(500) DEFAULT NULL COMMENT '扩展信息json',
  `c_tag1` varchar(36) DEFAULT NULL COMMENT '微信账号',
  `c_tag2` varchar(36) DEFAULT NULL COMMENT '冗余字段2',
  PRIMARY KEY (`c_id`),
  UNIQUE KEY `plat_user_account` (`c_account`)
) ENGINE=InnoDB AUTO_INCREMENT=102513 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Table structure for table `plat_user_domain_role_rel` */

DROP TABLE IF EXISTS `plat_user_domain_role_rel`;

CREATE TABLE `plat_user_domain_role_rel` (
  `user_id` bigint(20) NOT NULL COMMENT '字段注释',
  `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '默认为0，用于域管理员与域的关联关系管理',
  `domain_id` bigint(20) NOT NULL COMMENT '字段注释',
  PRIMARY KEY (`user_id`,`role_id`,`domain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用于管理普通用户和域管理员对域和角色的关联关系，其中域管理员只用于维护管理员和域的关系角色默认为0；普通用户的关联关系用';

/*Table structure for table `plat_user_org_relation` */

DROP TABLE IF EXISTS `plat_user_org_relation`;

CREATE TABLE `plat_user_org_relation` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `organization_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `sys_user_domain_role_rel` */

DROP TABLE IF EXISTS `sys_user_domain_role_rel`;

CREATE TABLE `sys_user_domain_role_rel` (
  `c_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `c_user_id` bigint(20) NOT NULL COMMENT '用户id',
  `c_role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色id，默认为0，用于域管理员与域的关联关系管理',
  `c_domain_id` bigint(20) NOT NULL COMMENT '域id',
  `c_tag1` varchar(36) DEFAULT NULL COMMENT '冗余字段1',
  `c_tag2` varchar(36) DEFAULT NULL COMMENT '冗余字段2',
  PRIMARY KEY (`c_id`),
  UNIQUE KEY `c_user_id` (`c_user_id`,`c_role_id`,`c_domain_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用于管理普通用户和域管理员对域和角色的关联关系，其中域管理员只用于维护管理员和域的关系角色默认为0；普通用户的关联关系用';

/*Table structure for table `ves_file_list` */

DROP TABLE IF EXISTS `ves_file_list`;

CREATE TABLE `ves_file_list` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `file_group` varchar(255) DEFAULT NULL COMMENT '文件分组',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `final_name` varchar(255) DEFAULT NULL COMMENT '文件存储名称',
  `file_path` varchar(255) DEFAULT NULL COMMENT '文件服务器路径',
  `mime_type` varchar(255) DEFAULT NULL COMMENT '文件的mimetype',
  `file_ext` varchar(255) DEFAULT NULL COMMENT '文件的后缀',
  `file_size` int(11) DEFAULT NULL COMMENT '文件大小',
  `md5` varchar(64) DEFAULT NULL COMMENT 'md5编码',
  `create_datetime` datetime DEFAULT NULL COMMENT '文件创建日期',
  `is_lock` int(2) DEFAULT NULL COMMENT '是否锁,1lock,0:unlock',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `ves_lock` */

DROP TABLE IF EXISTS `ves_lock`;

CREATE TABLE `ves_lock` (
  `NAME` varchar(200) NOT NULL,
  `node` varchar(200) NOT NULL COMMENT '字段注释',
  `expire_time` datetime DEFAULT NULL COMMENT 'column remarks',
  PRIMARY KEY (`NAME`)
) ENGINE=MEMORY DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `ves_node` */

DROP TABLE IF EXISTS `ves_node`;

CREATE TABLE `ves_node` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '节点id',
  `name` varchar(500) DEFAULT NULL COMMENT '节点名称',
  `ip` varchar(100) DEFAULT NULL COMMENT '节点ip',
  `port` int(5) DEFAULT NULL COMMENT '节点端口',
  `func` varchar(50) DEFAULT NULL COMMENT '节点类型',
  `groupid` int(10) DEFAULT NULL COMMENT '节点所在分组',
  `priority` int(5) DEFAULT NULL COMMENT '节点优先级',
  `alive` tinyint(1) DEFAULT NULL COMMENT 'column remarks',
  `installpath` varchar(500) DEFAULT NULL COMMENT '节点的安装地址',
  `startuptime` bigint(20) DEFAULT NULL COMMENT '节点启动时间',
  `updatetime` bigint(20) DEFAULT NULL COMMENT '节点数据更新时间',
  `description` varchar(500) DEFAULT NULL COMMENT 'column remarks',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ves_node_index1` (`ip`,`port`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `ves_node_group` */

DROP TABLE IF EXISTS `ves_node_group`;

CREATE TABLE `ves_node_group` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '字段注释',
  `name` varchar(200) DEFAULT NULL COMMENT '字段注释',
  `func` varchar(200) DEFAULT NULL COMMENT '字段注释',
  `parentid` int(20) DEFAULT NULL COMMENT '字段注释',
  `grouplevel` int(20) DEFAULT NULL COMMENT 'column remarks',
  `updatetime` bigint(20) DEFAULT NULL COMMENT '字段注释',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `ves_node_heartbeat` */

DROP TABLE IF EXISTS `ves_node_heartbeat`;

CREATE TABLE `ves_node_heartbeat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `nodeid` int(10) NOT NULL COMMENT '节点id',
  `occurtime` bigint(20) NOT NULL COMMENT '节点心跳发送时间',
  `nextoccurtime` bigint(20) NOT NULL COMMENT '节点心跳下一次应该的发送时间',
  `expireoccurtime` bigint(20) DEFAULT NULL COMMENT '节点心跳的超时时间',
  `occurcount` bigint(20) NOT NULL COMMENT '心跳的次数。刚刚启动时为1，然后递增。重启以后，又从1开始。如此迭代',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `ves_node_heartbeat_check` */

DROP TABLE IF EXISTS `ves_node_heartbeat_check`;

CREATE TABLE `ves_node_heartbeat_check` (
  `nodeid` int(10) NOT NULL AUTO_INCREMENT COMMENT '字段注释',
  `ckcount` bigint(20) NOT NULL DEFAULT '0',
  `hbcount` bigint(20) NOT NULL COMMENT '字段注释',
  `hboccurtime` bigint(20) DEFAULT '0',
  PRIMARY KEY (`nodeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `ves_platform_sequence` */

DROP TABLE IF EXISTS `ves_platform_sequence`;

CREATE TABLE `ves_platform_sequence` (
  `seq_name` varchar(50) NOT NULL COMMENT '字段注释',
  `cur_val` bigint(20) unsigned NOT NULL DEFAULT '100000',
  `cache_count` int(10) unsigned NOT NULL DEFAULT '500',
  PRIMARY KEY (`seq_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自增主键生成表';

/*Table structure for table `ves_spl_mng_wbh_biz_exp` */

DROP TABLE IF EXISTS `ves_spl_mng_wbh_biz_exp`;

CREATE TABLE `ves_spl_mng_wbh_biz_exp` (
  `id` bigint(20) NOT NULL COMMENT '字段注释',
  `report_id` bigint(20) NOT NULL COMMENT '对应业务报表的id',
  `available` int(11) NOT NULL COMMENT '百分比的数字部分',
  `mttr` int(11) NOT NULL COMMENT '字段注释',
  `mtbf` int(11) NOT NULL COMMENT '字段注释',
  `down_times` int(11) NOT NULL COMMENT '字段注释',
  `down_duration` int(11) NOT NULL COMMENT '字段注释',
  `alarm_times` int(11) NOT NULL COMMENT '字段注释',
  `unrecovery_alarm_times` int(11) NOT NULL COMMENT '字段注释',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '期望值产生时间',
  `creator_id` bigint(20) NOT NULL DEFAULT '0',
  `creator_name` varchar(30) NOT NULL DEFAULT '0',
  `is_inform` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1已经通知 0未通知',
  `inform_time` datetime DEFAULT NULL COMMENT 'column remarks',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用于存储极简模式管理者视角中业务报表的期望值\r\n该表拥有一条主键为1的初始化sql,用来设置期望值的默认值';

/*Table structure for table `ves_ssoforthird_basic` */

DROP TABLE IF EXISTS `ves_ssoforthird_basic`;

CREATE TABLE `ves_ssoforthird_basic` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `name` varchar(32) DEFAULT '' COMMENT '名字',
  `wsdl_url` varchar(200) DEFAULT '' COMMENT 'url',
  `describle` varchar(200) DEFAULT '' COMMENT '描述',
  `is_open` tinyint(4) DEFAULT '0' COMMENT '是否开启，默认0-不开启，1-开启',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `ves_sys_auditlog` */

DROP TABLE IF EXISTS `ves_sys_auditlog`;

CREATE TABLE `ves_sys_auditlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `oper_user` varchar(32) NOT NULL COMMENT '操作人',
  `oper_ip` varchar(15) NOT NULL COMMENT '操作人ip',
  `oper_module` varchar(50) NOT NULL COMMENT '操作模块',
  `module_id` bigint(20) NOT NULL COMMENT '字段注释',
  `oper_type` varchar(10) NOT NULL COMMENT '操作类型',
  `oper_object` varchar(200) NOT NULL COMMENT '操作对象',
  `oper_date` datetime NOT NULL COMMENT '操作时间',
  `del_status` varchar(10) DEFAULT NULL COMMENT 'column remarks',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='审计日志';

/*Table structure for table `ves_sys_config` */

DROP TABLE IF EXISTS `ves_sys_config`;

CREATE TABLE `ves_sys_config` (
  `id` bigint(20) NOT NULL COMMENT '字段注释',
  `content` varchar(6000) DEFAULT NULL COMMENT 'column remarks',
  `description` varchar(200) DEFAULT NULL COMMENT '用于描述本行数据内容结构，所属模块等',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `ves_sys_dict` */

DROP TABLE IF EXISTS `ves_sys_dict`;

CREATE TABLE `ves_sys_dict` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` varchar(30) NOT NULL COMMENT '分类',
  `CODE` varchar(20) NOT NULL COMMENT '代码，不可更改',
  `NAME` varchar(30) NOT NULL COMMENT '显示值，可更改',
  `p_code` varchar(20) NOT NULL DEFAULT '0' COMMENT '保留值，表示父节点的code码，默认值为0表示没有父节点',
  `is_valid` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0表示无效、1表示有效',
  `description` varchar(50) DEFAULT NULL COMMENT 'column remarks',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`,`CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8 COMMENT='数据字典';

/*Table structure for table `ves_sys_domain` */
DROP TABLE IF EXISTS `itsm_notice_clicks`;
CREATE TABLE `itsm_notice_clicks` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `noticeId` int(11) DEFAULT NULL COMMENT '通告表id',
  `clicks` bigint(20) DEFAULT NULL COMMENT '通告点击数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ves_sys_domain`;

CREATE TABLE `ves_sys_domain` (
  `id` bigint(20) NOT NULL COMMENT '字段注释',
  `NAME` varchar(20) NOT NULL COMMENT '字段注释',
  `description` varchar(200) DEFAULT NULL COMMENT 'column remarks',
  `creator_id` bigint(20) NOT NULL COMMENT '字段注释',
  `created_time` datetime NOT NULL COMMENT '字段注释',
  `STATUS` smallint(6) NOT NULL DEFAULT '1' COMMENT '0停用；1启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='域表';

DROP TABLE IF EXISTS `itsm_duty_list`;
CREATE TABLE `itsm_duty_list` (
  `id` int(11) NOT NULL,
  `startTime` date DEFAULT NULL COMMENT '开始时间',
  `endTime` date DEFAULT NULL COMMENT '结束世家',
  `domainId` bigint(20) DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL COMMENT '是否顺延0-否 1-是',
  `afterStartTime` date DEFAULT NULL COMMENT '顺延开始时间',
  `afterEndTime` date DEFAULT NULL COMMENT '顺延结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新值班班次表'

DROP TABLE IF EXISTS `itsm_duty_log`;

Create TABLE `itsm_duty_log` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `dutyId` int(11) NOT NULL COMMENT '值班表主键ID',
  `dutyTypeId` bigint(20) NOT NULL COMMENT '班次',
  `subDutyUserId` bigint(20) NOT NULL COMMENT '提交人ID',
  `dutyLog` varchar(1000) NOT NULL COMMENT '值班日志',
  `subDutyLogTime` datetime NOT NULL COMMENT '值班日志提交时间',
  `logStatus` tinyint(4) NOT NULL COMMENT '1-日志禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8





/*Table structure for table `ves_sys_domain_dcs_rel` */

DROP TABLE IF EXISTS `ves_sys_domain_dcs_rel`;

CREATE TABLE `ves_sys_domain_dcs_rel` (
  `domain_id` bigint(20) NOT NULL COMMENT '字段注释',
  `dcs_id` bigint(20) NOT NULL COMMENT '字段注释',
  `is_checked` smallint(6) NOT NULL DEFAULT '0' COMMENT '0没有选中；1选中',
  PRIMARY KEY (`domain_id`,`dcs_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `ves_sys_group_domain_rel` */

DROP TABLE IF EXISTS `ves_sys_group_domain_rel`;

CREATE TABLE `ves_sys_group_domain_rel` (
  `group_id` bigint(20) NOT NULL COMMENT '字段注释',
  `domain_id` bigint(20) NOT NULL COMMENT '字段注释',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `ves_sys_role_right_rel` */

DROP TABLE IF EXISTS `ves_sys_role_right_rel`;

CREATE TABLE `ves_sys_role_right_rel` (
  `role_id` bigint(20) NOT NULL COMMENT '字段注释',
  `right_id` bigint(20) NOT NULL COMMENT '字段注释',
  PRIMARY KEY (`role_id`,`right_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';

/*Table structure for table `ves_sys_user` */

DROP TABLE IF EXISTS `ves_sys_user`;

CREATE TABLE `ves_sys_user` (
  `id` bigint(20) NOT NULL COMMENT '字段注释',
  `NAME` varchar(20) NOT NULL COMMENT '字段注释',
  `sex` smallint(6) NOT NULL COMMENT '字段注释',
  `account` varchar(32) NOT NULL COMMENT '唯一',
  `PASSWORD` varchar(64) DEFAULT NULL COMMENT 'column remarks',
  `user_type` smallint(6) NOT NULL DEFAULT '1' COMMENT '1普通用户；2域管理员；3系统管理员；4超级管理员',
  `mobile` varchar(20) DEFAULT NULL COMMENT 'column remarks',
  `email` varchar(64) DEFAULT NULL COMMENT 'column remarks',
  `STATUS` smallint(6) NOT NULL DEFAULT '1' COMMENT '0停用；1启用',
  `creator_id` bigint(20) NOT NULL COMMENT '字段注释',
  `created_time` datetime NOT NULL COMMENT '字段注释',
  `lock_type` smallint(6) DEFAULT NULL COMMENT '锁定类型 1、被管理员锁定 2、密码输入超过规定次数 3、密码有效期超过规定时间',
  `lock_time` datetime DEFAULT NULL COMMENT '锁定时间',
  `up_pass_time` datetime DEFAULT NULL COMMENT '修改密码时间',
  `pass_error_cnt` smallint(6) DEFAULT '0' COMMENT '密码错误次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Table structure for table `ves_third_system` */

DROP TABLE IF EXISTS `ves_third_system`;

CREATE TABLE `ves_third_system` (
  `systemid` bigint(20) NOT NULL COMMENT '第三方系统id',
  `name` varchar(30) NOT NULL COMMENT '第三方系统名称',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatetime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `protocol` varchar(20) NOT NULL COMMENT '协议',
  `remark` varchar(50) NOT NULL COMMENT '备注',
  `ip` varchar(20) NOT NULL COMMENT '第三方系统IP',
  `serviceaddress` varchar(20) NOT NULL COMMENT '服务地址',
  PRIMARY KEY (`systemid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方系统表';

/*Table structure for table `ves_thirdsys_basic` */

DROP TABLE IF EXISTS `ves_thirdsys_basic`;

CREATE TABLE `ves_thirdsys_basic` (
  `id` bigint(20) DEFAULT NULL COMMENT '主键id',
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `wsdl_url` varchar(200) DEFAULT NULL COMMENT 'url',
  `describle` varchar(200) DEFAULT NULL COMMENT '描述',
  `state` varchar(20) DEFAULT 'success' COMMENT '同步状态-成功-success 失败-fail',
  `is_open` tinyint(4) DEFAULT '0' COMMENT '默认0-不开启，1-开启'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ves_user_resource_rel` */

DROP TABLE IF EXISTS `ves_user_resource_rel`;

CREATE TABLE `ves_user_resource_rel` (
  `user_id` bigint(20) NOT NULL COMMENT '字段注释',
  `domain_id` bigint(20) NOT NULL COMMENT '字段注释',
  `resource_id` bigint(20) NOT NULL COMMENT '字段注释',
  `type` tinyint(4) NOT NULL COMMENT '1-资源 2-资源组,3业务，4，拓扑',
  `readonly` char(1) NOT NULL DEFAULT '0' COMMENT '0代表无，1代表有',
  `readwrite` char(1) NOT NULL DEFAULT '0' COMMENT '0代表无，1代表有',
  PRIMARY KEY (`user_id`,`resource_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表注释';


DROP TABLE IF EXISTS `itsm_event_message`;

CREATE TABLE `itsm_event_message` (
  `id` int(11) NOT NULL COMMENT '主键',
  `type` int(11) DEFAULT NULL COMMENT '提醒类型1新任务2即将超时3已超时4催单5巡检提醒',
  `create_date` datetime DEFAULT NULL COMMENT '消息发送时间',
  `if_read` tinyint(1) DEFAULT '0' COMMENT '是否已读',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除0否1是',
  `receive_user` varchar(64) DEFAULT NULL COMMENT '消息接收人',
  `message` text COMMENT '消息内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `itsm_event_satisfaction_label` */

DROP TABLE IF EXISTS `itsm_event_satisfaction_label`;

CREATE TABLE `itsm_event_satisfaction_label` (
  `id` int(11) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '标签名称',
  `isOpen` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否开启评价；0不开启，1开启',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `itsm_cmdb_type_configuration`;
CREATE TABLE `itsm_cmdb_type_configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `parent_id` bigint(20) NOT NULL COMMENT '父级id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_type_data`;
CREATE TABLE `itsm_cmdb_type_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `describe` varchar(100) DEFAULT NULL COMMENT '描述',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级id',
  `type_id` bigint(20) NOT NULL COMMENT '类型定义表的id',
  `col_a` varchar(100) DEFAULT NULL COMMENT '冗余字段1',
  `col_b` varchar(100) DEFAULT NULL COMMENT '冗余字段2',
  `col_c` varchar(100) DEFAULT NULL COMMENT '冗余字段3',
  `col_d` varchar(100) DEFAULT NULL COMMENT '冗余字段4',
  `col_e` varchar(100) DEFAULT NULL COMMENT '冗余字段5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_position`;
CREATE TABLE `itsm_cmdb_position` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `p_name` varchar(255) NOT NULL COMMENT '位置名称',
  `state` int(11) NOT NULL COMMENT '状态',
  `org` int(11) NOT NULL COMMENT '所属组织',
  `type` varchar(255) NOT NULL COMMENT '类型',
  `city` varchar(255) DEFAULT NULL COMMENT '城市',
  `country` varchar(255) DEFAULT NULL COMMENT '国家',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_cm_contacts`;
CREATE TABLE `itsm_cmdb_cm_contacts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '联系人名字',
  `organization` varchar(100) NOT NULL COMMENT '联系人所在组织',
  `position` varchar(100) NOT NULL COMMENT '联系人位置',
  `email` varchar(30) NOT NULL COMMENT '联系人邮箱',
  `mobile` varchar(20) NOT NULL COMMENT '联系人手机号',
  `is_activity` char(2) NOT NULL COMMENT '该联系人是否活动，1活动，0未活动',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_cm_software_directory`;
CREATE TABLE `itsm_cmdb_cm_software_directory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '软件名称',
  `supplier` varchar(100) NOT NULL COMMENT '软件供应商',
  `version` varchar(100) NOT NULL COMMENT '软件版本',
  `type` varchar(100) NOT NULL COMMENT '软件类别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_category`;
CREATE TABLE `itsm_cmdb_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `_name` varchar(255) NOT NULL COMMENT '分级名称',
  `_code` varchar(255) NOT NULL COMMENT '分级代码',
  `icon` varchar(255) NOT NULL COMMENT '图标路径',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '级数',
  `parentId` int(11) NOT NULL COMMENT '父级id',
  `isPreset` int(11) DEFAULT '0' COMMENT '是否是预置数据',
  PRIMARY KEY (`id`),
  UNIQUE KEY `_code` (`_code`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_model`;
CREATE TABLE `itsm_cmdb_model` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `_name` varchar(255) NOT NULL COMMENT '模型名称',
  `_code` varchar(255) NOT NULL COMMENT '模型代码',
  `icon` varchar(255) DEFAULT '' COMMENT '模型图标',
  `parentId` int(11) DEFAULT NULL,
  `isPreset` int(11) DEFAULT '0' COMMENT '是否是预置数据',
  PRIMARY KEY (`id`),
  UNIQUE KEY `_code` (`_code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_model_def`;
CREATE TABLE `itsm_cmdb_model_def` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `model_id` int(11) NOT NULL,
  `_view` varchar(255) NOT NULL COMMENT '属性布局',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_model_def_attr_lump`;
CREATE TABLE `itsm_cmdb_model_def_attr_lump` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `model_def_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `position` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_model_def_attr_item`;
CREATE TABLE `itsm_cmdb_model_def_attr_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `model_def_attr_lump_id` int(11) NOT NULL,
  `attr_id` int(11) NOT NULL,
  `sort` int(11) DEFAULT NULL,
  `isRequired` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_model_def_link`;
CREATE TABLE `itsm_cmdb_model_def_link` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `model_def_id` int(11) NOT NULL,
  `type` int(11) NOT NULL COMMENT '0为关联的CI模型，1为关联的功能项',
  `link_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_form_sla_task`;
CREATE TABLE `itsm_form_sla_task` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `taskType` int(2) DEFAULT NULL COMMENT '任务类型,1:响应超计算,2:解决时限计算',
  `formId` varchar(64) NOT NULL COMMENT '表单id',
  `workSheetId` varchar(50) NOT NULL COMMENT '工单号',
  `formSubject` varchar(100) DEFAULT NULL COMMENT '工单主题',
  `processDefKey` varchar(100) DEFAULT NULL COMMENT '流程定义key',
  `slaId` bigint(20) NOT NULL COMMENT 'SLA id',
  `expireLength` bigint(20) DEFAULT NULL COMMENT 'ms，超时时间长度。冗余信息，不参与计算',
  `startDate` datetime DEFAULT NULL COMMENT '开始计算时间点。冗余信息，不参与计算。',
  `message` varchar(500) DEFAULT NULL COMMENT '超时提醒消息',
  `expireDate` datetime NOT NULL COMMENT '超时时间点',
  `notifyDate` datetime NOT NULL COMMENT '超时前提前时间点',
  `notifyFlag` tinyint(1) DEFAULT '0' COMMENT '是否已经通知',
  `pauseDate` datetime DEFAULT NULL COMMENT '暂停时间点',
  `pauseLength` bigint(20) NOT NULL DEFAULT '0' COMMENT '暂停总时长。可以多次暂停',
  `pause` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否暂停计算',
  `hasExpired` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已经超时，超时停止计算',
  PRIMARY KEY (`id`),
  FULLTEXT KEY `itsm_form_sla_task_formid` (`formId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_document`;
CREATE TABLE `itsm_cmdb_document` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '文档名称',
  `org_id` int(11) NOT NULL,
  `state` int(11) NOT NULL COMMENT '文档状态，1：草案 2：废弃 3：已发布',
  `type` int(11) NOT NULL COMMENT '文档类型',
  `description` varchar(255) NOT NULL COMMENT '文档注释',
  `version` varchar(64) DEFAULT NULL COMMENT '文档版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_document_attachment`;
CREATE TABLE `itsm_cmdb_document_attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL COMMENT '文件名称',
  `file_id` int(11) NOT NULL COMMENT '文件id',
  `doc_id` int(11) NOT NULL COMMENT '文档id',
  `upload_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '附件长传时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `itsm_event_satisfaction_label` */
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `itsm_cmdb_ci_relation`;
CREATE TABLE `itsm_cmdb_ci_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_id` bigint(20) NOT NULL COMMENT 'ci项id',
  `r_type_id` bigint(20) NOT NULL COMMENT '被关联的数据id',
  `r_attr_id` bigint(20) NOT NULL COMMENT 'ci项关联属性的id',
  `r_type` int(5) NOT NULL COMMENT '被关联的数据类型，1联系人2工单3ci项4文档',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_item_contacts_relational`;
CREATE TABLE `itsm_cmdb_item_contacts_relational` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_id` bigint(20) NOT NULL,
  `contacts_id` bigint(20) NOT NULL COMMENT '联系人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS ves_sys_date_sequence;
CREATE TABLE `ves_sys_date_sequence` (
  `module_key` varchar(100) NOT NULL COMMENT '模块key',
  `sequence_date` varchar(12) NOT NULL COMMENT '模块日期',
  `sequence_value` bigint(20) NOT NULL DEFAULT '1' COMMENT 'seq值',
  PRIMARY KEY (`module_key`,`sequence_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_inspect_asset`;
CREATE TABLE `itsm_inspect_asset` (
  `planId` bigint(20) unsigned DEFAULT NULL COMMENT '计划ID',
  `assetId` bigint(20) unsigned DEFAULT NULL COMMENT '关联的资产Id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_inspect_peotime`;
CREATE TABLE `itsm_inspect_peotime` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `planId` bigint(20) unsigned DEFAULT NULL COMMENT '计划ID',
  `timeType` varchar(150) DEFAULT NULL COMMENT '根据类型产生相应的时间表达式；格式为：每天，每周，每月，每季度第几月，第几天',
  `inspectDate` date DEFAULT NULL COMMENT '值班日期',
  `startTime` varchar(100) DEFAULT NULL COMMENT '巡检时间；格式00：00',
  `endTime` varchar(20) DEFAULT NULL,
  `timeOver` varchar(20) DEFAULT NULL,
  `userId` bigint(20) unsigned DEFAULT NULL COMMENT '巡检人ID',
  `userName` varchar(50) DEFAULT NULL COMMENT '巡检人名字',
  `contactsId` bigint(20) DEFAULT NULL COMMENT '联系人id',
  `contactsName` varchar(50) DEFAULT NULL COMMENT '联系人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8


DROP TABLE IF EXISTS `itsm_inspect_plan`;
CREATE TABLE `itsm_inspect_plan` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '巡检计划Id',
  `planSheetId` varchar(50) DEFAULT NULL COMMENT '计划编号',
  `state` tinyint(1) unsigned NOT NULL COMMENT '计划是否生效，1有效时间；0永久有效',
  `planStartTime` datetime DEFAULT NULL COMMENT '计划开始时间',
  `planEndTime` datetime DEFAULT NULL COMMENT '计划结束时间',
  `isDelete` tinyint(1) unsigned DEFAULT '0' COMMENT '是否已经删除了计划。1已经删除。0没有删除',
  `isStart` tinyint(1) unsigned DEFAULT NULL COMMENT '是否启用；1启用，0停用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC


DROP TABLE IF EXISTS `itsm_inspect_mould`;

CREATE TABLE `itsm_inspect_mould` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '序号',
  `mouldName` varchar(255) DEFAULT NULL COMMENT '名称',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `state` tinyint(1) unsigned DEFAULT '0' COMMENT '模板状态，1生效；0失效',
  `mouldFileId` bigint(20) DEFAULT NULL COMMENT '模板关联的文件id',
  `mouldFileName` varchar(255) DEFAULT NULL COMMENT '模板文件名',
  `domainId` bigint(20) unsigned DEFAULT NULL COMMENT '所属域Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC




DROP TABLE IF EXISTS `itsm_plan_inform`;

CREATE TABLE `itsm_plan_inform` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `planId` bigint(20) unsigned DEFAULT NULL COMMENT '计划ID',
  `title` varchar(200) DEFAULT NULL COMMENT '计划标题',
  `timeOver` int(11) unsigned DEFAULT NULL COMMENT '巡检时限；单位：小时',
  `mouldId` bigint(20) unsigned DEFAULT NULL COMMENT '巡检模板Id',
  `serviceId` bigint(20) unsigned DEFAULT NULL COMMENT '所属业务Id',
  `domainId` bigint(20) unsigned DEFAULT NULL COMMENT '所属域Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `itsm_plan_rule`;
CREATE TABLE `itsm_plan_rule`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键---规则Id',
  `planId` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '计划ID',
  `type` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '巡检类型，0每天；1每周；2每月；3每季度',
  `remindTime` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '提醒时间，单位分钟',
  `isEmail` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否邮箱提醒，1是；0否',
  `isTelphone` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否短信提醒，1是；0否',
  `isInsider` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否站内提醒，1是；0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `itsm_inspect_task`;

CREATE TABLE `itsm_inspect_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `planId` bigint(20) DEFAULT NULL COMMENT '计划表id',
  `planSheetId` varchar(50) DEFAULT NULL COMMENT '计划编号',
  `taskSheetId` varchar(50) DEFAULT NULL COMMENT '任务编号',
  `inspectionReport` varchar(255) DEFAULT NULL COMMENT '巡检报告',
  `reportTime` datetime DEFAULT NULL COMMENT '报告时间',
  `userName` varchar(50) DEFAULT NULL COMMENT '巡检人',
  `userId` bigint(20) DEFAULT NULL COMMENT '巡检人Id',
  `timeOver` bigint(20) DEFAULT NULL COMMENT '巡检时限',
  `inspectionStartTime` datetime DEFAULT NULL COMMENT '巡检开始时间',
  `inspectionEndDate` datetime DEFAULT NULL,
  `taskState` varchar(100) DEFAULT NULL COMMENT '巡检状态',
  `serviceId` bigint(20) unsigned DEFAULT NULL COMMENT '所属服务Id',
  `fileId` bigint(20) unsigned DEFAULT NULL COMMENT '巡检报告文件Id',
  `mouldId` bigint(20) unsigned DEFAULT NULL COMMENT '模板文件Id',
  `remindTime` bigint(20) unsigned DEFAULT NULL COMMENT '巡检提醒',
  `isEmail` tinyint(1) unsigned DEFAULT NULL COMMENT '邮箱提醒 0、不提醒。1、提醒',
  `isTelphone` tinyint(1) unsigned DEFAULT NULL COMMENT '短信提醒 0、不提醒、1提醒',
  `isInsider` tinyint(1) unsigned DEFAULT NULL COMMENT '是否站内提醒；0不提醒、1提醒',
  `domainId` bigint(20) unsigned DEFAULT NULL COMMENT '所属域Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=223501 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC




DROP TABLE IF EXISTS `itsm_inspect_cycle`;
CREATE TABLE `itsm_inspect_cycle` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '表的Id值',
  `planId` bigint(20) unsigned DEFAULT NULL COMMENT '计划Id',
  `updateDate` datetime DEFAULT NULL COMMENT '计划更新的时间点',
  `cycleDate` datetime DEFAULT NULL COMMENT '计划周期时间点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_model_audit`;
CREATE TABLE `itsm_cmdb_model_audit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `modelId` bigint(20) NOT NULL COMMENT '操作模型的id',
  `modelName` varchar(100) NOT NULL COMMENT '操作模型的名称',
  `type` int(3) NOT NULL COMMENT '操作类型，0增，1删，2改',
  `operation_user` bigint(20) NOT NULL COMMENT '操作人id',
  `operation_userName` varchar(100) NOT NULL COMMENT '操作人名称',
  `operation_content` varchar(3000) DEFAULT NULL COMMENT '操作内容',
  `operation_date` datetime(6) NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `itsm_cmdb_asset_manage_m`;
CREATE TABLE `itsm_cmdb_asset_manage_m` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `asset_manage_def_id` bigint(20) NOT NULL,
  `model_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_asset_manage_h`;
CREATE TABLE `itsm_cmdb_asset_manage_h` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `asset_manage_def_id` bigint(20) NOT NULL,
  `header_attr_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_cmdb_asset_manage_d`;
CREATE TABLE `itsm_cmdb_asset_manage_d` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_inspect_file`;
CREATE TABLE `itsm_inspect_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fileId` bigint(255) NOT NULL COMMENT '文件id',
  `fileName` varchar(255) NOT NULL COMMENT '文件名',
  `uploadUserId` bigint(50) NOT NULL COMMENT '上传用户id',
  `uploadTime` datetime NOT NULL COMMENT '上传时间',
  `taskNumber` varchar(50) NOT NULL COMMENT '任务编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_audit_function_model`;
CREATE TABLE `itsm_audit_function_model` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `module_key` varchar(255) NOT NULL COMMENT '模块自定义字段',
  `module_value` varchar(255) NOT NULL COMMENT '模块自定义字段名',
  `parent_id` int(10) NOT NULL COMMENT '模块父id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `itsm_audit_log`;
CREATE TABLE `itsm_audit_log` (
  `id` varchar(100) NOT NULL,
  `user_id` varchar(100) NOT NULL COMMENT '操作人id',
  `operation_obj_id` varchar(20) NOT NULL COMMENT '操作对象id',
  `function_modle_id` varchar(100) NOT NULL COMMENT '操作模块function_model的key',
  `operation_type` varchar(100) NOT NULL COMMENT '操作类型',
  `operation_content` varchar(255) NOT NULL COMMENT '操作内容',
  `operation_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS VES_QA_FIRED_TRIGGERS;
DROP TABLE IF EXISTS VES_QA_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS VES_QA_SCHEDULER_STATE;
DROP TABLE IF EXISTS VES_QA_LOCKS;
DROP TABLE IF EXISTS VES_QA_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS VES_QA_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS VES_QA_CRON_TRIGGERS;
DROP TABLE IF EXISTS VES_QA_BLOB_TRIGGERS;
DROP TABLE IF EXISTS VES_QA_TRIGGERS;
DROP TABLE IF EXISTS VES_QA_JOB_DETAILS;
DROP TABLE IF EXISTS VES_QA_CALENDARS;

CREATE TABLE VES_QA_JOB_DETAILS(
SCHED_NAME VARCHAR(120) NOT NULL,
JOB_NAME VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
JOB_CLASS_NAME VARCHAR(250) NOT NULL,
IS_DURABLE VARCHAR(1) NOT NULL,
IS_NONCONCURRENT VARCHAR(1) NOT NULL,
IS_UPDATE_DATA VARCHAR(1) NOT NULL,
REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP))
ENGINE=InnoDB;

CREATE TABLE VES_QA_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
JOB_NAME VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
NEXT_FIRE_TIME BIGINT(13) NULL,
PREV_FIRE_TIME BIGINT(13) NULL,
PRIORITY INTEGER NULL,
TRIGGER_STATE VARCHAR(16) NOT NULL,
TRIGGER_TYPE VARCHAR(8) NOT NULL,
START_TIME BIGINT(13) NOT NULL,
END_TIME BIGINT(13) NULL,
CALENDAR_NAME VARCHAR(200) NULL,
MISFIRE_INSTR SMALLINT(2) NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
REFERENCES VES_QA_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP))
ENGINE=InnoDB;

CREATE TABLE VES_QA_SIMPLE_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
REPEAT_COUNT BIGINT(7) NOT NULL,
REPEAT_INTERVAL BIGINT(12) NOT NULL,
TIMES_TRIGGERED BIGINT(10) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES VES_QA_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE VES_QA_CRON_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
CRON_EXPRESSION VARCHAR(120) NOT NULL,
TIME_ZONE_ID VARCHAR(80),
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES VES_QA_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE VES_QA_SIMPROP_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES VES_QA_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE VES_QA_BLOB_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
BLOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES VES_QA_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE VES_QA_CALENDARS (
SCHED_NAME VARCHAR(120) NOT NULL,
CALENDAR_NAME VARCHAR(200) NOT NULL,
CALENDAR BLOB NOT NULL,
PRIMARY KEY (SCHED_NAME,CALENDAR_NAME))
ENGINE=InnoDB;

CREATE TABLE VES_QA_PAUSED_TRIGGER_GRPS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE VES_QA_FIRED_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
ENTRY_ID VARCHAR(95) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
FIRED_TIME BIGINT(13) NOT NULL,
SCHED_TIME BIGINT(13) NOT NULL,
PRIORITY INTEGER NOT NULL,
STATE VARCHAR(16) NOT NULL,
JOB_NAME VARCHAR(200) NULL,
JOB_GROUP VARCHAR(200) NULL,
IS_NONCONCURRENT VARCHAR(1) NULL,
REQUESTS_RECOVERY VARCHAR(1) NULL,
PRIMARY KEY (SCHED_NAME,ENTRY_ID))
ENGINE=InnoDB;

CREATE TABLE VES_QA_SCHEDULER_STATE (
SCHED_NAME VARCHAR(120) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
CHECKIN_INTERVAL BIGINT(13) NOT NULL,
PRIMARY KEY (SCHED_NAME,INSTANCE_NAME))
ENGINE=InnoDB;

CREATE TABLE VES_QA_LOCKS (
SCHED_NAME VARCHAR(120) NOT NULL,
LOCK_NAME VARCHAR(40) NOT NULL,
PRIMARY KEY (SCHED_NAME,LOCK_NAME))
ENGINE=InnoDB;

CREATE INDEX IDX_VES_QA_J_REQ_RECOVERY ON VES_QA_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_VES_QA_J_GRP ON VES_QA_JOB_DETAILS(SCHED_NAME,JOB_GROUP);

CREATE INDEX IDX_VES_QA_T_J ON VES_QA_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_VES_QA_T_JG ON VES_QA_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_VES_QA_T_C ON VES_QA_TRIGGERS(SCHED_NAME,CALENDAR_NAME);
CREATE INDEX IDX_VES_QA_T_G ON VES_QA_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_VES_QA_T_STATE ON VES_QA_TRIGGERS(SCHED_NAME,TRIGGER_STATE);
CREATE INDEX IDX_VES_QA_T_N_STATE ON VES_QA_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_VES_QA_T_N_G_STATE ON VES_QA_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_VES_QA_T_NEXT_FIRE_TIME ON VES_QA_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);
CREATE INDEX IDX_VES_QA_T_NFT_ST ON VES_QA_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
CREATE INDEX IDX_VES_QA_T_NFT_MISFIRE ON VES_QA_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
CREATE INDEX IDX_VES_QA_T_NFT_ST_MISFIRE ON VES_QA_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
CREATE INDEX IDX_VES_QA_T_NFT_ST_MISFIRE_GRP ON VES_QA_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

CREATE INDEX IDX_VES_QA_FT_TRIG_INST_NAME ON VES_QA_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);
CREATE INDEX IDX_VES_QA_FT_INST_JOB_REQ_RCVRY ON VES_QA_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_VES_QA_FT_J_G ON VES_QA_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_VES_QA_FT_JG ON VES_QA_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_VES_QA_FT_T_G ON VES_QA_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_VES_QA_FT_TG ON VES_QA_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);

commit;
