SELECT * FROM task_task WHERE IsActive = 'T';

/******************************************************************************************************************************************/
UPDATE
	task_task
SET
	CustomSqlGreater =
					'SELECT TOP (@count)
	CONVERT(VARCHAR(50), [Pkg]) + \'_\' + CONVERT(VARCHAR(50), [StartCity]) AS [ItemKey],
	MAX([DataChange_LastTime]) AS [DataChange_LastTime]
FROM [PkgNoDeparture] (NOLOCK)
WHERE [DataChange_LastTime] > @datachange_lasttime
GROUP BY [Pkg], [StartCity]
ORDER BY [DataChange_LastTime] ASC, [Pkg] ASC, [StartCity] ASC',
	CustomSqlEquals =
					'SELECT TOP (@count)
	CONVERT(VARCHAR(50), [Pkg]) + \'_\' + CONVERT(VARCHAR(50), [StartCity]) AS [ItemKey],
	MAX([DataChange_LastTime]) AS [DataChange_LastTime]
FROM [PkgNoDeparture] (NOLOCK)
WHERE [DataChange_LastTime] = @datachange_lasttime
      AND [Pkg] > CAST(SUBSTRING(@itemkey, 0, CHARINDEX(\'_\', @itemkey, 1)) AS INT)
      AND [StartCity] > CAST(SUBSTRING(@itemkey, charindex(\'_\', @itemkey, 1) + 1, LEN(@itemkey) - charindex(\'_\', @itemkey, 1)) as  INT)
GROUP BY [Pkg], [StartCity]

ORDER BY [DataChange_LastTime] ASC, [Pkg] ASC, [StartCity] ASC'
where taskname = 'Engine.Tour.ShoppingService.Sync.ProductCloseDate';
/******************************************************************************************************************************************/
UPDATE
	task_task
SET
	CustomSqlGreater =
					'SELECT TOP (@count)
	[ProductID] AS [ItemKey],
        [DataChange_LastTime]
FROM [Prd_ProductSyncTimeStamp] (NOLOCK)
WHERE [DataChange_LastTime] > @datachange_lasttime
      AND Class = \'G\'
ORDER BY DataChange_LastTime ASC, [ProductID] ASC',
	CustomSqlEquals =
					'SELECT TOP (@count)
	[ProductID] AS [ItemKey],
        [DataChange_LastTime]
FROM [Prd_ProductSyncTimeStamp] (NOLOCK)
WHERE [DataChange_LastTime] = @datachange_lasttime
      AND [ProductID] > @itemkey
      AND Class = \'G\'
ORDER BY DataChange_LastTime ASC, [ProductID] ASC'
where taskname = 'Engine.Tour.ShoppingService.Sync.ProductGroup';
/******************************************************************************************************************************************/
UPDATE
	task_task
SET
	CustomSqlGreater =
					'SELECT TOP (@count)
	[ProductID] AS [ItemKey],
        [DataChange_LastTime]
FROM [Prd_ProductSyncTimeStamp] (NOLOCK)
WHERE [DataChange_LastTime] > @datachange_lasttime
      AND Class = \'I\'
ORDER BY DataChange_LastTime ASC, [ProductID] ASC',
	CustomSqlEquals =
					'SELECT TOP (@count)
	[ProductID] AS [ItemKey],
        [DataChange_LastTime]
FROM [Prd_ProductSyncTimeStamp] (NOLOCK)
WHERE [DataChange_LastTime] = @datachange_lasttime
      AND [ProductID] > @itemkey
      AND Class = \'I\'
ORDER BY DataChange_LastTime ASC, [ProductID] ASC'
where taskname = 'Engine.Tour.ShoppingService.Sync.ProductImage';
/******************************************************************************************************************************************/
UPDATE
	task_task
SET
	CustomSqlGreater =
					'SELECT TOP (@count)
	CONVERT(VARCHAR(50), [Pkg]) + \'_\' + CONVERT(VARCHAR(50), [StartCity]) AS [ItemKey],
	DataChange_LastTime
FROM [PkgMinPricePlus] (NOLOCK)
WHERE [DataChange_LastTime] > @datachange_lasttime
ORDER BY DataChange_LastTime ASC, [Pkg] ASC',
	CustomSqlEquals =
					'SELECT TOP (@count)
	CONVERT(VARCHAR(50), [Pkg]) + \'_\' + CONVERT(VARCHAR(50), [StartCity]) AS [ItemKey],
	DataChange_LastTime
FROM [PkgMinPricePlus] (NOLOCK)
WHERE [DataChange_LastTime] = @datachange_lasttime
      AND [Pkg] > CAST(SUBSTRING(@itemkey, 0, CHARINDEX(\'_\', @itemkey, 1)) AS INT)
ORDER BY DataChange_LastTime ASC, [Pkg] ASC'
where taskname = 'Engine.Tour.ShoppingService.Sync.ProductMinPrice';
/******************************************************************************************************************************************/
UPDATE
	task_task
SET
	CustomSqlGreater =
					'SELECT TOP (@count)
	CONVERT(VARCHAR(50), [Pkg]) + \'_\' + CONVERT(VARCHAR(50), [StartCity]) AS [ItemKey],
	MAX([DataChange_LastTime]) AS [DataChange_LastTime]
FROM [PkgMinPrice] (NOLOCK)
WHERE [DataChange_LastTime] > @datachange_lasttime
GROUP BY [Pkg], [StartCity]
ORDER BY [DataChange_LastTime] ASC, [Pkg] ASC, [StartCity] ASC',
	CustomSqlEquals =
					'SELECT TOP (@count)
	CONVERT(VARCHAR(50), [Pkg]) + \'_\' + CONVERT(VARCHAR(50), [StartCity]) AS [ItemKey],
	MAX([DataChange_LastTime]) AS [DataChange_LastTime]
FROM [PkgMinPrice] (NOLOCK)
WHERE [DataChange_LastTime] = @datachange_lasttime
      AND [Pkg] > CAST(SUBSTRING(@itemkey, 0, CHARINDEX(\'_\', @itemkey, 1)) AS INT)
      AND [StartCity] > CAST(SUBSTRING(@itemkey, charindex(\'_\', @itemkey, 1) + 1, LEN(@itemkey) - charindex(\'_\', @itemkey, 1)) as  INT)
GROUP BY [Pkg], [StartCity]

ORDER BY [DataChange_LastTime] ASC, [Pkg] ASC, [StartCity] ASC'
where taskname = 'Engine.Tour.ShoppingService.Sync.ProductMinPriceDailyInfo';
/******************************************************************************************************************************************/
UPDATE
	task_task
SET
	CustomSqlGreater =
					'SELECT TOP (@count)
	CONVERT(VARCHAR(50), [ProductID]) AS [ItemKey],
	[DataChange_LastTime]
FROM
(
	SELECT [ProductID], [ModifyTime] AS [DataChange_LastTime]
	FROM PkgProductDB.dbo.[Prd_ProductSegment](NOLOCK)
	WHERE [ModifyTime] > @datachange_lasttime
	UNION
	(
	SELECT ps.[ProductID], sr.[ModifyTime] AS [DataChange_LastTime]
	FROM [Prd_ProductSegment](NOLOCK)ps
	INNER JOIN [Prd_SegmentResource](NOLOCK) sr
		ON ps.segmentID = sr.segmentID
	WHERE sr.[ModifyTime] > @datachange_lasttime
	)
) AS T
ORDER BY [DataChange_LastTime] ASC, [ProductID] ASC',
	CustomSqlEquals =
					'SELECT TOP (@count)
	CONVERT(VARCHAR(50), [ProductID]) AS [ItemKey],
	[DataChange_LastTime]
FROM
(
	SELECT [ProductID], [ModifyTime] AS [DataChange_LastTime]
	FROM PkgProductDB.dbo.[Prd_ProductSegment](NOLOCK)
	WHERE [ModifyTime] = @datachange_lasttime
		AND [ProductID] > @itemkey
	UNION
	(
	SELECT ps.[ProductID], sr.[ModifyTime] AS [DataChange_LastTime]
	FROM [Prd_ProductSegment](NOLOCK)ps
	INNER JOIN [Prd_SegmentResource](NOLOCK) sr
		ON ps.segmentID = sr.segmentID
	WHERE sr.[ModifyTime] = @datachange_lasttime
		AND ps.[ProductID] > @itemkey
	)
) AS T
ORDER BY [DataChange_LastTime] ASC, [ProductID] ASC'
where taskname = 'Engine.Tour.ShoppingService.Sync.ProductResource';
/******************************************************************************************************************************************/
UPDATE
	task_task
SET
	CustomSqlGreater =
					'SELECT TOP (@count)
	CONVERT(VARCHAR(50), [ProductID]) + \'_\' + CONVERT(VARCHAR(50), [DepartureCityID]) AS [ItemKey],
	DataChange_LastTime
FROM [Prd_ProductRiskSchedule] (NOLOCK)
WHERE [DataChange_LastTime] > @datachange_lasttime
ORDER BY DataChange_LastTime ASC, [ProductID] ASC',
	CustomSqlEquals =
					'SELECT TOP (@count)
	CONVERT(VARCHAR(50), [ProductID]) + \'_\' + CONVERT(VARCHAR(50), [DepartureCityID]) AS [ItemKey],
	DataChange_LastTime
FROM [Prd_ProductRiskSchedule] (NOLOCK)
WHERE [DataChange_LastTime] = @datachange_lasttime
      AND [ProductID] > CAST(SUBSTRING(@itemkey, 0, CHARINDEX(\'_\', @itemkey, 1)) AS INT)
ORDER BY DataChange_LastTime ASC, [ProductID] ASC'
where taskname = 'Engine.Tour.ShoppingService.Sync.ProductRiskSchedule';
/******************************************************************************************************************************************/
UPDATE
	task_task
SET
	CustomSqlGreater =
					'SELECT TOP (@count)
	CONVERT(VARCHAR(50), [Pkg]) + \'_\' + CONVERT(VARCHAR(50), [StartCity]) AS [ItemKey],
	DataChange_LastTime
FROM [PkgDeparture] (NOLOCK)
WHERE [DataChange_LastTime] > @datachange_lasttime
ORDER BY DataChange_LastTime ASC, [Pkg] ASC',
	CustomSqlEquals =
					'SELECT TOP (@count)
	CONVERT(VARCHAR(50), [Pkg]) + \'_\' + CONVERT(VARCHAR(50), [StartCity]) AS [ItemKey],
	DataChange_LastTime
FROM [PkgDeparture] (NOLOCK)
WHERE [DataChange_LastTime] = @datachange_lasttime
      AND [Pkg] > CAST(SUBSTRING(@itemkey, 0, CHARINDEX(\'_\', @itemkey, 1)) AS INT)
ORDER BY DataChange_LastTime ASC, [Pkg] ASC'
where taskname = 'Engine.Tour.ShoppingService.Sync.ProductSchedule';
/******************************************************************************************************************************************/
UPDATE
	task_task
SET
	CustomSqlGreater =
					'SELECT TOP (@count)
	[ProductID] AS [ItemKey],
        [DataChange_LastTime]
FROM [Prd_ProductSyncTimeStamp] (NOLOCK)
WHERE [DataChange_LastTime] > @datachange_lasttime
      AND Class = \'T\'
ORDER BY DataChange_LastTime ASC, [ProductID] ASC',
	CustomSqlEquals =
					'SELECT TOP (@count)
	[ProductID] AS [ItemKey],
        [DataChange_LastTime]
FROM [Prd_ProductSyncTimeStamp] (NOLOCK)
WHERE [DataChange_LastTime] = @datachange_lasttime
      AND [ProductID] > @itemkey
      AND Class = \'T\'
ORDER BY DataChange_LastTime ASC, [ProductID] ASC'
where taskname = 'Engine.Tour.ShoppingService.Sync.ProductTagID';
/******************************************************************************************************************************************/
UPDATE
	task_task
SET
	CustomSqlGreater =
					'SELECT TOP (@count)
	[ProductID] AS [ItemKey],
        [DataChange_LastTime]
FROM [Prd_ProductSyncTimeStamp] (NOLOCK)
WHERE [DataChange_LastTime] > @datachange_lasttime
      AND Class = \'S\'
ORDER BY DataChange_LastTime ASC, [ProductID] ASC',
	CustomSqlEquals =
					'SELECT TOP (@count)
	[ProductID] AS [ItemKey],
        [DataChange_LastTime]
FROM [Prd_ProductSyncTimeStamp] (NOLOCK)
WHERE [DataChange_LastTime] = @datachange_lasttime
      AND [ProductID] > @itemkey
      AND Class = \'S\'
ORDER BY DataChange_LastTime ASC, [ProductID] ASC'
where taskname = 'Engine.Tour.ShoppingService.Sync.ProductTiming';
/******************************************************************************************************************************************/
UPDATE
	task_task
SET
	CustomSqlGreater =
					'SELECT TOP (@count)
	[ProductID] AS [ItemKey],
        [DataChange_LastTime]
FROM [Prd_ProductSyncTimeStamp] (NOLOCK)
WHERE [DataChange_LastTime] > @datachange_lasttime
      AND Class = \'V\'
ORDER BY DataChange_LastTime ASC, [ProductID] ASC',
	CustomSqlEquals =
					'SELECT TOP (@count)
	[ProductID] AS [ItemKey],
        [DataChange_LastTime]
FROM [Prd_ProductSyncTimeStamp] (NOLOCK)
WHERE [DataChange_LastTime] = @datachange_lasttime
      AND [ProductID] > @itemkey
      AND Class = \'V\'
ORDER BY DataChange_LastTime ASC, [ProductID] ASC'
where taskname = 'Engine.Tour.ShoppingService.Sync.ProductVideo';
/******************************************************************************************************************************************/
UPDATE
	task_task
SET
	CustomSqlGreater =
					'SELECT TOP(@count) * FROM
(SELECT
	[ProductID] AS [ItemKey],
    [DataChange_LastTime]
FROM
	[Prd_ProductSyncTimeStamp] (NOLOCK)
WHERE
	[Class] = \'A\' AND
	[DataChange_LastTime] > @datachange_lasttime

UNION

	SELECT
		[ProductID] AS [ItemKey],
		MAX(DataChange_LastTime) AS DataChange_LastTime
	FROM
		[Prd_ProductVisa] (NOLOCK)
	WHERE
		[DataChange_LastTime] > @datachange_lasttime
	GROUP BY
		[ProductID]
) AS T
ORDER BY DataChange_LastTime ASC, [ItemKey] ASC',
	CustomSqlEquals =
					'SELECT TOP(@count) * FROM
(SELECT
	[ProductID] AS [ItemKey],
    [DataChange_LastTime]
FROM
	[Prd_ProductSyncTimeStamp] (NOLOCK)
WHERE
	[Class] = \'A\' AND
	[DataChange_LastTime] = @datachange_lasttime AND
	[ProductID] > @itemkey

UNION

	SELECT
		[ProductID] AS [ItemKey],
		MAX(DataChange_LastTime) AS DataChange_LastTime
	FROM
		[Prd_ProductVisa] (NOLOCK)
	WHERE
		[DataChange_LastTime] =@datachange_lasttime AND
		[ProductID] > @itemkey
	GROUP BY
		[ProductID]
) AS T
ORDER BY DataChange_LastTime ASC, [ItemKey] ASC'
where taskname = 'Engine.Tour.ShoppingService.Sync.ProductVisa';