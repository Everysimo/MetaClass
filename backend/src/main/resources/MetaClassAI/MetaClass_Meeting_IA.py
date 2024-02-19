#!/usr/bin/env python
# coding: utf-8

# # Regressione lineare multipla

# ## Ottenimento del dataset

# In[8]:


import pandas as pd
import os
import matplotlib.pyplot as plt
from six.moves import urllib
import seaborn as sb
from matplotlib import style

style.use('seaborn-whitegrid')

plt.rcParams['figure.figsize'] = (20, 10)

# Verifica il contenuto del file
try:
  df = pd.read_csv(FILE_NAME)
  print("Contenuto del DataFrame:")
  print(df.head())
  # Visualizza l'istogramma
  df.hist()
  plt.show()
except pd.errors.EmptyDataError:
  print("Il file CSV è vuoto.")
except pd.errors.ParserError:
  print("Errore di parsing del file CSV.")
except Exception as e:
  print(f"Errore sconosciuto: {e}")


# ## 1)DataCleaning

# ### 1.1) Descrizione del dataset

# In[9]:


#descrizione dataset
df.describe()


# ### 1.2) Verifica valori null nel dataset

# In[ ]:


#verifica se ci sono dati null nel dataset
df.isnull().any()


# ### 1.3) Verifica valori N/A nel dataset

# In[ ]:


#verifica se ci sono dati NA nel dataset
df.isna().any()


# ###1.4) Data Plot

# In[ ]:


#data plot visualizzare dipendenze tra variabili
sb.pairplot(df)
#salvataggio plot
plt.savefig('pairplor.png')


# ###1.5) Plot Linearità e Multicollinearità

# In[10]:


#Data plot su un sottoinsieme preso in esame
sb.pairplot(df,x_vars=["Age","Gender", "MotionSickness", "ImmersionLevel"], y_vars=["Duration"])
#salvataggio linear plot
plt.savefig('linearPlot.png')


# In[ ]:


#correlazione tra le variabili
sb.heatmap(df.corr(), annot=True, cmap="RdYlGn", square=True)


# ## 2)Divisione in Training e Test Sets, Feature Selection

# ###2.1) Definizione classe Metrics

# In[ ]:


#oggetto che contiene le metriche
class Metrics1:
  #costruttore
  def __init__(self,mae,mse,rmse):
    self.mae=mae
    self.mse=mse
    self.rmse=rmse

  #ToString
  def __str__(self):
    return f'Metrics [mae= {self.mae} mse= {self.mse} rmse= {self.rmse} mean= {np.mean([self.mae,self.mse,self.rmse])}'


# ###2.2) Definizione classe MetricsResultContainer

# In[ ]:


#classe per contenere risultato delle metriche
class MetricsResultContainer:
  meanMAE = []
  meanMSE = []
  meanRMSE = []
  #costruttore
  def __init__(self,model,alg,scaler,param,metricsMean):
    self.model=model
    self.alg=alg
    self.scaler=scaler
    self.param=param
    self.metricsMean=metricsMean
    self.meanMAE = []
    self.meanMSE = []
    self.meanRMSE = []
  #funzione per stampare metriche
  def printMetrics(self):
    for m in self.metricsMean:
      self.meanMAE.append(m.mae)
      self.meanMSE.append(m.mse)
      self.meanRMSE.append(m.rmse)
    print("meanMAE=",np.mean(self.meanMAE))
    print("meanMSE=",np.mean(self.meanMSE))
    print("meanRMSE=",np.mean(self.meanRMSE))


# ### 2.3) Istanziazione funzioni per Regressione

# In[11]:


import copy
import matplotlib.pyplot as plt
import numpy as np
import statsmodels.api as sm

from sklearn import metrics
from sklearn.datasets import load_iris
from sklearn.feature_selection import VarianceThreshold, SelectKBest, chi2, f_regression
from sklearn.model_selection import RepeatedKFold, KFold
from sklearn.preprocessing import StandardScaler,MinMaxScaler,RobustScaler
from sklearn.base import clone

from yellowbrick.regressor import ResidualsPlot
from yellowbrick.base import Visualizer

from statsmodels.stats.diagnostic import het_breuschpagan
from statsmodels.compat import lzip

from termcolor import colored as cl

