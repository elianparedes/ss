import os

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

directorio = "../../output/va-etha/"

datos = []

colores = {}

colores_generados = iter(plt.cm.tab10.colors)

for archivo in os.listdir(directorio):
    if archivo.endswith(".csv"):
        ruta_completa = os.path.join(directorio, archivo)

        df = pd.read_csv(ruta_completa)

        df = df[df['n'] != 'n']

        df['va'] = pd.to_numeric(df['va'], errors='coerce')
        df['etha'] = pd.to_numeric(df['etha'], errors='coerce')

        n = df.iloc[0]['n']
        l = df.iloc[0]['l']
        if (n, l) not in colores:
            colores[(n, l)] = next(colores_generados)
        datos.append((df[['va', 'etha']], n, l))

plt.figure(figsize=(10, 5))

for df, n, l in datos:
    plt.scatter(df['etha'], df['va'], s=10, color=colores[(n, l)], label=f'N={n}, L={l}')

plt.xlabel('Eta')
plt.ylabel('Va')
plt.xlim(0, 5)
plt.ylim(0, 1)
plt.xticks(np.arange(0, 5.1, 0.5))
plt.yticks(np.arange(0, 1.1, 0.1))
plt.grid(True, which='both', linestyle='--', linewidth=0.5)

plt.legend(loc='upper left', bbox_to_anchor=(1, 1))

plt.tight_layout()
plt.savefig(directorio + 'va-eta-densities.png')
plt.show()
