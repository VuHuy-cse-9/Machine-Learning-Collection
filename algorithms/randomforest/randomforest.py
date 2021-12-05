from decisiontree import DecisionTree, TreeNode

class RandomForest:
    def __init__(self, n_trees=20, min_samples_split=3, max_depth=20, min_gain=1e-4, n_features = 4):
        self.n_trees = n_trees
        self.min_samples_split = min_samples_split
        self.max_depth = max_depth
        self.min_gain = 1e-4
        self.n_features = n_features
        
    def fit(self, X, y):
        self.trees = []
        for _ in np.arange(self.n_trees):
            treei = DecisionTree(self.max_depth, self.min_gain, self.min_samples_split, n_features=self.n_features)
            Xi, yi = self.bootstrap_sample(X, y)
            treei.fit(Xi, yi)
            self.trees.append(treei)
            
    def predict(self, X):
        #Get prediction for each tree
        tree_preds = np.array([tree.predict(X) for tree in self.trees]).T
        #Majority votes
        label_type = np.unique(tree_preds)
        label_predicts = np.zeros((X.shape[0], )) 
        label_counts = np.zeros((X.shape[0], ))
        for label in label_type:
            label_count_i = np.sum(tree_preds == label, axis=1)
            label_predicts = np.where(label_counts < label_count_i, label, label_predicts)
        return label_predicts   
    
    def bootstrap_sample(self, X, y):
        n_samples = X.shape[0]
        idxs = np.random.choice(n_samples, size=n_samples, replace=True) #Get random number from 0 -> n_sample - 1, with redundance
        return X[idxs], y[idxs]