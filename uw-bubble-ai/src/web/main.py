import torch
from flask import Flask, request, jsonify
from readerwriterlock import rwlock

from domain.usermatch import AIModelBasedUserMatcher, User

app = Flask(__name__)

lock = rwlock.RWLockFair()
ai_recommender = AIModelBasedUserMatcher(torch.device("cuda") if torch.cuda.is_available() else torch.device("cpu:0"),
                                         "./log", 16)

recommender_using = 0


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
    read_lock = lock.gen_rlock()
    read_lock.acquire()
    try:
        req = request.json
        usr_from = User(**req['from'])
        usr_to = [User(**usr) for usr in req['to']]
        return ai_recommender.match(usr_from, usr_to)
    finally:
        read_lock.release()


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
    write_lock = lock.gen_wlock()
    write_lock.acquire()
    try:
        reqs = request.json
        datas = [(User(**req['from']), User(**req['to']), req['score']) for req in reqs]
        ai_recommender.adjust(datas)
        return jsonify(True)
    finally:
        write_lock.release()


if __name__ == '__main__':
    app.run()
