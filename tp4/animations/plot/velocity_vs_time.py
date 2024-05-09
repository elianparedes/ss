import numpy as np
import pandas as pd
from matplotlib import pyplot as plt, ticker

from utils.plots import set_global_font_size, set_plot_size

def convert_seconds_to_ddhhmm(seconds):
    days = seconds // (24 * 3600)
    hours = (seconds % (24 * 3600)) // 3600
    minutes = (seconds % 3600) // 60
    return days, hours, minutes

csv_file = '../../output/dt-10.0-start-1.523586E7s.csv'

set_global_font_size(16)
fig, ax = plt.subplots()
set_plot_size(fig, 14, 8)

csv_data = pd.read_csv(csv_file)
velocity_values = csv_data['velocity']
time_values = csv_data['time'] * 10

distance_values = csv_data['distance_to_mars']

min_distance_index = distance_values.idxmin()
min_distance_velocity = velocity_values[min_distance_index]
min_distance = distance_values[min_distance_index]
min_time = time_values[min_distance_index] - time_values[0]
time_values -= time_values[0]

ax.axvline(x=min_time, color='gray', linestyle='--', label='')

time_values = time_values[:min_distance_index]
velocity_values = velocity_values[:min_distance_index]

print(min_distance)

plt.scatter(time_values, velocity_values, s=10)
plt.scatter([min_time], [min_distance_velocity], color='red', s=50)

ax.set_xlabel('Tiempo ($s$)')
ax.set_ylabel('Módulo de la velocidad de la nave ($\\frac{km}{s}$)')



ax.grid(True)
plt.tight_layout()

plt.show()