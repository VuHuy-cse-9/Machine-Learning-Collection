import numpy as np
from numpy.core.fromnumeric import shape
from activations import SigmoidActivationFunction
from utils import batch_generator


class BinaryRBM:
    """
    Implementation a Binary Restricted Boltzmann machine
    """

    def __init__(self,
                n_hidden_units=100,
                activation_function="sigmoid",
                optimization_algorithms="sgd",
                learning_rate = 1e-3,
                n_epochs=10,
                contrastive_divergence_iter=1,
                batch_size=32,
                verbose=True):
        self.n_hidden_units = n_hidden_units
        self.activation_function = activation_function
        self.optimization_algorithms = optimization_algorithms
        self.learning_rate = learning_rate
        self.n_epochs = n_epochs
        self.contrastive_divergence_iter = contrastive_divergence_iter
        self.batch_size = batch_size
        self.verbose = verbose

    def fit(self, X):
        """
        Fits a model given data
        :params X:array-like, shape = (n_samples, n_features)
        :return:
        """
        #Initialize RBM parameters
        self.n_visible_units = X.shape[1]
        if self.activation_function == "sigmoid":
            self.W = np.random.randn(self.n_hidden_units, self.n_visible_units) / np.sqrt(self.n_visible_units)
            self.c = np.random.randn(self.n_hidden_units) / np.sqrt(self.n_visible_units)
            self.b = np.random.randn(self.n_visible_units) / np.sqrt(self.n_visible_units)
            self._activation_function_class = SigmoidActivationFunction #Add activation function
        else:
            raise ValueError("Invalid activation function.")

        if self.optimization_algorithms == "sgd":
            self._stochastic_gradient_descent(X)
        else:
            raise ValueError("Invalid optimization algorithms.")

    def _stochastic_gradient_descent(self, _data):
        """
        Performs stochastic gradient descent algorithms
        :params data: array-like shape = (n_samples, n_features)
        :return: 
        """
        accum_delta_W = np.zeros(self.W.shape)
        accum_delta_b = np.zeros(self.b.shape)
        accum_delta_c = np.zeros(self.c.shape)
        for iteration in np.arange(1, self.n_epochs + 1):
            idx = np.random.permutation(_data.shape[0])
            data = _data[idx]
            for batch in batch_generator(self.batch_size, data):
                accum_delta_W[:] = .0
                accum_delta_b[:] = .0
                accum_delta_c[:] = .0
                for sample in batch:
                    delta_W, delta_b, delta_c = self._contrastive_divergence(sample)
                    accum_delta_W += delta_W
                    accum_delta_b += delta_b
                    accum_delta_c += delta_c
                self.W += self.learning_rate * (accum_delta_W / self.batch_size)
                self.b += self.learning_rate * (accum_delta_b / self.batch_size)
                self.c += self.learning_rate * (accum_delta_c / self.batch_size)
            if self.verbose:
                error = self._compute_reconstruction_error(data)
                print(">> Epoch %d finished \tRBM Reconstruction error %f" % (iteration, error))
        
        

    def _contrastive_divergence(self, vector_visible_units):
        """
        Computes gradients using contrastive divergence methods
        :params vector_visible_units: array-like shape = (n_features, )
        :return:
        """
        v_0 = vector_visible_units
        v_t = np.array(vector_visible_units)

        for t in np.arange(self.contrastive_divergence_iter):
            h_t = self._sample_hidden_units(v_t)    #Vector of {0, 1}, shape = (1, n_hidden_units)
            v_t = self._sample_visible_units(h_t)   #Vector of {0, 1}, shape = (1, n_visible_units)

        v_k = v_t
        h_0 = self._compute_hidden_units(v_0)
        h_k = self._compute_hidden_units(v_k)
        delta_W = np.outer(h_0, v_0) - np.outer(h_k, v_k)
        delta_b = v_0 - v_k
        delta_c = h_0 - h_k
        return delta_W, delta_b, delta_c

    def _sample_hidden_units(self, vector_visible_units):
        """
        Compute hidden units activations by sampling from a binomial distribution
        :params vector_visible_units: array-like, shape = (n_features, )
        :return:
        """
        hidden_units = self._compute_hidden_units(vector_visible_units)
        return (np.random.random_sample(len(hidden_units)) < hidden_units).astype(np.int32)

    def _sample_visible_units(self, vector_hidden_units):
        visible_units = self._compute_visible_units(vector_hidden_units)
        return (np.random.random_sample(len(visible_units)) < visible_units).astype(np.int32)

    def _compute_hidden_units(self, vector_visible_units):
        """
        Compute hidden units outputs
        :params vector_visible_units: array-like, shape = (n_features, )
        :return:
        """
        v =np.expand_dims(vector_visible_units, 0)
        h = np.squeeze(self._compute_hidden_units_matrix(v))
        return np.array([h]) if not h.shape else h

    def _compute_hidden_units_matrix(self, matrix_visible_units):
        """
        Compute hidden unit outputs
        :params matrix_visible_outputs: array-like, shape = (n_samples, n_features)
        :return:
        """
        return np.transpose(self._activation_function_class.function(
            np.dot(self.W, np.transpose(matrix_visible_units)) + self.c[:, np.newaxis]
        ))

    def _compute_visible_units(self, vector_hidden_units):
        """
        Compute visible unit outputs
        :params: array-like, shape = (n_features, )
        :return:
        """
        h = np.expand_dims(vector_hidden_units, 0)
        v = np.squeeze(self._compute_visible_units_matrix(h))
        return np.array([v]) if not v.shape else v

    def _compute_visible_units_matrix(self, matrix_hidden_units):
        """
        compute visible unit outputs
        :params: array-like, shape = (n_features, )
        :return:
        """
        return self._activation_function_class.function(
            np.dot(matrix_hidden_units, self.W) + self.b[np.newaxis, :]
        )

    def _compute_free_energy(self, vector_visible_units):
        pass

    def _compute_reconstruction_error(self, data):
        """
        Computes the reconstruction error of the data
        :params data: array-like, shape = (n_samples, n_features)
        :return
        """
        data_transformed = self.transform(data)
        data_reconstructed = self._reconstruct(data_transformed)
        return np.mean(np.sum((data_reconstructed - data) ** 2, 1))

    def transform(self, X):
        """
        Transforms data using the fitted model (Compute hidden units)
        :params X: array-like, shape = (n_samples, n_features)
        :return:
        """
        if len(X.shape) == 1:
            return self._compute_hidden_units(X)
        return self._compute_hidden_units_matrix(X)

    def _reconstruct(self, transformed_data):
        """
        Reconstruct visible units given hidden units
        :params transformed_data: array-like, shape = (n_samples, n_features)
        :return
        """
        if len(transformed_data.shape) == 1:
            return self._compute_visible_units(transformed_data)
        return self._compute_visible_units_matrix(transformed_data)
        