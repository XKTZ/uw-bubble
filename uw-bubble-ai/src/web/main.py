from multiprocessing import Lock

import torch
from flask import Flask, request, jsonify

from domain.usermatch import AIModelBasedUserMatcher, User

app = Flask(__name__)

lock = Lock()
ai_recommender = AIModelBasedUserMatcher(torch.device("cuda") if torch.cuda.is_available() else torch.device("cpu:0"),
                                         "./log", 16)

recommender_using = 0


def get_recommender():
    lock.acquire()
    try:
        return recommender_using
    finally:
        lock.release()


@app.route("/user/select/")
def request_user_comparison():
    """
    req format:
    {
    from: userfrom,
    to: [userto1, userto2, ...]
    }
    :return:
    """
    req = request.json
    usr_from = User(**req['from'])
    usr_to = [User(**usr) for usr in req['to']]
    return ai_recommender.match(usr_from, usr_to)


@app.route("/user/adjust/")
def request_user_adjustment():
    """
    req format:
    [
       [from: user_from,
       to: user_to,
       score: score]
    ...
    ]
    :return:
    """
    req = request.json
    datas = [(User(**req['from']), User(**req['to']), req['score'])]
    ai_recommender.adjust(datas)
    return jsonify(True)


if __name__ == '__main__':
    app.run()
