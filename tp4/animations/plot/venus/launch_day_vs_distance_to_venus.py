import pandas as pd
from matplotlib import pyplot as plt, ticker
from datetime import date, timedelta
from utils.plots import set_global_font_size, set_plot_size

csv_file = '../distances/venus-min-distance-days.csv'


def custom_date_fmt(value, _):
    initial_date = date(2024, 4, 26)
    return f'{(initial_date + timedelta(days=value)).strftime("%d/%m/%Y")}'


min_distance_days_data = pd.read_csv(csv_file)
min_distance_days_data = min_distance_days_data.sort_values('day')

distance_values = min_distance_days_data['distance']
day_values = min_distance_days_data['day']

set_global_font_size(16)
fig, ax = plt.subplots()
set_plot_size(fig, 14, 8)

plt.scatter(day_values, distance_values, s=4)

ax.set_xlabel('Fecha de lanzamiento')
ax.set_ylabel('$d_{{min}}$ entre la nave y Venus (km)')
ax.xaxis.set_major_formatter(ticker.ScalarFormatter(useMathText=True))
plt.yscale('log')

min_distance = min(distance_values)
min_distance_index = distance_values.idxmin()
min_day = day_values[min_distance_index]

plt.scatter(day_values[day_values == min_day], distance_values[day_values == min_day], color='red', s=50,
            label=f'Day {min_day}')

plt.gca().xaxis.set_major_formatter(ticker.FuncFormatter(custom_date_fmt))
plt.gca().tick_params(axis='x', rotation=45)

bbox_props = dict(boxstyle="round,pad=0.3", fc="white", ec="black", lw=1, alpha=0.9)
ax.annotate(f'$d_{{min}}$={min_distance:.2f} km\nen el día {custom_date_fmt(value=float(min_day), _=None)}',
            xy=(min_day, min_distance), xytext=(min_day + 64, min_distance),
            arrowprops=dict(facecolor='black', arrowstyle='-'),
            fontsize=16, ha='left', bbox=bbox_props)
ax.axvline(x=min_day, color='red', linestyle='--', label='')

ax.grid(True)
plt.tight_layout()

handles, labels = ax.get_legend_handles_labels()

plt.savefig('../output/launch_day_vs_distance_to_venus.png')
plt.show()
