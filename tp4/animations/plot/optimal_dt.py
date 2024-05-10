import os
import re

import pandas as pd
import matplotlib.pyplot as plt
from matplotlib import ticker

from utils.files import extract_dt
from utils.plots import set_global_font_size, set_plot_size
import utils.plots as plt_utils

energy_dir = "../data/energy/"
os.makedirs(energy_dir, exist_ok=True)

csv_files = [os.path.join(energy_dir, f) for f in os.listdir(energy_dir) if f.endswith('.csv')]
csv_files.sort()

lines_x_values = []
lines_y_values = []
legends = []

energy_pattern = r"energy_error_dt-([\d.]+)\.csv"

dt_legend_data = []

for csv_file in csv_files:
    try:
        dt = extract_dt(csv_file, energy_pattern)
        data = pd.read_csv(csv_file)
        lines_x_values.append(data['time'] * dt)
        lines_y_values.append(data['energy_error'])
        legends.append(f"{dt}")
    except ValueError as e:
        print(e)

set_global_font_size(16)
fig, ax = plt.subplots()
set_plot_size(fig, 14, 6)

for line_x_values, line_y_values, legend in zip(lines_x_values, lines_y_values, legends):
    plt.plot(line_x_values, line_y_values, label=legend)


ax.set_xlabel('Tiempo ($s$)')
ax.set_ylabel('$\\%P_E$')
ax.xaxis.set_major_formatter(ticker.ScalarFormatter(useMathText=True))
ax.ticklabel_format(style='sci', axis='x', scilimits=(0, 0))
ax.set_yscale('log')
ax.grid(True)

legend = plt_utils.PlotLegend.get_legend(ax)
legend.set_legend_outside().set_title('$dt$ ($s$)').set_title_font_size(16).set_title_horizontal_alignment(
    'left')

plt.tight_layout()
plt.savefig('output/optimal_dt.png')
plt.show()
