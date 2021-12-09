from abc import ABCMeta, abstractmethod
import numpy as np


class  ActivationFunction(object):
    """
    Class for abstract activation function
    """
    __metaclass__ = ABCMeta

    @abstractmethod
    def function(self, x):
        return

    @abstractmethod
    def prime(self, x):
        return

class SigmoidActivationFunction(ActivationFunction):
    @classmethod
    def function(cls, x):
        """
        Sigmoid function
        :params x:array-like, shape= (n_features, )
        :return
        """
        return 1 / (1 + np.exp(-x))

    @classmethod
    def prime(self, x):
        """
        Compute sigmoid first derivative
        :params x:array-like, shape = (n_features, )
        """
        return x * (1 - x)
