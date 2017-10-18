/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  langlv
 * Created: May 21, 2016
 */

delimiter //

DROP function IF exists SPLIT //

/**
 * This function used to split a string by delimiter
 */
CREATE FUNCTION SPLIT(
  x VARCHAR(255),
  delim VARCHAR(12),
  pos INT
)   RETURNS VARCHAR(255)
BEGIN
    RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
        LENGTH(SUBSTRING_INDEX(x, delim, pos -1)) + 1),
        delim, '');
END //


/**
 * This function used to get param value by param index
 * param string format: par1=val1,par2=val2,...
 */
DROP FUNCTION IF EXISTS GET_PARAM_BY_INDEX //

CREATE FUNCTION GET_PARAM_BY_INDEX(
    par_str varchar(4000), 
    idx int
) RETURNS VARCHAR(255)
BEGIN
    RETURN SPLIT(SPLIT(par_str,',',idx),'=',2);
END//




/**
 * This function used to log a debug information
 */

DROP PROCEDURE IF EXISTS LOG_DEBUG//
CREATE PROCEDURE LOG_DEBUG(IN src varchar(1000), in info varchar(4000))
BEGIN
    INSERT INTO DEBUG_TABLE(DEBUG_TIME, DEBUG_SOURCE, DEBUG_TEXT) 
    VALUES(NOW(), src, info);
    
    commit;
END//

delimiter ;





