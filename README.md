# Tématerv

## Személyes adatok

Név: Kalmár Roland

Neptun: LOAWRP

E-mail: h159379@stud.u-szeged.hu

Szak: programtervező informatikus - MSc - levelező 

Végzés várható ideje: 2023. nyár

## A diplomamunka tárgya

A diplomamunka tárgya egy oktatást támogató keretrendszer megvalósítása. A keretrendszer egy egyszerű platformot biztosít az oktatók számára, amellyel könnyen tudnak Java nyelvhez deklaratív módon leírni feladatokat. Továbbá lehetőség biztosít a pontozási rendszer felállítására és ez alapján biztosítja a feladat megvalósítások kiértékelését. 

A működés egyszerű és gyors. Az oktató lényegében "leprogramozza" a kért feladatot és azt Java annotációk segítségével leírja kiértékelés szempontjából.

Az annotációk szintaktikai vagy szemantikai helyesség ellenőrzésére szolgálhatnak. A keretrendszer eszközt biztosít arra, hogy az egyes "tesztek" között meghatározhatóak legyenek függőségi viszonyok, például: az x. szemantikai tesztet csak, akkor hajtsuk végre, ha y. elem szintaktikailag helyesen volt leírva. Bonyolultabb tesztek végrehajtásához, metódusokhoz hozzárendelhetőek egyedi tesztek is amelyet az oktatóknak külön kell megvalósítaniuk.

Miután a feladat és a hozzá tartozó ellenőrzések elkészültek, a keretrendszer képes végrehajtani egy ellenőrzést a teszteken, például: helyesek-e az ellenőrzések, nincs-e tranzitív függőség a tesztek között, lefutnak-e és így tovább.

Amennyiben a feladat és a tesztek helyesek, a keretrendszer képes legenerálni egy UML diagrammot és egy értékelési útmutatót pontszámokkal ellátva amely egyrészt az oktatók számára biztosít egy utolsó validációs lehetőséget, másrészt a feladat kiírás részét képezheti. 

Legvégül a keretrendszer képes a számára megadott az oktató által leírt feladat alapján egy megadott feladat kiértékelésére. Az eredményt egy fájlba írja amit a hallgató megtekinthet.

## Használni kívánt technológiák

Java, JUnit, Mockito, Maven, Git (GitHub)

## Tervezett ütemezés

- **2022. szeptember:** A keretrendszer alap struktúrájának megvalósítása, szintaktiai ellenőrzések implementálásának.
- **2022. október:**  Szemantikai ellenőrzések megvalósítása.
- **2022. november:** Tesztek ellenőrzésének végrehajtása - hibák jelzésének megvalósítása az oktatók felé.
- **2022. december:** A teszthez tartozó UML diagramm és értékelési útmutató legenerálásának megvalósítása.
- **2023. január:** A működés tesztelése, hibajavítás, további tesztek írása.
- **2023. február:** A diplomamunka írása I.
- **2023. március:** A diplomamunka írása II.
- **2023. április:** A diplomamunka írása III., befejezés, ellenőrzés.