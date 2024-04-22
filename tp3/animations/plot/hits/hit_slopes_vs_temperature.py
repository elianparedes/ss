import glob

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from matplotlib.ticker import AutoLocator

from plot.plot_utils import configure_plot_presets, plot_set_right_legends

def plot_slopes_means(slopes_means, slopes_stds, temperatures):
    configure_plot_presets(plt)
    plt.errorbar(x=temperatures, y=slopes_means, yerr=slopes_stds, capsize=5, label='Pendiente $m_{ct}$', fmt='o')

    plt.xlabel('Temperatura (U.A.)')
    plt.ylabel('Pendiente de la curva "Nro de colisiones vs. tiempo"')
    plt.gca().xaxis.set_major_locator(AutoLocator())
    plot_set_right_legends(plt)
    plt.gcf().text(0.83, 0.5, "\n$N = 300$\n$V = \{1 ,3, 6, 10\} \, (m/s)$")

    plt.xticks(temperatures)
    plt.grid(True)
    plt.tight_layout()
    plt.savefig("hit_slopes_vs_temperature.png")

    plt.show()


csv_folders = [
    '../../compute/hits/output/v1/',
    '../../compute/hits/output/v3/',
    '../../compute/hits/output/v6/',
    '../../compute/hits/output/v10/',
]

csv_files = []
for folder in csv_folders:
    csv_files.append(glob.glob(folder + "*.csv"))



slopes_means = []
slopes_stds = []
for pile in csv_files:

    slopes = np.array([])
    for file in pile:
        df = pd.read_csv(file)
        x = df['interval']
        y = df['all_hits']
        m, b = np.polyfit(x, y, 1)
        slopes = np.append(slopes, m)

    slopes_means.append(slopes.mean())
    slopes_stds.append(slopes.std())

speeds = np.array([1, 3, 6, 10])
temperatures = speeds ** 2

plot_slopes_means(slopes_means, slopes_stds, temperatures)
