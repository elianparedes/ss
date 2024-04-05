import matplotlib.pyplot as plt
import pandas as pd
import os

path_to_csv_folder = '../../output/va-time/'
csv_files = [file for file in os.listdir(path_to_csv_folder) if file.endswith('.csv')]

n_value = None
l_value = None

ethas = sorted([0.0, 2.0, 4.0])

# Configurar el tamaño de la figura para una proporción de 2:1
plt.figure(figsize=(25, 10))

for csv_file in csv_files:
    file_path = os.path.join(path_to_csv_folder, csv_file)
    data = pd.read_csv(file_path)
    if n_value is None:
        n_value = data['n'].iloc[0]
    if l_value is None:
        l_value = data['l'].iloc[0]
    etha_value = data['etha'].iloc[0]

    if ethas.__contains__(etha_value):
        plt.plot(data['time'], data['va'], label=f'etha = {etha_value}')

plt.xlabel('Tiempo (s)', fontsize=22, fontname='Times New Roman')
plt.ylabel('Va', fontsize=22, fontname='Times New Roman')
plt.title(f'N = {n_value}, L = {l_value}', fontsize=22, fontname='Times New Roman', loc='right')

# Configurar la leyenda para que aparezca al lado del gráfico
plt.legend(title='Etha:', fontsize=22, title_fontsize=22, loc='center left', bbox_to_anchor=(1, 0.5))

plt.xticks(fontsize=22, fontname='Times New Roman')
plt.yticks(fontsize=22, fontname='Times New Roman')

# Ajustar el layout para acomodar la leyenda fuera del gráfico
plt.tight_layout()

plt.savefig(path_to_csv_folder + 'va-time-etha' + '-' + str(l_value) + '-' + str(n_value) + '.png')
plt.show()