
CREATE TABLE `task_task_todo_items` (
  `Id`    INT(11)                 NOT NULL AUTO_INCREMENT         COMMENT '主键',
  `BatchNo` CHAR(60)                 NOT NULL DEFAULT ''           COMMENT '落地批次号',
	`TaskId` INT(11) 						    NOT NULL 		DEFAULT 0		 				COMMENT '任务Id',
	`ItemKey` VARCHAR(50) 		  	NOT NULL 		DEFAULT '' 					COMMENT 'ItemKey',
	`ItemKey_DataChange_LastTime` DATETIME NOT NULL 		DEFAULT now() 					COMMENT 'ItemKey对应的更新时间',
	`ShardingNo` INT(11) 		  	NOT NULL 		DEFAULT 0 			COMMENT '执行分片号',
	`Status` INT 			      		NOT NULL 		DEFAULT 0 						COMMENT '是否有效',
	`Priority` INT              NOT NULL 		DEFAULT 0 						COMMENT '优先级',
	`Remark` VARCHAR(500) 			      		NOT NULL 		DEFAULT '' 						COMMENT '备注',
	`CreateTime` DATETIME 	            	NOT NULL 		DEFAULT CURRENT_TIMESTAMP		COMMENT '创建时间',
	`DataChange_LastTime` DATETIME 		  	NOT NULL 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP	COMMENT '修改时间',
	PRIMARY KEY (`Id`),
	KEY `ix_task_task_todoitems_TaskId_ShardingNo` USING BTREE (`TaskId` ASC, `ShardingNo` ASC),
	KEY `ix_task_task_todoitems_BatchNo` USING BTREE (`BatchNo` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='task待处理事项';
