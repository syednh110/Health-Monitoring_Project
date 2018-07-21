

import pandas
from pandas.plotting import scatter_matrix
#import matplotlib.pyplot as plt
import numpy as np
from sklearn.ensemble import RandomForestClassifier
from sklearn import model_selection
from sklearn.metrics import classification_report
from sklearn.metrics import confusion_matrix
from sklearn.metrics import accuracy_score
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.neighbors import KNeighborsClassifier
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis
from sklearn.naive_bayes import GaussianNB
from sklearn.svm import SVC
from sklearn.decomposition import PCA
from sklearn.preprocessing import StandardScaler
from sklearn import preprocessing


# In[2]:

dataset=pandas.read_csv('CleavelandData.arff')
dataset.replace('?',-99999, inplace=True)

features = ['age','sex','cp','trestbps','chol','fbs','restecg','thalach','exang','oldpeak','slope','ca','thal']
x = dataset.loc[:, features].values
y = dataset.loc[:,['num']].values
normalized_X = preprocessing.normalize(x)
x = StandardScaler().fit_transform(normalized_X)
pandas.DataFrame(data = x, columns = features).head()

pca = PCA(n_components=13)
principalComponents = pca.fit_transform(x)
principalDf = pandas.DataFrame(data = principalComponents
             , columns = ['pc1','pc2','pc3','pc4','pc5','pc6','pc7','pc8','pc9','pc10','pc11','pc12','pc13'])
principalDf.head(5)

dataset[['num']].head()

finalDf = pandas.concat([principalDf, dataset[['num']]], axis = 1)
finalDf.head(50)


#print(finalDf.describe())


# class distribution
#print(finalDf.groupby('num').size())



X=np.array(finalDf.drop(['num'], 1))
Y=np.array(finalDf['num'])
validation_size = 0.20
seed = 13
X_train, X_validation, Y_train, Y_validation = model_selection.train_test_split(X, Y, test_size=validation_size, random_state=seed)




# Test options and evaluation metric
seed = 13
scoring = 'accuracy'



models = []
models.append(('LR', LogisticRegression()))
models.append(('LDA', LinearDiscriminantAnalysis()))
models.append(('KNN', KNeighborsClassifier()))
models.append(('CART', DecisionTreeClassifier()))
models.append(('NB', GaussianNB()))
models.append(('RF', RandomForestClassifier()))
models.append(('SVM', SVC()))
# evaluate each model in turn
results = []
names = []
for name, model in models:
	kfold = model_selection.KFold(n_splits=10, random_state=seed)
	cv_results = model_selection.cross_val_score(model, X_train, Y_train, cv=kfold, scoring=scoring)
	results.append(cv_results)
	names.append(name)
	msg = "%s: %f (%f)" % (name, cv_results.mean(), cv_results.std())
	#print(msg)


clf= LinearDiscriminantAnalysis()
#print(X_train)
clf.fit(X_train,Y_train)

input_data = [float(x) for x in input().split()]
example_measures=np.array(input_data)
example_measures=example_measures.reshape(1,-1)
prediction=clf.predict(example_measures)
print(prediction[0])

