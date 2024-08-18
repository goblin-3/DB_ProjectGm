Project Database Games 2023-2024 Giel Meyers:

Het doel van deze database is een overzicht bij te houden van zowel de locaties als de status van de verschillende games en bijhorende Miscellaneous (Misc.).
Zoals gevraagd stelt het DDL-schema enkel de hoofdtabellen voor, de tussentabellen zijn hier niet op weergegeven. De verbindingslijnen geven de relaties tussen de tabellen weer. Bijkomend duiden we aan welke relaties: één-op-één, één op oneindig of oneindig op oneindig zijn. Achter elke variabele zijn de SQLite types weergegeven. De gebruikte types zijn Numeric voor het weergeven van een geheel getal, Real voor de invoer van een komma-getal. Het veld Real gebruiken we voor het veld Price. Het type ‘Text’ gebruiken we voor de invoer van namen in de diverse velden. Dit type veld is enkel geschikt voor de invoer van een korte tekst.

De database bestaat uit de hoofdtabel Game, hier houden we de gegevens bij over de games zoals: de naam van de Game, het Releaseyear, het Genre, de Language, de Publisher, het type console, de prijs van de game en of een game al dan niet digitaal is.  De prijs wordt per spel ingegeven; deze waarde kan niet nul en niet negatief zijn. De waarde is niet gelinkt aan een extra tabel.
De andere hoofdtabel houdt de niet-spel gegevens bij onder de naam Misc. In deze tabel Misc houden we gelijkaardige gegevens bij als in de tabel Game, maar in plaats van console kiezen we hier voor Type waarin we onder andere posters, T-shirts, handleidingen, etc. als soorten kunnen opslaan.

In de tabel Console houden we buiten de diverse types consoles ook de PC-spellen bij als een console.  

De tabellen, Console/Type, Releaseyear en Publisher zijn aanpasbare lijsten met unieke gegevens die gelinkt zijn aan de hoofdtabellen Game en Misc door de gemeenschappelijke ID key . De Genre tabel heeft een oneindig op oneindige relatie met game en Misc en is daarom verbonden met een tussentabel. Deze aparte tabellen en tussentabellen laten toe om gemakkelijker op inhoud te zoeken of te filteren.

In de tabellen Musea en Warehouse houden we de locatie van elke game en Misc bij. Hiervoor houden ze de Misc_id en Game_id in deze tabellen bij. 
De tabel Rental is de tussentabel tussen User en Game en kan in de toekomst gebruikt worden om ook spellen te verhuren. Hier houden we o.a. de Rental date en Returndate bij met de customer Id. Dit item is reeds uitgewerkt in de database, maar nog niet in de user interface.

De tabel Customer kan de volgende types gebruiker bijhouden: :Admin, Customer en Employee.
De gebruiker of de Customer krijgt een sign up scherm aangeboden: First Name, Last Name en Email-Adres en Username, gevolgd door een paswoord. Zowel het veld email-adres als de UserName stellen we in als uniek veld.

We gebruiken een model voor een online-uitleensysteem voor onze games. Dit model gebruiken we ook om de vraag naar het aantal bezoekers te registreren. Een eenvoudige query kan ons tonen hoeveel bezoekers er op één dag een game uitlenen. Deze filter kunnen we later uitbreiden naar het aantal bezoekers op andere tijdstippen. Een registratie van de effectieve bezoekers aan één van onze musea is niet verder uitgewerkt.

In het model online-uitleensysteem krijgt de gebruiker bij zijn eerste bezoek een Login-scherm te zien. In de sign up kan de gebruiker de velden LastName, FirstName, een UserName die uniek moet zijn en het Email-adres invullen samen met een paswoord. 

Als de gebruiker eenmaal ingelogd is, wordt hem het volgende scherm aangeboden:
Bovenaan verschijnt de Username “Hello” “Username”
Daarna krijgt de gebruiker een lijst van spellen te zien . Hier kan de gebruiker één spel van kiezen. Na de keuze kan het spel nu enkel nog bewerkt of verwijderd worden maar naar de toekomst toe kan verhuur worden geimplementeerd worden. 
Na bewerkingen of toevoegingen van een nieuw gedoneerd spel, wordt de speler terug naar de lijst gestuurd. Op deze lijst kan ook gefilterd worden op locatie zodat de klant altijd weet of de Game op de gewenste locatie ter beschikking is. 
De login voor de Employee en de Admin verlopen gelijkaardig. Enkel De Administrator of “Admin” maakt de gebruikers ‘Admin’ en ‘Employee’ aan. Hiervoor zijn bij het signup scherm de bijkomende Checkboxen Employee en Admin voorzien.
Dit is nodig om ervoor te zorgen dat alleen de Admin en Employee toegang krijgen tot de input van de tabellen:Publisher, Genre, Console, Type, Musea, Warehouse. Dit vermindert de foutenlast en voorkomt het bijhouden van dubbele gegevens. De gebruiker heeft hier geen toegang.

De naam van het spel, bijvoorbeeld ‘New Super Mario Brothers’ kan wel meerdere keren voorkomen in de database; als deze gereleaset wordt voor meerdere types Consoles. Het is ook mogelijk dat er twee of meerdere dezelfde exemplaren aanwezig zijn in de collectie van onze musea. We kunnen deze gemakkelijk van elkaar onderscheiden omdat elke spel zijn eigen unieke id nummer heeft. 
Tevens vergemakkelijkt het de zoekopdracht. Het zorgt ervoor dat de gebruiker deze gegevens kan selecteren uit een keuzelijst. Het is de bedoeling dat deze gebruiker met een eenvoudige zoekopdracht de gezochte game of een Misc kan vinden zonder dat de gebruiker de gegevens kan manipuleren.

Zoals gevraagd voorzien we meerdere filtermogelijkheden. De eerste zoekmogelijkheid geeft weer op welke locatie een spel zich bevindt. Een volgende Query kan in de toekomst berekenen hoeveel bezoekers een spel uitgeleend hebben. We beperken ons tot de uitlening van een spel. Ook kunnen we in de derde Query weergeven welke genres het meeste worden uitgeleend.

Het budget van de musea wordt bijgehouden in het veld Cash_reserve in de tabellen Museum en Warehouse.


