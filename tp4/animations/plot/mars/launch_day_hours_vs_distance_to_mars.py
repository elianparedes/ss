from datetime import timedelta, date

import pandas as pd
from matplotlib import pyplot as plt, ticker

from utils.plots import set_global_font_size, set_plot_size


def custom_date_fmt(value, _):
    initial_date = date(2024, 4, 26)
    return f'{(initial_date + timedelta(days=value)).strftime("%d/%m/%Y")}'


day_range = [f'{custom_date_fmt(value=float(day), _=None)}' for day in [176, 177]]
split_day = custom_date_fmt(177, None)
current_day = custom_date_fmt(176, None)

csv_file = '../distances/min-distance-hour.csv'

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
plt.scatter(day_values[day_values == min_day], distance_values[day_values == min_day], color='red', s=50)

ax.set_xlabel(f'Horas entre los días {day_range}')
ax.set_ylabel('$d_{{min}}$ entre la nave y Marte (km)')
ax.xaxis.set_major_formatter(ticker.ScalarFormatter(useMathText=True))
plt.yscale('log')

# Adjust x-axis ticks
ax.set_xticks(range(0, 24, 2))
ax.set_xticklabels([f'{hour % 24:02}:00' for hour in range(0, 24, 2)], rotation=45, ha='right')

bbox_props = dict(boxstyle="round,pad=0.3", fc="white", ec="black", lw=1, alpha=0.9)
ax.annotate(f'$d_{{min}}$={min_distance:.2f} km\nel {current_day} a las {min_day}:00',
            xy=(min_day, min_distance), xytext=(min_day + 2, min_distance),
            arrowprops=dict(facecolor='black', arrowstyle='-'),
            fontsize=16, ha='left', bbox=bbox_props)

# Add vertical line at the split day
ax.axvline(x=min_day, color='red', linestyle='--')
ax.axvline(x=24, color='gray', linestyle='--')
ax.annotate(f'{split_day}', xy=(24, ax.get_ylim()[1] / 1.2), xytext=(24, ax.get_ylim()[1] / 1.2),
            fontsize=14, ha='center', va='center', bbox=bbox_props)

ax.grid(True)
plt.tight_layout()

handles, labels = ax.get_legend_handles_labels()

plt.savefig('../output/launch_day_hours_vs_distance_to_mars.png')
plt.show()
