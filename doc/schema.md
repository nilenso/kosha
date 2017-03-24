# Data scraped from different sources

## Sangeethapriya

### kritis
 id |          kriti          |    ragam     | composer
---:|:-----------------------:|:------------:|:----------
  1 | parama_pAvana_guNashAli | kAmavardhini | tyAgarAja


_(2865 entries)_

### renditions
 id  | concert_id |  concert_url  | track |  kriti | ragam  | composer  | main_artist  | ragam_id
-----|:----------:|:-------------:|:-----:|:------:|:------:|:---------:|:------------:|:---------
 102 | 00-C0098   | http://www.sangeethapriya.org/Downloads/mdr/MDR_Concert2/ |   207 | nagumOmu_ganalEni | AbhEri | tyAgarAja | MD Ramanathan |      195


 _(45700 entries)_
 _Has 10552 unique concerts._
 
### tracks

   id   | concert_id | concert_url  | track_number | track_url
-------:|:-----------:|:-----------:|:------------:|:----------
 63723 | 01-C0149   | http://www.sangeethamshare.org/tvg/UPLOADS-0001---0200/122-Savitha_Narasimhan/ |            1 | http://sangeethapriya.ravisnet.com:8080/cgi-bin/download.cgi?c2FuZ2VldGhhbXNoYXJlLm9yZy9wdWJsaWNfaHRtbC90dmcvVVBMT0FEUy0wMDAxLS0tMDIwMC8xMjItU2F2aXRoYV9OYXJhc2ltaGFuLzAxLXNoYW1iaE9fbWFoQWRFdmEtYmF1TGkubXAz


_(78121 entries)_
 _Has 10330 unique concerts and is a proper subset of renditions w.r.t. concerts._

## Karnatik

### data
 id | kriti | ragam  |  composer  | taalam | language | lyrics |   meaning   | notation |    url    | ragam_id
---:|:-----:|:------:|:----------:|:------:|:--------:|:------:|:-----------:|:--------:|:---------:|:----------
  3 | aadi pureeshwaram | aarabi | Muttuswaamee Dikshitar | aadi   | sanskrit | "content"=>"\"samaashTi caraNam\"=>\"vidhi haripoojita tyaagaraajaangam<br> aaditya kOTi prakaasha lingam\",\"pallavi\"=>\"aadi pureeswaram sadaa bhajEham<br> tripura sundaree samEta vara guruguha janakam<br> vandita munisha mukham<br> (aadi)\",\"madhyama kaalam\"=>\"nandi poojita swayambu lingam<br> naakaka vacatara saikaTa lingam<br>\",\"ciTTai swaram\"=>\"P,,DDPMP DDPPMGRS R,,DSRPD SRDSRMPD<br> S,,D,S,R MGRRSNDD R,,DSRPD SMPDSDPM<br> (aadi)\"", "has-named-stanzas"=>"true" | <b>Meaning:</b><br> I sing the glory of aadipureeshwara always, who is in the company of goddess Tripurasundari. He is the father of the supreme Guruguha and is adored by a host of sages. He is the renowned Tyaagaraaja worshipped by Brahma and Vishnu. His image shines wit the brilliance of crores of suns and the self-originated one worshipped by Nandi. His form made of sand is covered with an armour of serpents. |          | http://www.karnatik.com/c1725.shtml |      270


_(2698 entries)_

## Wikipedia

### ragas
 id |   raga_name    |    raga_link    | mela_number | melakartha
---:|:--------------:|:---------------:|:-----------:|:-----------
  1 | Rishabhavilāsa |                 |           1 | f
  3 | Kanakāngi      | /wiki/Kanakangi |           1 | t


_(942 entries)_

## scales
 id |  raga_name  |       arohanam       |      avarohanam
----|-------------|----------------------|----------------------
  1 | Rathnāngi   | S R1 G1 M1 P D1 N2 S | S N2 D1 P M1 G1 R1 S
  2 | Tatillatika | S R1 M1 P D1 S       | S D1 P M1 R1 S 


_(975 entries)_

-----------------------

# Domain Entities


## Ragam

A ragam has functional dependencies: `name`, `arohanam`, `avarohanam`, `raga_link` (wiki page), `melakartha` (if it is janya or not). 

A ragam belongsTo a ragam (`mela_number`). If it is melakartha, then it can only belong to itself.

A ragam hasMany kritis.

## Kriti

A kriti has functional dependencies: `name`, `composer`, `lyrics`, `taala`, `language`, `meaning`, `url` (karnatik page).

A kriti belongsTo a ragam (`ragam`).

A kriti hasMany renditions.

## Renditions/Tracks

A rendition has functional dependencies: `concert_id`, `concert_url`, `track_number`, `track_url`, `main_artist`.

A rendition belongsTo a kriti (`kriti`), and therefore also a ragam (`ragam`).


## Other entities possible:

1. Composers
2. Taala
3. Concerts
4. Artists


