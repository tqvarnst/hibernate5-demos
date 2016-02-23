--CREATE OR REPLACE FUNCTION add_three_values(v1 anyelement, v2 anyelement, v3 anyelement)
--RETURNS anyelement AS $$
--DECLARE
--    result ALIAS FOR $0;
--BEGIN
--    result := v1 + v2 + v3;
--    RETURN result;
--END;
--$$ LANGUAGE plpgsql;
--
--
--
--CREATE OR REPLACE FUNCTION add_three_values(v1 integer, v2 integer, v3 integer)
--RETURNS integer AS $$
--DECLARE
--    result integer;
--BEGIN
--    result := v1 + v2 + v3;
--    RETURN result;
--END;
--$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION my_sum(x int, y int, OUT sum int) AS 'SELECT x+y' LANGUAGE SQL;	

CREATE OR REPLACE FUNCTION add_three_values(v1 int, v2 int, v3 int, OUT sum int) AS 'SELECT v1+v2+v3' LANGUAGE SQL;