#funzione per generare il modello, divisione training e test, features scaling, selection
def generateModel(alg, scaler, model, select):
  #array di metriche (MAE,MSE,RMSE)
  metrics1 = []
  #generazione n_split test/trainig sets
  #uso algoritmo per dividere i dati di raining da quelli di test
  for train_index, test_index in alg.split(X,y):
    clone_model = clone(model)
    X_train, X_test = X.iloc[train_index], X.iloc[test_index]
    y_train, y_test = y.iloc[train_index], y.iloc[test_index]
    #feature scaling sui traing test
    X_train_z = scaler.fit_transform(X_train)
    X_test_z = scaler.transform(X_test)
    #applicazione feature selection su train_z
    X_train_z = select.fit_transform(X_train_z, y_train)
    X_test_z = select.transform(X_test_z)

    #training dell'algoritmo sui training set
    clone_model.fit(X_train_z,y_train)
    #validazione modello e applicazione predizione sui testSet
    y_pred = clone_model.predict(X_test_z)
    modelCopy = copy.copy(clone_model)
    #calcolo metriche predizione
    metrics1.append(
        Metrics1(metrics.mean_absolute_error(y_test,y_pred),
                 metrics.mean_squared_error(y_test,y_pred),
                 np.sqrt(metrics.mean_squared_error(y_test,y_pred))
                 )
        )
  return metrics1

#Scelta variabile dipendente (y) e indipendenti (X)
X=df.drop(columns=['Duration','VRHeadset',"UserID"])
y=df.Duration
#numero record nel dataset
k=len(df)
#calcolo k ideale da usare nelle tecniche di validazione deve essere il 30% della lunghezza del dataset
k= (k/(k*0.3))
#Kf divisione dataset per k gruppi per testare mediante due algoritmi Kfold-RepeateKFold
kf = KFold(n_splits=int(np.ceil(k)),random_state=42, shuffle=True)
#rKf con k gruppi, e 10 ripetizioni
rkf = RepeatedKFold(n_splits=int(np.ceil(k)), n_repeats=100, random_state=42)
#rkf con 3 gruppi e 10 ripetizioni per questioni  di utilizzo della ram messa a disposizione da google
rkfRF = RepeatedKFold(n_splits=3, n_repeats=10, random_state=42)
#instanziazione algritmo per selezione dei KBest individui
select = SelectKBest(f_regression, k=4)
#risultati: array di array di metriche
metricsResults = []


# ## 3) Model Testing

# ### 3.1) Linear Regression

# In[ ]:


from sklearn.linear_model import LinearRegression
#Linear Regression
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero ZScore e KFold
print("Linear Regression - ZScore Normalization - KF")
m = MetricsResultContainer("Linear Regression","KF","ZScore","Default",
  generateModel(kf, StandardScaler(),  LinearRegression(), select)
)
metricsResults.append(m)
m.printMetrics()
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero ZScore e RKFold
print("\nLinear Regression - ZScore Normalization - RKF")
m = MetricsResultContainer("Linear Regression","RKF","ZScore","Default",
  generateModel(rkf, StandardScaler(),  LinearRegression(), select)
)
metricsResults.append(m)
m.printMetrics()
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero Minimax e KFold
print("\nLinear Regression - MinMax Normalization - KF")
m = MetricsResultContainer("Linear Regression","KF","MinMax","Default",
  generateModel(kf, MinMaxScaler(),  LinearRegression(), select)
)
metricsResults.append(m)
m.printMetrics()
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero Minmax e RKFold
print("\nLinear Regression - MinMax Normalization - RKF")
m = MetricsResultContainer("Linear Regression","RKF","MinMax","Default",
  generateModel(rkf, MinMaxScaler(),  LinearRegression(), select)
)
metricsResults.append(m)
m.printMetrics()
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero RobustScaler e KFold
print("\nLinear Regression - RobustScaler Normalization - KF")
m = MetricsResultContainer("Linear Regression","KF","RobustScaler","Default",
  generateModel(kf, RobustScaler(),  LinearRegression(), select)
)
metricsResults.append(m)
m.printMetrics()
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero RobustScaler e RKFold
print("\nLinear Regression - RobustScaler Normalization - RKF")
m = MetricsResultContainer("Linear Regression","RKF","RobustScaler","Default",
  generateModel(rkf, RobustScaler(), LinearRegression(), select)
)
metricsResults.append(m)
m.printMetrics()


# ### 3.2) DecisionTree Regression

# In[ ]:


#utilizziamo DecisionTree Regression
from sklearn.tree import DecisionTreeRegressor
print("DecisionTreeRegressor - ZScore Normalization - KF")
m = MetricsResultContainer("DecisionTree Regression","KF","ZScore","Default",
  generateModel(kf, StandardScaler(),  DecisionTreeRegressor(), select)
)
metricsResults.append(m)
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero ZScore e KFold
m.printMetrics()

print("\nDecisionTreeRegressor - ZScore Normalization - RKF")
m = MetricsResultContainer("DecisionTree Regression","RKF","ZScore","Default",
  generateModel(rkf, StandardScaler(),  DecisionTreeRegressor(), select)
)
metricsResults.append(m)
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero ZScore e RKFold
m.printMetrics()

