from typing import List

from torch import nn


class UserMatchUpModel(nn.Module):
    """
    User match up model
    """
    _model: nn.Module

    def __init__(self, input_size: int, layers: List[int]):
        super(UserMatchUpModel, self).__init__()
        self._model = nn.Sequential(
            *self._get_layers(input_size, layers),
            nn.Linear(layers[-1], 1),
            nn.Sigmoid()
        )

    def _get_layers(self, input_size: int, layers: List[int]):
        layers = [input_size] + layers
        layer_models = []
        for i in range(0, len(layers) - 1):
            layer_models.append(nn.Linear(layers[i], layers[i + 1]))
            layer_models.append(nn.ReLU())
            layer_models.append(nn.BatchNorm1d(layers[i + 1]))
        return layer_models

    def forward(self, x):
        return self._model(x)
