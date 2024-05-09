import glob
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from matplotlib import ticker

from utils.files import extract_dt
from utils.plots import set_global_font_size, set_plot_size
import utils.plots as plt_utils

energy_pattern = r"energy_error_dt-([\d.]+)\.csv"

csv_files = glob.glob("../data/energy/*")

dt_values = []
mean_values = []
error_values = []

for csv_file in csv_files:
    dt = extract_dt(csv_file, energy_pattern)
    if dt not in [10, 100, 1000, 10000]:
        continue

    energy_data = pd.read_csv(csv_file)

    energy_error_values = energy_data['energy_error']
    time_values = energy_data['time']

    energy_errors_mean = np.mean(energy_error_values)
    energy_errors_std = np.std(energy_error_values)

    dt_values.append(dt)
    mean_values.append(energy_errors_mean)
    error_values.append(energy_errors_std)

set_global_font_size(16)
fig, ax = plt.subplots()
set_plot_size(fig, 14, 6)

plt.errorbar(dt_values, mean_values, yerr=error_values, fmt='o', color='blue', ecolor='red', capsize=5)

ax.grid(True)

plt.xscale('log')
plt.tight_layout()
plt.show()
