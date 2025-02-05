from pyswip import Prolog

prolog = Prolog()
prolog.consult("data.pl")  # Подключаем базу знаний

def check_description(description: str) -> bool:
    words = description.lower().split()
    result = list(prolog.query(f"is_valid_description({words})"))
    return bool(result)

def add_banned_word(word: str):
    prolog.assertz(f"banned_word('{word}')")

# Пример работы
description1 = "This wand is extremely powerful and dark"
description2 = "This is a legendary item of great power"

print(check_description(description1))  # False, потому что есть "dark"
print(check_description(description2))

add_banned_word("dangerous")
print(check_description("This is a dangerous artifact"))  # False