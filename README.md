# kosha

An open carnatic music database in the making.

## Why
Given how much carnatic music revolves around a (more or less) standard list of ragas, compositions, and composers, it is quite surprising that there isn't an authoritative source for this information on this internet today. The information however, is out there in the wild. Needles in the haystack of the internet.

This project exists to mine, categorize and curate that information into a strict database, thereby giving it the power of usability.

## Parts
There are scrapers (that mine for data from select websites), there is the relationship-maker (that connects data from different sources), and there is the database (adheres to schema, stores information, reports). Most of this codebase is in clojure, but there's no reason the scrapers can't be in another language.

## Usage

To run the API server:
`lein run -m kosha.main`
The server runs by default on port 3000. To specify a port, pass a port number using the -p option:
`lein run -m kosha.main -p 8080`

## Development

#### Prerequisites
You should have the following installed on your system:
1. [Clojure](https://clojure.org/guides/getting_started) (>= 1.7.0)
2. [Leiningen](https://leiningen.org/#install) (>= 2.7.0)
3. [Postgres](https://www.postgresql.org/download/) (>= 9.0)

#### Setup (from database dump)

Clone this repo and from the project directory, run:
```
> lein deps
> createdb kosha
> pg_restore -d kosha resources/db.dump
```
You can also use a custom database name by configuring it in `resources/config.edn`.

## Documentation
1. [schema.md](doc/schema.md): Describes the schema of scraped data and the domain entities.
2. [stitching.md](doc/stitching.md): Describes the problem of stitching data from 3 different sources in Kosha.

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
