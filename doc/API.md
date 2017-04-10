## API Endpoints
#### GET /search?query=\<query\>&type=\<type\>
Example:
* Query: `GET /search?query=gaula&type=ragam`

* Response:
```json
Status Code: 200
Body: [{"ragam-id":1813,"name":"Gowla","arohanam":"S R1 M1 P N3 S","avarohanam":"S N3 P M1 R1 G3 M1 R1 S","melakartha":false,"mela-ragam-id":922,"ragam-link":"/wiki/Gowla","data-source":"Wikipedia"},
{"ragam-id":401,"name":"gowla","arohanam":null,"avarohanam":null,"melakartha":null,"mela-ragam-id":null,"ragam-link":null,"data-source":"Karnatik"},
{"ragam-id":867,"name":"gauLa","arohanam":null,"avarohanam":null,"melakartha":null,"mela-ragam-id":null,"ragam-link":null,"data-source":"Sangeethapriya"},
{"ragam-id":1541,"name":"Gowri","arohanam":"S R1 M1 P N3 S","avarohanam":"S N3 D1 P M1 G3 R1 S","melakartha":false,"mela-ragam-id":922,"ragam-link":"","data-source":"Wikipedia"},
{"ragam-id":1316,"name":"Neela","arohanam":"S G3 M1 D2 N2 S","avarohanam":"S N2 D2 M1 G3 S","melakartha":false,"mela-ragam-id":935,"ragam-link":"","data-source":"Wikipedia"},
{"ragam-id":111,"name":"gowri","arohanam":null,"avarohanam":null,"melakartha":null,"mela-ragam-id":null,"ragam-link":null,"data-source":"Karnatik"},
{"ragam-id":1870,"name":"Godari","arohanam":"S R1 G1 R1 M2 G1 M2 P D1 N3 S","avarohanam":"S N3 D1 P M2 R1 S","melakartha":false,"mela-ragam-id":946,"ragam-link":"","data-source":"Wikipedia"},
{"ragam-id":1527,"name":"Gopriya","arohanam":"S R2 G3 M2 D1 N2 S","avarohanam":"S N2 D1 M2 G3 R2 S","melakartha":false,"mela-ragam-id":969,"ragam-link":"","data-source":"Wikipedia"},
{"ragam-id":1002,"name":"Soma","arohanam":"S R1 P M1 D1 N2 S","avarohanam":"S N2 D1 M1 P M1 G3 R1 S","melakartha":false,"mela-ragam-id":921,"ragam-link":"","data-source":"Wikipedia"},
{"ragam-id":418,"name":"bowli","arohanam":null,"avarohanam":null,"melakartha":null,"mela-ragam-id":null,"ragam-link":null,"data-source":"Karnatik"}]
```

#### GET /ragam/<ragam-id>
Example:
* Query: `GET /ragam/34`

* Response:
```json
Status Code: 200
Body: {
"ragam": {"ragam_id":34,"name":"lalitaa","arohanam":null,"avarohanam":null,"wiki_page":null,"melakartha":null,"mela_ragam_id":null,"data_source":"Karnatik"},
"kritis":[
	{"kriti_id":299,"name":"chandiranoliyil","composer":"Subramanya Bhaaratiyaar","taala":"caapu","language":"tamil","ragam_id":34,"url":"http://www.karnatik.com/c0006.shtml","data_source":"Karnatik"},
	{"kriti_id":323,"name":"cedipoke o","composer":"Walajapet Venkatramana Bhaagavatar","taala":"aadi","language":"telugu","ragam_id":34,"url":"http://www.karnatik.com/c3163.shtml","data_source":"Karnatik"}],
"parent-ragam":{"ragam_id":1812,"name":"M?yam?lava Gowla","arohanam":"S R1 G3 M1 P D1 N3 S","avarohanam":"S N3 D1 P M1 G3 R1 S","wiki_page":"/wiki/Mayamalavagowla","melakartha":true,"mela_ragam_id":null,"data_source":"Wikipedia"},"kritis":[],"parent-ragam":null}
}
```
