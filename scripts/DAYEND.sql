/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  langlv
 * Created: May 21, 2016
 */

/**
 * This procedure used for dayend processing
 */
DROP PROCEDURE IF EXISTS DAYEND;

delimiter //

CREATE PROCEDURE DAYEND(in param  varchar(1000))
BEGIN
    declare currdate date;
    declare cursor_ppid int default 0;
    
    -- pile properties
    declare start_date date;
    declare end_date date;
    declare rld double;
    declare rlp double;
    declare Vdown_max double;
    declare Vdown_avg double;
    declare Vup_max double;
    declare Vup_avg double;
    declare N_min double;
    declare N_avg double;
    declare RQ double;
    declare FQ double;
    declare FN double;
    declare FL double;
    declare FS double;

    -- cement properties
    declare totalCementIn double default 0;
    declare totalCementOut double default 0;
    declare totalCementDrl double default 0;
    declare totalMixingLoss double default 0;
    declare totalDrlLoss double default 0;

    declare end_of_cursor boolean DEFAULT FALSE;
    DECLARE cursor_pplist CURSOR FOR select distinct ppid from drlrecmemo where appsts='A' and endrec=1;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET end_of_cursor = TRUE;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION    
	BEGIN

	 GET DIAGNOSTICS CONDITION 1
		@p1 = MESSAGE_TEXT;
        
            rollback;
            select @p1;
	END;
    
    
    
    set currdate = str_to_date(split(param,'=',2),'%Y%m%d');
    
    -- BEGIN calculate pile information -------------------------
    -- step 1: update general information
    update pileplan pp 
    inner join
            (select a.ppid, a.DMID, a.rectime, d.code, c.eid, e.SID1, b.mpid  
                    from drlrecmemo a, drillingmachine b, employee c, team d, mixingplant e
                    where a.appsts='A' and a.endrec=1 and a.dmid = b.dmid and b.driver1 = c.eid and c.team=d.code and b.mpid=e.mpid
            ) dr on pp.ppid = dr.ppid
    set pp.dmid = dr.dmid, pp.driver = dr.eid, pp.team = dr.code, pp.sid = dr.sid1, pp.mpid = dr.mpid, pp.actend = dr.rectime;
    
    -- step 2: update drilling in progress status
    update pileplan pp
    inner join
            (select distinct ppid from drlrecmemo
            ) dr on pp.ppid = dr.ppid
    set pp.status = 'D'; -- D=drilling
            
    -- step 3: update statistic information
    open cursor_pplist;
    read_loop: LOOP
        FETCH cursor_pplist INTO cursor_ppid;

        IF end_of_cursor THEN
          LEAVE read_loop;
        END IF;

        call log_debug('cursor_ppid',cursor_ppid);


        -- get start drilling date
        select a.rectime into start_date 
        from drlrecmemo a where a.ppid=cursor_ppid and startrec=1;
	
        call log_debug('start_date',start_date);

        -- get end drilling date
        select a.rectime into end_date 
        from drlrecmemo a where a.ppid=cursor_ppid and endrec=1;
	
        call log_debug('end_date',end_date);

        -- get rld
        select ifnull(max(a.deepmeter),0) into rld
	from drlrecmemo a where a.ppid=cursor_ppid and emptydrill=1;
        
        call log_debug('rld',rld);
        -- get rlp
        select ifnull(max(a.deepmeter),0)-rld into rlp
        from drlrecmemo a where a.ppid=cursor_ppid;
        
        call log_debug('rlp',rlp);
        
        -- get Vdown_max
        select ifnull(max(a.drillmeter),0) into Vdown_max
        from drlrecmemo a where a.ppid=cursor_ppid and a.direction=1 and a.emptydrill=0;
        
        call log_debug('Vdown_max',Vdown_max);

        -- get Vdown_avg
        select ifnull(avg(a.drillmeter),0) into Vdown_avg
        from drlrecmemo a where a.ppid=cursor_ppid and a.direction=1 and a.emptydrill=0;
        
        call log_debug('Vdown_avg',Vdown_avg);
    
        -- get Vup_max
        select ifnull(max(a.drillmeter),0) into Vup_max
        from drlrecmemo a where a.ppid=cursor_ppid and a.direction=0 and a.emptydrill=0;
        
        call log_debug('Vup_max',Vup_max);

	-- get Vup_avg
	select ifnull(avg(a.drillmeter),0) into Vup_avg
        from drlrecmemo a where a.ppid=cursor_ppid and a.direction=0 and a.emptydrill=0;

        call log_debug('Vup_avg',Vup_avg);

	-- get N_min, N_avg, RQ
        select min(a.ndn), avg(a.ndn), max(a.rqtotal) into N_min, N_avg, RQ
        from drlrecmemo a where a.ppid=cursor_ppid;
        
        call log_debug('N_min, N_avg, RQ',concat('N_min=',N_min,' N_avg=',N_avg,' RQ=',RQ));

        -- get FQ
        select (ifnull(count(a.drid),0)*0.5/rlp)*100 into FQ
        from drlrecmemo a, pileplan b, piledesign c where a.ppid=cursor_ppid and a.ppid=b.ppid and b.code = c.code and a.rdq < c.qlim;
        
        call log_debug('FQ',FQ);

        -- get FN
        select (sum(ifnull(a.drillmeter,0))/rlp)*100 into FN
        from drlrecmemo a, pileplan b, piledesign c where a.ppid=cursor_ppid and a.ppid=b.ppid and b.code = c.code and a.ndn < c.nlim;

        call log_debug('FN',FN);
        
	-- get FL
        select ifnull((rlp-c.lp/rlp),0)*100 into FL
        from pileplan b, piledesign c where b.ppid=cursor_ppid and b.code = c.code limit 0,1;
        
        call log_debug('FL',FL);

	-- get FS
        select ifnull((RQ-c.Q/RQ),0)*100 into FS
        from pileplan b, piledesign c where b.ppid=cursor_ppid and b.code = c.code limit 0,1; 
				
        call log_debug('FS',FS);

	-- update to pile plan table
        update pileplan a set
            a.actstart = start_date,
            a.actend = end_date,
            a.rld = rld,
            a.rlp = rlp,
            a.dspdmax = Vdown_max,
            a.dspdavg = Vdown_avg,
            a.USPDMAX = Vup_max,
            a.USPDAVG = Vup_avg,
            a.nmin = N_min,
            a.nav = N_avg,
            a.rq = RQ,
            a.fq = FQ,
            a.fn = FN,
            a.fl = FL,
            a.fs = FS,
            a.STATUS = 'C' -- C=completed
        where a.ppid = cursor_ppid;
    
    END LOOP;
    CLOSE cursor_pplist;
	
    -- END
    
    -- calculate cement losses
    -- TODO: will do this later     
    
    


        
    -- clear drlrecmemo for approved records
    drop table if exists completed_piles;
    create temporary table completed_piles ENGINE=MEMORY as (select distinct ppid from drlrecmemo where endrec=1 and appsts='A');
    
    insert into drlrecmast(DRID,PPID,PRID,DMID,DRILLTIME,DEEPMETER,DIRECTION,EMPTYDRILL,DRILLMETER,ROTATEMETER,RQ,RQTOTAL,AMP,PSR,NDN,RDQ,RDQTOTAL,STARTREC,ENDREC,RECBY,RECTIME,APPBY,APPTIME) 
    select DRID,PPID,PRID,DMID,DRILLTIME,DEEPMETER,DIRECTION,EMPTYDRILL,DRILLMETER,ROTATEMETER,RQ,RQTOTAL,AMP,PSR,NDN,RDQ,RDQTOTAL,STARTREC,ENDREC,RECBY,RECTIME,APPBY,APPTIME
    from drlrecmemo where ppid in (select ppid from completed_piles);
    
    delete from drlrecmemo where ppid in (select ppid from completed_piles);
    


    -- commit all changes
    commit;
    select 'DAYEND PROCESS COMPLETED' as status;

END//

delimiter ;