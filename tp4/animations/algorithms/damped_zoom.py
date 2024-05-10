import math

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

from utils.plots import set_global_font_size, set_plot_size

amplitude = 1
gamma = 100
mass = 70
k = 10000


def damped_harmonic_oscillator(a, k, gamma, m, t):
    return a * np.exp(-(gamma / (2 * m)) * t) * np.cos(((k / m - (gamma ** 2) / (4 * (m ** 2))) ** 0.5) * t)

path = '../../output/algorithms/'
csv_file = 'i2.csv'

set_global_font_size(16)
fig, ax = plt.subplots()
set_plot_size(fig, 12, 8)

data = pd.read_csv(path + csv_file)
time_values = data['time']
verlet_values = data['verlet']
beeman_values = data['beeman']
gear_values = data['gear']

algorithms = ['Analítica', 'Verlet', 'Beeman', 'Gear']
lines = [damped_harmonic_oscillator(amplitude, k, gamma, mass, time_values), verlet_values, beeman_values, gear_values]
colors = ['black', 'blue', 'orange', 'green']

for i, line in enumerate(lines):
    plt.plot(time_values, line, label=algorithms[i], color=colors[i])


# plt.plot(time_values, position_values)
plt.xlabel('$dt$ (s)')
plt.ylabel('Posición')


plt.legend()
plt.tight_layout()
plt.grid(True)

plt.xlim(3.1545, 4.1546)
plt.ylim(0.1048, 0.1048)

plt.savefig('algorithms_damped.png')
plt.show()
