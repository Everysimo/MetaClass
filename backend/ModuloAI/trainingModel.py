# coding: utf-8
import pandas as pd

FILE_NAME = "../src/main/resources/ModuloAI/data.csv"

# Verifica il contenuto del file
try:
    df = pd.read_csv(FILE_NAME)
except pd.errors.EmptyDataError:
    print("Il file CSV è vuoto.")
except pd.errors.ParserError:
    print("Errore di parsing del file CSV.")
except Exception as e:
    print(e)

from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import RobustScaler
from sklearn2pmml import sklearn2pmml
from sklearn2pmml.pipeline import PMMLPipeline

#Scelta variabile dipendente (y) e indipendenti (X)
X=df.drop(columns=['Duration','VRHeadset',"UserID"])   #feature selection
y=df.Duration

# Adattamento e trasformazione dei dati con RobustScaler
scaler = RobustScaler()
X_scaled = scaler.fit_transform(X)

# Creazione del modello di regressione con Random Forest
model = RandomForestRegressor()
model.fit(X_scaled, y)

# Creazione del PMMLPipeline con il modello già addestrato
pipeline = PMMLPipeline([("Regression", model)])

# Tentativo di estrazione del pipeline in un file PMML con gestione delle eccezioni
try:
    sklearn2pmml(pipeline, "RegressoreDurataMeeting.pmml", with_repr=True)
except Exception as e:
    print("Si è verificato un errore durante l'estrazione del pipeline in un file PMML:")
    print(e)




