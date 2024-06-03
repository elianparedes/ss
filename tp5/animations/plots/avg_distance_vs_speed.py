import math

import pandas as pd
from matplotlib import pyplot as plt

from utils.plots import set_global_font_size, set_plot_size

csv_file = "../data/output/avg_distance_speed.csv"

data = pd.read_csv(csv_file)
vd_values = data['vd']
mean_values = data['avg']
std_values = data['std']

set_global_font_size(20)
fig, ax = plt.subplots()
set_plot_size(fig, 16, 9)

plt.errorbar(vd_values, mean_values,
             yerr=std_values, fmt='o', capsize=5)

ax.grid(True)

ax.set_ylabel('$\\text{Promedio de la distancia entre el Loco y la pelota}$ ($\\text{m}$)')
ax.set_xlabel('$\\text{Velocidad deseada}$ ($\\text{m/s}$)')

plt.tight_layout()
plt.savefig('avg_distance_vs_speed.png')
plt.show()