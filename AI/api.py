from fastapi import FastAPI
from pydantic import BaseModel
from prolog_processing import *


class PostRequest(BaseModel):
    user_input: str

logging.basicConfig(level=logging.INFO)
log = logging.getLogger(__name__)
app = FastAPI()

@app.post("/")
def root():
    return {"message": "Hello World"}

@app.post("/validate-save")
def check(request: PostRequest):
    log.info("Got request with user input: " + request.user_input)
    return process_artifact(request.user_input)

@app.get("/get-all")
def get_all_artifacts():
    return get_all()

@app.get("/get-all/{lvl}")
def get_all_artifacts(lvl):
    log.info("Got request with lvl: " + lvl)
    return get_all_with_specified_level(lvl)

@app.post("/level-of-danger")
def check_new(request: PostRequest):
    log.info("Got request with user input: " + request.user_input)
    return level_of_danger(request.user_input)