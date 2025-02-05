from typing import Optional

from fastapi import FastAPI
from pydantic import BaseModel
from prolog_processing import *


class PostRequest(BaseModel):
    user_input: Optional[str] = ""
    user_account_name: Optional[str] = None
    reason_to_save: Optional[str] = "default_reason"


logging.basicConfig(level=logging.INFO)
log = logging.getLogger(__name__)
app = FastAPI()

@app.post("/")
def root():
    return {"message": "Hello World"}

@app.post("/validate-save")
def check(request: PostRequest):
    log.info("Got request with user input: " + request.user_input + "with account_name: " + request.user_account_name)
    return process_artifact(request.user_input, request.user_account_name)



@app.get("/get-all")
async def get_all_artifacts():
    return get_all()

@app.get("/get-all/{lvl}")
def get_all_artifacts(lvl):
    log.info("Got request with lvl: " + lvl)
    return get_all_with_specified_level(lvl)

@app.post("/level-of-danger")
def check_new(request: PostRequest):
    log.info("Got request with user input: " + request.user_input)
    return level_of_danger(request.user_input)

@app.post("/description-validation")
def validate_description(request: PostRequest):
    log.info("Got request with user input: " + request.reason_to_save)
    return check_description(request.reason_to_save)

@app.post("/add-ban-user")
def ban_user(request: PostRequest):
    return add_banned_user(request.user_account_name)

@app.post("add-ban-word")
def ban_word(request: PostRequest):
    return add_banned_word(request.user_input)