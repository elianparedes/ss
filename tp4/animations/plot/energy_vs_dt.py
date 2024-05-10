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
set_plot_size(fig, 14, 9)


sorted_dt_values = np.sort(dt_values)
for i, sorted_dt_value in enumerate(sorted_dt_values):

    plt.errorbar([sorted_dt_value], [mean_values[dt_values.index(sorted_dt_value)]], yerr=[error_values[dt_values.index(sorted_dt_value)]], fmt='o', capsize=5, markersize=10)

ax.grid(True)

ax.set_xlabel('$dt$ ($s$)')
ax.set_ylabel('$\overline{\\%P_E}$')

plt.xscale('log')
plt.yscale('log')
plt.tight_layout()
plt.savefig('output/energy_error_mean_vs_dt')
plt.show()