print("\nDecisionTreeRegressor - MinMax Normalization - KF")
m = MetricsResultContainer("DecisionTree Regression","KF","MinMax","Default",
  generateModel(kf, MinMaxScaler(),  DecisionTreeRegressor(), select)
)
metricsResults.append(m)
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero Minimax e KFold
m.printMetrics()

print("\nDecisionTreeRegressor - MinMax Normalization - RKF")
m = MetricsResultContainer("DecisionTree Regression","RKF","MinMax","Default",
  generateModel(rkf, MinMaxScaler(),  DecisionTreeRegressor(), select)
)
metricsResults.append(m)
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero Minmax e RKFold
m.printMetrics()

print("\nDecisionTreeRegressor - RobustScaler Normalization - KF")
m = MetricsResultContainer("DecisionTree Regression","KF","Robust","Default",
  generateModel(kf, RobustScaler(),  DecisionTreeRegressor(), select)
)
metricsResults.append(m)
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero RobustScaler e KFold
m.printMetrics()

print("\nDecisionTreeRegressor - RobustScaler Normalization - RKF")
m = MetricsResultContainer("DecisionTree Regression","RKF","Robust","Default",
  generateModel(rkf, RobustScaler(), DecisionTreeRegressor(), select)
)
metricsResults.append(m)
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero RobustScaler e RKFold
m.printMetrics()


# ### 3.3) Lasso Regression

# In[ ]:


#Utilizzo Lasso regression
from sklearn import linear_model
lassoReg = linear_model.Lasso()
print("Lasso Regression - ZScore Normalization - KF")
m = MetricsResultContainer("Lasso Regression","KF","ZScore","Default",
  generateModel(kf, StandardScaler(),  lassoReg, select)
)
metricsResults.append(m)
m.printMetrics()

print("\nLasso Regression - ZScore Normalization - RKF")
m = MetricsResultContainer("Lasso Regression","RKF","ZScore","Default",
  generateModel(rkfRF, StandardScaler(),  lassoReg, select)
)
metricsResults.append(m)
m.printMetrics()

print("\nLasso Regression - MinMax Normalization - KF")
m = MetricsResultContainer("Lasso Regression","KF","MinMax","Default",
  generateModel(kf, MinMaxScaler(),  lassoReg, select)
)
metricsResults.append(m)
m.printMetrics()

print("\nLasso Regression - MinMax Normalization - RKF")
m = MetricsResultContainer("Lasso Regression","RKF","MinMax","Default",
  generateModel(rkfRF, MinMaxScaler(),  lassoReg, select)
)
metricsResults.append(m)
m.printMetrics()

print("\nLasso Regression - RobustScaler Normalization - KF")
m = MetricsResultContainer("Lasso Regression","KF","Robust","Default",
  generateModel(kf, RobustScaler(),  lassoReg, select)
)
metricsResults.append(m)
m.printMetrics()

print("\nLasso Regression - RobustScaler Normalization - RKF")
m = MetricsResultContainer("Lasso Regression","RKF","Robust","Default",
  generateModel(rkfRF, RobustScaler(), lassoReg, select)
)
metricsResults.append(m)
m.printMetrics()


# ### 3.4) Ridge Regression

# In[12]:


#Utilizzo Ridge Regression
from sklearn import linear_model
bridgeReg = linear_model.Ridge()
print("Ridge Regression - ZScore Normalization - KF")
#stampa le metriche uscenti dall'algoritmo utilizzato ovvero ZScore e KFold
m = MetricsResultContainer("Ridge Regression","KF","ZScore","Default",
  generateModel(kf, StandardScaler(),  bridgeReg, select)
)
metricsResults.append(m)
m.printMetrics()


print("\nRidge Regression - ZScore Normalization - RKF")
m = MetricsResultContainer("Ridge Regression","RKF","ZScore","Default",
  generateModel(rkfRF, StandardScaler(),  bridgeReg, select)
)
metricsResults.append(m)
m.printMetrics()

print("\nRidge Regression - MinMax Normalization - KF")
m = MetricsResultContainer("Ridge Regression","KF","MinMax","Default",
  generateModel(kf, MinMaxScaler(),  bridgeReg, select)
)
metricsResults.append(m)
m.printMetrics()

print("\nRidge Regression - MinMax Normalization - RKF")
m = MetricsResultContainer("Ridge Regression","RKF","MinMax","Default",
  generateModel(rkfRF, MinMaxScaler(),  bridgeReg, select)
)
metricsResults.append(m)
m.printMetrics()

