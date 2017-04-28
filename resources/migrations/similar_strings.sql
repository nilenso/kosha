/* similar_strings returns the pairs of all strings which have a specified similarity or higher. */
DROP FUNCTION similar_strings(text, text, text, integer);
CREATE TYPE string_match AS (name1 varchar(100), name2 varchar(100));
CREATE OR REPLACE function similar_strings(col text, table_name text, fn_name text, threshold_min int) RETURNS SETOF string_match AS
'
DECLARE
  row record;
  match record;
  ragam_name varchar(100);
  result string_match;
BEGIN
FOR row IN EXECUTE ''SELECT '' || quote_ident(col)  || '' FROM '' || quote_ident(table_name) || '';'' LOOP
  ragam_name := row.name;
  FOR match IN EXECUTE ''SELECT '' || quote_ident(col) || '' FROM '' || quote_ident(table_name) || '' WHERE '' || quote_ident(col) || '' != '' || quote_literal(ragam_name) || '' AND similarity_score ('' || quote_literal(ragam_name) || '', '' || quote_ident(col) || '') > '' || threshold_min || '';''
      LOOP
        result.name1 := ragam_name;
        result.name2 := match.name;
      RETURN NEXT result;
    END LOOP;
  END LOOP;
END;
'
language 'plpgsql';
