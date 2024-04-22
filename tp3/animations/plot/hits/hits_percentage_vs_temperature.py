import glob

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from matplotlib.ticker import AutoLocator

from plot.plot_utils import configure_plot_presets, plot_set_right_legends

percentage = 0.4
n = 300

def plot_slopes_means(slopes_means, slopes_stds, temperatures):
    global percentage
    configure_plot_presets(plt)
    plt.errorbar(x=temperatures, y=slopes_means, yerr=slopes_stds, capsize=5, label='Pendiente $m_{ct}$', fmt='o')

    plt.xlabel('Temperatura (U.A.)')
    plt.ylabel(f'Tiempo hasta que el {percentage * 100:.0f}% colisione con el obstáculo (s)')
    plt.gca().xaxis.set_major_locator(AutoLocator())
    plt.gcf().text(0.77, 0.5, "\n$N = 300$\n$V = \{1 ,3, 6, 10\} \, (m/s)$\n$It_{max}=30000$")
    plt.xticks(temperatures)
    plt.grid(True)
    plt.tight_layout()
    plt.subplots_adjust(right=0.75)  # Adjust the value as needed
    plt.savefig("hit_percentage_vs_temperature.png")

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



times_means = []
times_stds = []
for pile in csv_files:

    times_to_percentage = np.array([])
    for file in pile:
        df = pd.read_csv(file)

        for (index, row) in df.iterrows():
            x = row['interval']
            y = row['unique_hits']

            if y >= 0.4 * n:
                times_to_percentage = np.append(times_to_percentage, x)
                break

    print(len(times_to_percentage))
    times_means.append(times_to_percentage.mean())
    times_stds.append(times_to_percentage.std())

speeds = np.array([1, 3, 6, 10])
temperatures = speeds ** 2

plot_slopes_means(times_means, times_stds, temperatures)
