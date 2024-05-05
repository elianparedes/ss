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
csv_file = 'i1.csv'

data = pd.read_csv(path + csv_file)
time_values = data['time'][:1000]
position_values = data['x'][:1000]

plt.figure(figsize=(10, 6))
plt.plot(time_values, position_values)
plt.xlabel('dt')
plt.ylabel('mqe')
plt.grid(True)
plt.show()
