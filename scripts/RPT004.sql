/**
 * RPT004: Drilling productivity by driver
 */
delimiter //

DROP PROCEDURE IF EXISTS RPT004 //

CREATE PROCEDURE RPT004(in param  varchar(1000))
BEGIN
    declare fromdate date;
    declare todate date;
    declare minDrillingDate date;
    declare maxDrillingDate date;
    declare numberOfDays int;
    
    -- error handling part
    DECLARE EXIT HANDLER FOR SQLEXCEPTION    
	BEGIN

	 GET DIAGNOSTICS CONDITION 1
		@p1 = MESSAGE_TEXT;
        
		rollback;
		SELECT @p1;
	END;
    -- end error handler
    
    -- BEGIN BODY

    set fromdate = str_to_date(get_param_by_index(param,1),'%Y%m%d');
    set todate = str_to_date(get_param_by_index(param,2),'%Y%m%d');
    
    SELECT MIN(drilltime) INTO minDrillingDate 
      FROM drlrecmast
    WHERE drilltime BETWEEN fromdate AND todate;
		
    SELECT MAX(drilltime) INTO maxDrillingDate 
      FROM drlrecmast
    WHERE drilltime BETWEEN fromdate AND todate;
		
    set numberOfDays = datediff(maxDrillingDate, minDrillingDate);

    -- str_to_date('20160101','%Y%m%d') and now()

    SELECT 
        b.driverid, 
        b.drivername,
        case
            when numberOfDays < 0 then 0 
            when numberOfDays = 0 then SUM(a.drillmeter) 
            else SUM(a.drillmeter) / numberOfDays 
        end AS productivity
    FROM drlrecmast a, v_drlmachineinfo b
    WHERE a.drilltime BETWEEN fromdate AND todate
          AND a.dmid = b.dmid
    GROUP BY b.driverid;

    -- END BODY
END//

delimiter ;