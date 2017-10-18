/**
 * RPT002:Drilling workload by worker
 */
delimiter //

DROP PROCEDURE IF EXISTS RPT002 //

CREATE PROCEDURE RPT002(in param  varchar(1000))
BEGIN
    declare fromdate date;
    declare todate date;
    
    -- error handling part
    DECLARE EXIT HANDLER FOR SQLEXCEPTION    
	BEGIN

	 GET DIAGNOSTICS CONDITION 1
		@p1 = MESSAGE_TEXT;
        
            rollback;
            select @p1;
	END;
    -- end error handler
    
    -- BEGIN BODY

    set fromdate = str_to_date(get_param_by_index(param,1),'%Y%m%d');
    set todate = str_to_date(get_param_by_index(param,2),'%Y%m%d');
    

    -- str_to_date('20160101','%Y%m%d') and now()

    select b.driverid, b.drivername, sum(a.drillmeter) as workload from drlrecmast a, v_drlmachineinfo b
    where a.drilltime between fromdate and todate
            and a.dmid = b.dmid
    group by b.driverid;

    -- END BODY
END//

delimiter ;
