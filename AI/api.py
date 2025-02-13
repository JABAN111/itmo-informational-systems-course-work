from typing import Optional

from fastapi import FastAPI
from prolog_processing import *
from db import *


class PostRequest(BaseModel):
    user_input: Optional[str] = ""
    user_account_name: Optional[str] = None
    reason_to_save: Optional[str] = "default_reason"
    new_value: Optional[str] = ""


logging.basicConfig(level=logging.INFO)
log = logging.getLogger(__name__)
app = FastAPI()
init_db()

@app.post("/")
def root():
    return {"message": "Hello World"}

@app.post("/validate-save")
def check(request: PostRequest):
    log.debug("Got request with user input: " + request.user_input + "with account_name: " + request.user_account_name)
    return process_artifact(request.user_input, request.user_account_name)

@app.post("/get-specification")
def get_specification(request: PostRequest):
    return get_magical_specification(request.user_input)

@app.get("/get-all")
async def get_all_artifacts():
    return get_all_magical_properties()

@app.get("/get-all/{lvl}")
def get_all_artifacts(lvl):
    log.debug("Got request with lvl: " + lvl)
    return get_all_with_specified_level(lvl)

@app.post("/level-of-danger")
def check_new(request: PostRequest):
    log.debug("Got request with user input: " + request.user_input)
    return level_of_danger(request.user_input)

@app.post("/description-validation")
def validate_description(request: PostRequest):
    log.debug("Got request with user input: " + request.reason_to_save)
    return check_description(request.reason_to_save)

@app.post("/add-ban-user")
def ban_user(request: PostRequest):
    return add_banned_user(request.user_account_name)

@app.post("/add-ban-word")
def ban_word(request: PostRequest):
    return add_banned_word(request.user_input)

@app.post("/request-update-person")
def request_update_lvl(request: PostRequest):
    user_input = request.user_input
    new_value = request.new_value

    QueueService.add_or_update_queue(
        artifact_name = user_input,
        future_category = new_value,
    )
    return {"message": "Ok"}