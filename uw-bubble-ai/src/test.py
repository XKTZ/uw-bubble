import random
from typing import List

from domain.user import User, Gender, Programs, Interests
from domain.usermatch import AIModelBasedUserMatcher

import torch

from math import e


def sigmoid(x):
    return 1 / (1 + e ** (-x))


def count_same(x, y):
    cnt = 0
    for i, j in zip(x, y):
        if i == j and (not (i == 0 or j == 0)):
            cnt += 1
    return cnt


def count_diff(x, y):
    cnt = 0
    for i, j in zip(x, y):
        if i != j:
            cnt += 1
    return cnt


def gender_diff(a, b):
    return 1 if (Gender.Other in (a, b)) or a != b else 0.5


class Marker:
    age: float
    age_pow: float
    same_interests: float
    same_interests_pow: float
    diff_interests: float
    diff_interests_pow: float
    gender: float
    gender_pow: float
    faculty_same: float
    faculty_diff: float
    bias: float
    max_base: float

    def __init__(self,
                 age: float, age_pow: float,
                 same_interests: float, same_interests_pow: float,
                 diff_interests: float, diff_interests_pow: float,
                 gender: float, gender_pow: float,
                 faculty_same: float, faculty_diff: float,
                 bias: float,
                 max_base: float = 1200):
        super(Marker, self).__init__()
        self.age = age
        self.age_pow = age_pow
        self.same_interests = same_interests
        self.same_interests_pow = same_interests_pow
        self.diff_interests = diff_interests
        self.diff_interests_pow = diff_interests_pow
        self.gender = gender_pow
        self.gender_pow = gender_pow
        self.faculty_same = faculty_same
        self.faculty_diff = faculty_diff
        self.bias = bias
        self.max_base = max_base

    def __call__(self, user_from: User, user_to: User):
        """
        age_factor * abs(age1 - age2) ** age_pow
            + same_interests_factor * count_interests_same ** same_interests_pow
            + diff_interests_factor * count_interests_diff ** diff_interests_pow
            + gender_factor * (
                1 : (other in (user_from, user_to))
                1: (user_from != user_to)
                0.5: user_from == user_to
            ) ** gender_pow
            + faculty_same_factor * (same_faculty?)
            + faculty_diff_factor * (different_faculty?)
        """
        return sigmoid(self.age * (abs(user_from.age - user_to.age)) ** self.age_pow \
                       + self.same_interests * count_same(user_from.interests,
                                                          user_to.interests) ** self.same_interests_pow \
                       + self.diff_interests * count_diff(user_from.interests,
                                                          user_to.interests) ** self.diff_interests_pow \
                       + self.gender * gender_diff(user_from.age, user_to.age) ** self.gender_pow \
                       + self.faculty_same * (1 if user_from.program == user_to.program else 0) \
                       + self.faculty_diff * (1 if user_from.program == user_to.program else 0))


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


matcher = AIModelBasedUserMatcher(torch.device("cuda"))

marker = Marker(
    age=1.0, age_pow=1.5,
    same_interests=1.0, same_interests_pow=2.4,
    diff_interests=1.5, diff_interests_pow=0.8,
    gender=6.0, gender_pow=2,
    faculty_same=1.2, faculty_diff=0.6,
    bias=2.0,
)

usrs: List[User] = [rand_user() for i in range(10000)]


def train_once():
    train_data = []
    for i in range(10000):
        x = usrs[int(random.random() * len(usrs))]
        y = usrs[int(random.random() * len(usrs))]
        train_data.append((x, y, marker(x, y)))

    matcher.adjust(train_data, train_print=True, test=True)


def test_once():
    test_data = []
    for i in range(2000):
        x = usrs[int(random.random() * len(usrs))]
        y = usrs[int(random.random() * len(usrs))]
        test_data.append((x, y, marker(x, y)))


for i in range(100):
    train_once()
