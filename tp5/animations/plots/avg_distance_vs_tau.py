import pandas as pd
from matplotlib import pyplot as plt

from utils.plots import set_global_font_size, set_plot_size

csv_file = "../data/output/avg_distance_tau.csv"

data = pd.read_csv(csv_file)
vd_values = data['tau']
mean_values = data['avg']
std_values = data['std']

set_global_font_size(16)
fig, ax = plt.subplots()
set_plot_size(fig, 16, 9)

plt.errorbar(vd_values, mean_values,
             yerr=std_values, fmt='o', capsize=5, markersize=10)

ax.grid(True)
ax.set_ylabel('$\\text{Promedio de la distancia entre el loco y pelota}$ $\overline{D}$ ($\\text{m}$)')
ax.set_xlabel('$\\text{Tiempo de relajación}$ $\\tau$ ($\\text{s}$)')

plt.tight_layout()
plt.savefig('avg_distance_vs_tau.png')
plt.show()