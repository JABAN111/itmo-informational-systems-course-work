import logging
from pyswip import Prolog

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)
prolog_filename = "data.pl"
prolog = Prolog()
prolog.consult(prolog_filename)

def _send_query(query: str):
    return prolog.query(query)

def analyze_artifact(artifact_name):
    artifact_name = __process_data(artifact_name)
    result = _send_query(f'analyze_artifact({artifact_name})')
    return bool(list(result))

def _level_of_danger(artifact_name):
    debug_q = f"magical_property({artifact_name}, Level)."
    logging.info(f"Query: {debug_q}")

    query = list(_send_query(debug_q))  # Преобразуем генератор в список сразу

    logger.info(f"finished query: {query}, len: {len(query)}, list: {query} ")
    if query:
        return query[0]['Level']
    else:
        return "Unknown"

# Для локальных тестов
def main():
    artifact = input("(например: elder_wand): ").strip().lower()
    analyze_artifact(artifact)


def get_all():
    return list(_send_query("magical_property(Name, Lvl)"))


def get_all_with_specified_level(specified_level: str):
    return list(_send_query(f'magical_property(Name, {specified_level})'))


def process_artifact(artifact_name):
    logger.debug(
        f"Результат анализа артефакта: {analyze_artifact(artifact_name)}, уровень опасности: {_level_of_danger(artifact_name)},\n"
        f"Артефакт: {artifact_name}")
    return analyze_artifact(artifact_name)


def level_of_danger(artifact_name):
    return _level_of_danger(__process_data(artifact_name))

def __process_data(str):
    return str.strip().lower()

if __name__ == "__main__":
    main()
