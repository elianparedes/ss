import matplotlib.pyplot as plt
import csv

from matplotlib import ticker

import utils.plots as plots

csv_file = '../../output/min-distance-speed.csv'

speeds = []
distances = []

with open(csv_file, newline='') as file:
    reader = csv.DictReader(file)
    for row in reader:
        speeds.append(float(row['speed']))
        distances.append(float(row['distance']))

plots.set_global_font_size(16)
fig, ax = plt.subplots()
plots.set_plot_size(fig,14,6)
plt.scatter(speeds, distances, color='blue', label='Distance vs Speed')

ax.set_xlabel('Speed')
ax.set_ylabel('Distance')
ax.xaxis.set_major_formatter(ticker.ScalarFormatter(useMathText=True))
plt.yscale('log')
plt.xscale('log')

ax.grid(True)

plt.tight_layout()
plt.show()
