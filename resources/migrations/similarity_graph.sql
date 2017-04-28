/* The similarity_graph function returns all the edges of the graph where nodes are strings, and connected components are words within a similarity threshold of each other.  */
/* Usage example: similarity_graph('id_col_name', 'string_col_name', 'table_name', 'similarity_fn_name', 8) */
DROP FUNCTION similarity_graph(text, text, text, integer);
DROP TYPE graph_edge;
CREATE TYPE graph_edge AS (name1 int, name2 int);
CREATE OR REPLACE function similarity_graph(id_col text, col text, table_name text, fn_name text, threshold_min int) RETURNS SETOF graph_edge AS
'
DECLARE
  row record;
  match record;
  ragam_name varchar(100);
  ragam_name_id int;
  result graph_edge;
BEGIN
FOR row IN EXECUTE ''SELECT '' || quote_ident(id_col) || '', '' || quote_ident(col)  || '' FROM '' || quote_ident(table_name) || '';'' LOOP
  ragam_name := row.name;
  ragam_name_id := row.r_name_id;
  FOR match IN EXECUTE ''SELECT '' || quote_ident(id_col) || '', '' || quote_ident(col) || '' FROM '' || quote_ident(table_name) || '' WHERE '' || quote_ident(col) || '' != '' || quote_literal(ragam_name) || '' AND similarity_score ('' || quote_literal(ragam_name) || '', '' || quote_ident(col) || '') > '' || threshold_min || '';''
      LOOP
        result.name1 := ragam_name_id;
        result.name2 := match.r_name_id;
      RETURN NEXT result;
    END LOOP;
  END LOOP;
END;
'
language 'plpgsql';
