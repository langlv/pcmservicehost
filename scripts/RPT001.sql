/**
 * RPT001:  Drilling workload by machine
 */
delimiter //

DROP PROCEDURE IF EXISTS RPT001 //

CREATE PROCEDURE RPT001(in param  varchar(1000))
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
    

    select a.dmid, b.code, b.name, sum(a.drillmeter) as workload from drlrecmast a, drillingmachine b
    where a.drilltime between fromdate and todate
            and a.dmid = b.dmid
    group by a.dmid;

    -- END BODY
END//

delimiter ;
