# Parking System
Parking System est un programme de gestion de parking développé en Java. Il vous permet de gérer les opérations courantes d'un parking, telles que l'attribution de places de parking, l'enregistrement des tickets, le calcul des tarifs, etc...

## Clonage du repository
Pour cloner le repository en local, suivez les étapes suivantes :

1. Assurez-vous d'avoir Git installé sur votre machine.
2. Ouvrez une console ou un terminal.
3. Utilisez la commande suivante pour cloner le repository :

```bash
git clone https://github.com/NextSap/JAVA.oc-4.git
```

Cela créera une copie locale du repository sur votre machine.

## Configuration
Avant d'exécuter le programme Parking System, vous devez configurer certaines variables en créant un fichier '**.properties**' et en y ajoutant les valeurs nécessaires. Suivez les instructions ci-dessous :

1. Créez un fichier '**config.properties**'.
2. Ouvrez le fichier '**config.properties**' avec un éditeur de texte.
3. Ajoutez les variables suivantes avec leur valeur respective :

```properties
GET_NEXT_PARKING_SPOT=select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?
UPDATE_PARKING_SPOT=update parking set available = ? where PARKING_NUMBER = ?
GET_PARKING_SPOT=select * from parking where PARKING_NUMBER = ?
SAVE_TICKET=insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)
UPDATE_TICKET=update ticket set PRICE=?, OUT_TIME=? where ID=?
GET_TICKET=select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME limit 1
GET_TICKETS=select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? limit 2
BIKE_RATE_PER_HOUR=1.0
CAR_RATE_PER_HOUR=1.5
DB_PROD=your_production_database_link?serverTimezone=Europe/Brussels
DB_TEST=your_test_database_link?serverTimezone=Europe/Brussels
DB_USER_PROD=your_production_username
DB_PASSWORD_PROD=your_production_password
DB_USER_TEST=your_test_username
DB_PASSWORD_TEST=your_test_password
```
Assurez-vous de remplacer les valeurs suivantes par vos informations de connexion réelles pour les bases de données de production et de test :
* '**your_production_database_link**' ;
* '**your_test_database_link**' ; 
* '**your_production_username**' ;
* '**your_production_password**' ;
* '**your_test_username** ;
* '**your_test_password** ;

⚠️ Le serverTimezone des liens des deux bases de données doivent être paramétrés sur '**Europe/Brussels**'. ⚠️

4. Enregistrez le fichier '**config.properties**'.
5. Dans le fichier principal '**App.java**', remplacez la valeur de la variable statique *CONFIG_PATH* par le chemin d'accès de votre propre fichier '**config.properties**'