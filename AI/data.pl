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

% Определение правил допустимости сохранения
% Артефакты с уровнем "low" или "medium" можно хранить
can_store(Artifact) :-
    magical_property(Artifact, DangerLevel),
    (DangerLevel = "low" ; DangerLevel = "medium").

% Артефакты с уровнем "high" можно хранить только в хранилище повышенной безопасности
can_store(Artifact) :-
    magical_property(Artifact, "high"),
    fail.

% Артефакты с уровнем "extreme" хранению не подлежат
can_store(Artifact) :-
    magical_property(Artifact, "extreme"),
    fail. % Является неудачным случаем

% Правило для анализа артефакта
analyze_artifact(Artifact) :-
    (can_store(Artifact) -> true ; false).