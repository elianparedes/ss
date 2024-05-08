import glob
import math

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

G = 6.693 * (10 ** -20)


def distance(x1, y1, x2, y2):
    return np.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2)


def calculate_kinetic_energy(mass, velocity):
    return 0.5 * mass * velocity ** 2


def calculate_potential_energy(m1, m2, dist):
    return -G * m1 * m2 / dist


lines_y_values = []
lines_x_values = []

plt.figure(figsize=(10, 6))

csv_files = [
    '../../output/dt-100000.0-start-0.0d.csv',
    # '../../output/dt-10000.0-start-0.0d.csv',
    # '../../output/dt-1000.0-start-0.0d.csv',
    # '../../output/dt-100.0-start-0.0d.csv',
]
for csv_file in csv_files:
    data = pd.read_csv(csv_file)

    initial_energy = 0
    energy_values = []
    time_values = data['time'].unique()

    for timestep in time_values:
        timestep_data = data[data['time'] == timestep]

        total_energy = 0
        for i in range(len(timestep_data)):
            particle = timestep_data.iloc[i]
            name1 = particle['name']
            mass1 = particle['mass']
            velocity1 = particle['velocity']
            x1 = particle['x']
            y1 = particle['y']

            kinetic_energy = calculate_kinetic_energy(mass1, velocity1)

            potential_energy = 0
            for j in range(i + 1, len(timestep_data)):
                other = timestep_data.iloc[j]
                name2 = other['name']
                mass2 = other['mass']
                x2 = other['x']
                y2 = other['y']

                if name1 != name2:
                    d = distance(x1, y1, x2, y2)
                    potential_energy += calculate_potential_energy(mass1, mass2, d)

            total_energy += kinetic_energy + potential_energy

        if len(energy_values) == 0:
            initial_energy = total_energy

        error = np.abs((total_energy - initial_energy) / initial_energy) * 100
        energy_values.append(error)

    lines_y_values.append(energy_values)
    lines_x_values.append(time_values)

for (line_x_values, line_y_values) in zip(lines_x_values, lines_y_values):
    plt.plot(line_x_values, line_y_values)

plt.xlabel('t')
plt.ylabel('energy')
plt.grid(True)
plt.show()
