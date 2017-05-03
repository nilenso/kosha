/* The similarity_graph function returns all the edges of the graph where nodes are strings, and connected components are words within a similarity threshold of each other.  */
/* Usage example: SELECT source, targer FROM similarity_graph('table_name', 'similarity_fn_name', 8) */
/* Assumes that the id column is named 'name_id', and the column of strings is called 'names' */
DROP FUNCTION similarity_graph_id(text, text, integer);
CREATE OR REPLACE function similarity_graph_id(table_name text, fn_name text, threshold float, comparator text) RETURNS TABLE (source bigint, target bigint) AS
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
  RETURN QUERY EXECUTE format('SELECT name_id AS source, %1$s::bigint AS target FROM %3$I WHERE name != %2$L AND %4$s(%2$L, name) %6$s %5$s;', ragam_name_id, ragam_name, table_name, fn_name, threshold, comparator);
  END LOOP;
END;
$BODY$
language 'plpgsql';
