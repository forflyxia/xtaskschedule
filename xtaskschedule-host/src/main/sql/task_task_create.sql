
CREATE TABLE `task_task` (
	`TaskId` INT 						    NOT NULL 		AUTO_INCREMENT 					COMMENT '任务Id',
	`TaskName` VARCHAR(200) 			    NOT NULL 		DEFAULT '' 						COMMENT '任务名称',
	`NThreadsPerNode` INT 						    NOT NULL 		DEFAULT 1						COMMENT '单节点启用的最大线程数',
	`TaskArgs` VARCHAR(200) 			    NOT NULL 		DEFAULT '' 						COMMENT '参数',
	`RowsPerTime` INT 					    NOT NULL 		DEFAULT 500 						COMMENT '每次加载的数量',
	`DbType` VARCHAR(50) 				    NOT NULL 		DEFAULT '' 						COMMENT '数据库类型:mysql,sqlserver',
	`SqlExecutorType` VARCHAR(50) 		  	NOT NULL 		DEFAULT '' 						COMMENT 'sql执行方式:tablesql,customsql',
	`DbName` VARCHAR(50) 			      	NOT NULL 		DEFAULT '' 						COMMENT '数据库名',
	`TableName` VARCHAR(50) 			    NOT NULL 		DEFAULT '' 						COMMENT '表名',
	`KeyColumnName` VARCHAR(50) 		    NOT NULL 		DEFAULT '' 						COMMENT '表主键名称',
	`KeyColumnType` VARCHAR(50) 		    NOT NULL 		DEFAULT '' 						COMMENT '表主键类型',
	`CustomSqlEquals` VARCHAR(1000) 		NOT NULL 		DEFAULT '' 						COMMENT '自定义sql等于脚本',
	`CustomSqlGreater` VARCHAR(1000) 		NOT NULL 		DEFAULT '' 						COMMENT '自定义sql大于脚本',
	`TaskExecutorType` VARCHAR(50) 		  	NOT NULL 		DEFAULT '' 						COMMENT '任务执行类型:http',
	`DestinationAddress` VARCHAR(200) 		NOT NULL 		DEFAULT '' 						COMMENT '任务目的地址',
	`FailedRetryTimes` INT 				    NOT NULL 		DEFAULT 0 						COMMENT '失败重试次数',
	`HostNodes` VARCHAR(200) 				    NOT NULL 		DEFAULT '' 						COMMENT '所属服务器Sharding节点或ips',
	`Interval` INT(11)                NOT NULL    DEFAULT 0	            COMMENT '间隔时间',
	`IsDownload` INT 			      		NOT NULL 		DEFAULT 0 						COMMENT '是否落地数据',
	`IsLoop` INT 			      		NOT NULL 		DEFAULT 0 						COMMENT '是否循环监测',
	`Version` INT 			      			NOT NULL 		DEFAULT 0 						COMMENT '数据版本',
	`Status` INT 			      		NOT NULL 		DEFAULT 0						COMMENT '是否有效',
	`CreateTime` DATETIME 	            	NOT NULL 		DEFAULT CURRENT_TIMESTAMP		COMMENT '创建时间',
	`DataChange_LastTime` DATETIME 		  	NOT NULL 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP	COMMENT '修改时间',
	PRIMARY KEY (`TaskId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='task需要执行的任务';
