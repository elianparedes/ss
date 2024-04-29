import math

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

amplitude = 1
gamma = 100
mass = 70
k = 10000


def damped_harmonic_oscillator(a, k, gamma, m, t):
    return a * np.exp(-(gamma / (2 * m)) * t) * np.cos(((k / m - (gamma ** 2) / (4 * (m ** 2))) ** 0.5) * t)

path = '../../output/'

csv_files = [
    'i0.csv',
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

mqe_values = []
dt_values = []

for csv_file in csv_files:
    print(csv_file)
    df = pd.read_csv(path + csv_file)
    time_values = df['time']
    dt = df['dt'].iloc[0]
    predicted_values = df['x']
    expected_values = damped_harmonic_oscillator(amplitude, k, gamma, mass, time_values)
    mqe = np.mean((expected_values - predicted_values) ** 2)
    mqe_values.append(mqe)
    dt_values.append(dt)

plt.figure(figsize=(10, 6))
plt.scatter(dt_values, mqe_values)
plt.xlabel('dt')
plt.ylabel('mqe')
plt.grid(True)
plt.show()
