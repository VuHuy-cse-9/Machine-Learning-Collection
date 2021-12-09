from RBM import BinaryRBM
import numpy as np
from scipy.io import arff
from sklearn.model_selection import train_test_split

#Load data
data = np.array(arff.loadarff("BMW.arff")[0].tolist(), dtype=np.int8)
dataset = data[:, 0:7]
labels = data[:, 7]

print("Number of dataset: {}".format(dataset.shape[0]))
print("Number of features: {}".format(dataset.shape[1]))
print("Number of classes: {}".format(np.amax(labels) + 1))

for learning_rate in np.arange(1e-4, 1e-2, step=1e-4):
    model = BinaryRBM(n_hidden_units=100, learning_rate=learning_rate, n_epochs=30, verbose=False)
    model.fit(dataset)
    data_transform = model.transform(dataset)
    data_reconstructed = model._reconstruct(data_transform)
    print("learning_rate: {} - error: {}".format(learning_rate, np.mean(np.sum((dataset - data_reconstructed) ** 2, 1))))






