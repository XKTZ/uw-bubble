import torch
from flask import Flask, request, jsonify
from readerwriterlock import rwlock
from typing import List
from domain.user import Gender, Interests, Programs
from domain.usermatch import AIModelBasedUserMatcher, User
import random
import string

app = Flask(__name__)

lock = rwlock.RWLockFair()
ai_recommender = AIModelBasedUserMatcher(torch.device("cuda") if torch.cuda.is_available() else torch.device("cpu:0"),
                                         "./log", 16)

recommender_using = 0


def random_pick_3_interests() -> List[int]:
    interest = [0] * Interests.Number
    interest[0] = interest[1] = interest[2] = 1
    random.shuffle(interest)
    return interest


def random_pick_gender() -> int:
    x = random.random()
    if x < 0.45:
        return Gender.Male
    elif x < 0.9:
        return Gender.Female
    else:
        return Gender.Other


def rand_user() -> User:
    age = int(random.random() * 6) + 18
    faculty = int(random.random() * Programs.Number)
    interest = random_pick_3_interests()
    gender = random_pick_gender()
    return User(0, gender, age, faculty, interest)


@app.route("/user/random/")
def request_user_random():
    reqs = request.json
    users = [rand_user().to_dict() for i in range(reqs['size'])]
    return jsonify(users)


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
        ret = ai_recommender.match(usr_from, usr_to)
        return ret
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
    app.run(host="0.0.0.0")
