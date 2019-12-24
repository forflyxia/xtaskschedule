
CREATE TABLE `task_task_increment` (
	`TaskId` INT 						    NOT NULL 		DEFAULT 0		 				COMMENT '任务Id',
	`MinKey_LastTime` VARCHAR(50) 		  	NOT NULL 		DEFAULT '0' 					COMMENT '上一批数据执行到的最小Key',
	`MinTime_LastTime` VARCHAR(50) 		  	NOT NULL 		DEFAULT '1900-01-01' 			COMMENT '上一批数据执行到的最小时间',
	`MaxKey_LastTime` VARCHAR(50) 		  	NOT NULL 		DEFAULT '0' 					COMMENT '上一批数据执行到的最大Key',
	`MaxTime_LastTime` VARCHAR(50) 		  	NOT NULL 		DEFAULT '1900-01-01' 			COMMENT '上一批数据执行到的最大时间',
	`HasNewData` INT 			      	NOT NULL 		DEFAULT 0 						COMMENT '是否有最新数据',
	`Status` INT 			      		NOT NULL 		DEFAULT 0 						COMMENT '是否有效',
	`CreateTime` DATETIME 	            	NOT NULL 		DEFAULT CURRENT_TIMESTAMP		COMMENT '创建时间',
	`DataChange_LastTime` DATETIME 		  	NOT NULL 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP	COMMENT '修改时间',
	PRIMARY KEY (`TaskId`),
	KEY `ix_task_task_increment_DataChange_LastTime` USING BTREE (`DataChange_LastTime` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='task增量信息';
