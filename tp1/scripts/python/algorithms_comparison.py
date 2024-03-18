import matplotlib.pyplot as plt
import pandas as pd

df_bf = pd.read_csv('../../output/analytics_bf.csv')
df_cim = pd.read_csv('../../output/analytics_cim.csv')

df_bf.sort_values('N', inplace=True)
df_cim.sort_values('N', inplace=True)

plt.plot(df_bf['N'], df_bf['TIME'], label='BF', color='red')
plt.plot(df_cim['N'], df_cim['TIME'], label='CIM', color='blue')

plt.title('Comparación de Tiempo de Ejecución BF vs CIM')
plt.xlabel('Número de Partículas')
plt.ylabel('Tiempo (ms)')

plt.legend()

plt.grid(True)

plt.show()