print("\nRidge Regression - RobustScaler Normalization - KF")
m = MetricsResultContainer("Ridge Regression","KF","Robust","Default",
  generateModel(kf, RobustScaler(),  bridgeReg, select)
)
metricsResults.append(m)
m.printMetrics()

print("\nRidge Regression - RobustScaler Normalization - RKF")
m = MetricsResultContainer("Ridge Regression","RKF","Robust","Default",
  generateModel(rkfRF, RobustScaler(), bridgeReg, select)
)
metricsResults.append(m)
m.printMetrics()


# ### 3.5) Random Forest Regression

# In[ ]:


#Utilizzo Foresta regression
from sklearn.ensemble import RandomForestRegressor

print("RandomForestRegressor - ZScore Normalization - KF")
m = MetricsResultContainer("RandomForest Regression","KF","Robust","Default",
  generateModel(kf, StandardScaler(),  RandomForestRegressor(), select)
)
metricsResults.append(m)
m.printMetrics()

print("\nRandomForestRegressor - ZScore Normalization - RKF")
m = MetricsResultContainer("RandomForest Regression","RKF","Robust","Default",
  generateModel(rkfRF, StandardScaler(),  RandomForestRegressor(), select)
)
metricsResults.append(m)
m.printMetrics()

print("\nRandomForestRegressor - MinMax Normalization - KF")
m = MetricsResultContainer("RandomForest Regression","KF","MinMax","Default",
  generateModel(kf, MinMaxScaler(),  RandomForestRegressor(), select)
)
metricsResults.append(m)
m.printMetrics()

print("\nRandomForestRegressor- MinMax Normalization - RKF")
m = MetricsResultContainer("RandomForest Regression","RKF","MinMax","Default",
  generateModel(rkfRF, MinMaxScaler(),  RandomForestRegressor(), select)
)
metricsResults.append(m)
m.printMetrics()

print("\nRandomForestRegressor - RobustScaler Normalization - KF")
m = MetricsResultContainer("RandomForest Regression","KF","Robust","Default",
  generateModel(kf, RobustScaler(),  RandomForestRegressor(), select)
)
metricsResults.append(m)
m.printMetrics()

print("\nRandomForestRegressor - RobustScaler Normalization - RKF")
m = MetricsResultContainer("RandomForest Regression","RKF","Robust","Default",
  generateModel(rkfRF, RobustScaler(), RandomForestRegressor(), select)
)
metricsResults.append(m)
m.printMetrics()


# ### 3.6) SVR Regression

# In[ ]:


#Utilizzo SVR Regression
from sklearn.svm import SVR
print("SVR - ZScore Normalization - KF")
m = MetricsResultContainer("SVR Regression","KF","ZScore","Default",
  generateModel(kf, StandardScaler(),  SVR(), select)
)
metricsResults.append(m)
m.printMetrics()

print("\nSVR - ZScore Normalization - RKF")
m = MetricsResultContainer("SVR Regression","RKF","ZScore","Default",
  generateModel(rkfRF, StandardScaler(),  SVR(), select)
)
metricsResults.append(m)
m.printMetrics()

print("\nSVR - MinMax Normalization - KF")
m = MetricsResultContainer("SVR Regression","KF","MinMax","Default",
  generateModel(kf, MinMaxScaler(),  SVR(), select)
)
metricsResults.append(m)
m.printMetrics()

print("\nSVR- MinMax Normalization - RKF")
m = MetricsResultContainer("SVR Regression","RKF","MinMax","Default",
  generateModel(rkfRF, MinMaxScaler(),  SVR(), select)
)
metricsResults.append(m)
m.printMetrics()

print("\nSVR - RobustScaler Normalization - KF")
m = MetricsResultContainer("SVR Regression","KF","Robust","Default",
  generateModel(kf, RobustScaler(),  SVR(), select)
)
metricsResults.append(m)
m.printMetrics()

print("\nSVR - RobustScaler Normalization - RKF")
m = MetricsResultContainer("SVR Regression","RKF","Robust","Default",
  generateModel(rkfRF, RobustScaler(), SVR(), select)
)
metricsResults.append(m)
m.printMetrics()


# 

# ## 4) Creazione pipeline di machine learning

# In[ ]:


get_ipython().system('pip install sklearn2pmml')


# In[ ]:


from sklearn2pmml.pipeline import PMMLPipeline
from sklearn2pmml import sklearn2pmml
#creazione pipeline del modello più performante
#RandomForestRegression
pipeline = PMMLPipeline([("Regression", RobustScaler())])
pipeline.fit(X,y)
#estrazione pipiline in file .pmml
sklearn2pmml(pipeline, "RegressoreDurataMeeting.pmml", with_repr = True)

