from abc import abstractmethod
from collections import deque
from typing import List, Dict, Tuple, Deque

import torch
from torch import nn
from torch.utils.data import Dataset, DataLoader
from torch.utils.tensorboard import SummaryWriter

from domain.user import User, match_user
from model.model import UserMatchUpModel
from util.ProgressBar import ProgressBar


class UserMatcher:
    """
    General user matcher
    """

    def __init__(self):
        pass

    @abstractmethod
    def match(self, user: User, to: List[User]) -> Dict[int, float]:
        """
        Match users to some other users
        :param user: users
        :param to: other users
        :return: {userId: score given}
        """
        pass

    @abstractmethod
    def adjust(self, adjust_data: List[Tuple[User, User, float]], train_print: bool = False, test: bool = True):
        """
        Adjust the result
        :param adjust_data: adjusting data [(user_from, user_to, real score)]
        :param train_print: print data while training or not
        :param test: test after adjusting or not
        """
        pass

    @abstractmethod
    def test(self):
        """
        Test the past, put into the summary writer
        """
        pass

    def close(self):
        pass


class AIModelBasedUserMatcher(UserMatcher):
    """
    An AI model based user matcher
    """

    class _AIModelDataSet(Dataset):
        datas: List[Tuple[torch.Tensor, torch.Tensor]]

        def __init__(self, datas: List[Tuple[User, User, float]]):
            self.datas = [(match_user(d[0], d[1]), torch.tensor(d[2])) for d in datas]

        def __len__(self):
            return len(self.datas)

        def __getitem__(self, idx):
            return self.datas[idx]

    _model: UserMatchUpModel
    _device: torch.device

    _optim: torch.optim.Optimizer
    _loss: object

    _total_train_cnt: int
    _total_test_cnt: int

    _log: SummaryWriter

    _past: Deque[List[Tuple[User, User, float]]]
    _past_length_maintain: int

    def __init__(self, device: torch.device = torch.device("cpu:0"), log_dir: str = "log",
                 past_length_maintain: int = 32):
        super(AIModelBasedUserMatcher, self).__init__()
        self._model = UserMatchUpModel(User.feature_size() * 2, [256, 256]).to(device)
        self._device = device
        self._optim = torch.optim.Adam(self._model.parameters(), 1e-4)
        self._loss = nn.MSELoss()
        self._total_train_cnt = 0
        self._total_test_cnt = 0
        self._log = SummaryWriter(log_dir)

        self._past = deque()
        self._past_length_maintain = past_length_maintain

    def match(self, user: User, to: List[User]) -> Dict[int, float]:
        with torch.no_grad():
            result = self._model(torch.tensor([match_user(user, t) for t in to]).to(self._device))
            return {to[i].id: result[i].item() for i in range(len(to))}

    def adjust(self, adjust_data: List[Tuple[User, User, float]], train_print: bool = True, test: bool = True):
        # load data
        data_set = AIModelBasedUserMatcher._AIModelDataSet(adjust_data)
        data_loader = DataLoader(data_set, 8, shuffle=True)

        # add count one more
        self._total_train_cnt += 1

        cnt = 0

        # total loss
        loss_total = 0.0

        # size
        sz = len(data_loader)

        prog = ProgressBar(0)

        if train_print:
            print(f"AI Model Training Epoch {self._total_train_cnt} starts")

        # train
        for img, lbl in data_loader:
            cnt += 1
            img = img.to(self._device)
            lbl = lbl.to(self._device)

            out = self._model(img).reshape(-1)
            ls = self._loss(out, lbl)

            if train_print:
                self._log.add_scalar(f"AI Model Training Epoch {self._total_train_cnt} Effect", ls, cnt)
                prog(cnt, sz, f"loss: {ls.item()}")

            self._optim.zero_grad()
            ls.backward()
            self._optim.step()

            loss_total += ls.item()

        prog.close()

        if train_print:
            self._log.add_scalar("AI Model Training", loss_total / sz, self._total_train_cnt)
            print(f"Training epoch {self._total_train_cnt} finish, loss {loss_total / sz}")

        if test:
            self.test()

        self._past.append(adjust_data)
        while len(self._past) > self._past_length_maintain:
            self._past.popleft()

    def test(self):
        # distance between x and y
        dis = lambda x, y: torch.mean(torch.abs(x - y))
        self._total_test_cnt += 1

        print(f"Test {self._total_test_cnt} starts")

        with torch.no_grad():
            loss_total = 0.0
            dis_total = 0.0

            sz = 0

            # iterate through all data
            for data in self._past:
                data_set = AIModelBasedUserMatcher._AIModelDataSet(data)
                data_loader = DataLoader(data_set, batch_size=8, shuffle=False)

                sz_epoch = len(data_loader)
                cnt = 0
                sz += sz_epoch

                prog = ProgressBar(0)

                loss_data = 0.0
                dis_data = 0.0

                # data loader
                for img, lbl in data_loader:
                    cnt += 1
                    img = img.to(self._device)
                    lbl = lbl.to(self._device)

                    out = self._model(img).reshape(-1)

                    ls = self._loss(out, lbl)
                    d = dis(out, lbl)

                    loss_data += ls.item()
                    dis_data += d.item()

                    prog(cnt, sz_epoch, f"loss: {ls.item()},  distance: {d.item()}")

                loss_total += loss_data
                dis_total += dis_data

                prog.close()

            self._log.add_scalar(f"AI Model Test Loss", loss_total / max(sz, 1), self._total_test_cnt)
            self._log.add_scalar(f"AI Model Test Distance", dis_total / max(sz, 1), self._total_test_cnt)

            print(f"Testing epoch {self._total_test_cnt} finish, loss {loss_total / max(sz, 1)}, distance {dis_total / max(sz, 1)}")

    def close(self):
        self._log.close()
