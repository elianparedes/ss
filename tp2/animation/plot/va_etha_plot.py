import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv('../../output/va_etha/va_etha_31.6_4000.csv',dtype=str)

df['etha'] = pd.to_numeric(df['etha'], errors='coerce')
df['va'] = pd.to_numeric(df['va'], errors='coerce')
df['stDev'] = pd.to_numeric(df['stDev'], errors='coerce')

df.dropna(inplace=True)

n_value = df['n'].unique()[0]
l_value = df['l'].unique()[0]

plt.errorbar(df['etha'], df['va'], yerr=df['stDev'], fmt='o', capsize=5, label=f'N = {n_value}, L = {l_value}')

plt.xlabel('etha')
plt.ylabel('va')
plt.title('Representación de va en función de etha')
plt.legend()

plt.show()

