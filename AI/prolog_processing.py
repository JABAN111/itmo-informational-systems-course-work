import logging
from pyswip import Prolog

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

prolog_filename = "data.pl"
prolog = Prolog()
prolog.consult(prolog_filename)


def _send_query(query: str):
    logger.debug(f"sending query: {query}")
    return prolog.query(query)


def check_description(description: str) -> bool:
    words = description.lower().split()
    for word in words:
        request = f'banned_word("{word}")'
        query_result = list(_send_query(request))
        if query_result:
            logger.debug(f"ban word: {word}")
            return False
    return True

def add_banned_word(word: str):
    prolog.assertz(f"banned_word('{word}')")
    logger.info(f"Добавлено запрещённое слово: {word}")

def add_banned_user(user: str):
    prolog.assertz(f'banned_user("{user}")')

def analyze_artifact(artifact_name, user):
    artifact_name = __process_data(artifact_name)
    req = f'analyze_artifact({artifact_name}, "{user}")'
    print(f"request = {req}")
    result = _send_query(req)
    return bool(list(result))


def _level_of_danger(artifact_name):
    debug_q = f"magical_property({artifact_name}, Level)."
    query = list(_send_query(debug_q))
    if query:
        return query[0]['Level']
    else:
        return "Unknown"


def get_all():
    return list(_send_query("magical_property(Name, Lvl)"))


def get_all_with_specified_level(specified_level: str):
    return list(_send_query(f'magical_property(Name, {specified_level})'))


def process_artifact(artifact_name: str, user_account_name: str):
    logger.info(
        f"Результат анализа артефакта: {analyze_artifact(artifact_name, user_account_name)}, уровень опасности: {_level_of_danger(artifact_name)},\n"
        f"Артефакт: {artifact_name}"
        f"Имя пользователя {user_account_name}")
    return analyze_artifact(artifact_name, user_account_name)


# Функция для получения уровня опасности артефакта
def level_of_danger(artifact_name):
    return _level_of_danger(__process_data(artifact_name))


# Функция для обработки данных
def __process_data(str):
    return str.strip().lower()


if __name__ == "__main__":
    description1 = "This wand is extremely powerful and dark"
    description2 = "This is a legendary item of great power"
    print(check_description(description1))
    print(check_description(description2))

    add_banned_word("dangerous")
    print(check_description("This is a dangerous artifact"))

    artifact = input("Введите артефакт для анализа (например: elder_wand): ").strip().lower()
    print(f"Можно ли сохранить артефакт {artifact}: {process_artifact(artifact)}")
