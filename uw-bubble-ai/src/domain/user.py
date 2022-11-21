from typing import List, Dict

import torch


class Gender:
    Male = 0
    Female = 1
    Other = 2


class Programs:
    Number = 6


class Interests:
    Number = 16


class User:
    """
    A user's object
    """

    id: int
    gender: int
    age: int
    program: List[int]
    interests: List[int]

    @staticmethod
    def _program_id_to_program_array(program: int) -> List[int]:
        array = [0] * Programs.Number
        array[program] = 1
        return array

    def _program_array_to_program_id(program: List[int]) -> int:
        for i in range(len(program)):
            if program[i] != 0:
                return i
        return -1

    @staticmethod
    def feature_size():
        """
        Get the feature size
        :return: get the program size
        """
        return 1 + 1 + Programs.Number + Interests.Number

    def __init__(self, id: int, gender: int, age: int, program: int, interests: List[int]):
        """
        :param id: id of user
        :param gender: gender of user
        :param age: age of user
        :param program: program of user
        :param interests: interest of user (int[Interests.Number])
        """
        self.id = id
        self.gender = gender
        self.age = age
        self.program = User._program_id_to_program_array(program)
        self.interests = interests
        assert len(interests) == Interests.Number

    def to_torch_tensor(self) -> torch.Tensor:
        """
        turn the user into a pytorch array
        :return: pytorch array
        """
        return torch.tensor([self.gender, self.age, *self.program, *self.interests], dtype=torch.float)

    def to_dict(self) -> Dict:
        return {"gender": self.gender, "age": self.age, "program": User._program_array_to_program_id(self.program),
                "interests": self.interests}

    def __str__(self):
        return str({"gender": self.gender, "age": self.age, "program": User._program_array_to_program_id(self.program),
                    "interests": self.interests})


def match_user(user_from: User, user_to: User) -> torch.Tensor:
    """
    match two user together, create a tensor array showing the info
    :param user_from: user matching from
    :param user_to: user matching toto
    :return:
    """
    return torch.tensor([*user_from.to_torch_tensor(), *user_to.to_torch_tensor()])
