/* The similarity_graph function returns all the edges of the graph where nodes are strings, and connected components are words within a similarity threshold of each other.  */
/* Usage example: SELECT source, targer FROM similarity_graph('table_name', 'similarity_fn_name', 8) */
/* Assumes that the id column is named 'name_id', and the column of strings is called 'names' */
DROP FUNCTION similarity_graph_string(text, text, float);
CREATE OR REPLACE function similarity_graph_string(table_name text, fn_name text, threshold float, comparator text) RETURNS TABLE (source varchar(100), target varchar(100)) AS
$BODY$
DECLARE
  row record;
  match record;
  ragam_name varchar(100);
  ragam_name_id int;
BEGIN
FOR row IN EXECUTE format('SELECT name_id, name FROM %I;', table_name) LOOP
  ragam_name := row.name;
  ragam_name_id := row.name_id;
  RETURN QUERY EXECUTE format('SELECT name AS source, %1$L::varchar(100) AS target FROM %2$I WHERE name != %1$L AND %3$s(%1$L, name) %5$s %4$s;', ragam_name, table_name, fn_name, threshold, comparator);
  END LOOP;
END;
$BODY$
language 'plpgsql';
