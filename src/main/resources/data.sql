insert into users(ID, NAME, LASTNAME, EMAIL, PASSWORD)
values(1000, 'Rafał', 'Kowalski','kowalski@gmail.com', 'haslo1');

insert into users(ID, NAME, LASTNAME, EMAIL, PASSWORD)
values(2000, 'Mateusz', 'Nowak','nowak.mateusz@gmail.com', 'haslo2');

insert into users(ID, NAME, LASTNAME, EMAIL, PASSWORD)
values(3000, 'Antek', 'Lewandowski','lewandowski123@gmail.com', 'haslo3');


insert into users(ID, NAME, LASTNAME, EMAIL, PASSWORD)
values(4000, 'Wiktor', 'Piszczek','piszczek@gmail.com', 'haslo4');

--insert into users(ID, NAME, EMAIL, PASSWORD)
--values(50000, 'admin','admin@gmail.com', 'admin');




insert into roles(ID, NAME)
values(1, 'ROLE_ADMIN');
insert into users_roles(ROLE_ID, USER_ID)
values(1,1);

insert into post(ID, USER_ID, LOCAL_DATE_TIME, DESCRIPTION, TITLE)
values(1000, 1000, CURRENT_DATE(), 'Cześć, młodzi programiści! Dziś rozpoczynamy fascynującą podróż w świat programowania. Na naszym blogu będziemy odkrywać tajniki kodowania, tworzyć proste gry i zabawki oraz uczyć się, jak komputery działają. Gotowi na zabawę z kodem?', 'Zaczynamy Przygodę z Programowaniem!');

insert into post(ID, USER_ID, LOCAL_DATE_TIME, DESCRIPTION, TITLE)
values(2000, 1000, CURRENT_DATE(), 'Witajcie! Dziś dowiemy się, czym jest kod i jak działa. Kod to taki jak instrukcje dla komputera, które mówią mu, co ma robić. Podczas naszych zajęć nauczymy się, jak pisać te instrukcje w prosty i zrozumiały sposób!', 'Czym Jest Kod i Jak Działa?');

insert into post(ID, USER_ID, LOCAL_DATE_TIME, DESCRIPTION, TITLE)
values(3000, 3000, CURRENT_DATE(), 'Cześć, małe czarodziejki i czarodzieje kodu! W programowaniu tworzymy magiczne przepisy, które sprawiają, że komputer robi dokładnie to, co chcemy. Dziś nauczymy się, jakie są podstawowe "składniki" każdego kodu.', 'Kodowanie to Jak Tworzenie Magicznych Przepisów!');

insert into post(ID, USER_ID, LOCAL_DATE_TIME, DESCRIPTION, TITLE)
values(4000, 4000, CURRENT_DATE(), 'Hej, programujące dzieciaki! Programowanie to nie tylko nudne linijki kodu; to także tworzenie własnych gier i zabawek! Przygotujcie się na stworzenie swojej pierwszej gry komputerowej.', 'Gry i Zabawki, Które Tworzymy Sami!');


