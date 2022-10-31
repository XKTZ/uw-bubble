from typing import Union, Tuple, Callable
import math


class ProgressBar:
    original: int
    mx: int
    lastSize: int

    def __init__(self, original: int = 0):
        self.lastSize = 0
        self.now = original
        pass

    def __call__(self, now: int, mx: int, other: str = None):
        self.show(now, mx, other)

    def show(self, now: int, mx: int, other: str = None):
        self.now = now
        self.mx = mx
        self._display(other)

    def _display(self, other: str = None):
        cover = f"{' ' * self.lastSize}\r"
        msg = f"{self.now} / {self.mx}{'' if other is None else f' ; {other}'}"
        self.lastSize = len(msg)
        print(cover + msg, end="")

    def close(self):
        print()

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.close()
