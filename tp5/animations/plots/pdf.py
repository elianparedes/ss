import os

import pandas as pd
import matplotlib.pyplot as plt

FILE_NAME = 'player_speeds_pdf.csv'

# Ruta al archivo CSV (asegúrate de actualizarla según sea necesario)
current_dir = os.path.dirname(os.path.abspath(__file__))
input_dir = os.path.join(current_dir, '..', 'data', 'output')
csv_file_path = os.path.join(input_dir, FILE_NAME)

data = pd.read_csv(csv_file_path)

lunatic_data = data[data['player'] == 'lunatic']

plt.figure(figsize=(10, 6))
plt.plot(lunatic_data['bin_center'], lunatic_data['probability'], marker='o', linestyle='-', color='b')
plt.xlabel('Velocidad')
plt.ylabel('Probabilidad')
plt.title('Distribución de Probabilidad de Velocidad para Lunatic')
plt.grid(True)
plt.show()
