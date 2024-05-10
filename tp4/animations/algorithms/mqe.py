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

csv_files = [
    'i1.csv',
    'i2.csv',
    'i3.csv',
    'i4.csv',
    'i5.csv',
    'i6.csv',
    'i7.csv',
    'i8.csv',
    'i9.csv'
]

algorithms = ['verlet', 'beeman', 'gear']

mqe_values = {}
std_values = {}
for algorithm in algorithms:
    mqe_values[algorithm] = []
dt_values = []

for csv_file in csv_files:
    print(csv_file)
    df = pd.read_csv(path + csv_file)
    time_values = df['time']
    dt = df['dt'].iloc[0]
    expected_values = damped_harmonic_oscillator(amplitude, k, gamma, mass, time_values)
    for algorithm in algorithms:
        predicted_values = df[algorithm]
        mqe = np.mean((expected_values - predicted_values) ** 2)
        mqe_values[algorithm].append(mqe)

    dt_values.append(dt)

set_global_font_size(16)
fig, ax = plt.subplots()
set_plot_size(fig, 12, 8)

plt.yscale('log')
colors = ['blue', 'orange', 'green']

for i, algorithm in enumerate(algorithms):
    plt.scatter(dt_values, mqe_values[algorithm], label=algorithm.capitalize(), color=colors[i])

plt.xlabel('$dt$')
plt.ylabel('Error cuadrático medio $MQE$')
plt.grid(True)
plt.legend()
plt.tight_layout()
plt.savefig('../plot/output/algorithms-comparison.png')
plt.show()
