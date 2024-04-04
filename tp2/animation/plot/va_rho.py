import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv('../../output/debugging.csv', dtype=str)

df['etha'] = pd.to_numeric(df['etha'], errors='coerce')
df['va'] = pd.to_numeric(df['va'], errors='coerce')
df['stdev'] = pd.to_numeric(df['stdev'], errors='coerce')
df['n'] = pd.to_numeric(df['n'], errors='coerce')
df['l'] = pd.to_numeric(df['l'], errors='coerce')

df.dropna(inplace=True)

l_value = df['l'].unique()[0]

df['rho'] = df['n'] / (l_value ** 2)

etha_value = df['etha'].unique()[0]

rho_values = np.arange(0, 10.5, 0.5)

df_filtered = df[df['rho'].isin(rho_values)]

plt.figure(figsize=(8, 5))
plt.errorbar(df_filtered['rho'], df_filtered['va'], yerr=df_filtered['stdev'], fmt='o', capsize=5, label='Va')
plt.xlabel('Rho', fontsize=11)
plt.ylabel('Va', fontsize=11)
plt.title(f'Va vs Rho (Etha = {etha_value}, L = {l_value})', fontsize=12)
plt.grid(True)
plt.legend()
plt.show()
