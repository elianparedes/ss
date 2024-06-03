import pandas as pd
from matplotlib import pyplot as plt


def find_time_ranges(time_values, delta_time=1, tolerance=0):
    ranges = []
    current_range_start = time_values[0]
    current_range_end = time_values[0]

    for i in range(1, len(time_values)):
        if abs((time_values[i] - time_values[i - 1]) - delta_time) <= tolerance:
            current_range_end = time_values[i]
        else:
            if current_range_start != current_range_end:
                ranges.append((current_range_start, current_range_end))
            current_range_start = time_values[i]
            current_range_end = time_values[i]

    if current_range_start != current_range_end:
        ranges.append((current_range_start, current_range_end))

    return ranges


csv_file = '../../input/away.csv'

data = pd.read_csv(csv_file)

time_values = data['Frame']
min_time = min(time_values)
max_time = max(time_values)

ranges = find_time_ranges(time_values)

fig, ax = plt.subplots(figsize=(18, 4), facecolor="white")
ax.broken_barh(ranges, (0, 40), facecolor="red")
ax.set_ylim(5, 40)
ax.set_xlim(min_time, max_time)
plt.box(False)
plt.axis('off')
plt.yticks([])
plt.show()
