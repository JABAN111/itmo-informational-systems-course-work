% Определение магических свойств
magical_property(elder_wand, "extreme"). % Бузинная палочка
magical_property(sword_of_gryffindor, "medium"). % Меч Годрика Гриффиндора
magical_property(philosophers_stone, "high"). % Философский камень
magical_property(horcrux, "extreme"). % Крестраж
magical_property(invisibility_cloak, "low"). % Мантия-невидимка
magical_property(resurrection_stone, "high"). % Воскрешающий камень
magical_property(tom_riddles_diary, "high"). % Дневник Тома Риддла
magical_property(marauders_map, "low"). % Карта мародеров
magical_property(quidditch_broom, "low"). % Квиддичевая метла
magical_property(ghoul_in_a_dress, "medium"). % Призрак в платье (шутливый пример)


:- dynamic banned_user/1.
:- dynamic banned_word/1.

% Пример забаненных пользователей
banned_user("dolores_umbridge").
banned_user("gellert_grindelwald").
banned_user("tom_riddle").

% Пример запрещённых слов
banned_word("dark").
banned_word("cursed").
banned_word("forbidden").
banned_word("unforgivable").
banned_word("evil").

% Проверка, есть ли запрещённое слово в предложении
contains_banned_word([]) :- fail.
contains_banned_word([Word|_]) :-
    banned_word(Word), !.
contains_banned_word([_|Tail]) :-
    contains_banned_word(Tail).

% Функция валидации описания артефакта
is_valid_description(DescriptionWords) :-
    \+ contains_banned_word(DescriptionWords). % Описание считается валидным, если нет запрещённых слов

% Добавление нового запрещённого слова
ban_word(Word) :-
    assertz(banned_word(Word)).

% Удаление запрещённого слова (если нужно разбанить)
unban_word(Word) :-
    retract(banned_word(Word)).

% Артефакты с уровнем "low" или "medium" можно хранить
can_store(Artifact, User) :-
    \+ banned_user(User), % Проверяем, не забанен ли пользователь
    magical_property(Artifact, DangerLevel),
    (DangerLevel = "low" ; DangerLevel = "medium").

% Артефакты с уровнем "high" можно хранить только в хранилище повышенной безопасности
can_store(Artifact, User) :-
    \+ banned_user(User),
    magical_property(Artifact, "high"),
    fail.

% Артефакты с уровнем "extreme" хранению не подлежат
can_store(Artifact, User) :-
    \+ banned_user(User),
    magical_property(Artifact, "extreme"),
    fail.

% Если пользователь забанен, то хранить ничего нельзя
can_store(_, User) :-
    banned_user(User),
    fail.

% Правило для анализа артефакта
analyze_artifact(Artifact, User) :-
    (can_store(Artifact, User) -> true ; false).