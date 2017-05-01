/* The similarity_graph function returns all the edges of the graph where nodes are strings, and connected components are words within a similarity threshold of each other.  */
/* Usage example: SELECT source, targer FROM similarity_graph('table_name', 'similarity_fn_name', 8) */
/* Assumes that the id column is named 'name_id', and the column of strings is called 'names' */
DROP FUNCTION similarity_graph(text, text, integer);
CREATE OR REPLACE function similarity_graph(table_name text, fn_name text, threshold_min int) RETURNS TABLE (source bigint, target bigint) AS
'
DECLARE
  row record;
  match record;
  ragam_name varchar(100);
  ragam_name_id int;
BEGIN
FOR row IN EXECUTE ''SELECT name_id, name FROM '' || quote_ident(table_name) || '';'' LOOP
  ragam_name := row.name;
  ragam_name_id := row.name_id;
  RETURN QUERY EXECUTE ''SELECT name_id AS source, '' || quote_literal(ragam_name_id) || ''::bigint AS target FROM '' || quote_ident(table_name) || '' WHERE name != '' || quote_literal(ragam_name) || '' AND similarity_score ('' || quote_literal(ragam_name) || '', name) > '' || threshold_min || '';'';
  END LOOP;
END;
'
language 'plpgsql';
