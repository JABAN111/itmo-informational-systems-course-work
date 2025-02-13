:- dynamic magical_property/2.

% Магические свойства артефактов
magical_property(elder_wand, "extreme"). % Бузинная палочка
magical_property(sword_of_gryffindor, "medium"). % Меч Годрика Гриффиндора
magical_property(philosophers_stone, "high"). % Философский камень
magical_property(horcrux, "extreme"). % Крестраж
magical_property(invisibility_cloak, "low"). % Мантия-невидимка
magical_property(resurrection_stone, "high"). % Воскрешающий камень
magical_property(tom_riddles_diary, "high"). % Дневник Тома Риддла
magical_property(marauders_map, "low"). % Карта мародеров
magical_property(quidditch_broom, "low"). % Квиддичевая метла
magical_property(ghoul_in_a_dress, "medium"). % Призрак в платье

% Уровень опасности артефактов
magical_specification(elder_wand, 'Magical explosion').
magical_specification(sword_of_gryffindor, 'Physical danger').
magical_specification(philosophers_stone, 'Flammable').
magical_specification(horcrux, 'Magical explosion').
magical_specification(invisibility_cloak, 'Safe').
magical_specification(resurrection_stone, 'Flammable').
magical_specification(tom_riddles_diary, 'Flammable').
magical_specification(marauders_map, 'Safe').
magical_specification(quidditch_broom, 'Safe').
magical_specification(ghoul_in_a_dress, 'Requires caution').

% Обновление свойства артефакта
update_magical_property(Artifact, NewLevel) :-
    retract(magical_property(Artifact, _)),
    assertz(magical_property(Artifact, NewLevel)).

update_magical_specification(Artifact, NewSpecification) :-
    retract(magical_specification(Artifact, _)),
    assertz(magical_specification(Artifact, NewSpecification)).

% Забаненные пользователи
:- dynamic banned_user/1.
banned_user("dolores_umbridge").
banned_user("gellert_grindelwald").
banned_user("tom_riddle").

% Запрещённые слова (англ.)
:- dynamic banned_word/1.
banned_word("dark").
banned_word("cursed").
banned_word("forbidden").
banned_word("unforgivable").
banned_word("evil").
banned_word("shadow").
banned_word("damned").
banned_word("accursed").
banned_word("taboo").
banned_word("sinister").

% Запрещённые слова (рус.)
banned_word("тёмный").
banned_word("проклятый").
banned_word("запрещённый").
banned_word("непростительный").
banned_word("злой").
banned_word("тень").
banned_word("проклятие").
banned_word("нечестивый").
banned_word("табу").
banned_word("зловещий").

% Проверка, есть ли запрещённое слово в списке слов
contains_banned_word([Word|_]) :-
    banned_word(Word), !.
contains_banned_word([_|Tail]) :-
    contains_banned_word(Tail).

% Валидация описания артефакта
is_valid_description(DescriptionWords) :-
    \+ contains_banned_word(DescriptionWords). % Описание валидно, если нет запрещённых слов

% Добавить слово в список запрещённых
ban_word(Word) :-
    assertz(banned_word(Word)).

% Удалить слово из запрещённых
unban_word(Word) :-
    retract(banned_word(Word)).

% Правило хранения артефактов
can_store(Artifact, User) :-
    \+ banned_user(User), % Проверка, не забанен ли пользователь
    magical_property(Artifact, DangerLevel),
    ( DangerLevel = "low" ; DangerLevel = "medium" ), !.

can_store(Artifact, User) :-
    \+ banned_user(User),
    magical_property(Artifact, "high"),
    fail. % Требует специального хранилища

can_store(Artifact, User) :-
    \+ banned_user(User),
    magical_property(Artifact, "extreme"),
    fail. % Хранению не подлежит

can_store(_, User) :-
    banned_user(User),
    fail. % Забаненные пользователи ничего хранить не могут

% Анализ артефакта
analyze_artifact(Artifact, User) :-
    (can_store(Artifact, User) -> true ; false).