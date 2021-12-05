#!/usr/bin/env python
# coding: utf-8

# 
# Entropy tại một node:
# $$
# H(S)    = -\sum^{C}_{c=1}\frac{N_c}{N}\log(\frac{N_c}{N})
# $$
# Entropy của một thuộc tính tại một node
# $$
# H(x, S) = \sum_{k=1}^{K}\frac{m_k}{N}H(S_k)
# $$
# Với: 
# 
# 
# *   $m_k$ là số datapoint mà node đang giữ có giá trị $value_k$ của thuộc tính
# *   $N$ là số datapoint mà node này đang giữ
# 
# 
# Information gain nếu chọn thuộc tính này tại node này
# $$
# G(x, S) = H(S) - H(x, S)
# $$
# Hàm mục tiêu của thuật toán ID3
# $$
# x^* = argmax_x G(x, S) = argmin_x H(x, S) 
# $$

# In[7]:

import numpy as np


class TreeNode():
  def __init__(self, ids=[], entropy=0, depth = 0, children = []):
    self.ids   = ids                                #List of index of datapoints
    self.label = None                               #Label of node if it's a leaf
    self.entropy = entropy                          #Entropy of this node
    self.depth = depth                              #Depth of current node
    self.children = children                        #List of its child's node
    self.order = None                               #unique value of attribute that assigned for this node
    self.split_attribute = None                     #index of attribute assign for this node

  def set_properties(self, split_attribute, order):
    self.split_attribute = split_attribute
    self.order = order

  def set_label(self, label):
    self.label = label

def entropy(freqs):
  """
  List of frequency of number of labels.
  """
  result = 0
  for freq in freqs:
    if freq == 0: continue
    result -= freq / np.sum(freqs)  * np.log(freq / np.sum(freqs))
  return result

class DecisionTree():
  def __init__(self, max_depth=10, min_gain=1e-4, min_samples_split = 2, n_features=None):
      self.root = None                             #Root node
      self.Ntrain = 0
      self.max_depth = max_depth                   #Max depth of the trees
      self.min_gain = min_gain                     #Maximum entropy value for the node to be added
      self.min_samples_split = min_samples_split   #minimum number of dataset for node to split.
      self.n_features = n_features                 #Number of random features to train
      self.features_idxs = []                      #Index of random select features
  
  def fit(self, data, targets):
    """
    :params: attributes: List of attribute's name / not include label's attribute
    """
    self.Ntrain = data.shape[0]
    self.targets = targets
    self.labels = np.unique(targets)
    if self.n_features != None:                             #Select random number of feature
      if self.n_features < data.shape[1]:         
        self.features_idxs = self.bootstrap_feature(data, self.n_features)
        data = data[:, self.features_idxs]
    self.data = data

    ids = np.arange(self.Ntrain)
    self.root = TreeNode(ids=ids, entropy = self.get_entropy(ids), depth=0)
    queue = np.array([self.root])
    while len(queue):
      node, queue = queue[-1], queue[:-1]          #Remove the last node in queue
      if node.depth < self.max_depth or node.entropy < self.min_gain:
        node.children = self.split(node)
        if not node.children:                      #This node doesn't have child node - Leaf node
          self.set_label(node)                     #Set label for this node
        queue = np.concatenate((node.children, queue))    #Add new child node to tree
      else:                                        #Tree depth is maximum, Add new node
        self.set_label(node)

  def bootstrap_feature(self, X, r_features):
    """
    :params X: dataset 
            r_features: Number of feature to select
    :return idxs: Index of random select feature
    """
    n_features = X.shape[1]
    if n_features < r_features:
        r_features = n_features
        
    return np.random.choice(n_features, size=r_features, replace=False)


  def get_entropy(self, ids):
    """
    :params: ids: List of index of datapoints need to be calculate entropy (H(s))
    :return: entropy value of these datapoints
    """
    if len(ids) == 0: return 0
    freqs = []                                    #Freq of each labels in datapoints
    for label in self.labels:
      freq = np.sum(self.targets[ids] == label)
      freqs.append(freq)
    return entropy(np.array(freqs))

  def set_label(self, node):
    """
    Find label for a leaf by major voting.
    """
    ids = node.ids    
    best_label = None                             #Labels for major vote
    best_label_count = 0
    for label in self.labels:
      label_count = np.sum(self.targets[ids] == label)
      if best_label_count < label_count:
        best_label_count = label_count
        best_label = label
    node.set_label(best_label)

  def split(self, node):
    """
    :params node: parent have child need to be split
    :return children node
    """
    ids = node.ids                                #All datapoints'idx of this nodes
    best_gain = 0                                 #variable to store max information gain
    best_splits = []                              #Array to store datapoints of attribute has max information gain
    best_attribute = []                           #index of best attribute be selected
    order = None                                  #List of all unique value of atribute
    sub_data = self.data[ids, :]
    for att_i in np.arange(self.data.shape[1]):
      values = np.unique(sub_data[:, att_i])      #Get all unique value of attribute
      if len(values) == 1:                        #If only one value in attribute => entropy = 0
        continue  
      splits = []   
      for val in values:                          #Separate data's with different value into split array.
        sub_ids = ids[np.argwhere(sub_data[:, att_i] == val).flatten()] #Bug here.
        splits.append(sub_ids)
      
      is_continue = False 
      for split in splits:
        if (len(split) < self.min_samples_split): #Only split if number of dataset in each child node >= min_sample_splits
          is_continue = True
          break
      if (is_continue): continue   

      HxS = 0 
      for split in splits:                        #Calculate H(x,S) of all attributes:
        HxS += len(split) / len(ids) * self.get_entropy(split)

      gain = node.entropy - HxS                   #Get information gain
      if gain < self.min_gain: 
        continue                                  #Ignore if information gain is small
      if best_gain < gain:
        best_gain = gain
        best_splits = splits
        best_attribute = att_i
        order = values
    node.set_properties(best_attribute, order)
    child_nodes = [TreeNode(ids=best_split, entropy = self.get_entropy(best_split), depth=node.depth + 1) for best_split in best_splits]
    return child_nodes
      
      

  def predict(self, new_data):
    """
    :params new_data: new numpy_array, each row is a datapoint
    :return labels of new_data
    """
    if len(self.features_idxs) > 0:
      new_data = new_data[:, self.features_idxs]
    npoints = new_data.shape[0]
    labels  = []
    for n in np.arange(npoints):
      x = new_data[n, :]
      node = self.root                             #Start from root node
      while node.children:                         #Recursively pass through child's node until reach leaf node
        node = node.children[list(node.order).index(x[node.split_attribute])]
      labels.append(node.label)
    return labels

