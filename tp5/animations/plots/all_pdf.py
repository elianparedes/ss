import os

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import utils.plots as plots

FILE_NAME = 'all_player_speeds_pdf_30.csv'

current_dir = os.path.dirname(os.path.abspath(__file__))
input_dir = os.path.join(current_dir, '..', 'data', 'output')
csv_file_path = os.path.join(input_dir, FILE_NAME)

data = pd.read_csv(csv_file_path)

plots.set_global_font_size(20)
fig, ax = plt.subplots()
plots.set_plot_size(fig, 14, 9)

ax.plot(data['bin_center'], data['probability'], marker='o', linestyle='-')

# Configurar etiquetas y título del gráfico
ax.set_xlabel('Velocidad (m/s)')
plt.yscale('log')
ax.set_ylabel('Densidad de probabilidad')

plt.grid(True)
plt.tight_layout()
plt.show()
