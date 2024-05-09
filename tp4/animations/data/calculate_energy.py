import re
import os
import numpy as np
import pandas as pd

from utils.files import extract_dt

mass = {
    "sun": 1.989 * 10 ** 30,
    "earth": 5.972 * 10 ** 24,
    "mars": 6.39 * 10 ** 23,
    "spaceship": 200000
}

G = 6.693 * (10 ** -20)

csv_files = [
    #'../../output/dt-10000.0-start-0.0d.csv',
    #'../../output/dt-1000.0-start-0.0d.csv',
    #'../../output/dt-100.0-start-0.0d.csv',
    #'../../output/dt-10.0-start-0.0d.csv',
    #'../../output/dt-1.0-start-0.0d.csv',
    '../../output/dt-0.1-start-0.0d.csv'
]


extract_pattern = "dt-([\d.]+)-start-[\d.]+[ydhms]"

def distance(x1, y1, x2, y2):
    return np.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2)


def calculate_kinetic_energy(mass, velocity):
    return 0.5 * mass * velocity ** 2


def calculate_potential_energy(m1, m2, dist):
    return -G * m1 * m2 / dist

for csv_file in csv_files:
    data = pd.read_csv(csv_file)

    dt = extract_dt(csv_file,extract_pattern)

    initial_energy = 0
    energy_values = []
    time_values = data['time'].unique()

    for timestep in time_values:
        timestep_data = data[data['time'] == timestep]

        total_energy = 0
        for i in range(len(timestep_data)):
            particle = timestep_data.iloc[i]
            name1 = particle['name']
            mass1 = mass[name1]
            velocity1 = particle['velocity']
            x1 = particle['x']
            y1 = particle['y']

            kinetic_energy = calculate_kinetic_energy(mass1, velocity1)

            potential_energy = 0
            for j in range(i + 1, len(timestep_data)):
                other = timestep_data.iloc[j]
                name2 = other['name']
                mass2 = mass[name2]
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

    results = {'time': time_values, 'energy_error': energy_values}
    result_df = pd.DataFrame(results)

    output_dir = "./energy/"
    os.makedirs(output_dir, exist_ok=True)
    output_file = os.path.join(output_dir, f"energy_error_dt-{dt}.csv")
    result_df.to_csv(output_file, index=False)
