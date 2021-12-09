import numpy as np
"""
Reference:
yield & return: https://www.geeksforgeeks.org/use-yield-keyword-instead-return-keyword-python/
"""


def batch_generator(batch_size, data, labels=None):
    """
    Generate batch of samples
    :params data: array-like shape = (n_samples, n_features)
    :params labels: array-like shape = (n_samples, )
    :return
    """
    n_batches = int(np.ceil(data.shape[0] / float(batch_size)))
    idx = np.random.permutation(data.shape[0])
    data_shuffled = data[idx]
    if labels is not None:
        labels_shuffled = labels[idx]
    for i in np.arange(n_batches):
        start = i * batch_size
        end = start + batch_size
        if labels is not None:
            yield data_shuffled[start:end, :], labels_shuffled[start:end]
        else:
            yield data_shuffled[start:end, :]