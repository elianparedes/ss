import pandas as pd
from matplotlib import pyplot as plt, ticker

from utils.plots import set_global_font_size, set_plot_size

csv_file = 'distances/min-distance-hour.csv'

min_distance_days_data = pd.read_csv(csv_file)
distance_values = min_distance_days_data['distance']
day_values = min_distance_days_data['hour']

min_distance = min(distance_values)
min_distance_index = distance_values.idxmin()
min_day = day_values[min_distance_index]

set_global_font_size(16)
fig, ax = plt.subplots()
set_plot_size(fig, 14, 8)

plt.scatter(day_values, distance_values, s=30)
plt.scatter(day_values[day_values == min_day], distance_values[day_values == min_day], color='red', s=50, label='Day 176')

ax.set_xlabel('Horas en el día de lanzamiento 176')
ax.set_ylabel('Mínima distancia entre la nave y Marte (km)')
ax.xaxis.set_major_formatter(ticker.ScalarFormatter(useMathText=True))
plt.yscale('log')

bbox_props = dict(boxstyle="round,pad=0.3", fc="white", ec="black", lw=1, alpha=0.9)
ax.annotate(f'Mínima distancia: {min_distance:.2f} km\nen el día 176 a las {min_day}:00',
            xy=(min_day, min_distance), xytext=(min_day + 1, min_distance),
            arrowprops=dict(facecolor='black', arrowstyle='-'),
            fontsize=12, ha='left', bbox=bbox_props)
ax.axvline(x=min_day, color='gray', linestyle='--', label='')

ax.grid(True)
plt.tight_layout()

handles, labels = ax.get_legend_handles_labels()

plt.show()
